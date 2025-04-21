package Vue;

import Criminel.Affaire;
import Criminel.Criminel;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VueRecherche extends JFrame {
    private JTextField champRecherche;
    private JButton btnRechercher;
    private JTextArea resultatRecherche;
    private Modele modele;

    public VueRecherche(Modele modele) {
        this.modele = modele;

        setTitle("Recherche");
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Champ de recherche
        champRecherche = new JTextField();
        btnRechercher = new JButton("Rechercher");

        JPanel panelHaut = new JPanel();
        panelHaut.setLayout(new BorderLayout());
        panelHaut.add(champRecherche, BorderLayout.CENTER);
        panelHaut.add(btnRechercher, BorderLayout.EAST);

        add(panelHaut, BorderLayout.NORTH);

        // Zone de résultats
        resultatRecherche = new JTextArea();
        resultatRecherche.setEditable(false);
        add(new JScrollPane(resultatRecherche), BorderLayout.CENTER);

        // Action de recherche
        btnRechercher.addActionListener(e -> rechercher());

        setVisible(true);
    }

    private void rechercher() {
        String critere = champRecherche.getText().trim();
        StringBuilder sb = new StringBuilder();

        // Recherche dans les criminels
        List<Criminel> criminels = modele.getListeCriminel();
        for (Criminel c : criminels) {
            if (c.getNom().contains(critere) || c.getPrenom().contains(critere)) {
                sb.append("Criminel: ").append(c.getNom()).append(" ").append(c.getPrenom()).append("\n");
            }
        }

        // Recherche dans les affaires
        List<Affaire> affaires = modele.getListeAffaires();
        for (Affaire a : affaires) {
            if (a.getDescription().contains(critere) || a.getLieu().contains(critere)) {
                sb.append("Affaire: ").append(a.getDescription()).append(" à ").append(a.getLieu()).append("\n");
            }
        }

        // Affichage des résultats
        if (sb.length() == 0) {
            sb.append("Aucun résultat trouvé.");
        }
        resultatRecherche.setText(sb.toString());
    }
}