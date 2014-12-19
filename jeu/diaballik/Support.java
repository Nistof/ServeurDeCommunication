package jeu.diaballik;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 12-03-2014
 */

public class Support {
	private String couleur;
	private boolean haveBall;
	
	public Support( String couleur, boolean aBalle ) {
		this.couleur = couleur;
		this.haveBall = aBalle;
	}
	
	/**
	 * Donne la couleur du support
	 * @return Couleur du support
	 */
	public String getColor() {
		return this.couleur;
	}
	
	/**
	 * Donne le status du support (s'il possède une balle ou non)
	 * @return Vrai si le support possède une balle
	 */
	public boolean getHaveBall() {
		return this.haveBall;
	}
	
	/**
	 * Permet de donner ou retirer la balle à un support
	 */
	public void toggleHaveBall() {
		this.haveBall = !this.haveBall;
	}
}