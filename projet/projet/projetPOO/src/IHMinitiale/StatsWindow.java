package IHMinitiale;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import AppModPOOInitiale.AppModPOO;

class StatsWindow extends JFrame {

    private AppModPOO app;

    public StatsWindow(AppModPOO app) {
        this.app = app;

        setTitle("2eme fenetre");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        // Bouton durée moyenne
        JButton averageButton = new JButton("Durée moyenne");
        averageButton.addActionListener(e -> showAverage());
        add(averageButton);

        // Bouton nombre de tâches
        JButton requestCountButton = new JButton("Nb tâches utilisateur");
        requestCountButton.addActionListener(e -> showRequestCount());
        add(requestCountButton);

        setVisible(true);
    }

    private void showAverage() {
        double moyenne = app.reqDureeMoyenne();
        JOptionPane.showMessageDialog(this,
            "Durée moyenne entre opérations : " + moyenne + " secondes");
    }

    private void showRequestCount() {
        int nb = app.reqNbTachesUtilisateur();
        JOptionPane.showMessageDialog(this,
            "Nombre total de tâches effectuées : " + nb);
    }
}
