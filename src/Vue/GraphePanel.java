package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import Criminel.Affaire;
import Criminel.Criminel;

public class GraphePanel extends JPanel {

    private final VueGraphe parentFrame;
    private final List<int[]> coord;
    private final int currentIndex;
    private final List<Affaire> listeAffaires;
    private final List<Criminel> criminelsAffaire;
    private final List<int[]> criminelCoords;
    private final Map<Affaire, int[]> relatedAffairsCoords;

    public GraphePanel(VueGraphe parentFrame, List<int[]> coord, int currentIndex, List<Affaire> listeAffaires,
                       List<Criminel> criminelsAffaire, List<int[]> criminelCoords,
                       Map<Affaire, int[]> relatedAffairsCoords) {
        this.parentFrame = parentFrame;
        this.coord = coord;
        this.currentIndex = currentIndex;
        this.listeAffaires = listeAffaires;
        this.criminelsAffaire = criminelsAffaire;
        this.criminelCoords = criminelCoords;
        this.relatedAffairsCoords = relatedAffairsCoords;

        setupMouseListener();
    }

    private void setupMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                // Affaire principale
                for (int i = 0; i < coord.size(); i++) {
                    int[] affaireCoord = coord.get(i);
                    if (isPointInCircle(mouseX, mouseY, affaireCoord[0], affaireCoord[1], 40)) {
                        Affaire affaire = listeAffaires.get(i);
                        displayInfoInNewWindow("Informations Affaire Principale", affaire.toString());
                        return;
                    }
                }

                // Criminel
                for (int i = 0; i < criminelCoords.size(); i++) {
                    int[] criminelCoord = criminelCoords.get(i);
                    if (isPointInCircle(mouseX, mouseY, criminelCoord[0], criminelCoord[1], 40)) {
                        Criminel criminel = criminelsAffaire.get(i);
                        displayCriminelInfo(criminel);
                        return;
                    }
                }

                // Affaire secondaire
                for (Map.Entry<Affaire, int[]> entry : relatedAffairsCoords.entrySet()) {
                    int[] affaireCoord = entry.getValue();
                    if (isPointInCircle(mouseX, mouseY, affaireCoord[0], affaireCoord[1], 40)) {
                        Affaire affaire = entry.getKey();
                        displayInfoInNewWindow("Informations Affaire Associée", affaire.toString());
                        return;
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Liaisons criminels et affaires associées
        for (int i = 0; i < criminelsAffaire.size(); i++) {
            Criminel criminel = criminelsAffaire.get(i);
            int[] criminelCoord = criminelCoords.get(i);

            for (Affaire affaire : criminel.getAffaires()) {
                int[] affaireCoord;
                if (affaire.equals(listeAffaires.get(currentIndex))) {
                    affaireCoord = coord.get(currentIndex);
                } else {
                    affaireCoord = relatedAffairsCoords.get(affaire);
                }

                if (affaireCoord != null) {
                    g.setColor(Color.GRAY);
                    g.drawLine(criminelCoord[0] + 20, criminelCoord[1] + 20,
                            affaireCoord[0] + 20, affaireCoord[1] + 20);
                }
            }
        }

        // Dessiner l'affaire principale (rouge)
        int xCurrent = coord.get(currentIndex)[0];
        int yCurrent = coord.get(currentIndex)[1];
        g.setColor(Color.RED);
        g.fillOval(xCurrent, yCurrent, 40, 40);
        g.setColor(Color.BLACK);
        g.drawString(listeAffaires.get(currentIndex).getDescription(), xCurrent - 10, yCurrent + 60);

        // Dessiner les criminels (bleu)
        g.setColor(Color.BLUE);
        for (int i = 0; i < criminelsAffaire.size(); i++) {
            Criminel criminel = criminelsAffaire.get(i);
            int[] coords = criminelCoords.get(i);
            g.fillOval(coords[0], coords[1], 40, 40);
            g.setColor(Color.BLACK);
            g.drawString(criminel.getNom() + " " + criminel.getPrenom(), coords[0] - 20, coords[1] + 60);
            g.setColor(Color.BLUE); // Remettre en bleu pour les prochaines itérations
        }

        // Dessiner les affaires secondaires (noir)
        g.setColor(Color.BLACK);
        for (Map.Entry<Affaire, int[]> entry : relatedAffairsCoords.entrySet()) {
            Affaire affaire = entry.getKey();
            int[] coords = entry.getValue();
            g.fillOval(coords[0], coords[1], 40, 40);
            g.drawString(affaire.getDescription(), coords[0] - 20, coords[1] + 60);
        }
    }

    private boolean isPointInCircle(int px, int py, int cx, int cy, int diameter) {
        int radius = diameter / 2;
        double dx = px - (cx + radius);
        double dy = py - (cy + radius);
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    private void displayCriminelInfo(Criminel criminel) {
        StringBuilder details = new StringBuilder();
        details.append("Nom : ").append(criminel.getNom()).append("\n");
        details.append("Prénom : ").append(criminel.getPrenom()).append("\n");
        details.append("Peine Totale : ").append(criminel.getPeineTotale()).append(" ans\n");
        details.append("Crimes :\n");
        for (var crime : criminel.getCrimes()) {
            details.append("- ").append(crime.toString()).append("\n");
        }
        details.append("Description : ").append(criminel.getDescription()).append("\n");

        displayInfoInNewWindow("Informations Criminel", details.toString());
    }

    private void displayInfoInNewWindow(String title, String info) {
        JDialog infoDialog = new JDialog(parentFrame, title, true);
        infoDialog.setSize(400, 300);
        infoDialog.setLocationRelativeTo(parentFrame);
        JTextArea infoArea = new JTextArea(info);
        infoArea.setEditable(false);
        infoArea.setWrapStyleWord(true);
        infoArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(infoArea);
        infoDialog.add(scrollPane);
        infoDialog.setVisible(true);
    }
}