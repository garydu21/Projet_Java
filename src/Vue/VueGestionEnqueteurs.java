package Vue;

import Criminel.Enqueteur;
import Criminel.Affaire;
import Interface.RoundedBorder;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VueGestionEnqueteurs extends JFrame {

    private final Font fontButton = new Font("Arial", Font.BOLD, 15);
    private final Font fontName = new Font("Arial", Font.ITALIC + Font.BOLD, 15);
    private final Font fontDetail = new Font("Arial", Font.BOLD, 15);

    private Modele modele;
    private JList<String> listeEnqueteurs;
    private DefaultListModel<String> modeleListeEnqueteurs;
    private JTextArea detailsEnqueteur;

    public VueGestionEnqueteurs(Modele modele) {
        this.modele = modele;

        setTitle("Gestion des Enquêteurs");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel du haut
        JPanel panelHaut = new JPanel();
        panelHaut.setBackground(Color.LIGHT_GRAY);
        JLabel labelTitre = new JLabel("Liste des Enquêteurs");
        labelTitre.setFont(new Font("Arial", Font.BOLD, 20));
        panelHaut.add(labelTitre);
        add(panelHaut, BorderLayout.NORTH);

        // Panel central avec la liste des enquêteurs à gauche et les détails à droite
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));

        // Liste des enquêteurs
        modeleListeEnqueteurs = new DefaultListModel<>();
        listeEnqueteurs = new JList<>(modeleListeEnqueteurs);
        listeEnqueteurs.setFont(fontName);
        listeEnqueteurs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollListeEnqueteurs = new JScrollPane(listeEnqueteurs);
        panelCentral.add(scrollListeEnqueteurs);

        // Détails de l'enquêteur
        detailsEnqueteur = new JTextArea();
        detailsEnqueteur.setFont(fontDetail);
        detailsEnqueteur.setEditable(false);
        JScrollPane scrollDetailsEnqueteur = new JScrollPane(detailsEnqueteur);
        panelCentral.add(scrollDetailsEnqueteur);

        add(panelCentral, BorderLayout.CENTER);

        // Panel des boutons
        JPanel panelBoutons = new JPanel();
        panelBoutons.setBackground(Color.LIGHT_GRAY);

        JButton btnVoirAffaires = new JButton("Voir Affaires Assignées");
        btnVoirAffaires.setBorder(new RoundedBorder(10));
        btnVoirAffaires.setFont(fontButton);
        btnVoirAffaires.setBackground(Color.WHITE);
        btnVoirAffaires.setForeground(Color.BLACK);

        JButton btnModifier = new JButton("Modifier Enquêteur");
        btnModifier.setBorder(new RoundedBorder(10));
        btnModifier.setFont(fontButton);
        btnModifier.setBackground(Color.WHITE);
        btnModifier.setForeground(Color.BLACK);

        JButton btnFermer = new JButton("Fermer");
        btnFermer.setBorder(new RoundedBorder(10));
        btnFermer.setFont(fontButton);
        btnFermer.setBackground(Color.WHITE);
        btnFermer.setForeground(Color.BLACK);

        panelBoutons.add(btnVoirAffaires);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnFermer);
        add(panelBoutons, BorderLayout.SOUTH);

        // Remplir la liste des enquêteurs
        chargerListeEnqueteurs();

        // Ajouter les listeners
        listeEnqueteurs.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                afficherDetailsEnqueteur();
            }
        });

        btnVoirAffaires.addActionListener(e -> voirAffairesAssignees());
        btnModifier.addActionListener(e -> modifierEnqueteur());
        btnFermer.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void chargerListeEnqueteurs() {
        modeleListeEnqueteurs.clear();

        // Débugger - vérifier combien d'enquêteurs sont chargés
        System.out.println("Nombre d'enquêteurs: " + modele.getListeEnqueteurs().size());

        for (Enqueteur e : modele.getListeEnqueteurs()) {
            // Débugger - afficher les données de chaque enquêteur
            System.out.println("Enquêteur: " + e.getNom() + " " + e.getPrenom() + " (" + e.getGrade() + ")");

            String nom = e.getNom() != null ? e.getNom() : "Sans nom";
            String prenom = e.getPrenom() != null ? e.getPrenom() : "Sans prénom";
            String grade = e.getGrade() != null ? e.getGrade() : "Sans grade";

            // Créer un texte descriptif
            String texteAffichage = grade + " " + nom + " " + prenom;
            System.out.println("Ajout de l'élément: " + texteAffichage);

            modeleListeEnqueteurs.addElement(texteAffichage);
        }

        // S'assurer que le JList a un renderer adéquat
        listeEnqueteurs.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel) {
                    if (value != null) {
                        ((JLabel) renderer).setText(value.toString());
                    } else {
                        ((JLabel) renderer).setText("Enquêteur sans nom");
                    }
                }
                return renderer;
            }
        });

        // Forcer une actualisation de l'interface
        listeEnqueteurs.repaint();

        // Sélectionner le premier élément s'il y en a
        if (!modeleListeEnqueteurs.isEmpty()) {
            listeEnqueteurs.setSelectedIndex(0);
        }
    }

    private void afficherDetailsEnqueteur() {
        int index = listeEnqueteurs.getSelectedIndex();
        if (index >= 0) {
            Enqueteur enqueteur = modele.getListeEnqueteurs().get(index);
            StringBuilder sb = new StringBuilder();
            sb.append("ID: ").append(enqueteur.getId() != null ? enqueteur.getId() : "Non défini").append("\n");
            sb.append("Nom: ").append(enqueteur.getNom() != null ? enqueteur.getNom() : "Non défini").append("\n");
            sb.append("Prénom: ").append(enqueteur.getPrenom() != null ? enqueteur.getPrenom() : "Non défini").append("\n");
            sb.append("Grade: ").append(enqueteur.getGrade() != null ? enqueteur.getGrade() : "Non défini").append("\n\n");

            List<Affaire> affaires = enqueteur.getAffairesAssignees();
            // Vérifier si la liste est null avant d'appeler isEmpty()
            if (affaires == null) {
                // Si la liste est null, initialisons-la
                affaires = new ArrayList<>();
                enqueteur.setAffairesAssignees(affaires);
            }

            if (affaires.isEmpty()) {
                sb.append("Aucune affaire assignée à cet enquêteur.");
            } else {
                sb.append("Affaires assignées (").append(affaires.size()).append("):\n");
                for (Affaire a : affaires) {
                    sb.append(" - ").append(a.getDescription()).append(" (").append(a.getLieu()).append(")\n");
                    sb.append("   État: ").append(a.getEtat()).append("\n");
                }
            }

            detailsEnqueteur.setText(sb.toString());
        } else {
            detailsEnqueteur.setText("");
        }
    }

    private void voirAffairesAssignees() {
        int index = listeEnqueteurs.getSelectedIndex();
        if (index >= 0) {
            Enqueteur enqueteur = modele.getListeEnqueteurs().get(index);
            List<Affaire> affaires = enqueteur.getAffairesAssignees();

            // Vérifier si la liste est null
            if (affaires == null) {
                affaires = new ArrayList<>();
                enqueteur.setAffairesAssignees(affaires);
            }

            if (affaires.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Aucune affaire assignée à " + enqueteur.getGrade() + " " + enqueteur.getNom() + " " + enqueteur.getPrenom(),
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Création d'une fenêtre pour afficher les affaires
            JDialog dialog = new JDialog(this, "Affaires assignées à " + enqueteur.getGrade() + " " + enqueteur.getNom() + " " + enqueteur.getPrenom(), true);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());

            DefaultListModel<String> modeleAffaires = new DefaultListModel<>();
            for (Affaire a : affaires) {
                modeleAffaires.addElement("Affaire #" + a.getId() + " - " + a.getDescription() + " (" + a.getLieu() + ")");
            }

            JList<String> listeAffaires = new JList<>(modeleAffaires);
            listeAffaires.setFont(fontName);
            dialog.add(new JScrollPane(listeAffaires), BorderLayout.CENTER);

            JButton btnFermerDialog = new JButton("Fermer");
            btnFermerDialog.addActionListener(e -> dialog.dispose());
            JPanel panelBoutonDialog = new JPanel();
            panelBoutonDialog.add(btnFermerDialog);
            dialog.add(panelBoutonDialog, BorderLayout.SOUTH);

            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un enquêteur",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierEnqueteur() {
        int index = listeEnqueteurs.getSelectedIndex();
        if (index >= 0) {
            Enqueteur enqueteur = modele.getListeEnqueteurs().get(index);

            JTextField nomField = new JTextField(enqueteur.getNom());
            JTextField prenomField = new JTextField(enqueteur.getPrenom());

            String[] grades = {"Agent", "Inspecteur", "Lieutenant", "Capitaine", "Commissaire", "Inspecteur Principal"};
            JComboBox<String> gradeCombo = new JComboBox<>(grades);
            gradeCombo.setSelectedItem(enqueteur.getGrade());

            JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
            panel.add(new JLabel("Nom:"));
            panel.add(nomField);
            panel.add(new JLabel("Prénom:"));
            panel.add(prenomField);
            panel.add(new JLabel("Grade:"));
            panel.add(gradeCombo);

            int result = JOptionPane.showConfirmDialog(this, panel,
                    "Modifier l'enquêteur", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String nouveauNom = nomField.getText().trim();
                String nouveauPrenom = prenomField.getText().trim();
                String nouveauGrade = (String) gradeCombo.getSelectedItem();

                // Vérifier que les champs ne sont pas vides
                if (nouveauNom.isEmpty() || nouveauPrenom.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Le nom et le prénom ne peuvent pas être vides",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Mettre à jour l'enquêteur
                enqueteur.setNom(nouveauNom);
                enqueteur.setPrenom(nouveauPrenom);
                enqueteur.setGrade(nouveauGrade);

                // Sauvegarder les changements
                modele.sauvegarderEnqueteurs();
                modele.notifierChangement();

                // Mettre à jour l'affichage
                chargerListeEnqueteurs();
                listeEnqueteurs.setSelectedIndex(index);
                afficherDetailsEnqueteur();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un enquêteur",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}