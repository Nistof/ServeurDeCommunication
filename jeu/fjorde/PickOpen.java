package jeu.fjorde;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 01-05-2015
 */

public class PickOpen {
	
	private ArrayList<Tile> alTiles;
	
	/**
	 * Constructeur
	 * @param hidden Etat de la pioche
	 */
	public PickOpen(){
		this.alTiles = new ArrayList<Tile>(); 
	}
	
	/**
	 * Permet de savoir si la pioche est vide
	 * @return boolean 
	 */
	public boolean isEmpty() {
		if(this.alTiles.size() == 0 ){ return true; }
		return false;
	}
	
	public Tile getTile(int i){
		if(!this.isEmpty() && i>0 && i<this.alTiles.size()){
			return this.alTiles.get(i);
		}
		return null;
	}

	/**
	 * Supprime une tuile de la pioche
	 * @return Tile
	 */
	public Tile draw(int i){
		Tile t=null;
		if(!this.isEmpty() && i>0 && i<this.alTiles.size()){
			t = this.alTiles.get(i);
			this.alTiles.remove(i);
		}
		return t;
	}
	
	/**
	 * Ajoute à la pioche une tuile
	 * @param t Tuile à ajouter
	 */
	public void add(Tile t){
		alTiles.add(t);
	}
}
