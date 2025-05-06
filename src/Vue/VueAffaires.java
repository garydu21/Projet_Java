package Vue;

import Criminel.Affaire;
import Criminel.Criminel;
import Interface.RoundedBorder;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VueAffaires extends JFrame {

    private final Font fontButton = new Font("Arial", Font.BOLD, 15);
    private final Font fontName = new Font("Arial", Font.ITALIC + Font.BOLD, 15);
    private final Font fontDetail = new Font("Arial", Font.BOLD, 15);
    private final Font fontTitre = new Font("Arial", Font.BOLD, 30);


    private JList<String> listeAffaires;
    private DefaultListModel<String> listeModel;
    private JTextArea detailsAffaire;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnAssocierCriminel, btnPrediciton, btnGraphe;
    private Modele modele;


    private String lieu;

    public VueAffaires(Modele modele) {
        this.modele = modele;

        setTitle("Gestion des Affaires");
        setSize(800, 600);
        setLayout(new BorderLayout());
        // Haut
        JPanel panelHaut = new JPanel();
        JLabel label = new JLabel("Base des affaires criminelles");
        label.setFont(fontTitre);
        btnGraphe = new JButton("Graphe");
        btnGraphe.setBorder(new RoundedBorder(10));
        btnGraphe.setFont(fontButton);
        btnGraphe.setBackground(Color.LIGHT_GRAY);
        btnGraphe.setForeground(Color.BLACK);

        panelHaut.add(btnGraphe, BorderLayout.WEST);
        panelHaut.add(label);
        JButton btnPrediction = new JButton("Predire une affaire");
        btnPrediction.setBorder(new RoundedBorder(10));
        btnPrediction.setFont(fontButton);
        btnPrediction.setBackground(Color.LIGHT_GRAY);
        btnPrediction.setForeground(Color.BLACK);
        panelHaut.add(btnPrediction);
        add(panelHaut, BorderLayout.NORTH);

        // Centre
        JPanel panelCentre = new JPanel(new GridLayout(1, 2));
        listeModel = new DefaultListModel<>();
        listeAffaires = new JList<>(listeModel);
        listeAffaires.setBackground(Color.DARK_GRAY);
        listeAffaires.setFont(fontName);
        listeAffaires.setForeground(Color.WHITE);
        panelCentre.add(new JScrollPane(listeAffaires));

        detailsAffaire = new JTextArea();
        detailsAffaire.setFont(fontDetail);
        detailsAffaire.setBackground(Color.DARK_GRAY);
        detailsAffaire.setForeground(Color.LIGHT_GRAY);
        detailsAffaire.setEditable(false);
        panelCentre.add(new JScrollPane(detailsAffaire));
        add(panelCentre, BorderLayout.CENTER);

        // Bas
        JPanel panelBas = new JPanel();
        panelBas.setBackground(Color.LIGHT_GRAY);

        btnAjouter = new JButton("Ajouter");
        btnAjouter.setBorder(new RoundedBorder(10));
        btnAjouter.setFont(fontButton);
        btnAjouter.setBackground(Color.LIGHT_GRAY);
        btnAjouter.setForeground(Color.BLACK);

        btnModifier = new JButton("Modifier");
        btnModifier.setBorder(new RoundedBorder(10));
        btnModifier.setFont(fontButton);
        btnModifier.setBackground(Color.LIGHT_GRAY);
        btnModifier.setForeground(Color.BLACK);

        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBorder(new RoundedBorder(10));
        btnSupprimer.setFont(fontButton);
        btnSupprimer.setBackground(Color.LIGHT_GRAY);
        btnSupprimer.setForeground(Color.BLACK);

        btnAssocierCriminel = new JButton("Associer Criminel");
        btnAssocierCriminel.setBorder(new RoundedBorder(10));
        btnAssocierCriminel.setFont(fontButton);
        btnAssocierCriminel.setBackground(Color.LIGHT_GRAY);
        btnAssocierCriminel.setForeground(Color.BLACK);

        JButton btnDissocierCriminel = new JButton("Retirer Criminel");
        btnDissocierCriminel.setBorder(new RoundedBorder(10));
        btnDissocierCriminel.setFont(fontButton);
        btnDissocierCriminel.setBackground(Color.LIGHT_GRAY);
        btnDissocierCriminel.setForeground(Color.BLACK);

        JButton btnAjouterPlusieurs = new JButton("Associer plusieurs");
        btnAjouterPlusieurs.setBorder(new RoundedBorder(10));
        btnAjouterPlusieurs.setFont(fontButton);
        btnAjouterPlusieurs.setBackground(Color.LIGHT_GRAY);
        btnAjouterPlusieurs.setForeground(Color.BLACK);

        panelBas.add(btnAjouter);
        panelBas.add(btnModifier);
        panelBas.add(btnSupprimer);

        panelBas.add(btnAssocierCriminel);
        panelBas.add(btnAjouterPlusieurs);
        panelBas.add(btnDissocierCriminel);
        add(panelBas, BorderLayout.SOUTH);

        // Actions
        mettreAJourListe();

        listeAffaires.addListSelectionListener(e -> afficherDetails());
        btnAjouter.addActionListener(e -> ajouterAffaire());
        btnModifier.addActionListener(e -> modifierAffaire());
        btnSupprimer.addActionListener(e -> supprimerAffaire());
        btnAssocierCriminel.addActionListener(e -> associerCriminel());
        btnAjouterPlusieurs.addActionListener(e -> associerCriminelsMultiples());
        btnDissocierCriminel.addActionListener(e -> dissocierCriminel());
        btnPrediction.addActionListener(e -> predireAffaire());
        btnGraphe.addActionListener(e -> genererUnGraphe());

        setVisible(true);
    }

    private void genererUnGraphe(){
        int index = listeAffaires.getSelectedIndex();
        if (index >= 0) {
            new VueGraphe(this.modele, index);
        }
        else{
            JOptionPane.showMessageDialog(this, "Veuillez selectionner une affaire","Erreur",JOptionPane.ERROR_MESSAGE);
        }
    }

    public VueAffaires(Modele modele, String lieu) {
        this.lieu = lieu;
        this.modele = modele;

        setTitle("Gestion des Affaires");
        setSize(800, 600);
        setLayout(new BorderLayout());
        // Haut
        JPanel panelHaut = new JPanel();
        JLabel label = new JLabel("Base des affaires criminelles");
        label.setFont(fontTitre);
        btnGraphe = new JButton("Graphe");
        btnGraphe.setBorder(new RoundedBorder(10));
        btnGraphe.setFont(fontButton);
        btnGraphe.setBackground(Color.LIGHT_GRAY);
        btnGraphe.setForeground(Color.BLACK);

        panelHaut.add(btnGraphe, BorderLayout.WEST);
        panelHaut.add(label);
        JButton btnPrediction = new JButton("Predire une affaire");
        btnPrediction.setBorder(new RoundedBorder(10));
        btnPrediction.setFont(fontButton);
        btnPrediction.setBackground(Color.LIGHT_GRAY);
        btnPrediction.setForeground(Color.BLACK);
        add(panelHaut, BorderLayout.NORTH);

        // Centre
        JPanel panelCentre = new JPanel(new GridLayout(1, 2));
        listeModel = new DefaultListModel<>();
        listeAffaires = new JList<>(listeModel);
        listeAffaires.setBackground(Color.DARK_GRAY);
        listeAffaires.setFont(fontName);
        listeAffaires.setForeground(Color.WHITE);
        panelCentre.add(new JScrollPane(listeAffaires));

        detailsAffaire = new JTextArea();
        detailsAffaire.setFont(fontDetail);
        detailsAffaire.setBackground(Color.DARK_GRAY);
        detailsAffaire.setForeground(Color.LIGHT_GRAY);
        detailsAffaire.setEditable(false);
        panelCentre.add(new JScrollPane(detailsAffaire));
        add(panelCentre, BorderLayout.CENTER);

        // Bas
        JPanel panelBas = new JPanel();
        panelBas.setBackground(Color.LIGHT_GRAY);

        btnAjouter = new JButton("Ajouter");
        btnAjouter.setBorder(new RoundedBorder(10));
        btnAjouter.setFont(fontButton);
        btnAjouter.setBackground(Color.LIGHT_GRAY);
        btnAjouter.setForeground(Color.BLACK);

        btnModifier = new JButton("Modifier");
        btnModifier.setBorder(new RoundedBorder(10));
        btnModifier.setFont(fontButton);
        btnModifier.setBackground(Color.LIGHT_GRAY);
        btnModifier.setForeground(Color.BLACK);

        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBorder(new RoundedBorder(10));
        btnSupprimer.setFont(fontButton);
        btnSupprimer.setBackground(Color.LIGHT_GRAY);
        btnSupprimer.setForeground(Color.BLACK);

        btnAssocierCriminel = new JButton("Associer Criminel");
        btnAssocierCriminel.setBorder(new RoundedBorder(10));
        btnAssocierCriminel.setFont(fontButton);
        btnAssocierCriminel.setBackground(Color.LIGHT_GRAY);
        btnAssocierCriminel.setForeground(Color.BLACK);

        JButton btnDissocierCriminel = new JButton("Retirer Criminel");
        btnDissocierCriminel.setBorder(new RoundedBorder(10));
        btnDissocierCriminel.setFont(fontButton);
        btnDissocierCriminel.setBackground(Color.LIGHT_GRAY);
        btnDissocierCriminel.setForeground(Color.BLACK);

        JButton btnAjouterPlusieurs = new JButton("Associer plusieurs");
        btnAjouterPlusieurs.setBorder(new RoundedBorder(10));
        btnAjouterPlusieurs.setFont(fontButton);
        btnAjouterPlusieurs.setBackground(Color.LIGHT_GRAY);
        btnAjouterPlusieurs.setForeground(Color.BLACK);

        panelBas.add(btnAjouter);
        panelBas.add(btnModifier);
        panelBas.add(btnSupprimer);

        panelBas.add(btnAssocierCriminel);
        panelBas.add(btnAjouterPlusieurs);
        panelBas.add(btnDissocierCriminel);
        add(panelBas, BorderLayout.SOUTH);

        // Actions
        mettreAJourListe();

        listeAffaires.addListSelectionListener(e -> afficherDetails());
        btnAjouter.addActionListener(e -> ajouterAffaire());
        btnModifier.addActionListener(e -> modifierAffaire());
        btnSupprimer.addActionListener(e -> supprimerAffaire());
        btnAssocierCriminel.addActionListener(e -> associerCriminel());
        btnAjouterPlusieurs.addActionListener(e -> associerCriminelsMultiples());
        btnDissocierCriminel.addActionListener(e -> dissocierCriminel());
        btnPrediction.addActionListener(e -> predireAffaire());
        btnGraphe.addActionListener(e -> genererUnGraphe());

        setVisible(true);
    }

    public void predireAffaire() {
        Affaire affaire = getAffaireSelectionnee();
        if (affaire != null) {
            new VuePrediction(affaire, this.modele);
        }
        else {
            JOptionPane.showMessageDialog(this, "Veuillez selectionner une affaire", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Affaire getAffaireSelectionnee() {
        int index = listeAffaires.getSelectedIndex();
        if (index < 0) return null;

        List<Affaire> source = (lieu != null) ?
                modele.getListeAffaires().stream()
                        .filter(a -> a.getLieu().equalsIgnoreCase(lieu))
                        .toList()
                : modele.getListeAffaires();

        return source.get(index);
    }

    private void mettreAJourAffichage() {
        if (this.lieu != null) {
            mettreAJourListe(this.lieu);
        } else {
            mettreAJourListe();
        }
    }


    private void mettreAJourListe(String lieu) {
        listeModel.clear();
        for (Affaire a : modele.getListeAffaires()) {
            if (a.getLieu().equalsIgnoreCase(lieu)) {
                listeModel.addElement("Affaire #" + a.getId() + " - " + a.getDescription());
            }
        }
    }

    private void mettreAJourListe() {
        listeModel.clear();
        for (Affaire a : modele.getListeAffaires()) {
            listeModel.addElement("Affaire #" + a.getId() + " - " + a.getDescription());
        }
    }


    private void afficherDetails() {
        int index = listeAffaires.getSelectedIndex();
        if (index >= 0) {
            Affaire affaire;
            if (this.lieu != null) {
                List<Affaire> affairesLieu = modele.getListeAffaires().stream().filter(a -> a.getLieu().equalsIgnoreCase(this.lieu)).toList(); // J'ai aucune idée que comment ça fonctionne, je ferais un version personnel plus tard
                affaire = affairesLieu.get(index);
            }
            else{
                affaire = modele.getListeAffaires().get(index);
            }


            StringBuilder sb = new StringBuilder();
            sb.append("Description : ").append(affaire.getDescription()).append("\n");
            sb.append("Lieu : ").append(affaire.getLieu()).append("\n");
            sb.append("Date : ").append(affaire.getDate()).append("\n");
            sb.append("État : ").append(affaire.getEtat()).append("\n");
            sb.append("Informations supplémentaires : ").append(affaire.getInformationsSupplementaires()).append("\n");

            List<Criminel> suspects = affaire.getSuspects();
            if (!suspects.isEmpty()) {
                sb.append("Suspects :\n");
                for (Criminel c : suspects) {
                    sb.append(" - ").append(c.getNom()).append(" ").append(c.getPrenom()).append("\n");
                }
            }

            detailsAffaire.setText(sb.toString());
        }
    }

    private void ajouterAffaire() {
        try {
            int id = modele.getListeAffaires().size() + 1;
            String description = JOptionPane.showInputDialog(this, "Description :");
            String lieu = JOptionPane.showInputDialog(this, "Lieu :");
            String dateStr = JOptionPane.showInputDialog(this, "Date (yyyy-MM-dd) :");

            String[] etats = {"En cours", "Résolue"};
            JComboBox<String> etatCombo = new JComboBox<>(etats);
            etatCombo.setSelectedIndex(0); // valeur par défaut : "En cours"

            JPanel panelEtat = new JPanel();
            panelEtat.add(new JLabel("État de l'affaire :"));
            panelEtat.add(etatCombo);

            int resultEtat = JOptionPane.showConfirmDialog(this, panelEtat, "Choisir l'état de l'affaire", JOptionPane.OK_CANCEL_OPTION);
            if (resultEtat != JOptionPane.OK_OPTION) return;

            String etat = (String) etatCombo.getSelectedItem();

            String infos = JOptionPane.showInputDialog(this, "Informations supplémentaires :", "");

            if (description == null || lieu == null || dateStr == null || etat == null || infos == null) return;

            java.sql.Date date = java.sql.Date.valueOf(dateStr);

            Affaire affaire = new Affaire(id, description, lieu, date);
            affaire.setEtat(etat);
            affaire.setInformationsSupplementaires(infos);

            modele.ajouterAffaire(affaire);
            mettreAJourAffichage();
            listeAffaires.setSelectedIndex(listeAffaires.getLastVisibleIndex());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création de l'affaire.");
        }
    }

    private void modifierAffaire() {
        Affaire affaire = getAffaireSelectionnee();
        if (affaire != null) {
            String desc = JOptionPane.showInputDialog(this, "Modifier la description :", affaire.getDescription());
            String lieu = JOptionPane.showInputDialog(this, "Modifier le lieu :", affaire.getLieu());
            String dateStr = JOptionPane.showInputDialog(this, "Modifier la date (yyyy-MM-dd) :", affaire.getDate().toString());

            String[] etats = {"En cours", "Résolue"};
            JComboBox<String> etatCombo = new JComboBox<>(etats);
            etatCombo.setSelectedItem(affaire.getEtat());

            JPanel panelEtat = new JPanel();
            panelEtat.add(new JLabel("Modifier l'état :"));
            panelEtat.add(etatCombo);

            int resultEtat = JOptionPane.showConfirmDialog(this, panelEtat, "État", JOptionPane.OK_CANCEL_OPTION);
            if (resultEtat != JOptionPane.OK_OPTION) return;

            String etat = (String) etatCombo.getSelectedItem();

            String infos = JOptionPane.showInputDialog(this, "Modifier les infos supplémentaires :", affaire.getInformationsSupplementaires());

            if (desc != null && lieu != null && dateStr != null && etat != null && infos != null) {
                try {
                    affaire.setDescription(desc);
                    affaire.setLieu(lieu);
                    affaire.setEtat(etat);
                    affaire.setInformationsSupplementaires(infos);
                    affaire.setDate(java.sql.Date.valueOf(dateStr));
                    modele.sauvegarderAffaires();
                    mettreAJourAffichage();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Format de date invalide. Veuillez entrer la date au format yyyy-MM-dd.");
                }
            }
        }
    }

    private void supprimerAffaire() {
        Affaire affaire = getAffaireSelectionnee();
        if (affaire != null) {
            modele.getListeAffaires().remove(affaire);
            modele.sauvegarderAffaires();
            mettreAJourAffichage();
            detailsAffaire.setText("");
        }
    }

    private void associerCriminel() {
        Affaire affaire = getAffaireSelectionnee();
        if (affaire == null) return;

        List<Criminel> criminels = modele.getListeCriminel();

        if (criminels.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun criminel disponible.");
            return;
        }

        String[] noms = criminels.stream()
                .map(c -> c.getNom() + " " + c.getPrenom())
                .toArray(String[]::new);
        JComboBox<String> combo = new JComboBox<>(noms);
        combo.setPreferredSize(new Dimension(300, 25));

        JPanel panel = new JPanel();
        panel.add(new JLabel("Sélectionnez un criminel :"));
        panel.add(combo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Associer un criminel",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int iCriminel = combo.getSelectedIndex();
            if (iCriminel >= 0) {
                Criminel c = criminels.get(iCriminel);
                modele.mettreAJourAffaire(affaire, c);
                mettreAJourAffichage();
                listeAffaires.setSelectedIndex(listeAffaires.getSelectedIndex());
            }
        }
    }

    private void associerCriminelsMultiples() {
        Affaire affaire = getAffaireSelectionnee();
        if (affaire == null) return;

        List<Criminel> criminels = modele.getListeCriminel();

        if (criminels.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun criminel disponible.");
            return;
        }

        String[] noms = criminels.stream()
                .map(c -> c.getNom() + " " + c.getPrenom())
                .toArray(String[]::new);
        JList<String> list = new JList<>(noms);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        int result = JOptionPane.showConfirmDialog(this, scrollPane, "Associer plusieurs criminels",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int[] selectedIndices = list.getSelectedIndices();
            for (int i : selectedIndices) {
                Criminel c = criminels.get(i);
                modele.mettreAJourAffaire(affaire, c);
            }
            mettreAJourAffichage();
            listeAffaires.setSelectedIndex(listeAffaires.getSelectedIndex());
        }
    }

    private void dissocierCriminel() {
        Affaire affaire = getAffaireSelectionnee();
        if (affaire == null) return;

        List<Criminel> suspects = affaire.getSuspects();

        if (suspects.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun criminel associé à cette affaire.");
            return;
        }

        String[] noms = suspects.stream()
                .map(c -> c.getNom() + " " + c.getPrenom())
                .toArray(String[]::new);
        JComboBox<String> combo = new JComboBox<>(noms);
        combo.setPreferredSize(new Dimension(300, 25));

        JPanel panel = new JPanel();
        panel.add(new JLabel("Choisir un criminel à retirer :"));
        panel.add(combo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Retirer criminel",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int index = combo.getSelectedIndex();
            if (index >= 0) {
                Criminel c = suspects.get(index);
                modele.retirerCriminelAffaire(affaire, c);
                mettreAJourAffichage();
                listeAffaires.setSelectedIndex(listeAffaires.getSelectedIndex());
            }
        }
    }
}
