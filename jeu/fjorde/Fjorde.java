package jeu.fjorde;

//import java.io.FileNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;

import jeu.IJeu;
import server.Server;

public class Fjorde implements IJeu {
	private Board board;
	private FjordePlayer[] players;
	private int currentPlayer;
	private int nbPlayer;
	private Closed pickClose;
	private Open pickOpen;
	private Server server;
	private boolean putTile;

	private String tileInTheBoard;
	private int side;
	private int orientation;

	public Fjorde() {
		this.board = new Board();
		this.pickClose = new Pick(true);
		this.pickOpen = new Pick(false);
		this.players = new FjordePlayer[2];
		this.players[0] = new FjordePlayer("SARAH", "MARRON");
		this.players[1] = new FjordePlayer("FLORIAN", "BLANC");
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
		boolean b = false;

		if (!this.pickClose.isEmpty()) {
			if (this.players[currentPlayer].getTile() == null) {
				players[currentPlayer].draw(pickClose.draw());
				b = true;
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
			}/* else if (this.players[currentPlayer].getTile() != null) {
				if (putInOpenPick()) {
					players[currentPlayer].draw(pickOpen.draw(i));
					b = true;
				}
				
			}*/
		}
		return b;
	}



	/**
	 * 
	 * @param id L'identifiant de la tuile
	 * @param side Cote de la tuile sur leplateau
	 * @return Si la pose est correcte
	 */
	public boolean play(int id, int side) {

		if (id >= 0)
            return this.board.add(
            		players[currentPlayer].removeFromHand(), side, id);
        else
            return false;
	}

	/**
	 * Permet de poser un champ
	 * 
	 * @param i Indice de la tuile dans l'ArrayList
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

	/**
	 * 
	 * @return Si le joueur ne peut pas jouer, il pace la tuile dans 
	 */
	public boolean putInOpenPick() {
		if (this.players[currentPlayer].getTile() != null && board.getPossibilities(this.players[currentPlayer].getTile()).isEmpty()) {
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
		if (this.players[0].getScore() > this.players[1].getScore()) {
			return 0;
		} else if (this.players[1].getScore() > this.players[0].getScore()) {
			return 1;
		} else {
			if (this.players[0].getWinRound() > this.players[1].getWinRound()) {
				return 0;
			} else if (this.players[1].getWinRound() > this.players[0]
					.getWinRound()) {
				return 1;
			}
		}
		return -1;
	}
	
	/**
	 * 
	 */
	public void setRound() {
		if (players[0].getScore() > players[1].getScore())
			players[0].setWinRound();
		else if (players[1].getScore() > players[0].getScore())
			players[1].setWinRound();
		// Sinon egalite
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
		players[nbPlayer].setId(id);
		nbPlayer++;
	}

	@Override
	public void sendToAllPlayers(String action) throws IOException {
		System.out.println("Envoiee a tout le monde");
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
			this.players[currentPlayer].rotateTile(Integer.parseInt(action[4]));
			if (play(this.board.getTileByCode(action[2]),
					Integer.parseInt(action[3]))) {
				this.tileInTheBoard = action[2];
				this.side = Integer.parseInt(action[3]);
				this.orientation = Integer.parseInt(action[4]);
				this.putTile = true;
				return 0;
			} else
				return -1;

			// On ne peut pas poser une hutte avant d'avoir piocher
		} else if (action[1].equals("HUT") && this.putTile) {
			if (action[2].equals("YES"))
				putItem(this.board.getTileByCode(board.getTile(
						board.getSize() - 1).getCode()), 'H');
			return 1; // Pose d'une hutte

		} else if (action[1].equals("FIELD")) {
			if (putItem(this.board.getTileByCode(action[2]), 'C')) {
				this.tileInTheBoard = action[2];
				return 2; // Pose d'un champ
			} else
				return -1;

			// On ne peut pas piocher apres avoir poser une tuile
		} else if (action[1].equals("PICK") && !putTile) {
			if (draw())
				return 3; // Pioche dans la pioche ferme
			else
				return -1;

			// On ne peut pas piocher apres avoir poser une tuile
		} else if (action[1].equals("OPICK") && !putTile) {
			if (draw(this.pickOpen.getTileByCode(action[2])))
				return 4; // Piche dans la pioche ouverte
			else
				return -3;

		} else if (action[1].equals("SEND_TO_OPICK")) {
			if (putInOpenPick())
				return 5; // Defausse
			else
				return -4;
			
		} else if (action[1].equals("REQUEST_PLACEMENT")) { //Ajouter l'orientation
			if(players[currentPlayer].getTile()!=null) {
				players[currentPlayer].rotateTile(Integer.parseInt(action[2]));
				return 6;
			}
			else
				return -1;

		} else if (action[1].equals("ENDTURN")) {
			return 7; // Fin du tour
		}
		return -1;
	}
	
	private void sendPlacementList() throws IOException {
		sendToPlayer(":PLACEMENTLIST");
		for( Tile t : board.getPossibilities(players[currentPlayer].getTile()).keySet()) {
			sendToPlayer( ":" + t.getCode() + ":" + board.getPossibilities(players[currentPlayer].getTile()).get(t));
		}
		sendToPlayer(":ENDLIST");
	}

    @Override
	public void launchGame() throws IOException {
		boolean endTurn = false;
		boolean startTurn = true;
		int round = 0;
		int playerStartColonization = 0;
		boolean putField = false;
		System.out.println(this);
		sendToAllPlayers(":NAMELIST");
		for (int i = 0; i < players.length; i++) {
			sendToAllPlayers(":" + players[i].getId() + ":"
					+ players[i].getName());
		}
		sendToAllPlayers(":ENDLIST");

		for( int i=0; i<2; i++ ) {
			sendToPlayer(":PLAYER:"+currentPlayer);
			changePlayer();
		}
		
		sendToAllPlayers(":POSET:"+ this.board.getTile(0).getCode()+":0");
		for (int i = 1; i < board.getSize(); i++)
			sendToAllPlayers(":POSET:"+board.getTile(i-1)+":"+2*i+":"+board.getTile(i)+":"+board.getTile(i).getOrientation()+":NO");
		
		while (round < 3 && nbPlayer == 2) {
			startTurn = true;
			while (nbPlayer == 2 && !endDiscovery()) {			
				// Changement de joueur
				if (endTurn) {
					changePlayer();
					endTurn = false;
					putTile = false;
					startTurn = true;
				}
				
				//Debut de tour
				if (startTurn) {
					sendToPlayer(":START");
					startTurn = false;
				}

				String msg = "";
				try {
					msg = receiveFromPlayer();
				} catch (SocketTimeoutException e) {
					nbPlayer--;
					break;
				}

				int code = processMessage(msg);

				switch (code) {
				case -4:
					sendToPlayer(":SEND_TO_OPICK:NO");
					break;
				
				case -3:
					sendToPlayer(":OPICK:NO");
					break;
				
				case -2:
					sendToPlayer(":ERROR:ID");
					break;
				case -1:
					sendToPlayer(":ERROR:ENTRY");
					break;
				case 0: // Poser les tuiles
					putTile = true;

					if (players[currentPlayer].getNbHutte() != 0)
						sendToPlayer(":HUT");

					break;
				case 1: // Poser les huttes
					String[] action = msg.trim().split(":");

					sendToAllPlayers(":POSET:" + this.tileInTheBoard + ":"
							+ this.side + ":"
							+ this.board.getTile(board.getSize()-1) + ":"
							+ this.board.getTile(board.getSize()-1).getOrientation() + ":"
							+ action[2]);
					sendToPlayer(":ENDTURN");
					endTurn = true;
					break;
				case 3: // Piocher dans la pioche ferme
					sendToPlayer(":PICK:"
							+ players[currentPlayer].getTile().getCode());
					
					break;
				case 4: // Piocher dans la pioche ouverte
					sendToPlayer(":OPICK:YES");
					this.sendPlacementList();
					break;
				case 5: // Defausser
					if (endDiscovery()) {
						playerStartColonization = currentPlayer;
					}

					sendToPlayer(":SEND_TO_OPICK:YES");
					break;
					
				case 6: // Renvoie les possiblite de placement 
					this.sendPlacementList();
					break;
				case 7: // Finir le tour
					if (putTile)
						endTurn = true;
					else
						sendToPlayer(":ERROR:ENTRY");
					break;
				}

			}

			endTurn = false;
			currentPlayer = 1 - playerStartColonization;

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
				case -2: // Identifiant incorrecte
					sendToPlayer(":ERROR:ID");
					break;
				case -1: // Entrer incorrecte
					sendToPlayer(":ERROR:ENTRY");
					break;
				case 2: // Poser un champ
					putField = true;
					endTurn = true;
					
					sendToAllPlayers(":FIELD:" + this.tileInTheBoard);
					break;
				case 6:
					if (putField || putTileByPlayerIsValid())
						endTurn = true;
					else
						sendToPlayer(":ERROR:ENTRY");
					break;
				}

			}

			// Fin d'une manche
			this.board.setScore();
			this.setRound();
			sendToAllPlayers(":END:SCORE1:" + players[0].getScore()
					+ ":SCORE2:" + players[1].getScore());
			round++;
		}

		if (nbPlayer == 2)
			if (win() != -1)
				sendToAllPlayers(":WIN:" + players[win()].getId());
			else
				sendToAllPlayers(":EGALITE");
		else
			sendToAllPlayers(":CANCEL");

	}

	@Override
	public String toString() {
		return board.toString();
	}
}