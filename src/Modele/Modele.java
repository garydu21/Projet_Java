package Modele;

import Criminel.Criminel;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Scanner;

public class Modele extends Observable {


    private ArrayList<Criminel> listeCriminel;

    private int criminelSelect = 0;

    public Modele() {
        this.listeCriminel = new ArrayList<>();
    }

    public void addListeCriminel(Criminel c) {
        this.listeCriminel.add(c);

        System.out.println(c.getNom());
    }

    //    private ArrayList<Criminel> listeCriminel;
//
//    private Button[] tabButtons;
//
//    public Modele(){
//        this.listeCriminel = new ArrayList<>();
//    }
//
//    // Methode qui lorsque est appelé, va faire une recherche dans une liste de criminel et va
//    // Retourne une liste des criminels qui correspond aux String passé en paramêtre.
//    public ArrayList<Criminel> rechercheCriminel(String nomCriminel) {
//        ArrayList<Criminel> criminelBarreRecherce = new ArrayList<Criminel>(); // On initialise la liste qu'on va remplir
//
//        for (Criminel unCriminel : listeCriminel) { // On parcours la liste contenant tout les criminels
//            if (unCriminel.getNom().contains(nomCriminel)) { // On verifie si un nom de criminel contient les characters
//                criminelBarreRecherce.add(unCriminel); // On l'ajoute à la liste
//            }
//        }
//
//        return criminelBarreRecherce; // On retourne la liste
//    }
//
//    public int getIndexCriminel(Criminel unCriminel) {
//        for (Criminel criminel : listeCriminel) {
//            if (criminel.getNom().equals(unCriminel.getNom())) {
//                return listeCriminel.indexOf(criminel);
//            }
//        }
//        return this.listeCriminel.toArray().length;
//    }
//
//    public ArrayList<Criminel> getListeCriminel() {
//        return this.listeCriminel;
//    }
//
//    public ArrayList<Criminel> getCriminelAffaireCommun() {
//        ArrayList<Criminel> criminelAffaireCommun = new ArrayList<Criminel>();
//        ArrayList<Criminel> affaire = this.getListeCriminel();
//        for (Criminel c : affaire) {
//            for (Criminel c2 : affaire) {
//                if (!c.equals(c2)) {
//                    if (c.getPreuve().contains(c2.getPreuve())){
//                        criminelAffaireCommun.add(c);
//                    }
//                }
//            }
//        }
//    }
//
//    public void changeCriminel(Criminel criminel) {
//        this.listeCriminel.remove(getIndexCriminel(criminel));
//        this.listeCriminel.add(criminel);
//        this.setChanged();
//    }
}
