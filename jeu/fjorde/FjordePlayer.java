package jeu.fjorde;

import jeu.Player;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 01-05-2015
 */

public class FjordePlayer extends Player {
	private String color;
	private Tile tile;
	
	public FjordePlayer( String nom, String couleur, Pick p ) {
		super(nom);
		this.color = couleur;
		this.tile = p.draw();
	}
	
	/**
	 * Donne la couleur du joueur
	 * @return couleur du joueur
	 */
	public String getColor() {
		return this.color;
	}
	
	/**
	 * 
	 * @return
	 */
	public Tile getTile() {
		return this.tile;
	}
	
	public void put(Board board, Pick p){
		board.add(this.tile);
		this.tile=p.draw();
	}
	
}