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
        	if(item.equals(Item.HUTTE) && setHutte(item,p)){ b=true; }
        	if(item.equals(Item.CHAMP) && setChamp(item,p)){ b=true; }
        }
        return b;
    }

    /**
     * 
     * @param item La hutte
     * @param p le joueur jouant cette hutte
     * @return si une hutte a été ajoutée par un joueur
     */
    public boolean setHutte(Item item, FjordePlayer p){
    	boolean b = false;
    	
    	if( !p.putHutte() )
    		return b;
    	
    	if(!this.isStart()){
	    	int cpt=0;
	    	for(int i=0; i<this.types.length; i++){
	    		if(this.types[i].equals(Type.MONTAGNE)){ cpt++; }
	    	}
	        if( cpt == 6 ) {
	        	this.item = item;
	        	this.owner = p;
	        	b = true;
	        }
    	}
    	return b;
    }

    /**
     * 
     * @param item Le champ
     * @param p Le joueur jouant ce champ
     * @return si un champ a été ajouté par un joueur
     */
    public boolean setChamp(Item item, FjordePlayer p){
    	boolean b = false;
    	
    	if( !p.putChamp() )
    		return b;
    	
    	for(int i=0; i<this.neighboors.length; i++){
    		if(this.neighboors[i]!=null && this.neighboors[i].getItem()!=null && p==this.neighboors[i].getOwner()){
    			if(i!=5){
    				if(!this.types[i].equals(Type.MONTAGNE) && !this.types[i+1].equals(Type.MONTAGNE)
    						|| !this.types[i].equals(Type.EAU) && !this.types[i+1].equals(Type.EAU)){
    					this.item = item;
    		        	this.owner = p;
    		        	b = true;
    				}
    			}
    			else{
    				if(!this.types[i].equals(Type.MONTAGNE) && !this.types[0].equals(Type.MONTAGNE)
    						|| !this.types[i].equals(Type.EAU) && !this.types[0].equals(Type.EAU)){
    					this.item = item;
    		        	this.owner = p;
    		        	b = true;
    				}
    			}
    		}
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
    		//System.out.println(this.neighboors[i]);
    		if(this.neighboors[i]!= null)
    			s += i + " : " + this.neighboors[i].toString() + "\n"; 		
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
    	// Si l'id n'est pas bon
    	if( id<0 || id>this.neighboors.length )
        	return false;
        
    	// S'il y a déjà une tuile
        if( this.neighboors[id] != null )
        	return false;
        
        
        for( int i=0; i<t.neighboors.length; i++ ) {
    		if(this.getTypesById(id).get(0)==t.getTypesById(i).get(0) && 
					this.getTypesById(id).get(1)==t.getTypesById(i).get(1)) {
				neighboors[id] = t;
				t.neighboors[i]=this;
				return true;
			}
        }
        
        return false;
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
