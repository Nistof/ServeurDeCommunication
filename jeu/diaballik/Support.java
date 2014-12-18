package jeu.diaballik;

public class Support {
	private String couleur;
	private boolean haveBall;
	
	public Support( String couleur, boolean aBalle ) {
		this.couleur = couleur;
		this.haveBall = aBalle;
	}
	
	public String getColor() {
		return this.couleur;
	}
	
	public boolean getHaveBall() {
		return this.haveBall;
	}
	
	public void setHaveBall() {
		if( this.haveBall ) 
			this.haveBall = false;
		else
			this.haveBall = true;
	}
}