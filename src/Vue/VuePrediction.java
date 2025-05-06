package Vue;

import Criminel.Affaire;
import Criminel.Criminel;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.util.List; // Import explicite de java.util.List
import java.util.*;
import java.util.stream.Collectors;

public class VuePrediction extends JFrame {

    private final Font fontButton = new Font("Arial", Font.BOLD, 15);
    private final Font fontName = new Font("Arial", Font.ITALIC + Font.BOLD, 20);
    private final Font fontDetail = new Font("Arial", Font.BOLD, 15);
    private final Font fontDescription = new Font("Bitstream Vera Sans Mono", Font.PLAIN + Font.BOLD, 15);
    private final Font fontListe = new Font("Arial", Font.BOLD, 12);

    private Affaire affaire;
    private Modele modele;

    public VuePrediction(Affaire affaire, Modele modele) {
        this.affaire = affaire;
        this.modele = modele;


        this.setSize(600, 400);
        this.setTitle("Prediction");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);
        panel.setLayout(new GridLayout(3, 2, 10, 10));


        HashMap<Criminel, String> coupablesEtRaisons = chercherCoupablesEtRaisons();


        String texteCoupables = "Aucun criminel coupable";
        if (!coupablesEtRaisons.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Criminel, String> entry : coupablesEtRaisons.entrySet()) {
                Criminel criminel = entry.getKey();
                String raison = entry.getValue();
                sb.append(criminel.getNom()).append(" ").append(criminel.getPrenom())
                        .append(" - ").append(raison).append("\n");
            }
            texteCoupables = sb.toString(); // Je laisse le .trim() mais je suis pas sûr de l'utilité
        }


        String texteRaison = "Pas de coupable trouvé";
        if (!coupablesEtRaisons.isEmpty()) {
            texteRaison = "Coupables trouvés selon des critères d'affaires résolues similaires.";
        }


        JLabel labelCoupable = new JLabel("Coupable(s) :");
        labelCoupable.setFont(fontName);
        labelCoupable.setBackground(Color.BLACK);
        labelCoupable.setForeground(Color.WHITE);

        JTextArea textCoupable = new JTextArea(texteCoupables);
        textCoupable.setFont(fontDescription);
        textCoupable.setBackground(Color.WHITE);
        textCoupable.setForeground(Color.BLACK);

        textCoupable.setEditable(false);
        textCoupable.setLineWrap(true);
        textCoupable.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textCoupable);

        JLabel labelRaison = new JLabel("Raison :");
        labelRaison.setFont(fontName);
        labelRaison.setBackground(Color.BLACK);
        labelRaison.setForeground(Color.WHITE);

        JTextField textRaison = new JTextField(texteRaison);
        textRaison.setFont(fontDescription);
        textRaison.setBackground(Color.WHITE);
        textRaison.setForeground(Color.BLACK);

        textRaison.setEditable(false);

        String texteAffaireSelectionnee = affaire.getId() + " - " + affaire.getDescription() + " au " + affaire.getLieu();

        JLabel labelAffaire = new JLabel("Affaire Sélectionnée :");
        labelAffaire.setFont(fontName);
        labelAffaire.setBackground(Color.BLACK);
        labelAffaire.setForeground(Color.WHITE);

        JTextField textAffaire = new JTextField(texteAffaireSelectionnee);
        textAffaire.setFont(fontDescription);
        textAffaire.setBackground(Color.WHITE);
        textAffaire.setForeground(Color.BLACK);

        textAffaire.setEditable(false);

        panel.add(labelAffaire);
        panel.add(textAffaire);


        panel.add(labelCoupable);
        panel.add(scrollPane);

        panel.add(labelRaison);
        panel.add(textRaison);

        panel.add(labelAffaire);
        panel.add(textAffaire);

        this.add(panel);
        this.setVisible(true);
    }

    private HashMap<Criminel, String> chercherCoupablesEtRaisons() {
        HashMap<Criminel, String> coupablesEtRaisons = new HashMap<>();

        List<Affaire> affairesResolues = new ArrayList<>();
        for (Affaire a : modele.getListeAffaires()) {
            if ("Résolue".equalsIgnoreCase(a.getEtat())) {
                affairesResolues.add(a);
            }
        }


        for (Criminel suspect : affaire.getSuspects()) {


            for (Affaire autreAffaire : suspect.getAffaires()) {
                if ("Résolue".equalsIgnoreCase(autreAffaire.getEtat()) && !autreAffaire.equals(affaire)) {
                    coupablesEtRaisons.put(suspect, "Coupable de l'affaire " + autreAffaire.getDescription());
                }
            }

            boolean affaireSimilaireTrouvee = false;

            for (Affaire affaireResolue : affairesResolues) {
                if (affaireResolue.getDescription().equalsIgnoreCase(affaire.getDescription()) &&
                        affaireResolue.getLieu().equalsIgnoreCase(affaire.getLieu())) {
                    affaireSimilaireTrouvee = true;
                }
            }

            if (affaireSimilaireTrouvee) {
                if (!coupablesEtRaisons.containsKey(suspect)) {
                    coupablesEtRaisons.put(suspect, "Affaire similaire : " + affaire.getDescription() + " au " + affaire.getLieu());
                }
            }
        }

        return coupablesEtRaisons;
    }
}