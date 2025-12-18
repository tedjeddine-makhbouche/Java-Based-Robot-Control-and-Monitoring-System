package BDD;

import java.sql.SQLException;

public class main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        BD.connexionBD();
        BD.selectFromprofesseur("select * from professeur");
    }

}
