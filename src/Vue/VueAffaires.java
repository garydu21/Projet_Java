package Vue;

import Criminel.Affaire;
import Criminel.Criminel;
import Interface.RoundedBorder;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VueAffaires extends JFrame {

    private final Font fontButton = new Font("Arial", Font.BOLD, 15);
    private final Font fontName = new Font("Arial", Font.ITALIC + Font.BOLD, 15);
    private final Font fontDetail = new Font("Arial", Font.BOLD, 15);
    private final Font fontTitre = new Font("Arial", Font.BOLD, 30);


    private JList<String> listeAffaires;
    private DefaultListModel<String> listeModel;
    private JTextArea detailsAffaire;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnAssocierCriminel;
    private Modele modele;

    public VueAffaires(Modele modele) {
        this.modele = modele;

        setTitle("Gestion des Affaires");
        setSize(800, 600);
        setLayout(new BorderLayout());
        // Haut
        JPanel panelHaut = new JPanel();
        JLabel label = new JLabel("Base des affaires criminelles");
        label.setFont(fontTitre);
        panelHaut.add(label);
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

        setVisible(true);
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
            Affaire affaire = modele.getListeAffaires().get(index);
            StringBuilder sb = new StringBuilder();
            sb.append("Description : ").append(affaire.getDescription()).append("\n");
            sb.append("Lieu : ").append(affaire.getLieu()).append("\n");
            sb.append("Date : ").append(affaire.getDate()).append("\n");

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
            java.sql.Date date = java.sql.Date.valueOf(dateStr);

            Affaire affaire = new Affaire(id, description, lieu, date);
            modele.ajouterAffaire(affaire);
            mettreAJourListe();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création de l'affaire.");
        }
    }

    private void modifierAffaire() {
        int index = listeAffaires.getSelectedIndex();
        if (index >= 0) {
            Affaire affaire = modele.getListeAffaires().get(index);
            String desc = JOptionPane.showInputDialog(this, "Modifier la description :", affaire.getDescription());
            String lieu = JOptionPane.showInputDialog(this, "Modifier le lieu :", affaire.getLieu());
            String dateStr = JOptionPane.showInputDialog(this, "Modifier la date (yyyy-MM-dd) :", affaire.getDate().toString());

            affaire.setDescription(desc);
            affaire.setLieu(lieu);
            affaire.setDate(java.sql.Date.valueOf(dateStr));

            modele.ajouterAffaire(affaire);
            mettreAJourListe();
        }
    }

    private void supprimerAffaire() {
        int index = listeAffaires.getSelectedIndex();
        if (index >= 0) {
            modele.supprimerAffaire(index);
            mettreAJourListe();
            detailsAffaire.setText("");
        }
    }

    private void associerCriminel() {
        int iAffaire = listeAffaires.getSelectedIndex();
        if (iAffaire < 0) return;

        Affaire affaire = modele.getListeAffaires().get(iAffaire);
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
                afficherDetails();
            }
        }
    }

    private void associerCriminelsMultiples() {
        int iAffaire = listeAffaires.getSelectedIndex();
        if (iAffaire < 0) return;

        Affaire affaire = modele.getListeAffaires().get(iAffaire);
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
            afficherDetails();
        }
    }

    private void dissocierCriminel() {
        int iAffaire = listeAffaires.getSelectedIndex();
        if (iAffaire < 0) return;

        Affaire affaire = modele.getListeAffaires().get(iAffaire);
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
                afficherDetails();
            }
        }
    }
}
