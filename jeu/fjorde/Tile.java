package jeu.fjorde;

import java.util.ArrayList;

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
	private boolean[] possibilities;
	private boolean start;
	private FjordePlayer owner;
	private Item item;

	public Tile() {
		this.types = new Type[6];
		this.neighboors = new Tile[6];
	}

	/**
	 * Retourne l'etat de la pioche Construit une tuile
	 * 
	 * @param start Determine si c'est une tuile de départ
	 */
	public Tile(String type, boolean start) {
		this.types = new Type[6];
		this.neighboors = new Tile[6];
		this.possibilities = new boolean[6];
		this.start = start;
		this.item = null;
		this.owner = null;
		for (int i = 0; i < type.length(); i++) {
			this.types[i] = Type.getTypeById(type.charAt(i));
		}
		for (int i = 0; i < neighboors.length; i++) {
			this.neighboors[i] = new Tile();
		}

	}

	/**
	 * Permet de poser un item sur une tuile 
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
	public ArrayList<Type> getTypesById(int id) {
		ArrayList<Type> alT = new ArrayList<Type>();
		alT.add(types[id]);
		alT.add(types[(id + 1) % 6]);
		return alT;
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
	public String getNeighboors() {
		String s = "";
		for (int i = 0; i < this.neighboors.length; i++) {
			// System.out.println(this.neighboors[i]);
			if (this.neighboors[i] != null)
				s += i + " : " + this.neighboors[i].toString() + "\n";
		}
		return s;
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
	public boolean setNeighboorById(int id, Tile t) {
		// Si l'id n'est pas bon
		if (id < 0 || id > this.neighboors.length)
			return false;

		// S'il y a déjà une tuile
		if (this.neighboors[id] != null)
			return false;

		for (int i = 0; i < t.neighboors.length; i++) {
			if (this.getTypesById(id).get(0) == t.getTypesById(i).get(1)
					&& this.getTypesById(id).get(1) == t.getTypesById(i).get(0)) {
				neighboors[id] = t;
				possibilities[id % 6] = true;
				possibilities[(id + 1) % 6] = true;
				if (!t.isStart()) {
					if (isValid(typePossibilities())) {
						t.neighboors[i] = this;
						t.possibilities[i % 6] = true;
						t.possibilities[(i + 1) % 6] = true;
					}
				}
				return true;
			}
		}

		return false;
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

	public boolean isValid(ArrayList<Type> alT) {
		int cpt = 0, j = 0;
		for (int i = 0; i < this.types.length; i++) {
			if (this.types[i].equals(alT.get(0))) {
				for (Type t : alT) {
					if (t.equals(this.types[i + j])) {
						cpt++;
						j++;
					} else {
						cpt = 0;
					}

					if (i + j == 5) {
						j = -i;
					}
				}
			}
		}
		if (cpt == alT.size()) {
			return true;
		}
		return false;
	}

	public ArrayList<Type> typePossibilities() {
		ArrayList<Type> alT = new ArrayList<Type>();
		for (int i = 0; i < this.neighboors.length; i++) {
			if (this.neighboors[i] != null) {
				alT.add(this.neighboors[i].getTypesById(i).get(0));
			}
		}
		return alT;
	}

	public String getCode() {
		String s = "";
		for (int i = 0; i < types.length; i++) {
			s += types[i];
		}
		return s;
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < this.types.length; i++) {
			s += this.types[i].toString();
		}
		int cpt = 0;
		for (int a = 0; a < this.neighboors.length; a++) {
			if (neighboors[a] != null) {
				cpt++;
			}
		}
		s += ":" + cpt;
		return s;
	}
}
