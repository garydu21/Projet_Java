package Enqueteur;

import java.util.ArrayList;
import java.util.List;

public class Enquete {
    private String description;
    private boolean estTerminee; // true if investigation is closed
    private List<Enqueteur> enqueteurs; // multiple enquêteurs support

    public Enquete(String description) {
        this.description = description;
        this.estTerminee = false;
        this.enqueteurs = new ArrayList<>(); // Initialisation de la liste
    }

    // Getters et Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Enqueteur> getEnqueteurs() { return enqueteurs; }
    public void setEnqueteurs(List<Enqueteur> enqueteurs) { this.enqueteurs = enqueteurs; }

    public boolean isTerminee() { return estTerminee; }
    public void setTerminee(boolean terminee) { this.estTerminee = terminee; }

    public void assignerEnqueteur(Enqueteur enqueteur) {
        if (!this.enqueteurs.contains(enqueteur)) {
            this.enqueteurs.add(enqueteur);
        }
    }

    public void retirerEnqueteur(Enqueteur enqueteur) {
        this.enqueteurs.remove(enqueteur);
    }

    @Override
    public String toString() {
        String etat = estTerminee ? "[Terminee]" : "[En cours]";
        StringBuilder sb = new StringBuilder(etat + " " + description);
        if (!enqueteurs.isEmpty()) { // Vérifiez si la liste n'est pas vide
            sb.append(" Enquêteurs: ");
            for (Enqueteur e : enqueteurs) {
                sb.append(e.getNom()).append(" ").append(e.getPrenom()).append(", ");
            }
            sb.setLength(sb.length() - 2); // Remove last comma
        }
        return sb.toString();
    }
}