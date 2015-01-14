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

public class Pick implements Closed,Open {
	
	private ArrayList<Tile> alTiles;
	
	/**
	 * Constructeur
	 * @param hidden Etat de la pioche
	 */
	public Pick(boolean hidden){
		if(hidden) {
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
						this.alTiles.add(new Tile(s.split(":")[0],false,false));
					}
					
				}
	
				// fermeture
				fichier.close();
			} catch (Exception exc) {
				System.out.println("Erreur fichier" + exc);
			}
			
			Collections.shuffle(this.alTiles);
		} else {
			this.alTiles = new ArrayList<Tile>(); 
		}
	
	}
	
	/**
	 * Permet de savoir si la pioche est vide
	 * @return boolean 
	 */
	public boolean isEmpty() {
		if(this.alTiles.isEmpty() )
			return true;
		
		return false;
	}
	
	public int getSize() {
		return this.alTiles.size();
	}
	
	/**
	 * Ajoute a la pioche une tuile
	 * @param t Tuile a ajouter
	 */
	public void add(Tile t){
		alTiles.add(t);
	}
	
	/**
	 * @param i Indice de la tuile
	 * @return Une tuile a un indice donne (pioche ouverte)
	 */
	public Tile getTile(int i){
		if(!this.isEmpty() && i>0 && i<this.alTiles.size())
			return this.alTiles.get(i);
		
		return null;
	}

	/**
	 * @param i Indice de la tuile
	 * @return Tile Une tuile de la pioche a un indice donne (pioche ouverte)
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
	 * @return La derniere tuile de la pioche (pioche ferme)
	 */
	public Tile draw(){
		return alTiles.remove(alTiles.size()-1);
	}
	
	/**
	 * 
	 * @param code Le code de la tuile
	 * @return Le numero de la tuile dans l'ArrayList correspondant au code
	 */
	public int getTileByCode( String code ) {
		for( int i=0; i<this.alTiles.size(); i++ ) {
			if( this.alTiles.get(i).getCode().equals(code))
				return i;
		}
		
		return -1;
	}
}
