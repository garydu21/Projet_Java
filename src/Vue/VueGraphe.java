package Vue;

import javax.swing.*;
import Criminel.Affaire;
import Modele.Modele;
import Criminel.Criminel;

import java.awt.*;
import java.util.*;
import java.util.List;
import Vue.GraphePanel;

public class VueGraphe extends JFrame {

    private final Modele modele;
    private final Random rdm = new Random();
    private final int[][] coord;
    private final int currentIndex;
    private List<Affaire> listeAffaires;
    private List<Criminel> criminelsAffaire;
    private final List<int[]> criminelCoords = new ArrayList<>();
    private final Map<Affaire, int[]> relatedAffairsCoords = new HashMap<>();

    public VueGraphe(Modele mdl, int index) {
        this.modele = mdl;
        this.listeAffaires = modele.getListeAffaires();
        this.currentIndex = index;
        this.coord = new int[listeAffaires.size()][2];
        generateCoordinates();
        this.criminelsAffaire = getCriminel();
        generateCriminelCoordinates();
        genererCoordAffaire();

        setTitle("Graphe interactif");
        setSize(1500, 900);
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
     * Générer les coordonnées des affaires (zone centrale entre x = 500-1000 et y = 300-600)
     */
    private void generateCoordinates() {
        int boucle = 0;
        while (boucle < coord.length) {
            int x = rdm.nextInt(500, 1000);
            int y = rdm.nextInt(300, 600);

            boolean overlap = false;
            for (int[] existingCoord : coord) {
                if (Math.abs(existingCoord[0] - x) <= 60 &&
                        Math.abs(existingCoord[1] - y) <= 60) {
                    overlap = true;
                }
            }

            if (!overlap) {
                coord[boucle][0] = x;
                coord[boucle][1] = y;
                boucle++;
            }
        }
    }

    /**
     * Générer les coordonnées des criminels tout en respectant les zones définies
     */
    private void generateCriminelCoordinates() {
        criminelCoords.clear();

        for (int i = 0; i < criminelsAffaire.size(); i++) {
            int x, y;
            do {
                // Générer x dans la zone gauche (100-400) ou droite (1100-1400)
                boolean isLeftZone = rdm.nextBoolean();
                if (isLeftZone) {
                    x = rdm.nextInt(100, 400); // Zone gauche
                } else {
                    x = rdm.nextInt(1100, 1400); // Zone droite
                }

                // Générer y dans la zone haut (100-250) ou bas (650-800)
                boolean isTopZone = rdm.nextBoolean();
                if (isTopZone) {
                    y = rdm.nextInt(100, 250); // Zone en haut
                } else {
                    y = rdm.nextInt(650, 800); // Zone en bas
                }
            } while (superpose(x, y, criminelCoords)); // Vérifier si aucun chevauchement

            criminelCoords.add(new int[]{x, y});
        }
    }

    /**
     * Générer les coordonnées des affaires secondaires liées aux criminels
     */
    private void genererCoordAffaire() {
        relatedAffairsCoords.clear();

        for (Criminel criminel : criminelsAffaire) {
            for (Affaire affair : criminel.getAffaires()) {
                if (!affair.equals(listeAffaires.get(currentIndex)) &&
                        !relatedAffairsCoords.containsKey(affair)) {
                    int x, y;
                    x = rdm.nextInt(500, 1000);
                    y = rdm.nextInt(300, 600);
                    while (superpose(x, y, new ArrayList<>(relatedAffairsCoords.values()))) {

                        x = rdm.nextInt(500, 1000);
                        y = rdm.nextInt(300, 600);
                    }
                    relatedAffairsCoords.put(affair, new int[]{x, y});
                }
            }
        }
    }

    /**
     * Vérifier si un point est trop proche des autres
     */
    private boolean superpose(int x, int y, List<int[]> existingCoords) {
        for (int[] coord : existingCoords) {
            if (Math.abs(coord[0] - x) <= 60 && Math.abs(coord[1] - y) <= 60) {
                return true;
            }
        }
        return false;
    }

    /**
     * Récupérer la liste des criminels impliqués dans l'affaire courante
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