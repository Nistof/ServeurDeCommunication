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
	private int score;
	private int winRound;
	
	public FjordePlayer( String nom, String couleur ) {
		super(nom);
		this.color = couleur;
		this.nbHutte = 4;
		this.nbChamp = 20;
		this.score = 0;
		this.winRound = 0;
	}
	
	/**
	 * Donne la couleur du joueur
	 * @return La couleur du joueur
	 */
	public String getColor() {
		return this.color;
	}
	
	/**
	 * 
	 * @return La tuile issue de la main du joueur
	 */
	public Tile getTile() {
		return this.tile;
	}
	
	/**
	 * 
	 * @return Le score du joueur
	 */
	public int getScore() {
		return this.score;
	}
	
	/**
	 * 
	 * @return Le nombre de manche gagne
	 */
	public int getWinRound() {
		return this.winRound;
	}
	
	
	/**
	 * 
	 * @return Le nombre de champ que possede le joueur en main
	 */
	public int getNbChamp() {
		return this.nbChamp;
	}
	
	/**
	 * 
	 * @return Le nombre de hutte que possede le joueur en main
	 */
	public int getNbHutte() {
		return this.nbHutte;
	}
	
	/**
	 * 
	 * @param score Le score que le joueur gagne au cours d'une manche
	 * @return Si le score ajoute est valide
	 */
	public boolean setScore( int score ) {
		if( score>=0 ) {
			this.score += score;
			return true;
		}
		return false;
	}
	
	/**
	 * Incremente winRound si le joueur gagne une manche
	 */
	public void setWinRound() {
		this.winRound++;
	}
	
	/**
	 * Pose une hutte
	 * @return Si la pose d'une hutte est possible
	 */
	public boolean putHutte(){
		if(nbHutte>0){ 
			this.nbHutte--;
			return true;
		}
		return false;
	}
	
	/**
	 * Pose un champ
	 * @return Si la pose d'un champ est possible
	 */
	public boolean putChamp(){
		if(nbChamp>0){ 
			this.nbChamp--;
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param tile Ajoute une tuile dans la main du joueur
	 */
	public void draw(Tile tile) {
		this.tile = tile;
	}

	/**
	 * 
	 * @return La tuile supprime de la main
	 */
	public Tile removeFromHand() {
		Tile tmp = this.tile;
		tile = null;
		//System.out.println(tmp);
		return new Tile("ETMTTE",false,false);
	}
	
}