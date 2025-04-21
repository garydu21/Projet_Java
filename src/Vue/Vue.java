package Vue;

import Criminel.Criminel;
import Controleur.Controleur;
import Interface.RoundedBorder;
import Modele.Modele;
import Criminel.Affaire;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import Criminel.Crime;

public class Vue extends JFrame implements Observer {

    private final Font fontButton = new Font("Arial", Font.BOLD, 15);
    private final Font fontName = new Font("Arial", Font.ITALIC + Font.BOLD, 15);
    private final Font fontDetail = new Font("Arial", Font.BOLD, 15);
    private final Font fontDescription = new Font("Bitstream Vera Sans Mono", Font.PLAIN, 15);
    private final Font fontListe = new Font("Arial", Font.BOLD, 12);

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

        panelHaut.setBackground(Color.DARK_GRAY);
        ajouterCrime = new JButton("Liste des Crime");
        ajouterCrime.setBorder(new RoundedBorder(10));
        ajouterCrime.setFont(fontButton);
        ajouterCrime.setBackground(Color.LIGHT_GRAY);
        ajouterCrime.setForeground(Color.BLACK);
        panelHaut.add(ajouterCrime);


        btnGestionJson = new JButton("Gérer JSON");
        btnGestionJson.setBorder(new RoundedBorder(10));
        btnGestionJson.setFont(fontButton);
        btnGestionJson.setBackground(Color.LIGHT_GRAY);
        btnGestionJson.setForeground(Color.BLACK);
        panelHaut.add(btnGestionJson);


        panelHaut.add(new JButton("Base de données"));
        panelHaut.add(new JButton("Maps"));
        add(panelHaut, BorderLayout.NORTH);

        JButton btnAffaires = new JButton("Affaires");
        btnAffaires.setBorder(new RoundedBorder(10));
        btnAffaires.setFont(fontButton);
        btnAffaires.setBackground(Color.LIGHT_GRAY);
        btnAffaires.setForeground(Color.BLACK);
        panelHaut.add(btnAffaires);

        // Centre : Liste des criminels à gauche + Détails à droite
        JPanel panelCentre = new JPanel(new GridLayout(1, 2));
        panelCentre.setBackground(Color.DARK_GRAY);

        listeModel = new DefaultListModel<>();
        listeCriminels = new JList<>(listeModel);
        listeCriminels.setFont(fontName);
        listeCriminels.setBackground(Color.DARK_GRAY);
        listeCriminels.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(listeCriminels);
        panelCentre.add(scrollPane);

        detailsCriminel = new JPanel();
        detailsCriminel.setBackground(Color.DARK_GRAY);
        detailsCriminel.setForeground(Color.WHITE);
        infoCriminel = new JTextArea();
        infoCriminel.setFont(fontDetail);
        infoCriminel.setBackground(Color.DARK_GRAY);
        infoCriminel.setForeground(Color.LIGHT_GRAY);
        infoCriminel.setEditable(false);
        detailsCriminel.add(infoCriminel, BorderLayout.NORTH);

        panelCentre.add(new JScrollPane(detailsCriminel));

        add(panelCentre, BorderLayout.CENTER);

        // Bas : Boutons d'actions
        JPanel panelBas = new JPanel();
        panelBas.setBackground(Color.DARK_GRAY);

        btnModifier = new JButton("Modifier");
        btnModifier.setBorder(new RoundedBorder(10));
        btnModifier.setFont(fontButton);
        btnModifier.setBackground(Color.LIGHT_GRAY);
        btnModifier.setForeground(Color.BLACK);

        btnAjouter = new JButton("Ajouter");
        btnAjouter.setBorder(new RoundedBorder(10));
        btnAjouter.setFont(fontButton);
        btnAjouter.setBackground(Color.LIGHT_GRAY);
        btnAjouter.setForeground(Color.BLACK);

        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBorder(new RoundedBorder(10));
        btnSupprimer.setFont(fontButton);
        btnSupprimer.setBackground(Color.LIGHT_GRAY);
        btnSupprimer.setForeground(Color.BLACK);

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
        btnAffaires.addActionListener(e -> new VueAffaires(modele));

        setVisible(true);
    }

    private void mettreAJourListe() {
        int selectedIndex = listeCriminels.getSelectedIndex();

        listeModel.clear();
        for (Criminel c : modele.getListeCriminel()) {
            listeModel.addElement(c.getNom() + " " + c.getPrenom());
        }

        if (selectedIndex >= 0 && selectedIndex < listeModel.size()) {
            listeCriminels.setSelectedIndex(selectedIndex);
        }
    }

      public void afficherDetails() {

    int index = listeCriminels.getSelectedIndex();
    if (index >= 0) {
        Criminel c = modele.getListeCriminel().get(index);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < c.getCrimes().size(); i++){
            if (c.getCrimes().get(i).getIntitule() != null){
                sb.append(c.getCrimes().get(i).getIntitule());
            }
            if (i != c.getCrimes().size() - 1){
                sb.append(", ");
            }
        }

        String texte = "Nom : " + c.getNom() + "\n"
                + "Prénom : " + c.getPrenom() + "\n"
                + "Peine Totale : " + c.getPeineTotale() + " ans \n"
                + "Arrêter pour : " + (sb.isEmpty() ? "rien" : sb.toString()) + "\n";

        if (c.getAffaires() != null && !c.getAffaires().isEmpty()) {
            texte += "Affaires :\n";
            for (Affaire a : c.getAffaires()) {
                texte += " - " + a.getDescription() + " (" + a.getLieu() + ")\n";
            }
        }

        infoCriminel.setText(texte);
    }

    detailsCriminel.removeAll();
    detailsCriminel.setLayout(new BorderLayout());
    detailsCriminel.add(infoCriminel, BorderLayout.NORTH);

        panelDescription = new JPanel();
        panelDescription.setLayout(new BorderLayout());

        ajouterDescription = new JButton("Ajouter Description");
        ajouterDescription.setBackground(Color.WHITE);
        ajouterDescription.setForeground(Color.BLACK);
        ajouterDescription.setFont(fontButton);
        ajouterDescription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int index = listeCriminels.getSelectedIndex();
                    if (index >= 0 && index < modele.getListeCriminel().size()) {
                        Criminel mechant = modele.getListeCriminel().get(index);
                        modele.modifierCriminel(index, mechant.getNom(), mechant.getPrenom(), description.getText());
                        afficherDetails();
                    }
                }catch (Exception ex) {
                    int index = listeCriminels.getSelectedIndex();
                    if (index >= 0 && index < modele.getListeCriminel().size()) {
                        Criminel mechant = modele.getListeCriminel().get(index);
                        modele.modifierCriminel(index, mechant.getNom(), mechant.getPrenom(), "Une erreur est survenu, veuiller ressaisir le texte");
                        afficherDetails();
                    }
                }
            }
        });

        description = new JTextArea();
        description.setFont(fontDescription);
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
        panelDetails.setBackground(Color.DARK_GRAY);

        elements = new String[]{};
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
        liste.setFont(fontListe);
        panelDetails.add(liste);


        champTexte = new JTextField();
        champTexte.setPreferredSize(new Dimension(200, 20));
        champTexte.setEditable(false);
        panelDetails.add(champTexte);

        JButton ajouterCrime = new JButton("Ajouter");
        ajouterCrime.setBorder(new RoundedBorder(5));
        ajouterCrime.setFont(fontButton);
        ajouterCrime.setBackground(Color.LIGHT_GRAY);
        ajouterCrime.setForeground(Color.BLACK);

        JButton supprimerCrime = new JButton("Réinitialiser");
        supprimerCrime.setBorder(new RoundedBorder(5));
        supprimerCrime.setFont(fontButton);
        supprimerCrime.setBackground(Color.LIGHT_GRAY);
        supprimerCrime.setForeground(Color.BLACK);

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
                    modele.modifierCriminel(index, mechant.getNom(), mechant.getPrenom(), mechant.getDescription());

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
                            boolean aDejaCrime = false;
                            for (Crime c : mechant.getCrimes()){
                                if (c.getIntitule().equals(leCrime.getIntitule())) {
                                    aDejaCrime = true;
                                }
                            }
                            if (!aDejaCrime) {
                                mechant.ajouterCrime(leCrime);
                                modele.modifierCriminel(index, mechant.getNom(), mechant.getPrenom(), mechant.getDescription());
                            }
                            afficherDetails();
                        }
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
            listeCriminels.setSelectedIndex(modele.getListeCriminel().size() - 1);
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
        if (index > 0) {
            listeCriminels.setSelectedIndex(index - 1);
        } else if (!modele.getListeCriminel().isEmpty()) {
            listeCriminels.setSelectedIndex(0);
        }
    }

    private void afficherPopupGestionJson() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem importer = new JMenuItem("Importer JSON");
        importer.setBackground(Color.LIGHT_GRAY);
        importer.setForeground(Color.BLACK);

        JMenuItem exporter = new JMenuItem("Exporter JSON");
        exporter.setBackground(Color.LIGHT_GRAY);
        exporter.setForeground(Color.BLACK);

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

    public ArrayList<Crime> getStr(){
        return this.str;
    }

    public void removeStr(int index){
        this.str.remove(index);
    }

}