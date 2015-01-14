package jeu.fjorde;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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

public class Board {
	private static int CURRENT_ID = 0;
	private HashMap<Integer,Tile> hmTiles;

	/**
	 * Constructeur recupere les tuiles de depart pour les placer sur le plateau
	 */
	public Board(){
		this.hmTiles = new HashMap<Integer,Tile>(); 

		String nomFichier = System.getProperty("user.dir");
		nomFichier = nomFichier + "/Tuile.txt";
		Scanner fichier = null;
		String s;

		try { // ouverture
			fichier = new Scanner(new File(nomFichier));

			// traitement
			while (fichier.hasNext()) {
			    s = fichier.next();
				if(s != null && s.split(":")[1].equals("S")){
					this.hmTiles.put(CURRENT_ID++, new Tile(s.split(":")[0],true));
				}
			}

			// fermeture
			fichier.close();
		} catch (Exception exc) {
			System.out.println("Erreur fichier" + exc);
		}
		
		hmTiles.get(0).setNeighboorById(2,hmTiles.get(1));
		hmTiles.get(0).setNeighboorById(3,hmTiles.get(2));
		hmTiles.get(1).setNeighboorById(5,hmTiles.get(0));
		hmTiles.get(1).setNeighboorById(4,hmTiles.get(2));
		hmTiles.get(2).setNeighboorById(0,hmTiles.get(0));
		hmTiles.get(2).setNeighboorById(1,hmTiles.get(1));
		/*for(int i=0; i<alTiles.size(); i++){
			//alTiles.get(i-1).setNeighboor(alTiles.get(i));
			System.out.println(alTiles.get(i).getNeighboors());
		}*/
	}
	
	/**
	 * Donne pour un joueur donne les hutte qu'il possede sur le plateau
	 * @param p joueur de la tuile
	 * @return le nombre de hutte sur le plateau
	 */
	public int getNbHut(FjordePlayer p){
		int cpt = 0;
		for(Integer i : hmTiles.keySet()){
			Tile t = hmTiles.get(i);
			if(t.getOwner()==p && t.getItem()==Item.HUTTE){cpt++;}
		}
		return cpt;
	}
	
	/**
	 * Ajoute une tuile a la liste de tuiles presentent sur le plateau
	 * @param t Tuile a ajouter
	 */
	public boolean add(int id, int side, Tile t){
		if(t.setNeighboorById(side, hmTiles.get(id))){
			hmTiles.put(CURRENT_ID++,t);
			return true;
		}
		return false;
	}
	
	/**
	 * Attributs les scores aux joueurs
	 */
	public void setScore() {
		for( Tile t : this.alTiles ) {
			if( t.getItem()!=null && t.getItem()==Item.CHAMP)
				t.getOwner().setScore(1);
		}
	}
	
	/**
	 * 
	 * @return Le nombre de tuiles poser sur la table
	 */
	public int getSize(){
		return this.alTiles.size();
	}
	
	/**
	 * 
	 * @param i Indice de la tuile voulu
	 * @return Une tuile a un indice donne
	 */
	public Tile getTile(int i) {
		if( i>=0 && i<this.alTiles.size())
			return this.alTiles.get(i);
		
		return null;
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

	/**
	 * Permets de voir l'etat du plateau, les tuiles posees et leurs numeros
	 * @return une chaine de caractere de l'avance du jeu
	 */
	public String toString(){
		String s = "";
		for(int i=0;i<hmTiles.size(); i++){
			if(!hmTiles.get(i).isStart()){ s += "    "+ i +"   "; }
			else { s+= "         "; }
			
		}
		for(int i=0; i<hmTiles.size(); i++){
			//alTiles.get(i-1).setNeighboor(alTiles.get(i));
			System.out.println(hmTiles.get(i).getNeighboors());
			//System.out.println(alTiles.get(i).typePossibilities());
		}
		return s;
	}
}
