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
	
	public DiaballikPlayer( String nom, String couleur ) {
		super(nom);
		this.color = couleur;
	}
	
	/**
	 * Donne la couleur du joueur
	 * @return couleur du joueur
	 */
	public String getColor() {
		return this.color;
	}
}