package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ListeCrime extends JFrame {

    private JPanel panelHaut;
    private JPanel panelCentre;
    private JPanel panelGauche;
    private JPanel panelDroite;

    private JButton buttonAjouter;

    private JTextArea crime;
    private JTextArea sanction;

    public ListeCrime(){

        setTitle("Gestion des Criminels");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        add(panelHaut, BorderLayout.NORTH);
        add(panelCentre, BorderLayout.CENTER);

        buttonAjouter.addActionListener(e -> ajouterCrime());

        setVisible(true);
    }

    public void ajouterCrime(){

    }
}
