package Vue;

import Criminel.Crime;
import Interface.RoundedBorder;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AfficheCrime extends JFrame {

    private Vue view;
    private Modele mdl;
    private JPanel panelGlobal;
    private JTextArea champTexte;

    public AfficheCrime(Vue view, ListeCrime reloadListe, Modele modele) {
        this.mdl = modele;
        this.view = view;
        this.panelGlobal = new JPanel();

        setTitle("Information sur les crimes");
        setSize(400, 600);
        setLayout(new BorderLayout());

        // Panel principal
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        this.champTexte = new JTextArea();
        champTexte.setPreferredSize(new Dimension(250, 510));
        champTexte.setFont(new Font("Serif", Font.BOLD, 15));
        champTexte.setEditable(false);

        // Affichage initial des crimes
        StringBuilder sb = new StringBuilder();
        if (this.mdl.getListeCrimes().isEmpty()) {
            sb.append("Aucun crime existant 😔\n");
        } else {
            for (Crime c : this.mdl.getListeCrimes()) {
                sb.append(c.getIntitule()).append("\n");
            }
        }
        champTexte.setText(sb.toString());
        panel.add(champTexte);

        // Panel Sud (combo + champ + bouton)
        JPanel panelSud = new JPanel();
        panelSud.setBackground(Color.LIGHT_GRAY);
        panelSud.setLayout(new FlowLayout());

        String[] elements = new String[this.mdl.getListeCrimes().size()];
        for (int i = 0; i < mdl.getListeCrimes().size(); i++) {
            elements[i] = mdl.getListeCrimes().get(i).getIntitule();
        }

        JComboBox<String> liste = new JComboBox<>(elements);
        panelSud.add(liste);

        JTextField select = new JTextField();
        select.setFont(new Font("Serif", Font.BOLD, 15));
        select.setPreferredSize(new Dimension(200, 20));
        select.setEditable(false);
        panelSud.add(select);

        liste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) liste.getSelectedItem();
                select.setText(selectedItem);
            }
        });

        JButton suppCrime = new JButton("Supprimer");
        suppCrime.setBorder(new RoundedBorder(10));
        suppCrime.setBackground(Color.LIGHT_GRAY);
        suppCrime.setForeground(Color.BLACK);
        panelSud.add(suppCrime);

        suppCrime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!select.getText().isEmpty()) {
                    int existe = -1;
                    for (int i = 0; i < mdl.getListeCrimes().size(); i++) {
                        if (mdl.getListeCrimes().get(i).getIntitule().equals(select.getText())) {
                            existe = i;
                            break;
                        }
                    }
                    if (existe != -1) {
                        mdl.supprimerCrimes(existe);
                        view.afficherDetails();
                        reloadListe.updateAffichageListeCrimes();
                        updateAffichage(liste, champTexte);
                        select.setText("");
                    }
                }
            }
        });

        add(panel, BorderLayout.CENTER);
        add(panelSud, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateAffichage(JComboBox<String> liste, JTextArea champTexte) {
        // Mise à jour de la zone de texte
        StringBuilder sb = new StringBuilder();
        if (this.mdl.getListeCrimes().isEmpty()) {
            sb.append("Aucun crime existant 😔\n");
        } else {
            for (Crime c : this.mdl.getListeCrimes()) {
                sb.append(c.getIntitule()).append("\n");
            }
        }
        champTexte.setText(sb.toString());

        // Mise à jour de la ComboBox
        liste.removeAllItems();
        for (Crime c : this.mdl.getListeCrimes()) {
            liste.addItem(c.getIntitule());
        }
    }
}