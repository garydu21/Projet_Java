package Vue;

import Criminel.Criminel;
import Controleur.Controleur;
import Modele.Modele;
import Modele.Modifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Scanner;

public class Vue extends JFrame {
    private JList<String> listeCriminels;
    private DefaultListModel<String> listeModel;
    private JTextArea detailsCriminel;
    private JButton btnModifier, btnAjouter, btnSupprimer;
    private Controleur controleur;
    private Modifier modifier;
    private Modele modele;

    public Vue(Controleur controleur, List<Criminel> criminels) {
        this.controleur = controleur;

        setTitle("Gestion des Criminels");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Haut : Boutons pour navigation
        JPanel panelHaut = new JPanel();
        panelHaut.add(new JButton("Base de données"));
        panelHaut.add(new JButton("Graphique"));
        panelHaut.add(new JButton("Maps"));
        add(panelHaut, BorderLayout.NORTH);

        // Centre : Liste des criminels à gauche + Détails à droite
        JPanel panelCentre = new JPanel(new GridLayout(1, 2));
        listeModel = new DefaultListModel<>();
        listeCriminels = new JList<>(listeModel);
        JScrollPane scrollPane = new JScrollPane(listeCriminels);
        panelCentre.add(scrollPane);

        detailsCriminel = new JTextArea();
        detailsCriminel.setEditable(false);
        panelCentre.add(new JScrollPane(detailsCriminel));

        add(panelCentre, BorderLayout.CENTER);

        // Bas : Boutons d'actions
        JPanel panelBas = new JPanel();
        btnModifier = new JButton("Modifier");
        btnAjouter = new JButton("Ajouter");
        btnSupprimer = new JButton("Supprimer");
        panelBas.add(btnModifier);
        panelBas.add(btnAjouter);
        panelBas.add(btnSupprimer);
        add(panelBas, BorderLayout.SOUTH);

        // Remplir la liste des criminels
        for (Criminel c : criminels) {
            listeModel.addElement(c.getNom() + " " + c.getPrenom());
        }

        // Ajout des événements
        listeCriminels.addListSelectionListener(e -> afficherDetails(criminels));
        btnModifier.addActionListener(e -> modifierCriminel());
        btnAjouter.addActionListener(e -> ajouterCriminel());
        btnSupprimer.addActionListener(e -> supprimerCriminel());

        setVisible(true);
    }

    private void afficherDetails(List<Criminel> criminels) {
        int index = listeCriminels.getSelectedIndex();
        if (index >= 0) {
            Criminel c = criminels.get(index);
            detailsCriminel.setText("Nom : " + c.getNom() + "\n"
                    + "Prénom : " + c.getPrenom() + "\n"
                    + "Peine Totale : " + c.getPeineTotale() + " ans");
        }
    }

    private void modifierCriminel() {
        JOptionPane.showMessageDialog(this, "Modifier criminel - Fonction à implémenter");
    }

    private void ajouterCriminel() {
        //JOptionPane.showMessageDialog(this, "Ajouter criminel - Fonction à implémenter");
        //this.modifier.ajouterCriminelModifier();
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez entrer le nom d'un criminel : ");
        String nom = sc.nextLine();
        System.out.println("Veuillez entrer le prenom d'un criminel : ");
        String prenom = sc.nextLine();
        System.out.println("Merci, le criminel a été ajouter ! ");
        Criminel c = new Criminel(nom, prenom);

        modele.addListeCriminel(c);
    }

    private void supprimerCriminel() {
        JOptionPane.showMessageDialog(this, "Supprimer criminel - Fonction à implémenter");
    }
}
