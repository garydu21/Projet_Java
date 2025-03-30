package Vue;

import Criminel.Criminel;
import Controleur.Controleur;
import Modele.Modele;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.*;
import java.util.List;
import Criminel.Crime;

public class Vue extends JFrame implements Observer {
    private JList<String> listeCriminels;
    private DefaultListModel<String> listeModel;
    private JPanel detailsCriminel;
    private JButton btnModifier, btnAjouter, btnSupprimer, btnGestionJson;
    private Modele modele;
    private JTextArea infoCriminel;
    private JButton ajouterCrime;

    private JTextField champTexte;
    private JComboBox<String> liste;
    private ArrayList<Crime> str = new ArrayList<>();


    private JPanel panelDescription;
    private JTextArea description;
    private JButton ajouterDescription;

    private ListeCrime afficheListeCrime;
    private String[] elements;

    public Vue(Controleur controleur, Modele modele) {

        this.modele = modele;
        this.modele.addObserver(this);
        setTitle("Gestion des Criminels");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Haut : Boutons pour navigation
        JPanel panelHaut = new JPanel();
        ajouterCrime = new JButton("Liste des Crime");
        panelHaut.add(ajouterCrime);
        btnGestionJson = new JButton("Gérer JSON");
        panelHaut.add(btnGestionJson);
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

        detailsCriminel = new JPanel();
        infoCriminel = new JTextArea();
        infoCriminel.setEditable(false);
        detailsCriminel.add(infoCriminel, BorderLayout.NORTH);

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
        ajouterCrime.addActionListener(e -> fenetreCrime());

        setVisible(true);
    }

    private void mettreAJourListe() {
        listeModel.clear();
        for (Criminel c : modele.getListeCriminel()) {
            listeModel.addElement(c.getNom() + " " + c.getPrenom());
        }
    }

    public void afficherDetails() {

        int index = listeCriminels.getSelectedIndex();
        if (index >= 0) {
            Criminel c = modele.getListeCriminel().get(index);
            StringBuilder sb = new StringBuilder();
            for (Crime lesCrime : c.getCrimes()){
                if (lesCrime.getIntitule() != null){
                    sb.append(lesCrime.getIntitule()).append(" ");
                }
            }
            if (!sb.isEmpty()) {
                infoCriminel.setText("Nom : " + c.getNom() + "\n"
                        + "Prénom : " + c.getPrenom() + "\n"
                        + "Peine Totale : " + c.getPeineTotale() + " ans \n"
                        + "Arrêter pour : " + sb + "\n");
            }
            else{
                infoCriminel.setText("Nom : " + c.getNom() + "\n"
                        + "Prénom : " + c.getPrenom() + "\n"
                        + "Peine Totale : " + c.getPeineTotale() + " ans \n"
                        + "Arrêter pour : rien \n");
            }
        }


        detailsCriminel.removeAll();
        detailsCriminel.setLayout(new BorderLayout());
        detailsCriminel.add(infoCriminel, BorderLayout.NORTH);

        panelDescription = new JPanel();
        panelDescription.setLayout(new BorderLayout());

        ajouterDescription = new JButton("Ajouter Description");
        ajouterDescription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int index = listeCriminels.getSelectedIndex();
                    if (index >= 0 && index < modele.getListeCriminel().size()) {
                        Criminel mechant = modele.getListeCriminel().get(index);
                        modele.modifierCriminel(index, mechant.getNom(), mechant.getPrenom(), description.getText());
                    }
                }catch (Exception ex) {
                    int index = listeCriminels.getSelectedIndex();
                    if (index >= 0 && index < modele.getListeCriminel().size()) {
                        Criminel mechant = modele.getListeCriminel().get(index);
                        modele.modifierCriminel(index, mechant.getNom(), mechant.getPrenom(), "Une erreur est survenu, veuiller ressaisir le texte");
                    }
                }
            }
        });

        description = new JTextArea();
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setBackground(Color.LIGHT_GRAY);
        description.setPreferredSize(new Dimension(200, 200));

        JScrollPane scrollPaneDescription = new JScrollPane(description);
        panelDescription.add(scrollPaneDescription, BorderLayout.CENTER);
        panelDescription.add(ajouterDescription, BorderLayout.SOUTH);


        detailsCriminel.add(panelDescription, BorderLayout.CENTER);


        try {
            description.setText(modele.getListeCriminel().get(index).getDescription());
        }catch (Exception expe){}

        description.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                sauvegarderDescription();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                sauvegarderDescription();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private void sauvegarderDescription() {
                int index = listeCriminels.getSelectedIndex();
                if (index >= 0) {
                    Criminel mechant = modele.getListeCriminel().get(index);
                    mechant.setDescription(description.getText());
                }
            }
        });

        JPanel panelDetails = new JPanel(new FlowLayout());

        elements = new String[]{}; // A modifier lorsque l'Ensemble des crimes est ajouté
        try {
            if (!this.afficheListeCrime.getCrimes().isEmpty()) {
                elements = new String[this.afficheListeCrime.getCrimes().size()];
                int i = 0;
                for (Crime c : this.afficheListeCrime.getCrimes()) {
                    elements[i] = c.getIntitule();
                    i++;
                }
            }
        }catch (Exception expe){}

        liste = new JComboBox<>(elements);
        panelDetails.add(liste);

        champTexte = new JTextField(200);
        champTexte = new JTextField();
        champTexte.setPreferredSize(new Dimension(200, 20));
        champTexte.setEditable(false);
        panelDetails.add(champTexte);

        JButton ajouterCrime = new JButton("Ajouter");
        JButton supprimerCrime = new JButton("Supprimer");
        panelDetails.add(ajouterCrime,BorderLayout.SOUTH);
        panelDetails.add(supprimerCrime, BorderLayout.SOUTH);

        liste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) liste.getSelectedItem();
                champTexte.setText(selectedItem);
            }
        });

        supprimerCrime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{

                    int index = listeCriminels.getSelectedIndex();
                    Criminel mechant = modele.getListeCriminel().get(index);

                    mechant.resetPeineTotal();
                    mechant.getCrimes().clear();
                    afficherDetails();
                    modele.modifierCriminel(index, mechant.getNom(), mechant.getPrenom(), "");

                }catch (Exception expe){}
            }
        });

        ajouterCrime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!champTexte.getText().isEmpty()) {
                    try{
                        int index = listeCriminels.getSelectedIndex();
                        Criminel mechant = modele.getListeCriminel().get(index);

                        int peine = -1;
                        for (int i = 0; i < afficheListeCrime.getCrimes().size(); i++) {
                            if (champTexte.getText().equalsIgnoreCase(afficheListeCrime.getCrimes().get(i).getIntitule())) {
                                peine = afficheListeCrime.getCrimes().get(i).getPeine();
                            }
                        }
                        if (peine != -1) {
                            Crime leCrime = new Crime(peine, champTexte.getText());
                            mechant.ajouterCrime(leCrime);
                            afficherDetails();
                            modele.modifierCriminel(index, mechant.getNom(), mechant.getPrenom(), "");
                        }
                        afficherDetails();
                    }catch(Exception err){
                        afficherDetails();
                    }
                }
            }
        });

        detailsCriminel.add(panelDetails, BorderLayout.SOUTH);
        detailsCriminel.revalidate();
        detailsCriminel.repaint();
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
        if (index >= 0 && index < modele.getListeCriminel().size()) {
            Criminel c = modele.getListeCriminel().get(index);
            String nom = JOptionPane.showInputDialog(this, "Modifier le nom :", c.getNom());
            String prenom = JOptionPane.showInputDialog(this, "Modifier le prénom :", c.getPrenom());
            if (nom != null && prenom != null) {
                modele.modifierCriminel(index, nom, prenom,c.getDescription());
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

    private void appliquerModifications() {
        int index = listeCriminels.getSelectedIndex();
        if (index >= 0) {
            Criminel mechant = modele.getListeCriminel().get(index);
            modele.modifierCriminel(index, mechant.getNom(), mechant.getPrenom(), mechant.getDescription());
        }
    }

    public void fenetreCrime() {
        this.afficheListeCrime = new ListeCrime(this.str,this);
    }

}