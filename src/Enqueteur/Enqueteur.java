package Enqueteur;

public class Enqueteur {
    private String nom;
    private String prenom;
    private String id; // Un identifiant unique pour l'enquêteur
    private String grade; // Par exemple, "Inspecteur", "Agent", etc.

    public Enqueteur(String nom, String prenom, String id, String grade) {
        this.nom = nom;
        this.prenom = prenom;
        this.id = id;
        this.grade = grade;
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

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + grade + ")";
    }
}