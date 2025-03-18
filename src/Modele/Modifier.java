package Modele;

import Criminel.Criminel;

import java.util.Scanner;

public class Modifier {

    private Modele modele;

    public Modifier(Modele modele) {
        this.modele = modele;
    }

    public void ajouterCriminelModifier(Criminel criminel) {
        this.modele.addListeCriminel(criminel);
        System.out.println(criminel.getNom());
    }
}
