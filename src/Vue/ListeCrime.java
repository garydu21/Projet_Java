package Vue;

import Criminel.Crime;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ListeCrime extends JFrame {

    private JPanel panelHaut;
    private JPanel panelCentre;
    private JPanel panelGauche;
    private JPanel panelDroite;
    private JPanel panelBas;

    private JButton buttonAjouter;

    private JTextArea crime;
    private JTextArea sanction;

    private JTextField lesCrime;

    private ArrayList<Crime> crimes;
    public ListeCrime(ArrayList<Crime> crimes) {
        this.crimes = crimes;

        setTitle("Gestion des Criminels");
        setSize(400, 400);
        setLayout(new BorderLayout());

        panelHaut = new JPanel();

        buttonAjouter = new JButton("Ajouter");
        panelHaut.add(buttonAjouter);

        panelCentre = new JPanel();

        panelGauche = new JPanel();
        crime = new JTextArea();
        crime.setPreferredSize(new Dimension(150, 30));
        panelGauche.add(crime);

        panelDroite = new JPanel();
        sanction = new JTextArea();
        sanction.setPreferredSize(new Dimension(150, 30));
        panelDroite.add(sanction);

        panelCentre.add(panelGauche);
        panelCentre.add(panelDroite);

        panelBas = new JPanel();
        panelBas.setLayout(new BorderLayout());
        lesCrime = new JTextField();
        lesCrime.setEditable(false);

        lesCrime.setText("");
        StringBuilder sb = new StringBuilder();
        for (Crime c : crimes) {
            if (c.getIntitule().equalsIgnoreCase(crimes.getLast().getIntitule())) {
                sb.append(c.getIntitule()).append(":").append(c.getPeine());
            }
            else {
                sb.append(c.getIntitule()).append(":").append(c.getPeine()).append(" | ");
            }
        }
        lesCrime.setText(sb.toString());

        panelBas.add(lesCrime);

        add(panelHaut, BorderLayout.NORTH);
        add(panelCentre, BorderLayout.CENTER);
        add(panelBas, BorderLayout.SOUTH);

        buttonAjouter.addActionListener(e -> ajouterCrime());

        setVisible(true);
    }

    public void ajouterCrime(){
        int peine = Integer.parseInt(this.sanction.getText());
        String str = this.crime.getText();
        this.crimes.add(new Crime(peine, str));

        lesCrime.setText("");
        StringBuilder sb = new StringBuilder();
        for (Crime c : crimes) {
            if (c.getIntitule().equalsIgnoreCase(crimes.getLast().getIntitule())) {
                sb.append(c.getIntitule()).append(":").append(c.getPeine());
            }
            else {
                sb.append(c.getIntitule()).append(":").append(c.getPeine()).append(" | ");
            }
        }
        lesCrime.setText(sb.toString());


        this.revalidate();
        this.repaint();
    }

    public ArrayList<Crime> getCrimes(){
        return this.crimes;
    }
}
