package jeu.fjorde;

import jeu.Player;

public class Tile {
    
    private Type[] types;
    private Tile[] neighboors;
    private Player owner;
    private boolean start;
    private Item item;
    
    
    /**
     * Construit une tuile
     * @param start Détermine si c'est une tuile de départ
     */
    public Tile (boolean start) {
        this.types = new Type[6];
        this.neighboors = new Tile[6];
        this.start = start;
        this.item = null;
    }

    /**
     * Permet de poser un item sur une tuile
     * @param item L'item à poser
     * @param p Le joueur détenant l'item
     * @return vrai si la pose de l'item a réussi
     */
    public boolean setItem (Item item, Player p) {
        boolean b = false;
        if(this.item == null && item != null) {
            this.item = item;
            owner = p;
            b = true;
        }
        return b;
    }
    
    /**
     * Place une tuile voisine 
     * @param id Coté à utiliser
     * @param t Tuile à placer
     * @return vrai si la pose de la tuile a réussi
     */
    public boolean setNeighboorById (int id, Tile t) {
        boolean b = false;
        if(neighboors[id] == null) {
            neighboors[id] = t;
            b = true;
        }
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
}
