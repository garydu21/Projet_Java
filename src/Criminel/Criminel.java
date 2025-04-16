package Criminel;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Criminel {

    @Expose private int id;
    @Expose private String nom;
    @Expose private String prenom;
    @Expose private ArrayList<Crime> crimes = new ArrayList<>();
    @Expose private ArrayList<Preuve> preuves = new ArrayList<>();
    @Expose private int peineTotale;
    @Expose private List<Integer> idAffaires = new ArrayList<>();

    private String description = "";

    private transient List<Affaire> affaires = new ArrayList<>();

    public Criminel(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.peineTotale = 0;
        this.id = nom.hashCode() + prenom.hashCode();
        this.idAffaires = new ArrayList<>();
        this.affaires = new ArrayList<>();
        this.preuves = new ArrayList<>();
        this.description = "";
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getPeineTotale() {
        return peineTotale;
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

    public void resetPeineTotal() {
        this.peineTotale = 0;
    }

    public void ajouterCrime(Crime crime) {
        this.crimes.add(crime);
        this.peineTotale += crime.getPeine();
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Affaire> getAffaires() {
        if (affaires == null)
            affaires = new ArrayList<>();
        return affaires;
    }

    public List<Integer> getIdAffaires() {
        if (idAffaires == null)
            idAffaires = new ArrayList<>();
        return idAffaires;
    }

    public void ajouterAffaire(Affaire affaire) {
        if (!getAffaires().contains(affaire)) {
            getAffaires().add(affaire);
            if (!getIdAffaires().contains(affaire.getId()))
                getIdAffaires().add(affaire.getId());
        }
    }
}
