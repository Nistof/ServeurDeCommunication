package jeu.diaballik;

import jeu.Player;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 12-03-2014
 */

public class DiaballikPlayer extends Player {
	private String color;
	private Support[] pieces;
	
	public DiaballikPlayer( String nom, String couleur ) {
		super(nom);
		this.color = couleur;
		this.pieces = new Support[7];
		
		for( int i=0; i<pieces.length; i++ )
			this.pieces[i] = new Support( this.color, false );
			
		this.pieces[3].toggleHaveBall();
	}
	
	/**
	 * Donne la couleur du joueur
	 * @return couleur du joueur
	 */
	public String getColor() {
		return this.color;
	}
	
	/**
	 * Donne le support d'identifiant i du joueur
	 * @param i Identifiant du support
	 * @return Support d'identifiant i
	 */
	public Support getSupport( int i ) {
		if( i>=0 && i<pieces.length )
			return this.pieces[i];
			
		return null;
	}
}