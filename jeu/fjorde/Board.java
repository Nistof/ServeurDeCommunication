package jeu.fjorde;

import java.io.File;
import java.util.ArrayList;
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
	
	private ArrayList<Tile> alTiles;

	/**
	 * Constructeur r�cup�re les tuiles de d�part pour les placer sur le plateau
	 */
	public Board(){
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
				if(s != null && s.split(":")[1].equals("S")){
					this.alTiles.add(new Tile(s.split(":")[0],true));
				}
			}

			// fermeture
			fichier.close();
		} catch (Exception exc) {
			System.out.println("Erreur fichier" + exc);
		}
		
		for(int i=1; i<alTiles.size(); i++){
			alTiles.get(i-1).setNeighboor(alTiles.get(i));
			System.out.println(alTiles.get(i-1).getNeighboors());
		}
	}
	
	//public int 
	
	/**
	 * Donne pour un joueur donn� les hutte qu'il poss�de sur le plateau
	 * @param p joueur de la tuile
	 * @return le nombre de hutte sur le plateau
	 */
	public int getNbHut(FjordePlayer p){
		int cpt = 0;
		for(Tile t : alTiles){
			if(t.getOwner()==p && t.getItem()==Item.HUTTE){cpt++;}
		}
		return cpt;
	}
	
	/**
	 * Ajoute une tuile � la liste de tuiles pr�sentent sur le plateau
	 * @param t tuile a jouter
	 */
	public void add(Tile t){
		alTiles.add(t);
	}
	
	/**
	 * Permets de voir l'état du plateau, les tuiles pos�es et leurs num�ros
	 * @return une chaine de caract�re de l'avanc� du jeu
	 */
	public String boardDraw(){
		String s = "";
		s += this.toString() +"\n";
		for(int i=0;i<alTiles.size(); i++){
			if(!alTiles.get(i).isStart()){ s += "    "+ i +"   "; }
			else { s+= "        "; }
			
		}
		return s;
	}
	
	public String toString(){
		return alTiles.toString();
	}
}
