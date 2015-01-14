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
	 * Constructeur r�cup�re les tuiles de d�part pour les placer sur le plateau
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
	 * Donne pour un joueur donn� les hutte qu'il poss�de sur le plateau
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
	 * Ajoute une tuile � la liste de tuiles pr�sentent sur le plateau
	 * @param t tuile a jouter
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
			if( t.getItem()!=null)
				t.getOwner().setScore(1);
		}
	}

	/**
	 * Permets de voir l'état du plateau, les tuiles pos�es et leurs num�ros
	 * @return une chaine de caract�re de l'avanc� du jeu
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
