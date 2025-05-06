package Controleur;

import Modele.Modele;
import Vue.Main.Vue;

import javax.swing.*;

public class Controleur {
    private Modele modele;
    private Vue vue;

    public Controleur() {
        this.modele = new Modele();
        this.vue = new Vue(this, modele);
        modele.addObserver(vue);  // Permet à la vue d'être mise à jour automatiquement
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Controleur::new);
    }
}
