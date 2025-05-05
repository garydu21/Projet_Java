package Vue;

import javax.swing.*;
import Criminel.Affaire;
import Modele.Modele;
import Criminel.Criminel;

import java.awt.*;
import java.util.*;
import java.util.List;

public class VueGraphe extends JFrame {

    private final Modele modele;
    private final Random rdm = new Random();
    private final int[][] coord;
    private final int currentIndex;
    private List<Affaire> listeAffaires;
    private List<Criminel> criminelsAffaire;
    private final List<int[]> criminelCoords = new ArrayList<>();
    private final Map<Affaire, int[]> relatedAffairsCoords = new HashMap<>();

    // Taille minimale de la fenêtre
    private final int MIN_WIDTH = 1200;
    private final int MIN_HEIGHT = 800;

    // Distance minimale entre les points
    private final int MIN_DISTANCE = 100;

    // Centre du graphe
    private final int CENTER_X = 750;
    private final int CENTER_Y = 450;

    public VueGraphe(Modele mdl, int index) {
        this.modele = mdl;
        this.listeAffaires = modele.getListeAffaires();
        this.currentIndex = index;
        this.coord = new int[listeAffaires.size()][2];
        generateCoordinates();
        this.criminelsAffaire = getCriminel();
        generateCriminelCoordinates();
        genererCoordAffaire();

        // Définir la taille de la fenêtre en fonction du nombre d'affaires
        int windowWidth = Math.max(MIN_WIDTH, MIN_WIDTH + (listeAffaires.size()));
        int windowHeight = Math.max(MIN_HEIGHT, MIN_HEIGHT + (listeAffaires.size()));

        // Appliquer la taille calculée
        setTitle("Graphe interactif");
        setSize(windowWidth, windowHeight);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Passez toutes les données nécessaires au GraphePanel externe
        GraphePanel graphePanel = new GraphePanel(this, convertArrayToList(coord), currentIndex, listeAffaires,
                criminelsAffaire, criminelCoords, relatedAffairsCoords);
        add(graphePanel);

        setVisible(true);
    }

    private List<int[]> convertArrayToList(int[][] array) {
        List<int[]> list = new ArrayList<>();
        Collections.addAll(list, array); // Ajoute chaque ligne du tableau
        return list;
    }

    /**
     * Générer les coordonnées des affaires, en positionnant l'affaire principale au centre
     * et en plaçant les autres affaires dans des zones élargies pour augmenter la distance.
     */
    private void generateCoordinates() {
        List<int[]> existingCoords = new ArrayList<>(); // Liste des coordonnées générées

        // Définir des zones étendues pour les affaires
        int[][] zones = {
                {CENTER_X - 1000, CENTER_X + 1000, CENTER_Y - 500, CENTER_Y - 200},   // Haut (étendu)
                {CENTER_X - 1000, CENTER_X + 1000, CENTER_Y + 200, CENTER_Y + 500},  // Bas (étendu)
                {CENTER_X - 1300, CENTER_X - 400, 100, 800},                         // Gauche (étendu)
                {CENTER_X + 400, CENTER_X + 1300, 100, 800}                          // Droite (étendu)
        };

        // Positionner l'affaire principale au centre
        coord[currentIndex][0] = CENTER_X; // Position X centrale
        coord[currentIndex][1] = CENTER_Y; // Position Y centrale
        existingCoords.add(new int[]{CENTER_X, CENTER_Y}); // Ajouter au suivi des coordonnées existantes

        // Positionner les autres affaires
        for (int i = 0; i < coord.length; i++) {
            if (i == currentIndex) continue; // Sauter l'affaire principale (déjà positionnée)

            int x, y;
            boolean isPositionValid;
            int attempts = 0;

            // Répéter jusqu'à générer une position valide
            do {
                // Choisir une zone aléatoire (index entre 0 et 3 correspondant à haut, bas, gauche, droite)
                int zoneIndex = rdm.nextInt(zones.length);
                int[] zone = zones[zoneIndex]; // Récupérer les limites de la zone sélectionnée

                // Générer des coordonnées aléatoires dans les limites de la zone
                x = rdm.nextInt(zone[0], zone[1]);
                y = rdm.nextInt(zone[2], zone[3]);

                // Vérifier si les coordonnées respectent l'espacement minimum
                isPositionValid = !superpose(x, y, existingCoords);

                attempts++;
            } while (!isPositionValid && attempts < 100); // Limite pour éviter une boucle infinie

            if (isPositionValid) {
                coord[i][0] = x;
                coord[i][1] = y;
                existingCoords.add(new int[]{x, y});
            }
        }
    }

    /**
     * Générer les coordonnées des criminels autour du centre, mais avec des limites élargies.
     */
    private void generateCriminelCoordinates() {
        criminelCoords.clear();
        List<int[]> existingCoords = new ArrayList<>();

        // Ajouter les coordonnées de l'affaire principale au centre pour éviter la superposition
        existingCoords.add(new int[]{CENTER_X, CENTER_Y});

        for (int i = 0; i < criminelsAffaire.size(); i++) {
            int x, y;
            boolean isPositionValid;
            int attempts = 0;

            // Répéter jusqu'à générer des coordonnées valides proches du centre
            do {
                // Générer des coordonnées proches du centre avec des marges réduites
                x = rdm.nextInt(CENTER_X - 200, CENTER_X + 200); // Zone étroite pour X
                y = rdm.nextInt(CENTER_Y - 150, CENTER_Y + 150); // Zone étroite pour Y

                // Vérifier si les coordonnées respectent l'espacement minimum
                isPositionValid = !superpose(x, y, existingCoords);

                attempts++;
            } while (!isPositionValid && attempts < 100);

            if (isPositionValid) {
                criminelCoords.add(new int[]{x, y});
                existingCoords.add(new int[]{x, y}); // Ajouter aux coordonnées existantes
            }
        }
    }

    /**
     * Générer les coordonnées des affaires secondaires liées, en respectant une dispersion augmentée.
     */
    private void genererCoordAffaire() {
        relatedAffairsCoords.clear();
        List<int[]> existingCoords = new ArrayList<>(); // Liste des coordonnées générées

        // Définir des zones secondaires, avec des distances augmentées
        int[][] zones = {
                {CENTER_X - 600, CENTER_X + 600, 100, CENTER_Y - 300},   // Haut (élargi)
                {CENTER_X - 600, CENTER_X + 600, CENTER_Y + 300, 800},  // Bas (élargi)
                {100, CENTER_X - 500, 100, 800},                        // Gauche (élargi)
                {CENTER_X + 500, 1500, 100, 800}                        // Droite (élargi)
        };

        for (Criminel criminel : criminelsAffaire) {
            for (Affaire affair : criminel.getAffaires()) {
                if (!affair.equals(listeAffaires.get(currentIndex)) &&
                        !relatedAffairsCoords.containsKey(affair)) {

                    int x, y;
                    boolean isPositionValid;
                    int attempts = 0;

                    // Répéter jusqu'à générer une position valide
                    do {
                        // Choisir une zone élargie aléatoire
                        int zoneIndex = rdm.nextInt(zones.length);
                        int[] zone = zones[zoneIndex];

                        // Générer des coordonnées dans les limites de la zone
                        x = rdm.nextInt(zone[0], zone[1]);
                        y = rdm.nextInt(zone[2], zone[3]);

                        isPositionValid = !superpose(x, y, existingCoords);
                        attempts++;
                    } while (!isPositionValid && attempts < 100);

                    if (isPositionValid) {
                        // Ajouter les coordonnées validées
                        relatedAffairsCoords.put(affair, new int[]{x, y});
                        existingCoords.add(new int[]{x, y});
                    }
                }
            }
        }
    }

    /**
     * Vérifier si un point est trop proche des autres (distance minimale ajustée).
     */
    private boolean superpose(int x, int y, List<int[]> existingCoords) {
        for (int[] coord : existingCoords) {
            int dx = Math.abs(coord[0] - x);
            int dy = Math.abs(coord[1] - y);

            // Vérifier si la distance est inférieure
            if (dx < MIN_DISTANCE && dy < MIN_DISTANCE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Récupérer la liste des criminels impliqués dans l'affaire courante.
     */
    public List<Criminel> getCriminel() {
        List<Criminel> liste = new ArrayList<>();
        for (Criminel c : modele.getListeCriminel()) {
            if (c.getAffaires().contains(listeAffaires.get(currentIndex))) {
                liste.add(c);
            }
        }
        return liste;
    }
}