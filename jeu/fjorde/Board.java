package jeu.fjorde;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {
	
	private ArrayList<Tile> alTiles;

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
	
	
	
	public int getNbHut(FjordePlayer p){
		int cpt = 0;
		for(Tile t : alTiles){
			if(t.getOwner()==p && t.getItem()==Item.HUTTE){cpt++;}
		}
		return cpt;
	}
	
	public void add(Tile t){
		alTiles.add(t);
	}
	
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
