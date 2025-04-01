package Modele;

import Criminel.Criminel;
import Criminel.Affaire;
import Criminel.Affaire;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class Modele extends Observable {
    private static final String FICHIER_JSON = "criminels.json";
    private static final String FICHIER_AFFAIRES = "affaires.json";

    private List<Criminel> listeCriminel;
    private List<Affaire> listeAffaires;

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy") //Format ISO : yyyy-MM-dd, je n'ai pas encore regardé comment la passer en français - Angel
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

    public Modele() {
        this.listeCriminel = new ArrayList<>();
        this.listeAffaires = new ArrayList<>();
        chargerDonnees();
        chargerAffaires();
    }

    public List<Criminel> getListeCriminel() {
        return listeCriminel;
    }

    public List<Affaire> getListeAffaires() {
        return listeAffaires;
    }

    public void addListeCriminel(Criminel c) {
        this.listeCriminel.add(c);
        sauvegarderDonnees();
        setChanged();
        notifyObservers();
    }

    public void supprimerCriminel(int index) {
        if (index >= 0 && index < listeCriminel.size()) {
            listeCriminel.remove(index);
            sauvegarderDonnees();
            setChanged();
            notifyObservers();
        }
    }

    public void modifierCriminel(int index, String nom, String prenom) {
        if (index >= 0 && index < listeCriminel.size()) {
            listeCriminel.get(index).setNom(nom);
            listeCriminel.get(index).setPrenom(prenom);
            sauvegarderDonnees();
            setChanged();
            notifyObservers();
        }
    }

    public void exporterJson(File fichier) {
        try (FileWriter writer = new FileWriter(fichier)) {
            gson.toJson(listeCriminel, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importerJson(File fichier) {
        try (FileReader reader = new FileReader(fichier)) {
            Type listType = new TypeToken<ArrayList<Criminel>>() {}.getType();
            listeCriminel = gson.fromJson(reader, listType);
            setChanged();
            notifyObservers();
            sauvegarderDonnees();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ajouterAffaire(Affaire affaire) {
        listeAffaires.add(affaire);
        sauvegarderAffaires();
        setChanged();
        notifyObservers();
    }

    public void mettreAJourAffaire(Affaire affaire, Criminel criminel) {
        if (!affaire.getSuspects().contains(criminel)) {
            affaire.ajouterSuspect(criminel);
        }

        if (criminel.getAffaires() != null && !criminel.getAffaires().contains(affaire)) {
            criminel.ajouterAffaire(affaire);
        }

        sauvegarderAffaires();
        sauvegarderDonnees();
        setChanged();
        notifyObservers();
    }

    private void sauvegarderDonnees() {
        exporterJson(new File(FICHIER_JSON));
    }

    private void chargerDonnees() {
        File fichier = new File(FICHIER_JSON);
        if (fichier.exists()) {
            importerJson(fichier);
        }
    }

    private void sauvegarderAffaires() {
        try (FileWriter writer = new FileWriter(FICHIER_AFFAIRES)) {
            gson.toJson(listeAffaires, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reconstruireLiens() {
        // Créer une map des criminels par ID
        Map<Integer, Criminel> mapCriminels = new HashMap<>();
        for (Criminel c : listeCriminel) {
            mapCriminels.put(c.getId(), c);
        }

        // Relier les suspects aux affaires, et vice-versa
        for (Affaire a : listeAffaires) {
            for (int id : a.getIdCriminels()) {
                Criminel c = mapCriminels.get(id);
                if (c != null) {
                    a.getSuspects().add(c);
                    c.getAffaires().add(a);
                }
            }
        }
    }

    private void chargerAffaires() {
        File fichier = new File(FICHIER_AFFAIRES);
        if (fichier.exists()) {
            try (FileReader reader = new FileReader(fichier)) {
                Type listType = new TypeToken<ArrayList<Affaire>>() {}.getType();
                listeAffaires = gson.fromJson(reader, listType);
                reconstruireLiens(); // 👈 impératif
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void supprimerAffaire(int index) {
        if (index >= 0 && index < listeAffaires.size()) {
            listeAffaires.remove(index);
            sauvegarderAffaires();
            setChanged();
            notifyObservers();
        }
    }

    public void retirerCriminelAffaire(Affaire affaire, Criminel criminel) {
        affaire.getSuspects().remove(criminel);
        affaire.getIdCriminels().remove((Integer) criminel.getId());
        criminel.getAffaires().remove(affaire);
        criminel.getIdAffaires().remove((Integer) affaire.getId());

        sauvegarderAffaires();
        sauvegarderDonnees();
        setChanged();
        notifyObservers();
    }
}
