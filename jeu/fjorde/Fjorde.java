package jeu.fjorde;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

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
	 * 
	 * @return Si la pioche ferme est vide
	 */
	public boolean draw() {
		if (!this.pickClose.isEmpty()) {
			players[currentPlayer].draw(pickClose.draw());
			return true;
		}

		return false;
	}

	/**
	 * Permet de piocher dans la pioche ouverte
	 * 
	 * @param i
	 *            Indice de la tuile voulu
	 * @return Si la pioche ouverte est vide ou si l'indice est errone
	 */
	public boolean draw(int i) {
		if (i >= 0 && i < this.pickOpen.getSize()) {
			players[currentPlayer].draw(pickOpen.draw(i));
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
		
		return (id >= 0)?this.board
				.add(id, side, players[currentPlayer].removeFromHand())
				:false;
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
	 * @return Si c'est la fin de la phase colonisation (pose de champ impossible)
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
		if(!action[0].equals(players[currentPlayer].getId()))
			return -2;
		if (action[1].equals("POSET")) {
			if(play(this.board.getTileByCode(action[2]), Integer.parseInt(action[3])))	
				return 0;
			else
				return -1;
		
		} else if (action[1].equals("HUT")) {
			if(putItem(this.board.getTileByCode(action[2]), 'H'))
				return 1;
			else
				return -1;
		
		} else if(action[1].equals("FIELD")) {
			if(putItem(this.board.getTileByCode(action[2]), 'C'))
				return 2;
			else
				return -1;
		
		} else if(action[1].equals("PICK")) {
			if(draw()) 
				return 3;
			else
				return -1;
			
		} else if(action[1].equals("OPICK")) {
			if(draw(this.pickOpen.getTileByCode(action[2]))) 
				return 4;
			else
				return -1;
			
			
		} else if (action[1].equals("ENDTURN")) {
			return 2;
		}
		return -1;
	}
	
	@Override
	public void launchGame() throws IOException {
		boolean endTurn = false;
		int round = 0;
		System.out.println(this);
		sendToAllPlayers(":NAMELIST");
		for (int i = 0; i < players.length; i++) {
			sendToAllPlayers(":" + players[i].getId() + ":"
					+ players[i].getName());
		}
		sendToAllPlayers(":ENDLIST");

		while (round < 3 && nbPlayer == 2) {
			
			while(!endDiscovery() && !endColonization()) {
				// Changement de joueur
				if (endTurn) {
					changePlayer();
					endTurn = false;
				}
				
				String msg = "";
				sendToPlayer(":START");
				try {
					msg = receiveFromPlayer();
				} catch (SocketTimeoutException e) {
					nbPlayer--;
					break;
				}
				
				
				int	code = processMessage(msg);
				
				
				switch (code) {
					case -2:
						sendToPlayer(":ERROR:ID");
						break;
					case -1:
						sendToPlayer(":ERROR:ENTRY");
						break;
					case 0:
						
						break;
					case 1:
			
						break;
					case 2:
						
						break;
					case 3:
						sendToPlayer(":PICK");
						break;
				}
				
				
			}
			
			
			this.board.setScore();
			round++;
		}
	}
}
		
			

			/*switch (code) {
			case -2:
				sendToPlayer(":ERROR:ID");
				break;
			case -1:
				sendToPlayer(":ERROR:ENTRY");
				break;
			case 0:
			case 1:
				points = getState();
				sendToAllPlayers(":POINTLIST");
				for (String k : points.keySet()) {
					sendToAllPlayers(":" + k + ":" + points.get(k));
				}
				sendToAllPlayers(":ENDLIST");
				break;
			case 2:
				if (countTurn > 0)
					endTurn = true;
				else
					sendToPlayer(":ERROR:ENTRY");
				break;
			}
		}
		if (nbJoueur == 2) {
			if (win()) {
				sendToAllPlayers(":WIN:" + tabPlayer[currentPlayer].getId());
			} else {
				sendToAllPlayers(":END:" + tabPlayer[currentPlayer].getId());
			}
		} else {
			sendToAllPlayers(":CANCEL");
		}*/


