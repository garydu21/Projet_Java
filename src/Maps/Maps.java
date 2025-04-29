package Maps;

import Vue.VueAffaires;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import Modele.Modele;

public class Maps extends JFrame implements MouseListener {


    private Modele mdl;

    public Maps(Modele modele) {
        this.mdl = modele;
        setTitle("Carte de France");
        setSize(800, 800);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        this.addMouseListener(this);
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        ImageIcon imageIcon = new ImageIcon("image/Carte_France_geo_dep.png");
        g.drawImage(imageIcon.getImage(), 10, 25, 800, 800, null);
        g.setColor(Color.RED);

        //Paris
        g.fillOval(390,200,50,50);

        //Lille
        g.fillOval(440,70,25,25);

        //Lyon
        g.fillOval(550,460,25,25);

        //Marseille
        g.fillOval(575,660,25,25);

        //Toulouse
        g.fillOval(355,625,25,25);

        //Nantes
        g.fillOval(185,340,25,25);

        //Strasbourg
        g.fillOval(690,240,25,25);

        //Bordeaux
        g.fillOval(240,510,25,25);

        //Brest
        g.fillOval(40,240,25,25);

        //Caen
        g.fillOval(245,185,25,25);

        //Montpellier
        g.fillOval(495,625,25,25);

        //Tours
        g.fillOval(315,335,25,25);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX() >= 245 && e.getX() <= 270 && e.getY() >= 185 && e.getY() <= 205) {
            new VueAffaires(mdl,"Caen");
        }
        if (e.getX() >= 495 && e.getX() <= 520 && e.getY() >= 625 && e.getY() <= 650) {
            new VueAffaires(mdl,"Montpellier");
        }
        if (e.getX() >= 315 && e.getX() <= 340 && e.getY() >= 335 && e.getY() <= 360) {
            new VueAffaires(mdl,"Tours");
        }
        if (e.getX() >= 40 && e.getX() <= 65 && e.getY() >= 240 && e.getY() <= 265) {
            new VueAffaires(mdl,"Brest");
        }
        if (e.getX() >= 390 && e.getX() <= 440 && e.getY() >= 200 && e.getY() <= 250) {
            new VueAffaires(this.mdl,"Paris");
        }
        if (e.getX() >= 440 && e.getX() <= 465 && e.getY() >= 70 && e.getY() <= 95) {
            new VueAffaires(this.mdl,"Lille");
        }
        if (e.getX() >= 550 && e.getX() <= 575 && e.getY() >= 460 && e.getY() <= 485) {
            new VueAffaires(this.mdl,"Lyon");
        }
        if (e.getX() >= 575 && e.getX() <= 600 && e.getY() >= 660 && e.getY() <= 685) {
            new VueAffaires(this.mdl,"Marseille");
        }
        if (e.getX() >= 355 && e.getX() <= 380 && e.getY() >= 625 && e.getY() <= 650) {
            new VueAffaires(this.mdl,"Toulouse");
        }
        if (e.getX() >= 185 && e.getX() <= 205 && e.getY() >= 340 && e.getY() <= 365) {
            new VueAffaires(this.mdl,"Nantes");
        }
        if (e.getX() >= 690 && e.getX() <= 715 && e.getY() >= 240 && e.getY() <= 265) {
            new VueAffaires(this.mdl,"Strasbourg");
        }
        if (e.getX() >= 240 && e.getX() <= 265 && e.getY() >= 510 && e.getY() <= 535) {
            new VueAffaires(this.mdl,"Bordeaux");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
