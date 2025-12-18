package BDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BD {

    static private String url = "jdbc:mysql://mariadb-std-27e3ca74d485.apps.kappsul.su.univ-lorraine.fr/universite";
    static private String username = "root";
    static private String password = "NGzHII74Vy";
    static private Statement statement;

    public static void connexionBD() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, username, password);
        statement = connection.createStatement();
        System.out.println("You are connected to the DB");
    }

    static public void selectFromprofesseur(String rqt) throws SQLException {
        ResultSet resultSet = statement.executeQuery(rqt);
        while (resultSet.next()) {
            int id = resultSet.getInt("id_prof");
            String nom = resultSet.getString("nom");
            String prenom = resultSet.getString("prenom");
            int age = resultSet.getInt("age");
            String email = resultSet.getString("email");
            System.out.println("ID: " + id + ", nom: " + nom + ", prenom: " + prenom + ", age: " + age + ", email: " + email);
        }
    }
}
