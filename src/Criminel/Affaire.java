package Criminel;

import Enqueteur.Enqueteur;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Affaire {

    @Expose private int id;
    @Expose private String description;
    @Expose private String lieu;
    @Expose private Date date;

    @Expose private String etat = "En cours"; //
    @Expose private String informationsSupplementaires = "";

    @Expose private List<Integer> idCriminels = new ArrayList<>(); // pour la sauvegarde
    @Expose private List<String> idEnqueteurs = new ArrayList<>(); // pour la sauvegarde

    private transient List<Criminel> suspects = new ArrayList<>(); // non sauvegardé
    private transient List<Enqueteur> enqueteurs = new ArrayList<>(); // non sauvegardé

    public Affaire(int id, String description, String lieu, Date date) {
        this.id = id;
        this.description = description;
        this.lieu = lieu;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getLieu() {
        return lieu;
    }

    public Date getDate() {
        return date;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getInformationsSupplementaires() {
        return informationsSupplementaires;
    }

    public void setInformationsSupplementaires(String informationsSupplementaires) {
        this.informationsSupplementaires = informationsSupplementaires;
    }

    public List<Criminel> getSuspects() {
        if (suspects == null)
            suspects = new ArrayList<>();
        return suspects;
    }

    public List<Integer> getIdCriminels() {
        return idCriminels;
    }

    public List<Enqueteur> getEnqueteurs() {
        if (enqueteurs == null)
            enqueteurs = new ArrayList<>();
        return enqueteurs;
    }

    public List<String> getIdEnqueteurs() {
        if (idEnqueteurs == null)
            idEnqueteurs = new ArrayList<>();
        return idEnqueteurs;
    }

    public void ajouterSuspect(Criminel c) {
        if (!suspects.contains(c)) {
            suspects.add(c);
            if (!idCriminels.contains(c.getId()))
                idCriminels.add(c.getId());
        }
    }

    public void assignerEnqueteur(Enqueteur e) {
        if (!enqueteurs.contains(e)) {
            enqueteurs.add(e);
            if (!idEnqueteurs.contains(e.getId()))
                idEnqueteurs.add(e.getId());

            // Assignation réciproque (l'enquêteur contient l'affaire)
            if (!e.getAffairesAssignees().contains(this)) {
                e.assignerAffaire(this);
            }
        }
    }

    public void retirerEnqueteur(Enqueteur e) {
        enqueteurs.remove(e);
        idEnqueteurs.remove(e.getId());

        // Retrait réciproque (l'enquêteur ne contient plus l'affaire)
        if (e.getAffairesAssignees().contains(this)) {
            e.retirerAffaire(this);
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Affaire #" + id + " - " + description + " (" + lieu + ", " + date + ")");
        if (!enqueteurs.isEmpty()) {
            sb.append(" Enquêteurs: ");
            for (Enqueteur e : enqueteurs) {
                sb.append(e.getNom()).append(" ").append(e.getPrenom()).append(", ");
            }
            sb.setLength(sb.length() - 2); // Remove last comma
        }
        return sb.toString();
    }
}