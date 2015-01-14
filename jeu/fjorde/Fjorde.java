package jeu.fjorde;

import java.io.FileNotFoundException;
import java.io.IOException;

import server.Server;

import jeu.IJeu;

public class Fjorde implements IJeu {
	private Board board;
	private FjordePlayer[] players;
	private int currentPlayer;
	private int nbPlayer;
	private Closed pickClose;
	private Open pickOpen;
	private Server server;
	
	public Fjorde() {
		this.board = new Board();
		this.pickClose = new Pick(true);
		this.pickOpen = new Pick(false);
		this.players = new FjordePlayer[2];
		this.currentPlayer = 0;
		this.nbPlayer = 0;
		
		try {
			this.server = new Server(this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet de piocher dans la pioche ferme
	 * @return Si la pioche ferme est vide
	 */
	public boolean draw () {
		if( !this.pickClose.isEmpty()) {
			players[currentPlayer].draw(pickClose.draw());
			return true;
		}
		
		return false;
	}
	
	/**
	 * Permet de piocher dans la pioche ouverte
	 * @param i Indice de la tuile voulu
	 * @return Si la pioche ouverte est vide ou si l'indice est errone
	 */
	public boolean draw (int i) {
		if( i>=0 && i<this.pickOpen.getSize()) {
			players[currentPlayer].draw(pickOpen.draw(i));
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param id L'identifiant de la tuile
	 * @param side Cote de la tuile sur leplateau
	 * @return Si la pose est correcte 
	 */
	public boolean play(int id, int side) {
		return this.board.add(id, side, players[currentPlayer].removeFromHand());
	}
	
	/**
	 * Permet de poser une hutte
	 * @param i Indice de la tuile dans l'ArrayList
	 * @return Si la hutte a ete poser
	 */
	public boolean putHutte(int i){
		if(i>=0 && i<this.board.getSize()){
			this.board.getTile(i).setItem(Item.HUTTE, players[currentPlayer]);
			return true;
		}
		return false;
	}
	
	/**
	 * Permet de poser un champ
	 * @param i Indice de la tuile dans l'ArrayList
	 * @return Si le champ a ete poser
	 */
	public boolean putChamp(int i){
		if(i>=0 && i<this.board.getSize()){
			this.board.getTile(i).setItem(Item.CHAMP, players[currentPlayer]);
			return true;
		}
		return false;
	}
	
	
	public void putInPickOpen() {
		// A modifie
		this.pickOpen.add(players[currentPlayer].removeFromHand());
	}
	
	/**
	 * Change le joueur courant
	 */
	public void changePlayer() {
		this.currentPlayer = 1 - this.currentPlayer;
	}
	
	@Override
	public void add(String name, String id) {
		server.log("Ajout du joueur " + name + " avec l'id " + id);
		players[nbPlayer].setName(name);
		players[nbPlayer++].setId(id);
	}

	@Override
	public void sendToAllPlayers(String action) throws IOException {
		server.sendToAllClient(action);
	}

	@Override
	public void sendToPlayer(String action) throws IOException {
		server.log("Message envoyé au joueur " + players[currentPlayer].getName() +  " : " + action);
		server.sendToClient(players[currentPlayer].getId(), action);
		
	}

	@Override
	public String receiveFromPlayer() throws IOException {
		String s = server.receive();
	    server.log("Message reçu d'un joueur : " + s);
		return s;
	}

	@Override
	public int processMessage(String msg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void launchGame() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
