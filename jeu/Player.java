package jeu;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 12-03-2014
 */

public class Player {
	private String name;
	private String id;

	
	public Player( String nom ) {
		this.name = nom;
	}
	
	/**
	 * Donne le nom du joueur
	 * @return nom du joueur
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Défini l'identifiant du joueur
	 * @param id identifiant du joueur
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Donne l'identifiant du joueur
     * @return identifiant du joueur
     */
    public String getId() {
        return id;
    }

    /**
     * Défini le nom du joueur
     * @param name nom du joueur
     */
    public void setName(String name) {
        this.name = name;
    }
}
