package Modele;

import Criminel.Criminel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Modele extends Observable {
    private static final String FICHIER_JSON = "criminels.json";
    private List<Criminel> listeCriminel;

    public Modele() {
        this.listeCriminel = new ArrayList<>();
        chargerDonnees();
    }

    public List<Criminel> getListeCriminel() {
        return listeCriminel;
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

    public void exporterJson(File fichier) {
        try (FileWriter writer = new FileWriter(fichier)) {
            new Gson().toJson(listeCriminel, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importerJson(File fichier) {
        try (FileReader reader = new FileReader(fichier)) {
            Type listType = new TypeToken<ArrayList<Criminel>>() {}.getType();
            listeCriminel = new Gson().fromJson(reader, listType);
            setChanged();
            notifyObservers();
            sauvegarderDonnees();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}