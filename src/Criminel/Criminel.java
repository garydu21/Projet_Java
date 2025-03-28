package Criminel;

import java.util.ArrayList;

public class Criminel {

    private String nom;
    private String prenom;

    private ArrayList<Crime> crimes;

    private ArrayList<Preuve> preuves;

    private String description;

    private int peineTotale;

    public Criminel(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.crimes = new ArrayList<Crime>();
        this.peineTotale = 0;
        this.preuves = new ArrayList<Preuve>();
        this.description = "";
    }

    public void ajouterCrime(Crime crime) {
        this.crimes.add(crime);
        this.peineTotale += crime.getPeine();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public int getPeineTotale() {
        return this.peineTotale;
    }

    public String getNom() {
        return this.nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public ArrayList<Preuve> getPreuve(int index) {
        return this.preuves;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public ArrayList<Crime> getCrimes(){
        return this.crimes;
    }
}
