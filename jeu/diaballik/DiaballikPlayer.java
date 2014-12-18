package jeu.diaballik;

import jeu.Player;


public class DiaballikPlayer extends Player {
	private String color;
	private Support[] pieces;
	
	public DiaballikPlayer( String nom, String couleur ) {
		super(nom);
		this.color = couleur;
		this.pieces = new Support[7];
		
		for( int i=0; i<pieces.length; i++ )
			this.pieces[i] = new Support( this.color, false );
			
		this.pieces[3].setHaveBall();
	}
	
	public String getColor() {
		return this.color;
	}
	
	public Support getSupport( int i ) {
		if( i>=0 && i<pieces.length )
			return this.pieces[i];
			
		return null;
	}
}