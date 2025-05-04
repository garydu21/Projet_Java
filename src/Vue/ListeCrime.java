package Vue;

import Criminel.Crime;
import Interface.RoundedBorder;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ListeCrime extends JFrame {

    private final Font fontButton = new Font("Arial", Font.BOLD, 15);
    private final Font fontName = new Font("Arial", Font.ITALIC + Font.BOLD, 15);
    private final Font fontDetail = new Font("Arial", Font.BOLD, 15);
    private final Font fontDescription = new Font("Bitstream Vera Sans Mono", Font.PLAIN, 15);
    private final Font fontListe = new Font("Arial", Font.BOLD, 12);

    private JPanel panelHaut, panelCentre, panelGauche, panelDroite, panelBas;
    private JButton buttonAjouter;
    private JButton afficherCrime;
    private JTextArea crime, sanction;
    private JTextField lesCrime;
    private ArrayList<Crime> crimes;

    private Modele modele;
    private Vue view;

    public ListeCrime(ArrayList<Crime> crimes, Vue view, Modele modele) {
        this.modele = modele;
        this.view = view;
        this.crimes = crimes;

        setTitle("Crime");
        setSize(500, 300);
        setLayout(new BorderLayout());

        panelHaut = new JPanel();
        panelHaut.setBackground(Color.GRAY);

        buttonAjouter = new JButton("Ajouter un Crime");
        buttonAjouter.setBorder(new RoundedBorder(10));
        buttonAjouter.setFont(fontButton);
        buttonAjouter.setBackground(Color.WHITE);
        buttonAjouter.setForeground(Color.BLACK);

        afficherCrime = new JButton("Afficher les crimes");
        afficherCrime.setBorder(new RoundedBorder(10));
        afficherCrime.setFont(fontButton);
        afficherCrime.setBackground(Color.WHITE);
        afficherCrime.setForeground(Color.BLACK);

        panelHaut.add(buttonAjouter);
        panelHaut.add(afficherCrime);

        panelCentre = new JPanel(new GridLayout(2, 2, 10, 10));
        panelCentre.setBackground(Color.LIGHT_GRAY);

        panelGauche = new JPanel(new BorderLayout());
        panelGauche.setBackground(Color.LIGHT_GRAY);

        JLabel labelCrime = new JLabel("Nom du Crime :");
        labelCrime.setFont(fontDetail);
        crime = new JTextArea();
        crime.setPreferredSize(new Dimension(200, 30));
        crime.setText("Ex : Vol, Meurtre, Fraude...");
        crime.setFont(fontDescription);
        panelGauche.add(labelCrime, BorderLayout.NORTH);
        panelGauche.add(crime, BorderLayout.CENTER);

        panelDroite = new JPanel(new BorderLayout());
        panelDroite.setBackground(Color.LIGHT_GRAY);
        JLabel labelSanction = new JLabel("Peine associée (en années) :");
        labelSanction.setFont(fontDetail);
        sanction = new JTextArea();
        sanction.setPreferredSize(new Dimension(200, 30));
        sanction.setText("Ex : 5, 10, 20...");
        sanction.setFont(fontDescription);
        panelDroite.add(labelSanction, BorderLayout.NORTH);
        panelDroite.add(sanction, BorderLayout.CENTER);

        panelCentre.add(panelGauche);
        panelCentre.add(panelDroite);


        panelBas = new JPanel(new BorderLayout());
        panelBas.setBackground(Color.LIGHT_GRAY);

        JLabel labelListeCrime = new JLabel("Crimes enregistrés :");
        labelListeCrime.setFont(fontName);

        lesCrime = new JTextField();
        lesCrime.setEditable(false);
        lesCrime.setText("Aucun crime enregistré sur cette session.");
        panelBas.add(labelListeCrime, BorderLayout.NORTH);
        panelBas.add(lesCrime, BorderLayout.CENTER);


        add(panelHaut, BorderLayout.NORTH);
        add(panelCentre, BorderLayout.CENTER);
        add(panelBas, BorderLayout.SOUTH);


        buttonAjouter.addActionListener(e -> ajouterCrime());
        afficherCrime.addActionListener(e -> listeCrime());

        setVisible(true);
    }

    private void listeCrime(){
        new AfficheCrime(this.view,this.view.getStr(), this, this.modele);
    }

    public void ajouterCrime() {
        try {
            int peine = Integer.parseInt(this.sanction.getText().trim());
            String str = this.crime.getText().trim();

            if (str.isEmpty() || peine < 0) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un crime valide et une peine correcte.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            this.modele.addListeCrime(new Crime(peine, str));
            updateAffichageListeCrimes();


            crime.setText("");
            sanction.setText("");
            this.view.afficherDetails();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide pour la peine.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ArrayList<Crime> getCrimes() {
        return this.crimes;
    }

    public void updateAffichageListeCrimes() {
        if (crimes.isEmpty()) {
            lesCrime.setText("Aucun crime enregistré.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Crime c : crimes) {
                sb.append(c.getIntitule()).append(": ").append(c.getPeine()).append(" ans | ");
            }
            lesCrime.setText(sb.toString());
        }
    }

}
