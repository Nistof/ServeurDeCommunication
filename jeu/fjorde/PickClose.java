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

public class PickClose {
	
	private ArrayList<Tile> alTiles;
	
	/**
	 * Constructeur
	 * @param hidden Etat de la pioche
	 */
	public PickClose(){
		this.alTiles = new ArrayList<Tile>(); 

		String nomFichier = System.getProperty("user.dir");
		nomFichier = nomFichier + "/Tuile.txt";
		Scanner fichier = null;
		String s;

		try { // ouverture
			fichier = new Scanner(new File(nomFichier));

			// traitement
			while (fichier.hasNext()) {
				s = fichier.next();
				if(s != null && s.split(":")[1].equals("F")){
					this.alTiles.add(new Tile(s.split(":")[0],false));
				}
				
			}

			// fermeture
			fichier.close();
		} catch (Exception exc) {
			System.out.println("Erreur fichier" + exc);
		}
		
		Collections.shuffle(this.alTiles);
	
	}
	
	/**
	 * Permet de savoir si la pioche est vide
	 * @return boolean 
	 */
	public boolean isEmpty() {
		if(this.alTiles.size() == 0 ){ return true; }
		return false;
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
