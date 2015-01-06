package jeu.fjorde;

import java.util.ArrayList;

public class Pick {
	
	private ArrayList<Tile> alTiles;
	private boolean hidden;
	
	/**
	 * Constructeur
	 * @param hidden Etat de la pioche
	 */
	public Pick(boolean hidden){
		this.alTiles = new ArrayList<Tile>(); 
		this.hidden = hidden;
	}
	
	/**
	 * Permet de savoir si la pioche est caché ou pas
	 * @return boolean 
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * Supprime une tuile de la pioche
	 * @return Tile
	 */
	public Tile draw(){
		return alTiles.remove(alTiles.size()-1);
	}
	
	/**
	 * Ajoute à la pioche une tuile
	 * @param t Tuile à ajouter
	 */
	public void add(Tile t){
		alTiles.add(t);
	}
}
