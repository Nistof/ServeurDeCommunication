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
	private int nbHutte;
	private int nbChamp;
	
	public FjordePlayer( String nom, String couleur, Closed p ) {
		super(nom);
		this.color = couleur;
		this.tile = p.draw();
		this.nbHutte = 4;
		this.nbChamp = 20;
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
	
	public boolean putHutte(){
		if(nbHutte>0){ 
			this.nbHutte--;
			return true;
		}
		return false;
	}
	
	public boolean putChamp(){
		if(nbChamp>0){ 
			this.nbChamp--;
			return true;
		}
		return false;
	}
	
	public void put(Board board, Closed pickClose){
		board.add(this.tile);
		this.tile=pickClose.draw();
	}
	
}