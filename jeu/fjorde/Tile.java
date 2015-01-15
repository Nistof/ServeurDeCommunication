package jeu.fjorde;


/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 05-01-2015
 */

public class Tile {

    private Type[] types;
    private Tile[] neighboors;
    private boolean fake;
    private boolean start;
    private FjordePlayer owner;
    private Item item;

    public Tile() {
        this("FFFFFF", false, true);
    }

    /**
     * Retourne l'etat de la pioche Construit une tuile
     * 
     * @param start
     *            Determine si c'est une tuile de départ
     */
    public Tile(String type, boolean start, boolean fake) {
        this.fake = fake;
        this.types = new Type[6];
        this.neighboors = new Tile[6];
        this.start = start;
        this.item = null;
        this.owner = null;
        for (int i = 0; i < type.length(); i++) {
            this.types[i] = Type.getTypeById(type.charAt(i));
        }
    }

    public int getNeighboorsCount () {
    	return this.neighboors.length;
    }
    
    /**
     * Permet de poser un item sur une tuile
     * 
     * @param item
     *            L'item à poser
     * @param p
     *            Le joueur détenant l'item
     * @return Si la pose de l'item a reussi
     */
    public boolean setItem(Item item, FjordePlayer p) {
        boolean b = false;
        if (this.item == null && item != null) {
            if (item.equals(Item.HUTTE) && putHutteIsValid(p)
                    || item.equals(Item.CHAMP) && putChampIsValid(p)) {
                this.item = item;
                this.owner = p;
                b = true;
            }
        }
        return b;
    }

    /**
     * 
     * @param p
     *            Joueur qui demande si le placement d'un champ est possible
     * @return Si le placement du champ est possible
     */
    public boolean putChampIsValid(FjordePlayer p) {
        boolean b = false;

        if (p.getNbChamp() == 0)
            return b;

        for (int i = 0; i < this.neighboors.length; i++) {
            if (this.neighboors[i] != null
                    && this.neighboors[i].getItem() != null
                    && p == this.neighboors[i].getOwner()) {
                if (!this.types[i].equals(Type.MONTAGNE)
                        && !this.types[(i + 1) % 6].equals(Type.MONTAGNE)
                        || !this.types[i].equals(Type.EAU)
                        && !this.types[(i + 1) % 6].equals(Type.EAU)) {
                    b = true;
                }
            }
        }
        return b;
    }

    /**
     * 
     * @param p
     *            Joueur qui demande si le placement d'une hutte est possible
     * @return Si le placement d'une hutte est possible
     */
    public boolean putHutteIsValid(FjordePlayer p) {
        boolean b = false;

        if (p.getNbHutte() == 0)
            return b;

        // On ne peut pas poser une hutte sur les tuiles de depart
        if (!this.isStart()) {
            int cpt = 0; // Nombre de somments montagne sur la tuile
            for (int i = 0; i < this.types.length; i++) {
                if (this.types[i].equals(Type.MONTAGNE))
                    cpt++;
            }

            // Si la tuile n'est pas recouverte entierement de montagne
            if (cpt != 6)
                b = true;
        }
        return b;
    }

    /**
     * @return si la tuile est de depart ou pas
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
     * @return l'item pose sur la tuile
     */
    public Item getItem() {
        return item;
    }

    /**
     * permet d'avoir les sommet correspondant � une id donn�e
     * 
     * @param id
     *            numero de cote
     * @return une arraylist de sommet
     */
    public Type[] getTypesBySide(int side) {
        Type[] neighboors = new Type[2];
        neighboors[0] = types[side];
        neighboors[1] = types[(side + 1) % 6];
        return neighboors;
    }

    /**
     * verifie si c'est un voisin, et l'ajoute au tableau
     * 
     * @param t
     *            une tuile
     * @return boolean
     */
    /*
     * public boolean setNeighboor (Tile t) { for(int i=0;
     * i<this.neighboors.length; i++){ if(neighboors[i] == null) { for(int j=0;
     * j<t.neighboors.length; j++){ if(t.neighboors[i] == null){
     * if(this.getTypesById(i).get(0)==t.getTypesById(j).get(0) &&
     * this.getTypesById(i).get(1)==t.getTypesById(j).get(1)){ neighboors[i] =
     * t; t.neighboors[j]=this; return true; } } } } } return false; }
     */

    /**
     * @return la liste des voisins
     */
    public Tile getNeighboorBySide(int side) {
    	return neighboors[side];
        /*String s = "";
        for (int i = 0; i < this.neighboors.length; i++) {
            // System.out.println(this.neighboors[i]);
            if (this.neighboors[i] != null)
                s += i + " : " + this.neighboors[i].toString() + "\n";
        }
        return s;*/
    }

    /**
     * Place une tuile voisine
     * 
     * @param id
     *            Cote a utiliser
     * @param t
     *            Tuile a placer
     * @return vrai si la pose de la tuile a reussi
     */
    public boolean setNeighboorById(Tile t, int side) {
        System.out.println("Tuile : " + t + " place sur le cote " + side + " de " + this + "\n");
        boolean b = false;
        //System.out.println(this.neighboors[side]);
        if ((this.neighboors[side] == null || this.neighboors[side].isFakeTile())) {
            if (this.isStart() && t.isStart()) {
                b = true;
                this.neighboors[side] = t;
                //setNeighboorByCircle(t, side, 0);
                t.setNeighboorById(this, (side + 3) % 6);
            }
            else {
                if(getNumberOfRealNeighboors(side) >= 1 && isValid(t, side) && setNeighboorByCircle(t, side, 0)) {
                    t.neighboors[(side+3)%6] = this;
                    this.neighboors[side] = t;
                    b = true;
                }
            }
        }
        return b;
    }

    public int getNumberOfRealNeighboors(int side) {
		int n = 0;
		if(this.neighboors[(6+side-1)%6] != null && !this.neighboors[(6+side-1)%6].isFakeTile()) {
			n++;
		}
		if(this.neighboors[(side+1)%6] != null && !this.neighboors[(side+1)%6].isFakeTile()) {
			n++;
		}
		return n;
	}

    public int getFirstRealNeighboor() {
    	int t = -1;
    	for(int i = 0; t == -1 && i < this.neighboors.length; i++) {
    		if(this.neighboors[i] != null && !this.neighboors[i].isFakeTile()) {
    			t = i;
    		}
    	}
    	return t;
    }
    
	private boolean setNeighboorByCircle (Tile t, int sideOrg, int turn) {
    	boolean b = true;
    	if(turn == 0) {
    		b &= getNeighboorsByDirection(sideOrg, -1).setNeighboorByCircle(t, (sideOrg+1)%6, turn+1) &&
    				getNeighboorsByDirection(sideOrg, 1).setNeighboorByCircle(t, (6+sideOrg-1)%6, turn-1);
    	}
    	else if (Math.abs(turn) == 3) {
    		if(isValid(t, sideOrg)) {
    			this.neighboors[sideOrg] = t;
    			t.neighboors[(sideOrg+3)%6] = this;
    		}
    	}
    	else {
    		System.out.println(turn);
    		int direction = turn/Math.abs(turn);
    		b &= isValid(t, sideOrg) && getNeighboorsByDirection(sideOrg, direction).setNeighboorByCircle(t, (6+sideOrg-direction)%6, turn+direction);
    		if(b) {
    			this.neighboors[sideOrg] = t;
    			t.neighboors[(sideOrg+3)%6] = this;
    		}
    	}
        return b;
    }
    
    public boolean isFakeTile() {
        return fake;
    }

    private Tile getNeighboorsByDirection (int side, int direction) {
        Tile t = neighboors[(6+side+direction)%6];
        System.out.println(t);
        return t;
    }
    
    /**
     * Permet de fixer les 6 types de la tuile
     * 
     * @param types
     *            La chaine a utiliser pour definir les types
     */
    public void setTypes(String types) {
        for (int i = 0; i < types.length(); i++) {
            this.types[i] = Type.getTypeById(types.charAt(i));
        }
    }

    public String getCode() {
        String s = "";
        for (int i = 0; i < types.length; i++) {
            s += types[i];
        }
        return s;
    }

    public boolean isValid (Tile t, int side) {
    	System.out.println("Side : " + side);
        Type[] neighboors1 = this.getTypesBySide(side);
        Type[] neighboors2 = t.getTypesBySide((side+3)%6);
        boolean b = true;
        b &= (neighboors1[0] == neighboors2[1] && neighboors1[1] == neighboors2[0]) || isGhost(neighboors1);
        System.out.println(neighboors1[0] + " : " + neighboors1[1]);
        System.out.println(neighboors2[0] + " : " + neighboors2[1]);
        return b;
    }
    
    private boolean isGhost(Type[] neighboors1) {
        return neighboors1[0] == null && neighboors1[1] == null;
    }

    public String toString() {
        String s = "";
        for (int i = 0;!isFakeTile() &&  i < this.types.length; i++) {
            s += this.types[i].toString();
        }
        int cpt = 0,  f= 0;
        for (int a = 0; a < this.neighboors.length; a++) {
            if (neighboors[a] != null && !neighboors[a].isFakeTile()) {
                cpt++;
            }
            if(neighboors[a] == null)
            	f++;
        }
        s += ":" + cpt + ":" + f;
        return s;
    }

    public void generateGhosts() {
        for (int i = 0; i < neighboors.length; i++) {
        	if(neighboors[i] == null) {
        		neighboors[i] = new Tile();
        		neighboors[i].neighboors[(i+3)%6] = this;
        	}
        }
        for (int i = 0; i < neighboors.length; i++) {
            neighboors[i].neighboors[((i+3%6)+1)%6] = neighboors[(6+i-1)%6];
            neighboors[i].neighboors[(6+(i+3%6)-1)%6] = neighboors[(i+1)%6];
        }
        
    }
    
    
}
