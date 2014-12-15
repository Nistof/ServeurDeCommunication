package jeu.diaballik;

import jeu.Joueur;


public class JoueurDiaballik extends Joueur {
	private String couleur;
	private Support[] pieces;
	
	public JoueurDiaballik( String nom, String couleur ) {
		super(nom);
		this.couleur = couleur;
		this.pieces = new Support[7];
		
		for( int i=0; i<pieces.length; i++ )
			this.pieces[i] = new Support( this.couleur, false );
			
		this.pieces[3].setABalle();
	}
	
	public String getCouleur() {
		return this.couleur;
	}
	
	public Support getSupport( int i ) {
		if( i>=0 && i<pieces.length )
			return this.pieces[i];
			
		return null;
	}
}