package BDInitiale;

import java.sql.*;

public class BaseDeDonnees {

    
    // 1. Informations de connexion MySQL
    
    private static final String URL =
        "jdbc:mysql://mariadb-std-27e3ca74d485.apps.kappsul.su.univ-lorraine.fr:3306/Robot_app?useSSL=false";

    private static final String USER = "root";
    private static final String PASSWORD = "NGzHII74Vy";

    
    // 2. Connexion à la base de données
    
    private Connection connect() throws Exception {

        // Driver imposé pour mysql-connector-j 9.0.0
        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    
    // 3. Enregistrer une tâche (début)
    
    public void enregistrerTache(String typeCommande, int idUtilisateur) throws Exception {

        String sql =
            "INSERT INTO tache(typeCommande, dateHeure, statut, idUtilisateur) " +
            "VALUES (?, NOW(), 'en cours', ?)";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, typeCommande);
            ps.setInt(2, idUtilisateur);
            ps.executeUpdate();
        }
    }

    
    // 4. Terminer la dernière tâche
   
    public void terminerDerniereTache() throws Exception {

        String sql =
            "UPDATE tache SET statut='terminee', dateHeure=NOW() " +
            "WHERE id = (SELECT id FROM tache ORDER BY id DESC LIMIT 1)";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.executeUpdate();
        }
    }

    // 5. Durée moyenne entre deux tâches successives
    
    public double getDureeMoyenne() {

        String sql =
            "SELECT AVG(TIMESTAMPDIFF(SECOND, t1.dateHeure, t2.dateHeure)) AS moyenne " +
            "FROM tache t1 JOIN tache t2 ON t2.id = t1.id + 1";

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next())
                return rs.getDouble("moyenne");

        } catch (Exception e) {
            System.out.println("Erreur getDureeMoyenne : " + e.getMessage());
        }

        return 0;
    }


    // 6. Nombre de requêtes par utilisateur
    
    public int getNbRqParOperateur(int idUtilisateur) {

        String sql = "SELECT COUNT(*) AS nb FROM tache WHERE idUtilisateur=?";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUtilisateur);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getInt("nb");

        } catch (Exception e) {
            System.out.println("Erreur getNbRqParOperateur : " + e.getMessage());
        }

        return 0;
    }

    
    // 7. Rapport complet utilisé dans StatsWindow
  
    public String reqRapport() {

        StringBuilder sb = new StringBuilder();

        String sql =
            "SELECT t.id, t.typeCommande, t.dateHeure, t.statut, u.nom " +
            "FROM tache t JOIN utilisateur u ON u.id = t.idUtilisateur";

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id"))
                  .append(" | Commande: ").append(rs.getString("typeCommande"))
                  .append(" | Date: ").append(rs.getString("dateHeure"))
                  .append(" | Statut: ").append(rs.getString("statut"))
                  .append(" | Utilisateur: ").append(rs.getString("nom"))
                  .append("\n");
            }

        } catch (Exception e) {
            return "Erreur rapport : " + e.getMessage();
        }

        return sb.toString();
    }


    // 8. Ajouter une notification

    public void ajouterNotification(String message, int idUtilisateur) throws Exception {

        String sql = "INSERT INTO notification(message, idUtilisateur, dateHeure) " +
                     "VALUES(?, ?, NOW())";

        try (Connection con = connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, message);
            ps.setInt(2, idUtilisateur);
            ps.executeUpdate();
        }
    }

   
    // 9. Lire toutes les notifications (affichage dans l'IHM)
  
    public String lireNotifications() {

        StringBuilder sb = new StringBuilder();

        String sql =
            "SELECT n.message, n.dateHeure, u.nom " +
            "FROM notification n LEFT JOIN utilisateur u ON u.id = n.idUtilisateur " +
            "ORDER BY n.id DESC";

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                sb.append(rs.getString("dateHeure"))
                  .append(" → ")
                  .append(rs.getString("message"))
                  .append(" (")
                  .append(rs.getString("nom"))
                  .append(")")
                  .append("\n");
            }

        } catch (Exception e) {
            return "Erreur lors de la lecture des notifications : " + e.getMessage();
        }

        return sb.toString();
    }
}
