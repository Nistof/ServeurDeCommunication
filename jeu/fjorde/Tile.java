package jeu.fjorde;

import java.util.ArrayList;
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

public class Tile {
    
    private Type[] types;
    private Tile[] neighboors;
    private boolean start; 
    private FjordePlayer owner;
    private Item item;
    
    
    /**Retourne l'etat de la pioche
     * Construit une tuile
     * @param start Détermine si c'est une tuile de départ
     */
    public Tile (String type, boolean start) {
        this.types = new Type[6];
        this.neighboors = new Tile[6];
        this.start = start;
        this.item = null;
        this.owner = null;
        for(int i=0; i<type.length(); i++){ this.types[i] = Type.getTypeById(type.charAt(i)); }
    }

	/**
     * Permet de poser un item sur une tuile
     * @param item L'item à poser
     * @param p Le joueur détenant l'item
     * @return vrai si la pose de l'item a réussi
     */
    public boolean setItem (Item item, FjordePlayer p) {
        boolean b = false;
        if(this.item == null && item != null) {
            this.item = item;
            this.owner = p;
            b = true;
        }
        return b;
    }
    
    /**
     * @return si la tuile est de d�part ou pas
     */
    public boolean isStart() {
		return start;
	}

    /**
     * @return le joueur a qui appartient la tuile
     */
	public FjordePlayer getOwner() {
		return owner;
	}
   
	/**
	 * @return l'item pos� sur la tuile
	 */
    public Item getItem() {
		return item;
	}
    
    /**
     * permet d'avoir les sommet correspondant � une id donn�e
     * @param id num�ro de cot�
     * @return une arraylist de sommet
     */
    public ArrayList<Type> getTypesById(int id){
    	ArrayList<Type> alT = new ArrayList<Type>();
    	for(int i=0; i<2; i++){
    		if(id+i==6){
     			alT.add(this.types[0]);
     		}
    		else{alT.add(this.types[id+i]);}
    	}
    	return alT;
    }
    
    /**
     * verifie si c'est un voisin, et l'ajoute au tableau
     * @param t une tuile
     * @return boolean 
     */
    public boolean setNeighboor (Tile t) {
        //boolean b = false;
    	for(int i=0; i<this.neighboors.length; i++){
	    	if(neighboors[i] == null) {
	            for(int j=0; j<t.neighboors.length; j++){
	            	if(t.neighboors[i] == null){
            			if(this.getTypesById(i).get(0)==t.getTypesById(j).get(0) && 
            					this.getTypesById(i).get(1)==t.getTypesById(j).get(1)){
            				neighboors[i] = t;
            				t.neighboors[j]=this;
            				return true;
            			}
	            	}
	            }
	        }
    	}
        return false;         
    }
    
    /**
     * @return la liste des voisins
     */
	public String getNeighboors() {
		String s = "";
    	for(int i=0; i<this.neighboors.length; i++){	
    		System.out.println(this.neighboors[i]);
    		//if(this.neighboors[i]!= null)
    			//s += this.neighboors[i].toString(); 		
    	}
    	return s;
	}

	/**
     * Place une tuile voisine 
     * @param id Coté à utiliser
     * @param t Tuile à placer
     * @return vrai si la pose de la tuile a réussi
     */
    public boolean setNeighboorById (int id, Tile t) {
        boolean b = false;

        return b;
    }
    
    /**
     * Permet de fixer les 6 types de la tuile
     * @param types la chaîne à utiliser pour définir les types
     */
    public void setTypes (String types) {
        for(int i = 0; i < types.length() ; i++) {
            this.types[i] = Type.getTypeById(types.charAt(i));
        }
    }
    
    public String toString(){
    	String s = "";
    	for(int i=0; i<this.types.length; i++){	
    		s += this.types[i].toString(); 		
    	}
    	return s;
    }
}
