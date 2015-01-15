package jeu.fjorde;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	 * Constructeur recupere les tuiles de depart pour les placer sur le plateau
	 */
	public Board() {
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
				if (s != null && s.split(":")[1].equals("S")) {
					Tile t = new Tile(s.split(":")[0], true, false);
					this.alTiles.add(t);
				}
			}

			// fermeture
			fichier.close();
		} catch (FileNotFoundException exc) {
			System.out.println("Erreur fichier" + exc);
		}

		/* Liaison des tuiles de depart puis generation des fantomes */
		alTiles.get(0).setNeighboorById(alTiles.get(1), 2);
		alTiles.get(0).generateGhosts();
		alTiles.get(1).setNeighboorById(alTiles.get(2), 4);
		alTiles.get(1).generateGhosts();
		alTiles.get(2).setNeighboorById(alTiles.get(0), 0);
		alTiles.get(2).generateGhosts();
	}

	/**
	 * Donne pour un joueur donne les hutte qu'il possede sur le plateau
	 * 
	 * @param p joueur de la tuile
	 * @return le nombre de hutte sur le plateau
	 */
	public int getNbHut(FjordePlayer p) {
		int cpt = 0;
		for (Tile t : alTiles) {
			if (t.getOwner() == p && t.getItem() == Item.HUTTE) {
				cpt++;
			}
		}
		return cpt;
	}

	/**
	 * Ajoute une tuile a la liste de tuiles presentent sur le plateau
	 * 
	 * @param t Tuile a ajouter
	 */
	public boolean add(Tile t, int side, int id) {
		if (alTiles.get(id).setNeighboorById(t, side)) {
			alTiles.add(t);
			alTiles.get(alTiles.size() - 1).generateGhosts();
			return true;
		}

		return false;
	}

	/**
	 * Attribue les scores aux joueurs
	 */
	public void setScore() {
		for (Tile t : this.alTiles) {
			if (t.getItem() != null && t.getItem() == Item.CHAMP)
				t.getOwner().setScore(1);
		}
	}

	/**
	 * 
	 * @return Le nombre de tuiles posé sur la table
	 */
	public int getSize() {
		return this.alTiles.size();
	}

	/**
	 * 
	 * @param i Indice de la tuile voulue
	 * @return Une tuile a un indice donne
	 */
	public Tile getTile(int i) {
		if (i >= 0 && i < this.alTiles.size())
			return this.alTiles.get(i);

		return null;
	}

	/**
	 * 
	 * @param code Le code de la tuile
	 * @return Le numero de la tuile dans l'ArrayList correspondant au code
	 */
	public int getTileByCode(String code) {
		for (int i = 0; i < this.alTiles.size(); i++) {
			if (this.alTiles.get(i).getCode().equals(code))
				return i;
		}

		return -1;
	}
	
	/**
	 * Retourne les possibilites de jeu selon une tuile donne
	 * @param selection La tuile a jouer
	 * @return une hashmap liant une tuile a un cote
	 */
	public HashMap<Tile, Integer> getPossibilities(Tile selection) {
		HashMap<Tile, Integer> hmTiles = new HashMap<Tile, Integer>();
		for (Tile t : getVoid()) {
			boolean b = true;
			for (int i = 0; b & i < t.getNeighboorsCount(); i++) {
				Tile currentNeighboor = t.getNeighboorBySide(i);
				if(currentNeighboor != null)
					b &= currentNeighboor.isValid(selection, (i + 3) % 6);
			}
			if (b) {
				int firstNeighboor = t.getFirstRealNeighboor();
				hmTiles.put(t.getNeighboorBySide(firstNeighboor),
						(firstNeighboor + 3) % 6);
			}
		}
		return hmTiles;
	}
	
	/**
	 * Permet de connaitre les tuiles fantomes ayant 2 voisins cote a cote
	 * @return un hashset des tuiles concernees
	 */
	private HashSet<Tile> getVoid() {
		HashSet<Tile> hsTile = new HashSet<Tile>();
		for (Tile t : alTiles) {
			for (int i = 0; i < t.getNeighboorsCount(); i++) {
				if (t.getNumberOfRealNeighboors(i) >= 1) {
					Tile n = t.getNeighboorBySide(i);
					if (n.isFakeTile()
							&& n.getNumberOfRealNeighboors((i + 3) % 6) >= 1) {
						hsTile.add(n);
					}
				}
			}
		}
		System.out.println(hsTile);
		return hsTile;
	}

	/**
	 * Permet de voir l'etat du plateau, les tuiles posees et leurs numeros
	 * @return une chaine de caractere de l'avance du jeu
	 */
	public String toString() {
		String s = "";
		for (Tile t : alTiles) {
			s += t + ",";
		}
		return s;
	}
}
