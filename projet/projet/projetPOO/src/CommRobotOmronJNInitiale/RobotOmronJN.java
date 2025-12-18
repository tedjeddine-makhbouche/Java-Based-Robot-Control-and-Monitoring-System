package CommRobotOmronJNInitiale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RobotOmronJN {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public RobotOmronJN() throws IOException {
        socket = new Socket("localhost", 1234);
        out = new PrintWriter(socket.getOutputStream(), true);
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    
    // MÉTHODE GÉNÉRIQUE POUR ENVOYER DES COMMANDES

    private String envoyerCommande(String commande, String conditionFin, String messageRetour) throws IOException {

        out.println(commande);

        String line;
        while (!(line = in.readLine()).startsWith(conditionFin)) {}

        return messageRetour + " : " + line;
    }

   
    // 1. RÉCUPÉRER PRODUIT
   
    public String recupererProduit() throws IOException {
        return envoyerCommande("goTo s-112", "Arrived at", "Produit récupéré");
    }

    
    // 2. DÉPOSER PRODUIT
    
    public String deposerProduit() throws IOException {
        return envoyerCommande("goTo s-108", "Arrived at", "Produit déposé");
    }

    
    // 3. RETOURNER AU DOCK
    
    public String retournerDock() throws IOException {
        return envoyerCommande("dock", "DockingState: Docked", "Dock atteint");
    }
}
