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

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\yaaco\\Desktop\\bureau\\javaProject\\image\\Carte_France_geo_dep.png");

        JLabel label = new JLabel(imageIcon);

        add(label);

        //405
        //255
        this.addMouseListener(this);
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\yaaco\\Desktop\\bureau\\javaProject\\image\\Carte_France_geo_dep.png");
        JLabel label = new JLabel(imageIcon);
        g.drawImage(imageIcon.getImage(), 10, 25, 800, 800, null);
        g.setColor(Color.RED);
        g.fillOval(390,200,50,50);
        add(label);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX() >= 390 && e.getX() <= 440 && e.getY() >= 200 && e.getY() <= 250) {
            new VueAffaires(this.mdl,"Paris");
        }
        System.out.println(e.getX());
        System.out.println(e.getY());
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
