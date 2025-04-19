package Vue;

import Criminel.Affaire;
import Criminel.Criminel;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VueAffaires extends JFrame {
    private JList<String> listeAffaires;
    private DefaultListModel<String> listeModel;
    private JTextArea detailsAffaire;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnAssocierCriminel;
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
        panelHaut.add(label);
        add(panelHaut, BorderLayout.NORTH);

        // Centre
        JPanel panelCentre = new JPanel(new GridLayout(1, 2));
        listeModel = new DefaultListModel<>();
        listeAffaires = new JList<>(listeModel);
        panelCentre.add(new JScrollPane(listeAffaires));

        detailsAffaire = new JTextArea();
        detailsAffaire.setEditable(false);
        panelCentre.add(new JScrollPane(detailsAffaire));
        add(panelCentre, BorderLayout.CENTER);

        // Bas
        JPanel panelBas = new JPanel();
        btnAjouter = new JButton("Ajouter");
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");

        btnAssocierCriminel = new JButton("Associer Criminel");
        JButton btnDissocierCriminel = new JButton("Retirer Criminel");
        JButton btnAjouterPlusieurs = new JButton("Associer plusieurs");

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

    public VueAffaires(Modele modele, String lieu) {
        this.lieu = lieu;
        this.modele = modele;

        setTitle("Gestion des Affaires");
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Haut
        JPanel panelHaut = new JPanel();
        JLabel label = new JLabel("Base des affaires criminelles");
        panelHaut.add(label);
        add(panelHaut, BorderLayout.NORTH);

        // Centre
        JPanel panelCentre = new JPanel(new GridLayout(1, 2));
        listeModel = new DefaultListModel<>();
        listeAffaires = new JList<>(listeModel);
        panelCentre.add(new JScrollPane(listeAffaires));

        detailsAffaire = new JTextArea();
        detailsAffaire.setEditable(false);
        panelCentre.add(new JScrollPane(detailsAffaire));
        add(panelCentre, BorderLayout.CENTER);

        // Bas
        JPanel panelBas = new JPanel();
        btnAjouter = new JButton("Ajouter");
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");

        btnAssocierCriminel = new JButton("Associer Criminel");
        JButton btnDissocierCriminel = new JButton("Retirer Criminel");
        JButton btnAjouterPlusieurs = new JButton("Associer plusieurs");

        panelBas.add(btnAjouter);
        panelBas.add(btnModifier);
        panelBas.add(btnSupprimer);

        panelBas.add(btnAssocierCriminel);
        panelBas.add(btnAjouterPlusieurs);
        panelBas.add(btnDissocierCriminel);
        add(panelBas, BorderLayout.SOUTH);

        // Actions
        mettreAJourAffichage();

        listeAffaires.addListSelectionListener(e -> afficherDetails());
        btnAjouter.addActionListener(e -> ajouterAffaire());
        btnModifier.addActionListener(e -> modifierAffaire());
        btnSupprimer.addActionListener(e -> supprimerAffaire());
        btnAssocierCriminel.addActionListener(e -> associerCriminel());
        btnAjouterPlusieurs.addActionListener(e -> associerCriminelsMultiples());
        btnDissocierCriminel.addActionListener(e -> dissocierCriminel());

        setVisible(true);
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

            affaire.setDescription(desc);
            affaire.setLieu(lieu);
            if (desc != null && lieu != null && dateStr != null) {
                try {

                    affaire.setDate(java.sql.Date.valueOf(dateStr));
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Format de date invalide. Veuillez entrer la date au format yyyy-MM-dd.");
                }
            }
            modele.sauvegarderAffaires();
            mettreAJourAffichage();
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
