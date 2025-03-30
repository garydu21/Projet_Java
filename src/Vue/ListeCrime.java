package Vue;

import Criminel.Crime;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ListeCrime extends JFrame {

    private JPanel panelHaut, panelCentre, panelGauche, panelDroite, panelBas;
    private JButton buttonAjouter;
    private JTextArea crime, sanction;
    private JTextField lesCrime;
    private ArrayList<Crime> crimes;

    private Vue view;

    public ListeCrime(ArrayList<Crime> crimes, Vue view) {
        this.view = view;
        this.crimes = crimes;

        setTitle("Gestion des Crimes");
        setSize(500, 300);
        setLayout(new BorderLayout());

        // Panel Haut : Bouton Ajouter
        panelHaut = new JPanel();
        buttonAjouter = new JButton("Ajouter un Crime");
        panelHaut.add(buttonAjouter);

        // Panel Centre : Champs de saisie pour crime et sanction
        panelCentre = new JPanel(new GridLayout(2, 2, 10, 10));

        panelGauche = new JPanel(new BorderLayout());
        JLabel labelCrime = new JLabel("Nom du Crime :");
        crime = new JTextArea();
        crime.setPreferredSize(new Dimension(200, 30));
        crime.setText("Ex : Vol, Meurtre, Fraude...");
        panelGauche.add(labelCrime, BorderLayout.NORTH);
        panelGauche.add(crime, BorderLayout.CENTER);

        panelDroite = new JPanel(new BorderLayout());
        JLabel labelSanction = new JLabel("Peine associée (en années) :");
        sanction = new JTextArea();
        sanction.setPreferredSize(new Dimension(200, 30));
        sanction.setText("Ex : 5, 10, 20...");
        panelDroite.add(labelSanction, BorderLayout.NORTH);
        panelDroite.add(sanction, BorderLayout.CENTER);

        panelCentre.add(panelGauche);
        panelCentre.add(panelDroite);

        // Panel Bas : Liste des crimes ajoutés
        panelBas = new JPanel(new BorderLayout());
        JLabel labelListeCrime = new JLabel("Crimes enregistrés :");
        lesCrime = new JTextField();
        lesCrime.setEditable(false);
        lesCrime.setText("Aucun crime enregistré.");
        panelBas.add(labelListeCrime, BorderLayout.NORTH);
        panelBas.add(lesCrime, BorderLayout.CENTER);

        // Ajout des panels à la fenêtre
        add(panelHaut, BorderLayout.NORTH);
        add(panelCentre, BorderLayout.CENTER);
        add(panelBas, BorderLayout.SOUTH);

        // Action du bouton Ajouter
        buttonAjouter.addActionListener(e -> ajouterCrime());

        setVisible(true);
    }

    public void ajouterCrime() {
        try {
            int peine = Integer.parseInt(this.sanction.getText().trim());
            String str = this.crime.getText().trim();

            if (str.isEmpty() || peine < 0) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un crime valide et une peine correcte.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            this.crimes.add(new Crime(peine, str));

            // Mise à jour de la liste des crimes
            StringBuilder sb = new StringBuilder();
            for (Crime c : crimes) {
                sb.append(c.getIntitule()).append(": ").append(c.getPeine()).append(" ans | ");
            }
            lesCrime.setText(sb.toString());

            // Réinitialiser les champs
            crime.setText("");
            sanction.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide pour la peine.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
        }
        this.view.afficherDetails();
    }

    public ArrayList<Crime> getCrimes() {
        return this.crimes;
    }
}
