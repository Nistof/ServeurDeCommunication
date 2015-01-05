package jeu.fjorde;

public class Tile {
    
    private Type[] types;
    private boolean start;
    private Item item;
    
    
    /**
     * Construit une tuile
     * @param start Détermine si c'est une tuile de départ
     */
    public Tile (boolean start) {
        this.types = new Type[6];
        this.start = start;
        this.item = null;
    }
    
    /**
     * 
     * @param item
     * @return
     */
    public boolean setItem (Item item) {
        boolean b = true;
        if(this.item != null || item == null) {
            b = false;
        }
        else {
            this.item = item;
        }
        return b;
    }
    
    
}
