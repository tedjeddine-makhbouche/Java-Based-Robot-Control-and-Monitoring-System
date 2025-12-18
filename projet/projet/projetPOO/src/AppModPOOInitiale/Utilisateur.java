package AppModPOOInitiale;

public class Utilisateur {

    protected String id;
    protected String nom;
    protected String role;

    public Utilisateur(String id, String nom, String role) {
        this.id = id;
        this.nom = nom;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getRole() {
        return role;
    }
}
