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

    private transient List<Affaire> affaires = new ArrayList<>();

    public Criminel(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.peineTotale = 0;
        this.id = nom.hashCode() + prenom.hashCode(); // simple identifiant basé sur nom/prenom
        this.idAffaires = new ArrayList<>();
        this.affaires = new ArrayList<>();
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

    public void ajouterCrime(Crime crime) {
        crimes.add(crime);
        peineTotale += crime.getPeine();
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
        if (!affaires.contains(affaire)) {
            affaires.add(affaire);
            if (!idAffaires.contains(affaire.getId()))
                idAffaires.add(affaire.getId());
        }
    }
}
