package Vue;

import Criminel.Crime;
import Criminel.Criminel;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AfficheCrime extends JFrame {

    private Vue view;
    private Modele mdl;
    private JPanel panelGlobal;
    private JTextArea champTexte;

    public AfficheCrime(Vue view, ArrayList<Crime> lesCrime,ListeCrime reloadListe, Modele modele){
        this.mdl = modele;
        this.view = view;
        this.panelGlobal = new JPanel();

        setTitle("Information sur les crimes");
        setSize(400, 600);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        this.champTexte = new JTextArea();
        champTexte.setPreferredSize(new Dimension(200, 450));
        champTexte.setFont(new Font("Serif", Font.BOLD, 15));
        champTexte.setEditable(false);

        StringBuilder sb = new StringBuilder();
        if (this.mdl.getListeCrimes().isEmpty()){
            sb.append("Aucun crime existant 😔\n");
        }
        else {
            for (Crime c : this.mdl.getListeCrimes()) {
                sb.append(c.getIntitule()).append("\n");
            }
        }
        champTexte.setText(sb.toString());
        panel.add(champTexte);

        JPanel panelSud = new JPanel();
        panelSud.setLayout(new FlowLayout());

        String[] elements = new String[]{};
        if (!this.mdl.getListeCrimes().isEmpty()) {
            elements = new String[this.mdl.getListeCrimes().size()];
            int i = 0;
            for (Crime c : this.mdl.getListeCrimes()) {
                elements[i] = c.getIntitule();
                i++;
            }
        }

        JComboBox<String> liste = new JComboBox<>(elements);
        panelSud.add(liste);



        JTextField select = new JTextField();
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
        panelSud.add(suppCrime);

        suppCrime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!select.getText().isEmpty()) {

                    int existe = -1;
                    for (int i = 0; i < lesCrime.size(); i++) {
                        if (lesCrime.get(i).getIntitule().equals(select.getText())) {
                            existe = i;
                        }
                    }
                    if (existe != -1) {
                        modele.supprimerCrimes(existe);
                        view.afficherDetails();
                        reloadListe.updateAffichageListeCrimes();
                        updateAffichage(lesCrime, liste, champTexte);
                        select.setText("");
                    }
                }
            }
        });



        add(panel, BorderLayout.CENTER);
        add(panelSud, BorderLayout.SOUTH);

        setVisible(true);
    }


    private void updateAffichage(ArrayList<Crime> lesCrime, JComboBox<String> liste, JTextArea champTexte) {
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
