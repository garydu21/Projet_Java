package Criminel;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Affaire {

    @Expose private int id;
    @Expose private String description;
    @Expose private String lieu;
    @Expose private Date date;

    @Expose private List<Integer> idCriminels = new ArrayList<>(); // pour la sauvegarde

    private transient List<Criminel> suspects = new ArrayList<>(); // non sauvegardé

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

    public List<Criminel> getSuspects() {
        if (suspects == null)
            suspects = new ArrayList<>();
        return suspects;
    }

    public List<Integer> getIdCriminels() {
        return idCriminels;
    }

    public void ajouterSuspect(Criminel c) {
        if (!suspects.contains(c)) {
            suspects.add(c);
            if (!idCriminels.contains(c.getId()))
                idCriminels.add(c.getId());
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
        return "Affaire #" + id + " - " + description + " (" + lieu + ", " + date + ")";
    }
}
