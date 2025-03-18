package Controleur;

import Modele.Modele;
import Vue.Vue;

import javax.swing.*;
import java.util.Observer;

public class Controleur {
    private Modele modele;
    private Vue vue;

    public Controleur() {
        this.modele = new Modele();
        this.vue = new Vue(this, modele);
        modele.addObserver((Observer) vue);  // Permet à la vue d'être mise à jour automatiquement
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Controleur::new);
    }
}
