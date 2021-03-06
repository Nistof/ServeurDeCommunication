package jeu.diaballik;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Scanner;

import jeu.IJeu;
import server.Server;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 12-03-2014
 */

public class Diaballik implements IJeu {
	private Support[][] plateau;
	private DiaballikPlayer[] tabPlayer;
	private boolean[][] updates;
	private int currentPlayer;
	private Server server;
	private int nbJoueur;
	private boolean endTurn;
	
	public Diaballik() {
		this.tabPlayer = new DiaballikPlayer[2];
		this.tabPlayer[0] = new DiaballikPlayer("Joueur 1", "Blanc");
		this.tabPlayer[1] = new DiaballikPlayer("Joueur 2", "Noir");
		this.currentPlayer = 0;
		this.nbJoueur = 0;
		this.updates = new boolean[7][7];
		this.plateau = new Support[7][7];
		
		try {
			this.server = new Server(this);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Diaballik(boolean variante) {
		this();
		
		if (variante) {
			for (int i = 0; i < plateau.length; i++) {
				if (i == 1 || i == 5) {
					this.plateau[0][i] = new Support(tabPlayer[1].getColor(), false);
					this.updates[0][i] = true;
					this.plateau[6][i] = new Support(tabPlayer[0].getColor(), false);
					this.updates[6][i] = true;
				} else {
					this.plateau[0][i] = new Support(tabPlayer[0].getColor(), false);
					this.updates[0][i] = true;
					this.plateau[6][i] = new Support(tabPlayer[1].getColor(), false);
					this.updates[6][i] = true;
				}
			}
		} else {
			for (int i = 0; i < plateau.length; i++) {
				this.plateau[0][i] = new Support(tabPlayer[0].getColor(), false);
				this.updates[0][i] = true;
				this.plateau[6][i] = new Support(tabPlayer[1].getColor(), false);
				this.updates[6][i] = true;
			}
		}
		this.plateau[0][3].toggleHaveBall();
		this.plateau[6][3].toggleHaveBall();
	}
	
	public Diaballik(String file) {
		this();
		
		try {
			Scanner sc = new Scanner(new FileReader(file));
			while(sc.hasNext()) {
				String line = sc.nextLine();
				String[] coord = line.split(",");
		
				switch(coord[2].charAt(0)) {
					case 'B':
						this.plateau[Integer.parseInt(coord[0])][Integer.parseInt(coord[1])] = new Support(tabPlayer[0].getColor(), false);
						this.updates[Integer.parseInt(coord[0])][Integer.parseInt(coord[1])] = true;
						break;
					case 'N':
						this.plateau[Integer.parseInt(coord[0])][Integer.parseInt(coord[1])] = new Support(tabPlayer[1].getColor(), false);
						this.updates[Integer.parseInt(coord[0])][Integer.parseInt(coord[1])] = true;
						break;	
					case 'b':
						this.plateau[Integer.parseInt(coord[0])][Integer.parseInt(coord[1])] = new Support(tabPlayer[0].getColor(), true);
						this.updates[Integer.parseInt(coord[0])][Integer.parseInt(coord[1])] = true;
						break;
					case 'n':
						this.plateau[Integer.parseInt(coord[0])][Integer.parseInt(coord[1])] = new Support(tabPlayer[1].getColor(), true);
						this.updates[Integer.parseInt(coord[0])][Integer.parseInt(coord[1])] = true;
						break;
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean moveB(String color, String src, String dest) {
		String[] s = dest.split(",");
		String[] s2 = src.split(",");
		int cpt = 0, x = 0, y = 0;
		int[] p = new int[2];
		int[] p2 = new int[2];
		for (int i = 0; i < 2; i++) {
			p[i] = Integer.parseInt(s[i]);
			p2[i] = Integer.parseInt(s2[i]);

			
		}
        this.updates[p[0]][p[1]] = true;
        this.updates[p2[0]][p2[1]] = true;
		if (plateau[p2[0]][p2[1]] != null && plateau[p2[0]][p2[1]].getHaveBall()
				&& plateau[p2[0]][p2[1]].getColor().equals(color)) {
			x = p2[0];
			y = p2[1];
		} else 
			return false;
		
		if (plateau[p[0]][p[1]] != null) {
			for (int i = 1; i < plateau.length; i++) {
				if (x - i >= 0 && x - i < 7 && y - i >= 0 && y - i < 7
						&& plateau[x - i][y - i] == plateau[p[0]][p[1]]
						&& plateau[x - i][y - i].getColor().equals(color)) {
					cpt = 0;
					for (int j = 1; j < plateau.length; j++) {
						if (x - i + j < x
								&& y - i + j < y && plateau[x - i + j][y - i + j]!=null 
								&& !plateau[x - i + j][y - i + j].getColor()
										.equals(color)) {
							cpt++;
						}
					}
					if (cpt == 0) {
						plateau[p[0]][p[1]].toggleHaveBall();
						plateau[x][y].toggleHaveBall();
						return true;
					}
				}
				if (x - i >= 0 && x - i < 7
						&& plateau[x - i][y] == plateau[p[0]][p[1]]
						&& plateau[x - i][y].getColor().equals(color)) {
					cpt = 0;
					for (int j = 1; j < plateau.length; j++) {
						if (x - i + j < x && plateau[x - i + j][y]!=null 
								&& !plateau[x - i + j][y].getColor().equals(
										color)) {
							cpt++;
						}
					}
					if (cpt == 0) {
						plateau[p[0]][p[1]].toggleHaveBall();
						plateau[x][y].toggleHaveBall();
						return true;
					}
				}
				if (x - i >= 0 && x - i < 7 && y + i < 7 && y + i >= 0
						&& plateau[x - i][y + i] == plateau[p[0]][p[1]]
						&& plateau[x - i][y + i].getColor().equals(color)) {
					cpt = 0;
					for (int j = 1; j < plateau.length; j++) {
						if (x - i + j < x
								&& y + i - j > y && plateau[x - i + j][y + i - j]!=null 
								&& !plateau[x - i + j][y + i - j].getColor()
										.equals(color)) {
							cpt++;
						}
					}
					if (cpt == 0) {
						plateau[p[0]][p[1]].toggleHaveBall();
						plateau[x][y].toggleHaveBall();
						return true;
					}
				}
				if (y - i < 7 && y - i >= 0
						&& plateau[x][y - i] == plateau[p[0]][p[1]]
						&& plateau[x][y - i].getColor().equals(color)) {
					cpt = 0;
					for (int j = 1; j < plateau.length; j++) {
						if (y - i + j < y
								&& plateau[x][y - i + j]!=null && !plateau[x][y - i + j].getColor().equals(
										color)) {
							cpt++;
						}
					}
					if (cpt == 0) {
						plateau[p[0]][p[1]].toggleHaveBall();
						plateau[x][y].toggleHaveBall();
						return true;
					}
				}
				if (y + i < 7 && y + i >= 0
						&& plateau[x][y + i] == plateau[p[0]][p[1]]
						&& plateau[x][y + i].getColor().equals(color)) {
					cpt = 0;
					for (int j = 1; j < plateau.length; j++) {
						if (y + i - j > y && plateau[x][y + i - j]!=null 
								&& !plateau[x][y + i - j].getColor().equals(
										color)) {
							cpt++;
						}
					}
					if (cpt == 0) {
						plateau[p[0]][p[1]].toggleHaveBall();
						plateau[x][y].toggleHaveBall();
						return true;
					}
				}
				if (x + i >= 0 && x + i < 7 && y - i < 7 && y - i >= 0
						&& plateau[x + i][y - i] == plateau[p[0]][p[1]]
						&& plateau[x + i][y - i].getColor().equals(color)) {
					cpt = 0;
					for (int j = 1; j < plateau.length; j++) {
						if (x + i - j > x
								&& y - i + j < y && plateau[x + i - j][y - i + j]!=null 
								&& !plateau[x + i - j][y - i + j].getColor()
										.equals(color)) {
							cpt++;
						}
					}
					if (cpt == 0) {
						plateau[p[0]][p[1]].toggleHaveBall();
						plateau[x][y].toggleHaveBall();
						return true;
					}
				}
				if (x + i >= 0 && x + i < 7
						&& plateau[x + i][y] == plateau[p[0]][p[1]]
						&& plateau[x + i][y].getColor().equals(color)) {
					cpt = 0;
					for (int j = 1; j < plateau.length; j++) {
						if (x + i - j > x && plateau[x + i - j][y]!=null 
								&& !plateau[x + i - j][y].getColor().equals(
										color)) {
							cpt++;
						}
					}
					if (cpt == 0) {
						plateau[p[0]][p[1]].toggleHaveBall();
						plateau[x][y].toggleHaveBall();
						return true;
					}
				}
				if (x + i >= 0 && x + i < 7 && y + i < 7 && y + i >= 0
						&& plateau[x + i][y + i] == plateau[p[0]][p[1]]
						&& plateau[x + i][y + i].getColor().equals(color)) {
					cpt = 0;
					for (int j = 1; j < plateau.length; j++) {
						if (x + i - j > x
								&& y + i - j > y && plateau[x + i - j][y + i - j]!=null 
								&& !plateau[x + i - j][y + i - j].getColor()
										.equals(color)) {
							cpt++;
						}
					}
					if (cpt == 0) {
						plateau[p[0]][p[1]].toggleHaveBall();
						plateau[x][y].toggleHaveBall();
						return true;
					}

				}
			}
		}
		return false;
	}

	public boolean moveS(String color, String src, String dir) {
		String[] j = src.split(",");
		int[] p = new int[2];
		for (int i = 0; i < 2; i++) {
			p[i] = Integer.parseInt(j[i]);
		}
		this.updates[p[0]][p[1]] = true;
		if (plateau[p[0]][p[1]].getHaveBall()) {
			return false;
		}
		if (p[0] != 0 && dir.equals("N")
				&& plateau[p[0]][p[1]].getColor().equals(color)
				&& plateau[p[0] - 1][p[1]] == null) {
			plateau[p[0] - 1][p[1]] = plateau[p[0]][p[1]];
			this.updates[p[0] - 1][p[1]] = true;
			plateau[p[0]][p[1]] = null;
			return true;
		}
		if (p[0] != 6 && dir.equals("S")
				&& plateau[p[0]][p[1]].getColor().equals(color)
				&& plateau[p[0] + 1][p[1]] == null) {
			plateau[p[0] + 1][p[1]] = plateau[p[0]][p[1]];
			this.updates[p[0] + 1][p[1]] = true;
			plateau[p[0]][p[1]] = null;
			return true;
		}
		if (p[1] != 6 && dir.equals("E")
				&& plateau[p[0]][p[1]].getColor().equals(color)
				&& plateau[p[0]][p[1] + 1] == null) {
			plateau[p[0]][p[1] + 1] = plateau[p[0]][p[1]];
			this.updates[p[0]][p[1]+1] = true;
			plateau[p[0]][p[1]] = null;
			return true;
		}
		if (p[1] != 0 && dir.equals("O")
				&& plateau[p[0]][p[1]].getColor().equals(color)
				&& plateau[p[0]][p[1] - 1] == null) {
			plateau[p[0]][p[1] - 1] = plateau[p[0]][p[1]];
			this.updates[p[0]][p[1]-1] = true;
			plateau[p[0]][p[1]] = null;
			return true;
		}
		return false;
	}

	public boolean isBlocked(DiaballikPlayer player) {
		int c1, c2, x;
		for (int i = 0; i < plateau.length; i++) {
			if (plateau[i][0] != null) {
				c1 = 1;
				c2 = 0;
				x = i;
				if (x - 1 >= 0
						&& plateau[x - 1][0] != null
						&& !plateau[x - 1][0].getColor().equals(
								player.getColor())) {
					c2++;
				}
				if (x + 1 < 7
						&& plateau[x + 1][0] != null
						&& !plateau[x + 1][0].getColor().equals(
								player.getColor())) {
					c2++;
				}

				for (int j = 1; j < plateau.length; j++) {
					if (plateau[x][j] != null
							&& plateau[x][j].getColor().equals(
									player.getColor())) {
						c1++;
						if (x - 1 >= 0
								&& plateau[x - 1][j] != null
								&& !plateau[x - 1][j].getColor().equals(
										player.getColor())) {
							c2++;
						}
						if (x + 1 < 7
								&& plateau[x + 1][j] != null
								&& !plateau[x + 1][j].getColor().equals(
										player.getColor())) {
							c2++;
						}
					}
					if (x - 1 >= 0
							&& plateau[x - 1][j] != null
							&& plateau[x - 1][j].getColor().equals(
									player.getColor())) {
						x = x - 1;
						c1++;
						if (x - 1 >= 0
								&& plateau[x - 1][j] != null
								&& !plateau[x - 1][j].getColor().equals(
										player.getColor())) {
							c2++;
						}
						if (x + 1 < 7
								&& plateau[x + 1][j] != null
								&& !plateau[x + 1][j].getColor().equals(
										player.getColor())) {
							c2++;
						}
					}
					if (x + 1 < 7
							&& plateau[x + 1][j] != null
							&& plateau[x + 1][j].getColor().equals(
									player.getColor())) {
						x = x + 1;
						c1++;
						if (x - 1 >= 0
								&& plateau[x - 1][j] != null
								&& !plateau[x - 1][j].getColor().equals(
										player.getColor())) {
							c2++;
						}
						if (x + 1 < 7
								&& plateau[x + 1][j] != null
								&& !plateau[x + 1][j].getColor().equals(
										player.getColor())) {
							c2++;
						}
					}
				}
				if (c1 == plateau.length && c2 >= 3) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean win() {
		for (int i = 0; i < plateau.length; i++) {
			if (plateau[0][i] != null && plateau[0][i].getHaveBall()
					&& plateau[0][i].getColor().equals("Noir")
					|| plateau[6][i] != null && plateau[6][i].getHaveBall()
					&& plateau[6][i].getColor().equals("Blanc")) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		String s, sep = "+";

		for (int i = 0; i < plateau.length; i++)
			sep += "---+";

		s = sep + "\n";
		for (int i = 0; i < plateau.length; i++) {
			s += "|";
			for (int j = 0; j < plateau[0].length; j++) {
				if (plateau[i][j] == null)
					s += "   |";
				else {
					if (plateau[i][j].getHaveBall()) {
						if (plateau[i][j].getColor().charAt(0) == 'N') {
							s += " n |";
						}
						if (plateau[i][j].getColor().charAt(0) == 'B') {
							s += " b |";
						}
					}

					else
						s += " " + plateau[i][j].getColor().charAt(0) + " |";
				}
			}
			s += "\n" + sep + "\n";
		}

		return s;
	}

	@Override
	public void add(String name, String id) {
	    server.log("Ajout du joueur " + name + " avec l'id " + id);
		tabPlayer[nbJoueur].setName(name);
		tabPlayer[nbJoueur++].setId(id);
	}

	@Override
	public void sendToAllPlayers(String action) throws IOException {
		server.sendToAllClient(action);
	}

	public void sendToPlayer(String action) throws IOException {
	    server.log("Message envoyé au joueur " + tabPlayer[currentPlayer].getName() +  " : " + action);
		server.sendToClient(tabPlayer[currentPlayer].getId(), action);
	}

	public String receiveFromPlayer() throws IOException,SocketTimeoutException {
	    String s = server.receive();
	    server.log("Message reçu d'un joueur : " + s);
		return s;
	}

	public HashMap<String, Character> getState() {
		HashMap<String, Character> points = new HashMap<String, Character>();
		for (int i = 0; i < plateau.length; i++) {
			for (int j = 0; j < plateau[0].length; j++) {
				if (updates[i][j]) {
				    System.out.println("Updated");
					char sym;
				    if(plateau[i][j]==null)
					    sym = ' ';
				    else {
					    sym = (plateau[i][j].getColor().charAt(0));
					    sym = (plateau[i][j].getHaveBall())?Character.toLowerCase(sym):sym;
				    }
					updates[i][j] = false;
					points.put(i + "," + j, sym);
				}
			}
		}
		return points;
	}

	@Override
	public void launchGame() throws IOException {
		boolean ballPlayed = false;
		int countTurn = 0;
		System.out.println(this);
		sendToAllPlayers(":NAMELIST");
		for (int i = 0; i < tabPlayer.length; i++) {
			sendToAllPlayers(":" + tabPlayer[i].getId() + ":"
					+ tabPlayer[i].getName());
		}
		sendToAllPlayers(":ENDLIST");
		HashMap<String, Character> points = getState();
		sendToAllPlayers(":POINTLIST");
		for(String k : points.keySet()) {
			sendToAllPlayers(":" + k + ":" + points.get(k));
		}
		sendToAllPlayers(":ENDLIST");
		while (!win() && nbJoueur == 2) {
			if(endTurn) {
				changePlayer();
				countTurn = 0;
				endTurn = false;
				ballPlayed = false;
			}
			String msg = "";
			if(isBlocked(tabPlayer[currentPlayer])) {
				break;
			}
			sendToPlayer(":START");
			try {
				msg = receiveFromPlayer();
			} catch(SocketTimeoutException e) {
				nbJoueur--;
				break;
			}
			
			String[] action = msg.trim().split(":");
			int code;
			
			if( action[1].equals("MOVEB") && ballPlayed )
				code = -1;
			else
				code = processMessage(msg);
			
			switch(code) {
				case -2:
					sendToPlayer(":ERROR:ID");
					break;
				case -1:
					sendToPlayer(":ERROR:ENTRY");
					break;
				case 0:
					ballPlayed = true;
				case 1:
					countTurn++;
					if(countTurn == 3)
						endTurn = true;
					points = getState();
					sendToAllPlayers(":POINTLIST");
					for(String k : points.keySet()) {
						sendToAllPlayers(":" + k + ":" + points.get(k));
					}
					sendToAllPlayers(":ENDLIST");
					break;
				case 2:
					if(countTurn > 0)
						endTurn = true;
					else
						sendToPlayer(":ERROR:ENTRY");
					break;
			}
		}
		if(nbJoueur == 2) {
			if(win()) {
				sendToAllPlayers(":WIN:" + tabPlayer[currentPlayer].getId());
			}
			else {
				sendToAllPlayers(":END:" + tabPlayer[currentPlayer].getId());
			}
		}
		else  {
			sendToAllPlayers(":CANCEL");
		}
	}

	private void changePlayer() {
		currentPlayer = 1 - currentPlayer;
	}

	@Override
	public int processMessage(String msg) {
		System.out.println(msg);
		String[] action = msg.trim().split(":");
		if(!action[0].equals(tabPlayer[currentPlayer].getId()))
			return -2;
		if (action[1].equals("MOVEB")) {
			if(moveB(tabPlayer[currentPlayer].getColor(), action[2], action[3]))
				return 0;
			else
				return -1;
		} else if (action[1].equals("MOVES")) {
			if(moveS(tabPlayer[currentPlayer].getColor(), action[2], action[3]))
				return 1;
			else
				return -1;
		} else if (action[1].equals("ENDTURN")) {
			return 2;
		}
		return -1;
	}

}
