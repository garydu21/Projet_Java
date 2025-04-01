package Vue;

import Criminel.Criminel;
import Controleur.Controleur;
import Modele.Modele;
import Criminel.Affaire;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Vue extends JFrame implements Observer {
    private JList<String> listeCriminels;
    private DefaultListModel<String> listeModel;
    private JTextArea detailsCriminel;
    private JButton btnModifier, btnAjouter, btnSupprimer, btnGestionJson;
    private Modele modele;

    public Vue(Controleur controleur, Modele modele) {
        this.modele = modele;
        this.modele.addObserver(this);

        setTitle("Gestion des Criminels");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Haut : Boutons pour navigation
        JPanel panelHaut = new JPanel();
        btnGestionJson = new JButton("Gérer JSON");
        panelHaut.add(btnGestionJson);
        panelHaut.add(new JButton("Base de données"));
        panelHaut.add(new JButton("Maps"));
        add(panelHaut, BorderLayout.NORTH);

        JButton btnAffaires = new JButton("Affaires");
        panelHaut.add(btnAffaires);
        btnAffaires.addActionListener(e -> new VueAffaires(modele));


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
        mettreAJourListe();

        // Ajout des événements
        listeCriminels.addListSelectionListener(e -> afficherDetails());
        btnModifier.addActionListener(e -> modifierCriminel());
        btnAjouter.addActionListener(e -> ajouterCriminel());
        btnSupprimer.addActionListener(e -> supprimerCriminel());
        btnGestionJson.addActionListener(e -> afficherPopupGestionJson());

        setVisible(true);
    }

    private void mettreAJourListe() {
        listeModel.clear();
        for (Criminel c : modele.getListeCriminel()) {
            listeModel.addElement(c.getNom() + " " + c.getPrenom());
        }
    }

    private void afficherDetails() {
        int index = listeCriminels.getSelectedIndex();
        if (index >= 0) {
            Criminel c = modele.getListeCriminel().get(index);
            detailsCriminel.setText("Nom : " + c.getNom() + "\n"
                    + "Prénom : " + c.getPrenom() + "\n"
                    + "Peine Totale : " + c.getPeineTotale() + " ans\n");

            if (c.getAffaires() != null && !c.getAffaires().isEmpty()) {
                detailsCriminel.append("Affaires :\n");
                for (Affaire a : c.getAffaires()) {
                    detailsCriminel.append(" - " + a.getDescription() + " (" + a.getLieu() + ")\n");
                }
            }
        }
    }

    private void ajouterCriminel() {
        String nom = JOptionPane.showInputDialog(this, "Nom du criminel :");
        String prenom = JOptionPane.showInputDialog(this, "Prénom du criminel :");
        if (nom != null && prenom != null) {
            modele.addListeCriminel(new Criminel(nom, prenom));
        }
    }

    private void modifierCriminel() {
        int index = listeCriminels.getSelectedIndex();
        if (index >= 0) {
            Criminel c = modele.getListeCriminel().get(index);
            String nom = JOptionPane.showInputDialog(this, "Modifier le nom :", c.getNom());
            String prenom = JOptionPane.showInputDialog(this, "Modifier le prénom :", c.getPrenom());
            if (nom != null && prenom != null) {
                modele.modifierCriminel(index, nom, prenom);
            }
        }
    }

    private void supprimerCriminel() {
        int index = listeCriminels.getSelectedIndex();
        if (index >= 0) {
            modele.supprimerCriminel(index);
        }
    }

    private void afficherPopupGestionJson() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem importer = new JMenuItem("Importer JSON");
        JMenuItem exporter = new JMenuItem("Exporter JSON");

        importer.addActionListener(e -> importerJson());
        exporter.addActionListener(e -> exporterJson());

        popupMenu.add(importer);
        popupMenu.add(exporter);
        popupMenu.show(btnGestionJson, btnGestionJson.getWidth() / 2, btnGestionJson.getHeight() / 2);
    }

    private void importerJson() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            modele.importerJson(fileChooser.getSelectedFile());
        }
    }

    private void exporterJson() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            modele.exporterJson(fileChooser.getSelectedFile());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourListe();
    }
}