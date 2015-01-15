package jeu.fjorde;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;

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
		this.players[0] = new FjordePlayer("SARAH", "MARRON");
		this.players[0] = new FjordePlayer("FLORIAN", "BLANC");
		this.currentPlayer = 0;
		this.nbPlayer = 0;

		/*
		 * try { this.server = new Server(this); } catch (FileNotFoundException
		 * e) { e.printStackTrace(); }
		 */
	}

	/**
	 * Permet de piocher dans la pioche ferme
	 * 
	 * @return Si la pioche ferme est vide
	 */
	public boolean draw() {
		boolean b = false;

		if (!this.pickClose.isEmpty()) {
			if (this.players[currentPlayer].getTile() == null) {
				players[currentPlayer].draw(pickClose.draw());
				b = true;
			} else if (this.players[currentPlayer].getTile() != null) {
				if (putInPickOpen()) {
					players[currentPlayer].draw(pickClose.draw());
					b = true;
				}
			}
		}

		return b;
	}

	/**
	 * Permet de piocher dans la pioche ouverte
	 * 
	 * @param i
	 *            Indice de la tuile voulu
	 * @return Si la pioche ouverte est vide ou si l'indice est errone
	 */
	public boolean draw(int i) {
		boolean b = false;

		if (i >= 0 && i < this.pickOpen.getSize()) {
			if (this.players[currentPlayer].getTile() == null) {
				players[currentPlayer].draw(pickOpen.draw(i));
				b = true;
			} else if (this.players[currentPlayer].getTile() != null) {
				if (putInPickOpen()) {
					players[currentPlayer].draw(pickOpen.draw(i));
					b = true;
				}
			}
		}
		return b;
	}

	/**
	 * 
	 * @return Si le joueur peut se deffausser
	 */
	public boolean discardTile() {
		if (players[currentPlayer].getTile() != null) { // AJOUTER UN TEST
			this.pickOpen.add(players[currentPlayer].removeFromHand());
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param id
	 *            L'identifiant de la tuile
	 * @param side
	 *            Cote de la tuile sur leplateau
	 * @return Si la pose est correcte
	 */
	public boolean play(int id, int side) {

		return (id >= 0) ? this.board.add(
				players[currentPlayer].removeFromHand(), side, id) : false;
	}

	/**
	 * Permet de poser un champ
	 * 
	 * @param i
	 *            Indice de la tuile dans l'ArrayList
	 * @return Si le champ a ete poser
	 */
	public boolean putItem(int i, char c) {
		if (i >= 0 && i < this.board.getSize()) {
			this.board.getTile(i).setItem(Item.getItemById(c),
					players[currentPlayer]);
			return true;
		}
		return false;
	}

	public boolean putInPickOpen() {
		// A modifie : Test si la personne ne peut pas jouer
		if (this.players[currentPlayer].getTile() != null) {
			this.pickOpen.add(players[currentPlayer].removeFromHand());
			return true;
		}
		return false;
	}

	/**
	 * Change le joueur courant
	 */
	public void changePlayer() {
		this.currentPlayer = 1 - this.currentPlayer;
	}

	/**
	 * 
	 * @return Si le joueur courrant peut poser un champ
	 */
	public boolean putTileByPlayerIsValid() {
		boolean b = true;
		for (int i = 0; i < this.board.getSize(); i++) {
			if (this.board.getTile(i).putChampIsValid(players[currentPlayer]))
				b = false;
		}

		return b;
	}

	/**
	 * @return l'id du joueur gagnant
	 */
	public int win() {
		if (this.players[currentPlayer].getScore() > this.players[1 - currentPlayer]
				.getScore()) {
			return currentPlayer;
		}
		return (1 - currentPlayer);
	}

	/**
	 * 
	 * @return Si c'est la fin de la phase colonisation (pose de champ
	 *         impossible)
	 */
	public boolean endColonization() {
		boolean b = true;
		for (int i = 0; i < this.board.getSize(); i++) {
			if (this.board.getTile(i).putChampIsValid(players[0])
					|| this.board.getTile(i).putChampIsValid(players[1]))
				b = false;
		}

		return b;
	}

	/**
	 * 
	 * @return Si c'est la fin de la phase de decouverte (pioche ferme vide)
	 */
	public boolean endDiscovery() {
		return this.pickClose.isEmpty();
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
		server.log("Message envoyé au joueur "
				+ players[currentPlayer].getName() + " : " + action);
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
		System.out.println(msg);
		String[] action = msg.trim().split(":");
		if (!action[0].equals(players[currentPlayer].getId()))
			return -2;
		if (action[1].equals("POSET")) {
			if (play(this.board.getTileByCode(action[2]),
					Integer.parseInt(action[3])))
				return 0;
			else
				return -1;

		} else if (action[1].equals("HUT") ) {
			if (action[2].equals("TRUE"))
				putItem(this.board.getTileByCode(board.getTile(board.getSize()-1).getCode()), 'H');
			return 1;

		} else if (action[1].equals("FIELD")) {
			if (putItem(this.board.getTileByCode(action[2]), 'C'))
				return 2;
			else
				return -1;

		} else if (action[1].equals("PICK")) {
			if (draw())
				return 3;
			else
				return -1;

		} else if (action[1].equals("OPICK")) {
			if (draw(this.pickOpen.getTileByCode(action[2])))
				return 4;
			else
				return -1;

		} else if (action[1].equals("SEND_TO_OPICK")) {
			if (discardTile())
				return 5;
			else
				return -1;

		} else if (action[1].equals("ENDTURN")) {
			return 6;
		}
		return -1;
	}

	@Override
	public void launchGame() throws IOException {
		boolean endTurn = false;
		int round = 0;
		int playerStartColonization;
		String codePutTile = "";
		boolean putTile = false;
		boolean putField = false;
		System.out.println(this);
		sendToAllPlayers(":NAMELIST");
		for (int i = 0; i < players.length; i++) {
			sendToAllPlayers(":" + players[i].getId() + ":"
					+ players[i].getName());
		}
		sendToAllPlayers(":ENDLIST");

		while (round < 3) {

			sendToAllPlayers(":DISCOVERY");
			while (nbPlayer == 2 && !endDiscovery()) {
				// Changement de joueur
				if (endTurn) {
					changePlayer();
					endTurn = false;
					putTile = false;
				}

				String msg = "";
				sendToPlayer(":START");
				try {
					msg = receiveFromPlayer();
				} catch (SocketTimeoutException e) {
					nbPlayer--;
					break;
				}

				String[] action = msg.trim().split(":");
				int code;

				/** Test pour les regles du jeu **/
				// On ne peut pas poser une hutte avant d'avoir piocher
				if (action[1].equals("HUT") && !putTile)
					code = -1;
				else
					code = processMessage(msg);

				// On ne peut pas piocher apres avoir poser une tuile
				if (action[1].equals("PICK") && putTile
						|| action[1].equals("OPICK") && putTile)
					code = -1;
				else
					code = processMessage(msg);

				
				switch (code) {
				case -2:
					sendToPlayer(":ERROR:ID");
					break;
				case -1:
					sendToPlayer(":ERROR:ENTRY");
					break;
				case 0:
					putTile = true;
					
					if(players[currentPlayer].getNbHutte()!=0)
						sendToPlayer(":HUT");
					
					
					break;
				case 1:
					codePutTile = players[currentPlayer].getTile().getCode();
					action = msg.trim().split(":");
					
					sendToAllPlayers(":POSET:" + codePutTile + ":"
							+ action[2]);
					break;
				case 3:
					sendToPlayer(":PICK:"
							+ players[currentPlayer].getTile().getCode());
					break;
				case 4:
					sendToPlayer(":OPICK:127");
					break;
				case 5:
					if(endDiscovery()) {
						playerStartColonization = currentPlayer;
					}
						
					sendToPlayer("SEND_TO_OPICK:127");
					break;
				case 6:
					if (putTile)
						endTurn = true;
					else
						sendToPlayer(":ERROR:ENTRY");
					break;
				}

			}

			
			
			
			sendToAllPlayers(":COLONIZATION");
			while (nbPlayer == 2 && !endColonization()) {
				// Changement de joueur
				if (endTurn) {
					changePlayer();
					endTurn = false;
					putField = false;
				}

				String msg = "";
				sendToPlayer(":START");
				try {
					msg = receiveFromPlayer();
				} catch (SocketTimeoutException e) {
					nbPlayer--;
					break;
				}

				int code = processMessage(msg);

				switch (code) {
				case -2:
					sendToPlayer(":ERROR:ID");
					break;
				case -1:
					sendToPlayer(":ERROR:ENTRY");
					break;
				case 2:
					putField = true;
					sendToPlayer(":FIELD:TRUE");
					break;
				case 6:
					if (putField || putTileByPlayerIsValid())
						endTurn = true;
					else
						sendToPlayer(":ERROR:ENTRY");
					break;
				}

			}

			this.board.setScore();
			round++;
		}
	}

	@Override
	public String toString() {
		return board.toString();
	}
}