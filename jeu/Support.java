package jeu;

public class Support {
	private String couleur;
	private boolean aBalle;
	
	public Support( String couleur, boolean aBalle ) {
		this.couleur = couleur;
		this.aBalle = aBalle;
	}
	
	public String getCouleur() {
		return this.couleur;
	}
	
	public boolean getABalle() {
		return this.aBalle;
	}
	
	public void setABalle() {
		if( this.aBalle ) 
			this.aBalle = false;
		else
			this.aBalle = true;
	}
}