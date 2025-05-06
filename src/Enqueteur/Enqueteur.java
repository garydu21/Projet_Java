package Enqueteur;

import Criminel.Affaire;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Enqueteur {
    @Expose private String nom;
    @Expose private String prenom;
    @Expose private String id; // Un identifiant unique pour l'enquêteur
    @Expose private String grade; // Par exemple, "Inspecteur", "Agent", etc.
    @Expose private List<Affaire> affairesAssignees; // Liste des affaires assignées à cet enquêteur

    public Enqueteur(String nom, String prenom, String id, String grade) {
        this.nom = nom != null ? nom : "";
        this.prenom = prenom != null ? prenom : "";
        this.id = id != null ? id : "";
        this.grade = grade != null ? grade : "";
        this.affairesAssignees = new ArrayList<>();
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<Affaire> getAffairesAssignees() {
        // S'assurer que la liste n'est jamais null
        if (affairesAssignees == null) {
            affairesAssignees = new ArrayList<>();
        }
        return affairesAssignees;
    }

    public void setAffairesAssignees(List<Affaire> affairesAssignees) {
        this.affairesAssignees = affairesAssignees;
    }

    public void assignerAffaire(Affaire affaire) {
        if (!this.affairesAssignees.contains(affaire)) {
            this.affairesAssignees.add(affaire);
            // Assignation réciproque (l'affaire contient l'enquêteur)
            if (!affaire.getEnqueteurs().contains(this)) {
                affaire.assignerEnqueteur(this);
            }
        }
    }

    public void retirerAffaire(Affaire affaire) {
        this.affairesAssignees.remove(affaire);
        // Retrait réciproque (l'affaire ne contient plus l'enquêteur)
        if (affaire.getEnqueteurs().contains(this)) {
            affaire.retirerEnqueteur(this);
        }
    }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + grade + ")";
    }
}