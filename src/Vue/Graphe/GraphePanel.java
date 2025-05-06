package Vue.Graphe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Criminel.Affaire;
import Criminel.Criminel;

public class GraphePanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    private static final int NODE_SIZE = 40;
    private static final int LABEL_OFFSET = 20;
    private static final float DASH_PATTERN[] = {10.0f, 5.0f};
    private static final Color MAIN_AFFAIR_COLOR = new Color(220, 20, 60);
    private static final Color CRIMINAL_COLOR = new Color(30, 144, 255);
    private static final Color RELATED_AFFAIR_COLOR = new Color(50, 50, 50);
    private static final Color LABEL_COLOR = new Color(0, 0, 0);
    private static final Color SELECTED_NODE_BORDER = new Color(255, 215, 0);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color LINK_COLOR_MAIN = new Color(50, 205, 50);
    private static final Stroke NORMAL_STROKE = new BasicStroke(2.0f);
    private static final Stroke SELECTED_STROKE = new BasicStroke(3.0f);
    private static final Stroke DASHED_STROKE = new BasicStroke(
            1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, DASH_PATTERN, 0.0f
    );

    private final VueGraphe parentFrame;
    private final List<int[]> coord;
    private final int currentIndex;
    private final List<Affaire> listeAffaires;
    private final List<Criminel> criminelsAffaire;
    private final List<int[]> criminelCoords;
    private final Map<Affaire, int[]> relatedAffairsCoords;
    private final Map<Criminel, Color> criminelColors = new HashMap<>();

    private double zoomFactor = 1.0;
    private int offsetX = 0;
    private int offsetY = 0;
    private Point lastMousePosition;

    private Object selectedNode = null;
    private Map<Object, Rectangle> nodeBounds = new HashMap<>();

    public GraphePanel(VueGraphe parentFrame, List<int[]> coord, int currentIndex,
                       List<Affaire> listeAffaires, List<Criminel> criminelsAffaire,
                       List<int[]> criminelCoords, Map<Affaire, int[]> relatedAffairsCoords) {
        this.parentFrame = parentFrame;
        this.coord = coord;
        this.currentIndex = currentIndex;
        this.listeAffaires = listeAffaires;
        this.criminelsAffaire = criminelsAffaire;
        this.criminelCoords = criminelCoords;
        this.relatedAffairsCoords = relatedAffairsCoords;

        initializeCriminelColors();

        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        setToolTipText("Molette: zoom, Clic+Glisser: déplacer, Clic: sélectionner");

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        addLegend();
    }

    private void addLegend() {
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setBackground(new Color(255, 255, 255, 200));
        legendPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JLabel titleLabel = new JLabel("Légende");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel mainAffairLegend = createLegendItem(MAIN_AFFAIR_COLOR, "Affaire principale");
        JPanel criminalLegend = createLegendItem(CRIMINAL_COLOR, "Criminels");
        JPanel relatedAffairLegend = createLegendItem(RELATED_AFFAIR_COLOR, "Affaires liées");
        JPanel linkLegend = createLegendItem(LINK_COLOR_MAIN, "Liens");

        legendPanel.add(titleLabel);
        legendPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        legendPanel.add(mainAffairLegend);
        legendPanel.add(criminalLegend);
        legendPanel.add(relatedAffairLegend);
        legendPanel.add(linkLegend);

        this.add(legendPanel);
        legendPanel.setBounds(20, 20, 150, 120);
    }

    private JPanel createLegendItem(Color color, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        panel.setOpaque(false);

        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(15, 15));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(colorBox);
        panel.add(label);

        return panel;
    }

    private void initializeCriminelColors() {
        Color[] colorPalette = {
                new Color(70, 130, 180),
                new Color(0, 128, 128),
                new Color(106, 90, 205),
                new Color(255, 140, 0),
                new Color(178, 34, 34),
                new Color(85, 107, 47),
                new Color(139, 69, 19),
                new Color(153, 50, 204),
                new Color(47, 79, 79),
                new Color(255, 105, 180)
        };

        for (int i = 0; i < criminelsAffaire.size(); i++) {
            Criminel criminel = criminelsAffaire.get(i);
            if (!criminelColors.containsKey(criminel)) {
                criminelColors.put(criminel, colorPalette[i % colorPalette.length]);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.translate(offsetX, offsetY);
        g2d.scale(zoomFactor, zoomFactor);

        drawConnections(g2d);

        drawNodes(g2d);

        nodeBounds.clear();

        drawMainAffair(g2d);

        drawCriminals(g2d);

        drawRelatedAffairs(g2d);
    }

    private void drawConnections(Graphics2D g2d) {
        int xCurrent = coord.get(currentIndex)[0];
        int yCurrent = coord.get(currentIndex)[1];

        for (int i = 0; i < criminelsAffaire.size(); i++) {
            Criminel criminel = criminelsAffaire.get(i);
            int[] criminelCoord = criminelCoords.get(i);

            g2d.setStroke(NORMAL_STROKE);
            g2d.setColor(LINK_COLOR_MAIN);
            g2d.drawLine(xCurrent + NODE_SIZE/2, yCurrent + NODE_SIZE/2,
                    criminelCoord[0] + NODE_SIZE/2, criminelCoord[1] + NODE_SIZE/2);

            for (Affaire affaire : criminel.getAffaires()) {
                if (!affaire.equals(listeAffaires.get(currentIndex))) {
                    int[] affaireCoord = relatedAffairsCoords.get(affaire);
                    if (affaireCoord != null) {
                        g2d.setColor(criminelColors.get(criminel));
                        g2d.setStroke(DASHED_STROKE);
                        g2d.drawLine(criminelCoord[0] + NODE_SIZE/2, criminelCoord[1] + NODE_SIZE/2,
                                affaireCoord[0] + NODE_SIZE/2, affaireCoord[1] + NODE_SIZE/2);
                    }
                }
            }
        }
    }

    private void drawNodes(Graphics2D g2d) {
    }

    private void drawMainAffair(Graphics2D g2d) {
        int xCurrent = coord.get(currentIndex)[0];
        int yCurrent = coord.get(currentIndex)[1];
        Affaire affairePrincipale = listeAffaires.get(currentIndex);

        Rectangle bounds = new Rectangle(xCurrent, yCurrent, NODE_SIZE, NODE_SIZE);
        nodeBounds.put(affairePrincipale, bounds);

        if (selectedNode == affairePrincipale) {
            g2d.setColor(SELECTED_NODE_BORDER);
            g2d.fillOval(xCurrent - 2, yCurrent - 2, NODE_SIZE + 4, NODE_SIZE + 4);
        }

        g2d.setColor(MAIN_AFFAIR_COLOR);
        g2d.fillOval(xCurrent, yCurrent, NODE_SIZE, NODE_SIZE);

        g2d.setColor(LABEL_COLOR);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));

        String description = affairePrincipale.getDescription();
        if (description.length() > 25) {
            description = description.substring(0, 22) + "...";
        }

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(description);

        g2d.drawString(description,
                xCurrent + (NODE_SIZE - textWidth) / 2,
                yCurrent + NODE_SIZE + LABEL_OFFSET);
    }

    private void drawCriminals(Graphics2D g2d) {
        for (int i = 0; i < criminelsAffaire.size(); i++) {
            Criminel criminel = criminelsAffaire.get(i);
            int[] coords = criminelCoords.get(i);

            Rectangle bounds = new Rectangle(coords[0], coords[1], NODE_SIZE, NODE_SIZE);
            nodeBounds.put(criminel, bounds);

            if (selectedNode == criminel) {
                g2d.setColor(SELECTED_NODE_BORDER);
                g2d.fillOval(coords[0] - 2, coords[1] - 2, NODE_SIZE + 4, NODE_SIZE + 4);
            }

            g2d.setColor(criminelColors.get(criminel));
            g2d.fillOval(coords[0], coords[1], NODE_SIZE, NODE_SIZE);

            g2d.setColor(LABEL_COLOR);
            g2d.setFont(new Font("Arial", Font.BOLD, 11));

            String nom = criminel.getNom() + " " + criminel.getPrenom();
            if (nom.length() > 20) {
                nom = nom.substring(0, 17) + "...";
            }

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(nom);

            g2d.drawString(nom,
                    coords[0] + (NODE_SIZE - textWidth) / 2,
                    coords[1] + NODE_SIZE + LABEL_OFFSET);
        }
    }

    private void drawRelatedAffairs(Graphics2D g2d) {
        for (Map.Entry<Affaire, int[]> entry : relatedAffairsCoords.entrySet()) {
            Affaire affaire = entry.getKey();
            int[] coords = entry.getValue();

            Rectangle bounds = new Rectangle(coords[0], coords[1], NODE_SIZE, NODE_SIZE);
            nodeBounds.put(affaire, bounds);

            if (selectedNode == affaire) {
                g2d.setColor(SELECTED_NODE_BORDER);
                g2d.fillOval(coords[0] - 2, coords[1] - 2, NODE_SIZE + 4, NODE_SIZE + 4);
            }

            g2d.setColor(RELATED_AFFAIR_COLOR);
            g2d.fillOval(coords[0], coords[1], NODE_SIZE, NODE_SIZE);

            g2d.setColor(LABEL_COLOR);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));

            String description = affaire.getDescription();
            if (description.length() > 20) {
                description = description.substring(0, 17) + "...";
            }

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(description);

            g2d.drawString(description,
                    coords[0] + (NODE_SIZE - textWidth) / 2,
                    coords[1] + NODE_SIZE + LABEL_OFFSET);
        }
    }

    private void displayCriminelInfo(Criminel criminel) {
        StringBuilder details = new StringBuilder();
        details.append("Nom : ").append(criminel.getNom()).append("\n")
                .append("Prénom : ").append(criminel.getPrenom()).append("\n")
                .append("Peine Totale : ").append(criminel.getPeineTotale()).append(" ans\n")
                .append("Crimes :\n");

        for (var crime : criminel.getCrimes()) {
            details.append("- ").append(crime.getIntitule()).append(" (").append(crime.getPeine()).append(" ans)\n");
        }

        // Correction ici: vérifier si la description n'est pas null avant d'appeler isEmpty()
        String description = criminel.getDescription();
        if (description != null && !description.isEmpty()) {
            details.append("\nDescription : ").append(description).append("\n");
        }

        details.append("\nAffaires impliquées :\n");
        for (Affaire a : criminel.getAffaires()) {
            details.append("- ").append(a.getDescription())
                    .append(" (").append(a.getLieu()).append(")\n");
        }

        displayInfoInNewWindow("Informations sur " + criminel.getNom() + " " + criminel.getPrenom(), details.toString());
    }

    private void displayAffaireInfo(Affaire affaire) {
        StringBuilder details = new StringBuilder();
        details.append("ID : ").append(affaire.getId()).append("\n")
                .append("Description : ").append(affaire.getDescription()).append("\n")
                .append("Lieu : ").append(affaire.getLieu()).append("\n")
                .append("Date : ").append(affaire.getDate()).append("\n")
                .append("État : ").append(affaire.getEtat()).append("\n");

        if (affaire.getInformationsSupplementaires() != null && !affaire.getInformationsSupplementaires().isEmpty()) {
            details.append("Informations supplémentaires : ")
                    .append(affaire.getInformationsSupplementaires()).append("\n");
        }

        details.append("\nSuspects impliqués :\n");
        List<Criminel> suspects = affaire.getSuspects();
        if (!suspects.isEmpty()) {
            for (Criminel c : suspects) {
                details.append("- ").append(c.getNom()).append(" ").append(c.getPrenom())
                        .append(" (Peine totale: ").append(c.getPeineTotale()).append(" ans)\n");
            }
        } else {
            details.append("Aucun suspect associé à cette affaire.\n");
        }

        displayInfoInNewWindow("Informations sur l'affaire: " + affaire.getDescription(), details.toString());
    }

    private void displayInfoInNewWindow(String title, String info) {
        JDialog infoDialog = new JDialog(parentFrame, title, true);
        infoDialog.setSize(500, 400);
        infoDialog.setLocationRelativeTo(parentFrame);

        JTextArea infoArea = new JTextArea(info);
        infoArea.setEditable(false);
        infoArea.setWrapStyleWord(true);
        infoArea.setLineWrap(true);
        infoArea.setMargin(new Insets(10, 10, 10, 10));
        infoArea.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(infoArea);
        infoDialog.add(scrollPane);

        infoDialog.setVisible(true);
    }

    private boolean isPointInCircle(int px, int py, int cx, int cy, int diameter) {
        int radius = diameter / 2;
        double dx = px - (cx + radius);
        double dy = py - (cy + radius);
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    private Object findNodeAtPosition(int x, int y) {
        int adjustedX = (int)((x - offsetX) / zoomFactor);
        int adjustedY = (int)((y - offsetY) / zoomFactor);

        for (Map.Entry<Object, Rectangle> entry : nodeBounds.entrySet()) {
            Rectangle bounds = entry.getValue();
            if (isPointInCircle(adjustedX, adjustedY, bounds.x, bounds.y, NODE_SIZE)) {
                return entry.getKey();
            }
        }

        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Object node = findNodeAtPosition(e.getX(), e.getY());

        if (node != null) {
            selectedNode = node;
            repaint();

            if (node instanceof Criminel) {
                displayCriminelInfo((Criminel) node);
            } else if (node instanceof Affaire) {
                displayAffaireInfo((Affaire) node);
            }
        } else {
            selectedNode = null;
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            lastMousePosition = e.getPoint();
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && lastMousePosition != null) {
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
        Object node = findNodeAtPosition(e.getX(), e.getY());
        if (node != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double zoomChange = e.getWheelRotation() * -0.1;
        double newZoom = zoomFactor + zoomChange;

        if (newZoom >= 0.2 && newZoom <= 3.0) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            double factor = newZoom / zoomFactor;
            int newOffsetX = (int)(mouseX - factor * (mouseX - offsetX));
            int newOffsetY = (int)(mouseY - factor * (mouseY - offsetY));

            zoomFactor = newZoom;
            offsetX = newOffsetX;
            offsetY = newOffsetY;

            repaint();
        }
    }
}