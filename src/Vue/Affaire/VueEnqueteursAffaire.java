package Vue.Affaire;

import Criminel.Affaire;
import Enqueteur.Enqueteur;
import Interface.RoundedBorder;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VueEnqueteursAffaire extends JFrame {

    private final Font fontButton = new Font("Arial", Font.BOLD, 15);
    private final Font fontName = new Font("Arial", Font.ITALIC + Font.BOLD, 15);
    private final Font fontDetail = new Font("Arial", Font.BOLD, 15);

    private Modele modele;
    private Affaire affaireSelectionnee;
    private JList<String> listeEnqueteursDisponibles;
    private JList<String> listeEnqueteursAssignes;
    private DefaultListModel<String> modelDisponibles;
    private DefaultListModel<String> modelAssignes;

    public VueEnqueteursAffaire(Modele modele, Affaire affaire) {
        this.modele = modele;
        this.affaireSelectionnee = affaire;

        setTitle("Gestion des Enquêteurs pour l'affaire: " + affaire.getDescription());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel du haut - informations sur l'affaire
        JPanel panelHaut = new JPanel();
        panelHaut.setBackground(Color.LIGHT_GRAY);
        JLabel labelAffaire = new JLabel("Affaire: " + affaire.getDescription() + " (" + affaire.getLieu() + ")");
        labelAffaire.setFont(fontName);
        panelHaut.add(labelAffaire);
        add(panelHaut, BorderLayout.NORTH);

        // Panel central - listes d'enquêteurs
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));

        // Panel des enquêteurs disponibles
        JPanel panelDisponibles = new JPanel(new BorderLayout());
        JLabel labelDisponibles = new JLabel("Enquêteurs disponibles");
        labelDisponibles.setFont(fontDetail);
        modelDisponibles = new DefaultListModel<>();
        listeEnqueteursDisponibles = new JList<>(modelDisponibles);
        listeEnqueteursDisponibles.setFont(fontName);
        JScrollPane scrollDisponibles = new JScrollPane(listeEnqueteursDisponibles);
        panelDisponibles.add(labelDisponibles, BorderLayout.NORTH);
        panelDisponibles.add(scrollDisponibles, BorderLayout.CENTER);

        // Panel des enquêteurs assignés
        JPanel panelAssignes = new JPanel(new BorderLayout());
        JLabel labelAssignes = new JLabel("Enquêteurs assignés à l'affaire");
        labelAssignes.setFont(fontDetail);
        modelAssignes = new DefaultListModel<>();
        listeEnqueteursAssignes = new JList<>(modelAssignes);
        listeEnqueteursAssignes.setFont(fontName);
        JScrollPane scrollAssignes = new JScrollPane(listeEnqueteursAssignes);
        panelAssignes.add(labelAssignes, BorderLayout.NORTH);
        panelAssignes.add(scrollAssignes, BorderLayout.CENTER);

        panelCentral.add(panelDisponibles);
        panelCentral.add(panelAssignes);
        add(panelCentral, BorderLayout.CENTER);

        // Panel des boutons
        JPanel panelBoutons = new JPanel();
        panelBoutons.setBackground(Color.LIGHT_GRAY);

        JButton btnAssigner = new JButton("Assigner >>");
        btnAssigner.setBorder(new RoundedBorder(10));
        btnAssigner.setFont(fontButton);
        btnAssigner.setBackground(Color.WHITE);
        btnAssigner.setForeground(Color.BLACK);

        JButton btnRetirer = new JButton("<< Retirer");
        btnRetirer.setBorder(new RoundedBorder(10));
        btnRetirer.setFont(fontButton);
        btnRetirer.setBackground(Color.WHITE);
        btnRetirer.setForeground(Color.BLACK);

        JButton btnFermer = new JButton("Fermer");
        btnFermer.setBorder(new RoundedBorder(10));
        btnFermer.setFont(fontButton);
        btnFermer.setBackground(Color.WHITE);
        btnFermer.setForeground(Color.BLACK);

        panelBoutons.add(btnAssigner);
        panelBoutons.add(btnRetirer);
        panelBoutons.add(btnFermer);
        add(panelBoutons, BorderLayout.SOUTH);

        // Chargement des listes
        actualiserListes();

        // Actions des boutons
        btnAssigner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignerEnqueteurSelectionne();
            }
        });

        btnRetirer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retirerEnqueteurSelectionne();
            }
        });

        btnFermer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void actualiserListes() {
        modelDisponibles.clear();
        modelAssignes.clear();

        List<Enqueteur> enqueteursAssignes = affaireSelectionnee.getEnqueteurs();

        for (Enqueteur e : modele.getListeEnqueteurs()) {
            if (enqueteursAssignes.contains(e)) {
                modelAssignes.addElement(e.getNom() + " " + e.getPrenom() + " (" + e.getGrade() + ")");
            } else {
                modelDisponibles.addElement(e.getNom() + " " + e.getPrenom() + " (" + e.getGrade() + ")");
            }
        }
    }

    private void assignerEnqueteurSelectionne() {
        int index = listeEnqueteursDisponibles.getSelectedIndex();
        if (index >= 0) {
            // Trouver l'enquêteur correspondant dans la liste des disponibles
            List<Enqueteur> disponibles = modele.getListeEnqueteurs().stream()
                    .filter(e -> !affaireSelectionnee.getEnqueteurs().contains(e))
                    .toList();

            if (index < disponibles.size()) {
                Enqueteur enqueteur = disponibles.get(index);
                modele.assignerEnqueteurAffaire(enqueteur, affaireSelectionnee);
                actualiserListes();
            }
        }
    }

    private void retirerEnqueteurSelectionne() {
        int index = listeEnqueteursAssignes.getSelectedIndex();
        if (index >= 0) {
            List<Enqueteur> assignes = affaireSelectionnee.getEnqueteurs();
            if (index < assignes.size()) {
                Enqueteur enqueteur = assignes.get(index);
                modele.retirerEnqueteurAffaire(enqueteur, affaireSelectionnee);
                actualiserListes();
            }
        }
    }
}