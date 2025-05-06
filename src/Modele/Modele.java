package Modele;

import Criminel.Criminel;
import Criminel.Affaire;
import Criminel.Affaire;
import Criminel.Enqueteur;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import Criminel.Crime;

public class Modele extends Observable {
    private static final String FICHIER_JSON = "criminels.json";
    private static final String FICHIER_AFFAIRES = "affaires.json";
    private static final String FICHIER_CRIMES = "crimes.json";

    private List<Criminel> listeCriminel;
    private List<Affaire> listeAffaires;
    private ArrayList<Crime> listeCrimes;
    private List<Enqueteur> listeEnqueteurs;
    private static final String FICHIER_ENQUETEURS = "enqueteurs.json";

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy") //Format ISO : yyyy-MM-dd, je n'ai pas encore regardé comment la passer en français - Angel
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

    public Modele() {
        this.listeCriminel = new ArrayList<>();
        this.listeAffaires = new ArrayList<>();
        this.listeCrimes = new ArrayList<>();
        this.listeEnqueteurs = new ArrayList<>();
        chargerDonnees();
        chargerAffaires();
        chargerCrimes();
        chargerEnqueteurs();
    }

    public ArrayList<Crime> getListeCrimes() {
        return this.listeCrimes;
    }

    public List<Criminel> getListeCriminel() {
        return listeCriminel;
    }

    public List<Affaire> getListeAffaires() {
        return listeAffaires;
    }

    public List<Enqueteur> getListeEnqueteurs() {
        return listeEnqueteurs;
    }

    public void addListeCrime(Crime c){
        this.listeCrimes.add(c);
        sauvegarderCrimes();
        setChanged();
        notifyObservers();
    }

    public void supprimerCrimes(int index) {
        if (index >= 0 && index < listeCrimes.size()) {
            listeCrimes.remove(index);
            sauvegarderCrimes();
            setChanged();
            notifyObservers();
        }
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

    public void modifierCriminel(int index, String nom, String prenom, String description) {
        if (index >= 0 && index < listeCriminel.size()) {
            listeCriminel.get(index).setNom(nom);
            listeCriminel.get(index).setPrenom(prenom);
            listeCriminel.get(index).setDescription(description);
            sauvegarderDonnees();
            setChanged();
            notifyObservers();
        }
    }

    public void sauvegarderEnqueteurs() {
        try (FileWriter writer = new FileWriter(FICHIER_ENQUETEURS)) {
            gson.toJson(listeEnqueteurs, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void chargerEnqueteurs() {
        File fichier = new File(FICHIER_ENQUETEURS);
        if (fichier.exists()) {
            try (FileReader reader = new FileReader(fichier)) {
                Type listType = new TypeToken<ArrayList<Enqueteur>>() {}.getType();
                listeEnqueteurs = gson.fromJson(reader, listType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void assignerEnqueteurAffaire(Enqueteur enqueteur, Affaire affaire) {
        affaire.assignerEnqueteur(enqueteur);
        sauvegarderAffaires();
        sauvegarderEnqueteurs();
        setChanged();
        notifyObservers();
    }

    public void retirerEnqueteurAffaire(Enqueteur enqueteur, Affaire affaire) {
        affaire.retirerEnqueteur(enqueteur);
        sauvegarderAffaires();
        sauvegarderEnqueteurs();
        setChanged();
        notifyObservers();
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

    private void sauvegarderCrimes() {
        try (FileWriter writer = new FileWriter(FICHIER_CRIMES)) {
            gson.toJson(listeCrimes, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void chargerDonnees() {
        File fichier = new File(FICHIER_JSON);
        if (fichier.exists()) {
            importerJson(fichier);
        }
    }

    private void chargerCrimes() {
        File fichier = new File(FICHIER_CRIMES);
        if (fichier.exists()) {
            try (FileReader reader = new FileReader(fichier)) {
                Type listType = new TypeToken<ArrayList<Crime>>() {}.getType();
                listeCrimes = gson.fromJson(reader, listType); // Charger les crimes à partir du fichier JSON
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void sauvegarderAffaires() {
        try (FileWriter writer = new FileWriter(FICHIER_AFFAIRES)) {
            gson.toJson(listeAffaires, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reconstruireLiens() {
        Map<Integer, Criminel> mapCriminels = new HashMap<>();
        for (Criminel c : listeCriminel) {
            mapCriminels.put(c.getId(), c);
        }

        Map<String, Enqueteur> mapEnqueteurs = new HashMap<>();
        for (Enqueteur e : listeEnqueteurs) {
            mapEnqueteurs.put(e.getId(), e);
        }

        for (Affaire a : listeAffaires) {
            for (int id : a.getIdCriminels()) {
                Criminel c = mapCriminels.get(id);
                if (c != null) {
                    a.getSuspects().add(c);
                    c.getAffaires().add(a);
                }
            }

            for (String id : a.getIdEnqueteurs()) {
                Enqueteur e = mapEnqueteurs.get(id);
                if (e != null) {
                    a.getEnqueteurs().add(e);
                    e.getAffairesAssignees().add(a);
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
    // Notify observers helper method (public wrapper)
    public void notifierChangement() {
        setChanged();
        notifyObservers();
    }
}
