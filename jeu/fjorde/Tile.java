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

	private String originalCode;
	private String orientedCode;
    private Type[] types;
    private Tile[] neighboors;
    private boolean fake;
    private boolean start;
    private FjordePlayer owner;
    private Item item;
    private int orientation;
    
    /*
     * Ce constructeur construit une tuile fantôme 
     */
    public Tile() {
        this("FFFFFF", false, true);
    }

    
    /**
     * 
     * @param type Le type de la tuile
     * @param start Indique s'il s'agit d'une tuile de départ
     * @param fake Indique si c'est une fausse tuile
     */
    public Tile(String type, boolean start, boolean fake) {
        this.fake = fake;
        this.types = new Type[6];
        this.neighboors = new Tile[6];
        this.start = start;
        this.item = null;
        this.owner = null;
        this.orientation = 0;
        this.originalCode = type;
        this.orientedCode = type;
        for (int i = 0; i < type.length(); i++) {
            this.types[i] = Type.getTypeById(type.charAt(i));
        }
    }
    
    
    private void updateTypes (int newOrientation) {
    	char[] typeC = originalCode.toCharArray();
    	System.out.println(originalCode);
    	char tmp1, tmp2;
		for (int i = 0; i < newOrientation; i++) {
			tmp2 = typeC[0];
			for (int j = 0; j < 6; j++) {
				tmp1 = typeC[(j+1)%6];
				typeC[(j+1)%6] = tmp2;
				tmp2 = tmp1;
			}
		}
    	this.orientedCode = new String(typeC);
    	System.out.println(orientedCode);
    	for (int i = 0; i < orientedCode.length()-1; i++) {
            this.types[i] = Type.getTypeById(orientedCode.charAt(i));
        }
    }
    
    public int getOrientation() {
		return orientation;
	}


	public void setOrientation(int orientation) {
		updateTypes(orientation);
        this.orientation = orientation;
    }
    
    /*
     * Renvoie le nombre de voisins
     */
    public int getNeighboorsCount () {
    	return this.neighboors.length;
    }
    
    /**
     * Permet de poser un item sur une tuile
     * 
     * @param item L'item à poser
     * @param p Le joueur détenant l'item
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
     * @param p Joueur qui demande si le placement d'un champ est possible
     * @return Si le placement du champ est possible
     */
    public boolean putChampIsValid(FjordePlayer p) {
        boolean b = false;

        if (p.getNbChamp() == 0)
            return b;

        for (int i = 0; i < this.neighboors.length; i++) {
            if (this.neighboors[i%6] != null
                    && this.neighboors[i%6].getItem() != null
                    && p == this.neighboors[i].getOwner()) {
                if (!this.types[i%6].equals(Type.MONTAGNE)
                        && !this.types[(i + 1) % 6].equals(Type.MONTAGNE)
                        || !this.types[i%6].equals(Type.EAU)
                        && !this.types[(i + 1) % 6].equals(Type.EAU)) {
                    b = true;
                }
            }
        }
        return b;
    }

    /**
     * 
     * @param p Joueur qui demande si le placement d'une hutte est possible
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
                if (this.types[i%6].equals(Type.MONTAGNE))
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
     * permet d'avoir les sommet correspondant a une id donnee
     * 
     * @param id numero de cote
     * @return une arraylist de sommet
     */
    public Type[] getTypesBySide(int side) {
        Type[] neighboors = new Type[2];
        neighboors[0] = types[side%6];
        neighboors[1] = types[(side + 1) % 6];
        return neighboors;
    }

    /**
     * Permet d'obtenir le voisin par son id
     * @param side Cote utilise pour renvoyer le voisin
     * @return Le voisin au cote side
     */
    public Tile getNeighboorBySide(int side) {
    	return neighboors[(6+side)%6];
    }

    /**
     * Place une tuile sur un des cotes
     * @param t La tuile a placer
     * @param side Le cote sur lequel la placer
     * @return
     */
    public boolean setNeighboorById(Tile t, int side) {
        boolean b = false;
        if ((this.neighboors[side%6] == null || this.neighboors[side%6].isFakeTile())) {
            if (this.isStart() && t.isStart()) {
                b = true;
                this.neighboors[side%6] = t;
                t.setNeighboorById(this, (side + 3) % 6);
            }
            else {
                if(getNumberOfRealNeighboors((side)%6) >= 1 && isValid(t, side%6) && setNeighboorByCircle(t, side%6, 0)) {
                    t.neighboors[(side+3)%6] = this;
                    this.neighboors[side%6] = t;
                    b = true;
                }
            }
        }
        return b;
    }
    
    /**
     * Renvoie le nombre de voisins reels sur le cote concerne
     * @param side Le cote a inspecter
     * @return Le nombre de voisins reels
     */
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
    
    /**
     * Renvoie le premier voisin reel de la tuile
     * @return Le premier voisin reel trouve
     */
    public int getFirstRealNeighboor() {
    	int t = -1;
    	for(int i = 0; t == -1 && i < this.neighboors.length; i++) {
    		if(this.neighboors[i%6] != null && !this.neighboors[i%6].isFakeTile()) {
    			t = i;
    		}
    	}
    	return t;
    }
    
    /**
     * Fixe les voisins en faisant le tour recusivement (permet de combler les trous)
     * @param t La tuile a ajouter
     * @param sideOrg Le cote d'origine
     * @param turn Le tour courant
     * @return vrai si la pose a reussi
     */
	private boolean setNeighboorByCircle (Tile t, int sideOrg, int turn) {
    	boolean b = true;
    	if(turn == 0) {
    		b &= getNeighboorsByDirection(sideOrg%6, -1).setNeighboorByCircle(t, (sideOrg+1)%6, turn+1) &&
    				getNeighboorsByDirection((sideOrg)%6, 1).setNeighboorByCircle(t, (6+sideOrg-1)%6, turn-1);
    	}
    	else if (Math.abs(turn) == 3) {
    		if(isValid(t, sideOrg)) {
    			this.neighboors[sideOrg] = t;
    			t.neighboors[(sideOrg+3)%6] = this;
    		}
    	}
    	else {
    		int direction = turn/Math.abs(turn);
    		b &= isValid(t, (sideOrg)%6) && getNeighboorsByDirection((sideOrg)%6, direction).setNeighboorByCircle(t, (6+sideOrg-direction)%6, turn+direction);
    		if(b) {
    			this.neighboors[sideOrg] = t;
    			t.neighboors[(sideOrg+3)%6] = this;
    		}
    	}
        return b;
    }
    
	/**
	 * Permet de savoir si c'est une fausse tuile
	 * @return vrai si c'est une fausse tuile
	 */
    public boolean isFakeTile() {
        return fake;
    }
    
    /**
     * Permet d'avoir le voisin dans une direction donnee
     * @param side Le cote d'ou l'on part
     * @param direction 1 pour le sens horaire, -1 pour le sens anti-horaire
     * @return La tuile voisine dans la direction donnee
     */
    private Tile getNeighboorsByDirection (int side, int direction) {
        return neighboors[(6+side+direction)%6];

    }
    
    /**
     * Permet de fixer les 6 types de la tuile
     * @param types La chaine a utiliser pour definir les types
     */
    public void setTypes(String types) {
        for (int i = 0; i < types.length(); i++) {
            this.types[(i)%6] = Type.getTypeById(types.charAt(i));
        }
    }
    
    /**
     * Donne le code la tuile (Ex : "ETTMET")
     * @return Le code de la tuile
     */
    public String getCode() {
    	return this.originalCode;
    }
    
    /**
     * Permet de voir si une tuile peut etre place contre la tuile en cours
     * @param t la tuile a placer
     * @param side le cote ou l'on place
     * @return vrai si le placement est valide
     */
    public boolean isValid (Tile t, int side) {
        Type[] neighboors1 = this.getTypesBySide(side);
        Type[] neighboors2 = t.getTypesBySide((side+3)%6);
        boolean b = true;
        b &= (neighboors1[0] == neighboors2[1] && neighboors1[1] == neighboors2[0]) || isGhost(neighboors1);
        return b;
    }
    
    /**
     * 
     * @param neighboors1 types d'un cote d'une tuile
     * @return vrai si les types sont fantomes
     */
    private boolean isGhost(Type[] neighboors1) {
        return neighboors1[0] == null && neighboors1[1] == null;
    }
    
    
    
    public String toString() {
        String s = "";
        for (int i = 0;!isFakeTile() &&  i < this.types.length; i++) {
            s += this.types[i].toString();
        }
        if(isFakeTile()) {
        	s += "Fantome";
        }
        return s;
    }
    
    /**
     * Permet de generer les tuiles fantomes qui permettront
     * de faire des liaisons entre les tuiles meme non adjacentes
     */
    public void generateGhosts() {
        for (int i = 0; i < neighboors.length; i++) {
        	if(neighboors[(i)%6] == null) {
        		neighboors[(i)%6] = new Tile();
        		neighboors[(i)%6].neighboors[(i+3)%6] = this;
        	}
        }
        for (int i = 0; i < neighboors.length; i++) {
            neighboors[(i)%6].neighboors[(((i+3)%6)+1)%6] = neighboors[(6+i-1)%6];
            neighboors[(i)%6].neighboors[(6+((i+3)%6)-1)%6] = neighboors[(i+1)%6];
        }
        
    }
    
    
}
