package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import Criminel.Affaire;
import Criminel.Criminel;

public class GraphePanel extends JPanel implements MouseListener, MouseMotionListener {

    private final VueGraphe parentFrame;
    private final List<int[]> coord;
    private final int currentIndex;
    private final List<Affaire> listeAffaires;
    private final List<Criminel> criminelsAffaire;
    private final List<int[]> criminelCoords;
    private final Map<Affaire, int[]> relatedAffairsCoords;

    private final Map<Criminel, Color> criminelColors = new HashMap<>(); // Stocker les couleurs des criminels
    private final Random colorGenerator = new Random(); // Pour générer des couleurs aléatoires

    private double zoomFactor = 1.0; // Facteur de zoom (1.0 signifie taille normale)
    private int offsetX = 0; // Décalage horizontal pour le déplacement
    private int offsetY = 0; // Décalage vertical pour le déplacement
    private Point lastMousePosition; // Dernier point enregistré pour suivre le déplacement de la souris

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

        initializeCriminelColors(); // Générer les couleurs pour les criminels


        // Ajouter les listeners
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Générer une couleur aléatoire sombre.
     */
    private Color generateRandomColor() {
        // Générer des valeurs RGB sombres (entre 0 et 128)
        int red = colorGenerator.nextInt(129); // Limité à 128
        int green = colorGenerator.nextInt(129);
        int blue = colorGenerator.nextInt(129);
        return new Color(red, green, blue);
    }

    /**
     * Initialiser des couleurs uniques et sombres pour chaque criminel.
     */
    private void initializeCriminelColors() {
        for (Criminel criminel : criminelsAffaire) {
            if (!criminelColors.containsKey(criminel)) {
                criminelColors.put(criminel, generateRandomColor());
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(offsetX, offsetY);
        g2d.scale(zoomFactor, zoomFactor);

        // Définir une palette de couleurs pour les lignes
        Color[] lineColors = {Color.GREEN, Color.BLACK, Color.BLUE, Color.ORANGE, Color.MAGENTA};

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
                    // Ligne colorée en fonction de l'index (alternance avec les couleurs du tableau)
                    g2d.setColor(lineColors[i % lineColors.length]);

                    // Dessiner la ligne reliant le criminel à l'affaire
                    g2d.drawLine(criminelCoord[0] + 20, criminelCoord[1] + 20,
                            affaireCoord[0] + 20, affaireCoord[1] + 20);
                }
            }
        }

        // Dessiner l'affaire principale (rouge)
        int xCurrent = coord.get(currentIndex)[0];
        int yCurrent = coord.get(currentIndex)[1];
        g2d.setColor(Color.RED);
        g2d.fillOval(xCurrent, yCurrent, 40, 40);
        g2d.setColor(Color.BLACK);
        g2d.drawString(listeAffaires.get(currentIndex).getDescription(), xCurrent - 10, yCurrent + 60);

        // Dessiner les criminels (bleu)
        for (int i = 0; i < criminelsAffaire.size(); i++) {
            Criminel criminel = criminelsAffaire.get(i);
            int[] coords = criminelCoords.get(i);

            // Cercle du criminel en bleu
            g2d.setColor(Color.BLUE);
            g2d.fillOval(coords[0], coords[1], 40, 40);

            // Texte noir pour le nom
            g2d.setColor(Color.BLACK);
            g2d.drawString(criminel.getNom() + " " + criminel.getPrenom(), coords[0] - 20, coords[1] + 60);
        }

        // Dessiner les affaires secondaires (noir)
        g2d.setColor(Color.BLACK);
        for (Map.Entry<Affaire, int[]> entry : relatedAffairsCoords.entrySet()) {
            Affaire affaire = entry.getKey();
            int[] coords = entry.getValue();
            g2d.fillOval(coords[0], coords[1], 40, 40);
            g2d.drawString(affaire.getDescription(), coords[0] - 20, coords[1] + 60);
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
        details.append("Nom : ").append(criminel.getNom()).append("\n")
                .append("Prénom : ").append(criminel.getPrenom()).append("\n")
                .append("Peine Totale : ").append(criminel.getPeineTotale()).append(" ans\n")
                .append("Crimes :\n");
        for (var crime : criminel.getCrimes()) {
            details.append("- ").append(crime.getIntitule()).append("\n");
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

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int xCurrent = coord.get(currentIndex)[0];
        int yCurrent = coord.get(currentIndex)[1];
        if (isPointInCircle(x, y, xCurrent, yCurrent, 40)) {
            Affaire affairePrincipale = listeAffaires.get(currentIndex);
            String details = "Description : " + affairePrincipale.getDescription() + "\n"
                    + "Lieu : " + affairePrincipale.getLieu();
            displayInfoInNewWindow("Informations Affaire", details);
            return;
        }

        for (int i = 0; i < criminelsAffaire.size(); i++) {
            Criminel criminel = criminelsAffaire.get(i);
            int[] coords = criminelCoords.get(i);

            if (isPointInCircle(x, y, coords[0], coords[1], 40)) {
                displayCriminelInfo(criminel);
                return;
            }
        }

        for (Map.Entry<Affaire, int[]> entry : relatedAffairsCoords.entrySet()) {
            Affaire affaire = entry.getKey();
            int[] coords = entry.getValue();

            if (isPointInCircle(x, y, coords[0], coords[1], 40)) {
                String details = "Description : " + affaire.getDescription() + "\n"
                        + "Lieu : " + affaire.getLieu();
                displayInfoInNewWindow("Informations Affaire", details);
                return;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            lastMousePosition = e.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            int deltaX = e.getX() - lastMousePosition.x;
            int deltaY = e.getY() - lastMousePosition.y;
            offsetX += deltaX;
            offsetY += deltaY;
            lastMousePosition = e.getPoint();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}