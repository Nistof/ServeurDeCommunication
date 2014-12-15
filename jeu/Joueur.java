package jeu;

public class Joueur {
	private String nom;
	private String id;

	
	public Joueur( String nom ) {
		this.nom = nom;
	}
	
	public String getNom() {
		return this.nom;
	}

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
