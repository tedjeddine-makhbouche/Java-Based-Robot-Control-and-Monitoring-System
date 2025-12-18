package IHMinitiale;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import AppModPOOInitiale.AppModPOO;
import AppModPOOInitiale.Operateur;
import AppModPOOInitiale.RespAtelier;
import AppModPOOInitiale.Utilisateur;

public class IHM extends JFrame {
	
    private JComboBox<String> userDropdown; 
    private JComboBox<String> taskDropdown; 
    private JLabel statusLabel;
    private JTextArea notifArea;
    private AppModPOO app;

    public IHM() {

        setTitle("Robot Task Selection");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        
        // 1) Choix utilisateur
        
        userDropdown = new JComboBox<>(new String[]{
            "1 - Nabil GAFFOUCHE (operateur)",
            "2 - John STEWARD (operateur)",
            "3 - Amine LAEREDJ (responsable)"
        });
        add(userDropdown);

        JButton selectUserButton = new JButton("Sélectionner utilisateur");
        selectUserButton.addActionListener(e -> initialiserApp());
        add(selectUserButton);

       
        // 2) Choix de la tâche
       
        taskDropdown = new JComboBox<>(new String[]{
            "Récupérer produit",
            "Déposer produit",
            "Retour au dock"
        });
        add(taskDropdown);

        JButton executeButton = new JButton("OK");
        executeButton.addActionListener(new ExecuteTaskListener());
        add(executeButton);

        
        // 3) Affichage du statut
       
        statusLabel = new JLabel("Aucun utilisateur sélectionné");
        add(statusLabel);

        
        // 4) Bouton Statistiques (seul responsable)
        
        JButton openStatsButton = new JButton("cliquer pour allez vers une autre fenetre");
        openStatsButton.addActionListener(e -> {

            if (app == null) {
                statusLabel.setText("Sélectionnez d'abord un utilisateur !");
                return;
            }

            if (!app.getUtilisateur().getRole().equals("responsable")) {
                statusLabel.setText("Accès refusé : réservé au responsable atelier.");
                return;
            }

            new StatsWindow(app);
        });
        add(openStatsButton);

        
        // 5) Zone Notifications
       
        notifArea = new JTextArea(10, 45);
        notifArea.setEditable(false);

        JScrollPane scrollNotif = new JScrollPane(
            notifArea,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        add(scrollNotif);

        // Mise à jour automatique des notifications
        Timer timer = new Timer(2000, e -> rafraichirNotifications());
        timer.start();

        setVisible(true);
    }

    private void initialiserApp() {

        String choix = (String) userDropdown.getSelectedItem();

        if (choix.startsWith("1")) {
            Utilisateur u = new Operateur("1", "Nabil GAFFOUCHE");
            app = new AppModPOO(u);
            statusLabel.setText("Utilisateur sélectionné : Nabil GAFFOUCHE");
        }
        else if (choix.startsWith("2")) {
            Utilisateur u = new Operateur("2", "John STEWARD");
            app = new AppModPOO(u);
            statusLabel.setText("Utilisateur sélectionné : John STEWARD");
        }
        else if (choix.startsWith("3")) {
            Utilisateur u = new RespAtelier("3", "Amine LAEREDJ");
            app = new AppModPOO(u);
            statusLabel.setText("Utilisateur sélectionné : Amine LAEREDJ");
        }
    }

    private void rafraichirNotifications() {
        if (app != null) {
            notifArea.setText(app.reqNotifications());
        }
    }

    private class ExecuteTaskListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (app == null) {
                statusLabel.setText("Sélectionnez d'abord un utilisateur !");
                return;
            }

            String selectedTask = (String) taskDropdown.getSelectedItem();
            String resultat = "";

            if (selectedTask.equals("Récupérer produit")) {
                resultat = app.choisirCommande("RECUPERER");
            }
            else if (selectedTask.equals("Déposer produit")) {
                resultat = app.choisirCommande("DEPOSER");
            }
            else if (selectedTask.equals("Retour au dock")) {
                resultat = app.choisirCommande("DOCK");
            }

            statusLabel.setText(resultat);
        }
    }
}
