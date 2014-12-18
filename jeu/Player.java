package jeu;

public class Player {
	private String nom;
	private String id;

	
	public Player( String nom ) {
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
