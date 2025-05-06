package Vue;

import Enqueteur.Enquete;
import Enqueteur.Enqueteur;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VueEnquetes extends JFrame {

    private Modele modele;

    private DefaultListModel<Enquete> modeleListeEnquetes;
    private JList<Enquete> listeEnquetes;

    private DefaultListModel<Enqueteur> modeleListeEnqueteurs;
    private JList<Enqueteur> listeEnqueteursDisponibles;

    private JTextField inputDescription;
    private JCheckBox checkTerminee;

    private JButton btnAjouter;
    private JButton btnModifier;
    private JButton btnSupprimer;
    private JButton btnSauvegarder;

    private JPanel detailPanel;

    private Enquete enqueteSelectionnee = null;

    public VueEnquetes(Modele modele) {
        this.modele = modele;

        setTitle("Gestion des Enquêtes");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Left panel : list of enquêtes
        modeleListeEnquetes = new DefaultListModel<>();
        listeEnquetes = new JList<>(modeleListeEnquetes);
        listeEnquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollEnquetes = new JScrollPane(listeEnquetes);
        scrollEnquetes.setPreferredSize(new Dimension(250, 0));
        add(scrollEnquetes, BorderLayout.WEST);

        // Right panel : details + buttons
        detailPanel = new JPanel();
        detailPanel.setLayout(new BorderLayout(5,5));
        add(detailPanel, BorderLayout.CENTER);

        // Detail inputs
        JPanel panelFields = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Description label + textfield
        gbc.gridx = 0; gbc.gridy = 0;
        panelFields.add(new JLabel("Description:"), gbc);
        inputDescription = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        panelFields.add(inputDescription, gbc);

        // Checkbox for terminé
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFields.add(new JLabel("Terminé:"), gbc);
        checkTerminee = new JCheckBox();
        gbc.gridx = 1; gbc.gridy = 1;
        panelFields.add(checkTerminee, gbc);

        // Enquêteurs list label + list (multi-selection)
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelFields.add(new JLabel("Enquêteurs associés (Ctrl+Click pour multiselect):"), gbc);

        modeleListeEnqueteurs = new DefaultListModel<>();
        listeEnqueteursDisponibles = new JList<>(modeleListeEnqueteurs);
        listeEnqueteursDisponibles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollEnqueteurs = new JScrollPane(listeEnqueteursDisponibles);
        scrollEnqueteurs.setPreferredSize(new Dimension(200, 100));
        gbc.gridy = 3;
        panelFields.add(scrollEnqueteurs, gbc);

        detailPanel.add(panelFields, BorderLayout.CENTER);

        // Buttons panel
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAjouter = new JButton("Ajouter Enquête");
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");
        btnSauvegarder = new JButton("Sauvegarder");

        panelButtons.add(btnAjouter);
        panelButtons.add(btnModifier);
        panelButtons.add(btnSupprimer);
        panelButtons.add(btnSauvegarder);

        detailPanel.add(panelButtons, BorderLayout.SOUTH);

        // Load enquêtes and enquêteurs into lists
        chargerListeEnquetes();
        chargerListeEnqueteurs();

        // Listeners
        listeEnquetes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectionEnquete();
            }
        });

        btnAjouter.addActionListener(e -> ajouterEnquete());
        btnModifier.addActionListener(e -> modifierEnquete());
        btnSupprimer.addActionListener(e -> supprimerEnquete());
        btnSauvegarder.addActionListener(e -> sauvegarderEnquete());

        // Initialize buttons state
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
        btnSauvegarder.setEnabled(false);

        setVisible(true);
    }

    private void chargerListeEnquetes() {
        modeleListeEnquetes.clear();
        for (Enquete e : modele.getListeEnquetes()) {
            modeleListeEnquetes.addElement(e);
        }
    }

    private void chargerListeEnqueteurs() {
        modeleListeEnqueteurs.clear();
        for (Enqueteur enqueteur : modele.getListeEnqueteurs()) {
            modeleListeEnqueteurs.addElement(enqueteur);
        }
    }

    private void selectionEnquete() {
        enqueteSelectionnee = listeEnquetes.getSelectedValue();
        if (enqueteSelectionnee != null) {
            inputDescription.setText(enqueteSelectionnee.getDescription());
            checkTerminee.setSelected(enqueteSelectionnee.isTerminee());

            // Select enquêteurs in the enquêteurs JList
            List<Enqueteur> associes = enqueteSelectionnee.getEnqueteurs();
            int[] indices = associes.stream()
                    .mapToInt(e -> modeleListeEnqueteurs.indexOf(e))
                    .filter(i -> i >= 0)
                    .toArray();
            listeEnqueteursDisponibles.setSelectedIndices(indices);

            btnModifier.setEnabled(true);
            btnSupprimer.setEnabled(true);
            btnSauvegarder.setEnabled(true);
        } else {
            inputDescription.setText("");
            checkTerminee.setSelected(false);
            listeEnqueteursDisponibles.clearSelection();
            btnModifier.setEnabled(false);
            btnSupprimer.setEnabled(false);
            btnSauvegarder.setEnabled(false);
        }
    }

    private void ajouterEnquete() {
        String description = JOptionPane.showInputDialog(this, "Description de la nouvelle enquête :");
        if (description != null && !description.trim().isEmpty()) {
            Enquete e = new Enquete(description.trim());
            modele.ajouterEnquete(e);

            chargerListeEnquetes();
            listeEnquetes.setSelectedValue(e, true);
        }
    }

    private void modifierEnquete() {
        if (enqueteSelectionnee == null) return;
        String nouvelleDesc = JOptionPane.showInputDialog(this, "Modifier la description :", enqueteSelectionnee.getDescription());
        if (nouvelleDesc != null && !nouvelleDesc.trim().isEmpty()) {
            enqueteSelectionnee.setDescription(nouvelleDesc.trim());
            modele.notifierChangement();
            chargerListeEnquetes();
            listeEnquetes.setSelectedValue(enqueteSelectionnee, true);
        }
    }

    private void supprimerEnquete() {
        if (enqueteSelectionnee == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cette enquête ?", "Confirmer la suppression", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            modele.supprimerEnquete(enqueteSelectionnee);
            chargerListeEnquetes();
            enqueteSelectionnee = null;
            selectionEnquete();
        }
    }

    private void sauvegarderEnquete() {
        if (enqueteSelectionnee == null) return;

        String description = inputDescription.getText().trim();
        boolean estTerminee = checkTerminee.isSelected();

        List<Enqueteur> selectedEnqueteurs = listeEnqueteursDisponibles.getSelectedValuesList();

        enqueteSelectionnee.setDescription(description);
        enqueteSelectionnee.setTerminee(estTerminee);
        enqueteSelectionnee.setEnqueteurs(selectedEnqueteurs);

        modele.notifierChangement();

        chargerListeEnquetes();
        listeEnquetes.setSelectedValue(enqueteSelectionnee, true);
        JOptionPane.showMessageDialog(this, "Enquête sauvegardée avec succès !");
    }
}