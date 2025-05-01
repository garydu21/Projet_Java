package Vue;

import Criminel.Affaire;
import Criminel.Criminel;
import Modele.Modele;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VueRecherche extends JFrame {
    private JTextField champRecherche;
    private JButton btnRechercher;
    private JTable tableResultats;
    private DefaultTableModel tableModel;
    private Modele modele;
    private JComboBox<String> comboType;

    public VueRecherche(Modele modele) {
        this.modele = modele;

        setTitle("Recherche");
        setSize(700, 400);
        setLayout(new BorderLayout());

        // Combobox du type de recherche
        comboType = new JComboBox<>(new String[]{"Tous", "Criminel", "Affaire"});

        // Champ de recherche
        champRecherche = new JTextField();
        btnRechercher = new JButton("Rechercher");

        JPanel panelHaut = new JPanel(new BorderLayout());
        panelHaut.add(comboType, BorderLayout.WEST);
        panelHaut.add(champRecherche, BorderLayout.CENTER);
        panelHaut.add(btnRechercher, BorderLayout.EAST);
        add(panelHaut, BorderLayout.NORTH);

        // Table pour afficher les résultats
        String[] colonnes = {"Type", "Nom", "Prénom", "Date", "Lieu", "Crimes"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableResultats = new JTable(tableModel);
        tableResultats.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tableResultats), BorderLayout.CENTER);

        // Action de recherche
        btnRechercher.addActionListener(e -> rechercher());

        // Double-clic sur ligne pour afficher les détails
        tableResultats.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tableResultats.getSelectedRow();
                    if (row != -1) {
                        String type = (String) tableModel.getValueAt(row, 0);
                        if ("Criminel".equals(type)) {
                            String nom = (String) tableModel.getValueAt(row, 1);
                            String prenom = (String) tableModel.getValueAt(row, 2);
                            Criminel criminel = modele.getListeCriminel().stream()
                                    .filter(c -> c.getNom().equalsIgnoreCase(nom) && c.getPrenom().equalsIgnoreCase(prenom))
                                    .findFirst().orElse(null);
                            if (criminel != null) {
                                Vue vue = new Vue(null, modele);
                                vue.selectionnerEtAfficherCriminel(criminel);
                                dispose();
                            }
                        } else if ("Affaire".equals(type)) {
                            String description = (String) tableModel.getValueAt(row, 1);
                            String lieu = (String) tableModel.getValueAt(row, 4);
                            Affaire affaire = modele.getListeAffaires().stream()
                                    .filter(a -> a.getDescription().equalsIgnoreCase(description) && a.getLieu().equalsIgnoreCase(lieu))
                                    .findFirst().orElse(null);
                            if (affaire != null) {
                                JOptionPane.showMessageDialog(VueRecherche.this,
                                        "Détails de l'affaire :\n" +
                                                "Description : " + affaire.getDescription() + "\n" +
                                                "Lieu : " + affaire.getLieu() + "\n" +
                                                "Date : " + affaire.getDate());
                            }
                        }
                    }
                }
            }
        });

        setVisible(true);
    }

    private void rechercher() {
        String critere = champRecherche.getText().trim().toLowerCase();
        String selectionType = (String) comboType.getSelectedItem();
        tableModel.setRowCount(0); // Clear previous results

        if ("Tous".equals(selectionType) || "Criminel".equals(selectionType)) {
            List<Criminel> criminels = modele.getListeCriminel();
            for (Criminel c : criminels) {
                boolean matchesNom = c.getNom().toLowerCase().contains(critere);
                boolean matchesPrenom = c.getPrenom().toLowerCase().contains(critere);
                if (matchesNom || matchesPrenom) {
                    StringBuilder crimesStr = new StringBuilder();
                    for (int i = 0; i < c.getCrimes().size(); i++) {
                        crimesStr.append(c.getCrimes().get(i).getIntitule());
                        if (i != c.getCrimes().size() - 1) {
                            crimesStr.append (", ");
                        }
                    }
                    tableModel.addRow(new Object[]{
                            "Criminel",
                            c.getNom(),
                            c.getPrenom(),
                            "",
                            "",
                            crimesStr.toString()
                    });
                }
            }
        }

        if ("Tous".equals(selectionType) || "Affaire".equals(selectionType)) {
            List<Affaire> affaires = modele.getListeAffaires();
            for (Affaire a : affaires) {
                boolean matchesDesc = a.getDescription().toLowerCase().contains(critere);
                boolean matchesLieu = a.getLieu().toLowerCase().contains(critere);
                if (matchesDesc || matchesLieu) {
                    tableModel.addRow(new Object[]{
                            "Affaire",
                            a.getDescription(),
                            "",
                            a.getDate() != null ? a.getDate().toString() : "",
                            a.getLieu(),
                            ""
                    });
                }
            }
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Aucun résultat trouvé.");
        }
    }
}