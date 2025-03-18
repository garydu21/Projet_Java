package Modele;

import Criminel.Criminel;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Modele extends Observable {
    private List<Criminel> listeCriminel;

    public Modele() {
        this.listeCriminel = new ArrayList<>();
    }

    public List<Criminel> getListeCriminel() {
        return listeCriminel;
    }

    public void addListeCriminel(Criminel c) {
        this.listeCriminel.add(c);
        setChanged();
        notifyObservers();
    }

    public void supprimerCriminel(int index) {
        if (index >= 0 && index < listeCriminel.size()) {
            listeCriminel.remove(index);
            setChanged();
            notifyObservers();
        }
    }

    public void modifierCriminel(int index, String nom, String prenom) {
        if (index >= 0 && index < listeCriminel.size()) {
            listeCriminel.get(index).setNom(nom);
            listeCriminel.get(index).setPrenom(prenom);
            setChanged();
            notifyObservers();
        }
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
