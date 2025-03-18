package Controleur;

import Criminel.Criminel;
import Vue.Vue;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Controleur {
    private List<Criminel> criminels;
    private Vue vue;

    public Controleur() {
        this.criminels = new ArrayList<>();
        this.vue = new Vue(this, criminels);
    }

    private void rafraichirVue() {
        SwingUtilities.invokeLater(() -> {
            vue.dispose();
            vue = new Vue(this, criminels);
        });
    }

    public static void main(String[] args) {
        new Controleur();
    }
}
