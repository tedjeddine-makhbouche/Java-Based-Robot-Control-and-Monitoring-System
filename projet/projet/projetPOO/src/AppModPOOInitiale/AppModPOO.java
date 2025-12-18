package AppModPOOInitiale;

import BDInitiale.BaseDeDonnees;
import CommRobotOmronJNInitiale.RobotOmronJN;

public class AppModPOO {

    private RobotOmronJN robot; 
    private BaseDeDonnees bd;
    private Utilisateur utilisateur;


    // Constructeur

    public AppModPOO(Utilisateur utilisateur) {
        try {
            this.robot = new RobotOmronJN();
            this.bd = new BaseDeDonnees();
            this.utilisateur = utilisateur;
        } catch (Exception e) {
            System.out.println("Erreur initialisation AppModPOO : " + e.getMessage());
        }
    }

   
    // Getter nécessaire pour vérifier le rôle dans l'IHM

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }


    // Méthode principale appelée par l’IHM : exécution commande
  
    public String choisirCommande(String commande) {

        try {
            int idUser = Integer.parseInt(utilisateur.getId());

            // 1) Enregistrement de la tâche
            bd.enregistrerTache(commande, idUser);

            // 2) Notification début
            bd.ajouterNotification("Commande " + commande + " démarrée par " + utilisateur.getNom(), idUser);

            // 3) Exécution du robot
            String resultatRobot = "";

            switch (commande) {
                case "RECUPERER":
                    resultatRobot = robot.recupererProduit();
                    break;

                case "DEPOSER":
                    resultatRobot = robot.deposerProduit();
                    break;

                case "DOCK":
                    resultatRobot = robot.retournerDock();
                    break;

                default:
                    return "Commande inconnue.";
            }

            // 4) Mise à jour : tâche terminée
            bd.terminerDerniereTache();

            // 5) Notification fin
            bd.ajouterNotification("Commande " + commande + " terminée.", idUser);

            return resultatRobot;

        } catch (Exception e) {

            // En cas d'erreur → notification d'erreur
            try {
                int idUser = Integer.parseInt(utilisateur.getId());
                bd.ajouterNotification("Erreur durant " + commande + " : " + e.getMessage(), idUser);
            } catch (Exception ignored) {}

            return "Erreur : " + e.getMessage();
        }
    }


    // Statistiques : durée moyenne

    public double reqDureeMoyenne() {
        return bd.getDureeMoyenne();
    }

    // Statistiques : nb de tâches de l'opérateur
  
    public int reqNbTachesUtilisateur() {
        int idUser = Integer.parseInt(utilisateur.getId());
        return bd.getNbRqParOperateur(idUser);
    }

    
    // Rapport complet
    
    public String reqRapport() {
        return bd.reqRapport();
    }

   
    // Notifications pour l'IHM
    
    public String reqNotifications() {
        return bd.lireNotifications();
    }
}