package jeu.morpion;

import java.io.IOException;

import jeu.IJeu;
import jeu.Player;
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

public class Morpion implements IJeu {
	private static char[] symbols = {'X', 'O'};
    private int player;
	private int playersCount;
	private Player[] players;
	private Server server;
	private char[][] grid;
	
	public Morpion() {
		this.player = 0;
		this.players = new Player[2];
		this.players[0] = new Player("X");
		this.players[1] = new Player("O");
		this.grid = new char[3][3];
		
		for ( int j = 0; j < grid.length; j++)
			for ( int i = 0; i < grid[0].length; i++)
				grid[i][j] = ' ';	
		this.server = new Server(this);
	}
	
	/**
	 * Vérification de la grille de jeu pour savoir s'il est encore possible de jouer
	 * @return Vrai si il est encore possible de jouer
	 */
	public boolean canPlay() {
		boolean b = false;
		
		for ( int j = 0; j < grid.length; j++)
			for ( int i = 0; i < grid[0].length; i++)
				if ( grid[i][j] == ' ' )
					b = true;
		return b;
	}
	
	/**
	 * Condition de victoire d'une partie
	 * @return Vrai si il y a un gagnant
	 */
	public boolean win() {
		return 	( grid[1][1] == 'X' || grid[1][1] == 'O') &&
				(( grid[0][1] == grid[1][1] && grid[0][1] == grid[2][1] ) ||
				( grid[1][0] == grid[1][1] && grid[1][0] == grid[1][2] ) ||
				( grid[0][0] == grid[1][1] && grid[0][0] == grid[2][2] ) ||
				( grid[0][2] == grid[1][1] && grid[0][2] == grid[2][0] ))||
				( grid[0][0] == 'X' || grid[0][0] == 'O') &&
				(( grid[0][0] == grid[1][0] && grid[0][0] == grid[2][0] ) ||
				( grid[0][0] == grid[0][1] && grid[0][0] == grid[0][2] ))||
				( grid[2][2] == 'X' || grid[2][2] == 'O') &&
				(( grid[0][2] == grid[1][2] && grid[0][2] == grid[2][2] ) ||
				( grid[2][0] == grid[2][1] && grid[2][0] == grid[2][2] ));
	}
	
	/**
	 * Changement de joueur
	 */
	public void changePlayer () { player = (player+1)%2; }
	
	/**
	 * Donne le joueur courant
	 * @return Joueur courant
	 */
	public char getPlayer () { return (player == 0)?'X':'O'; }
	
	/**
	 * Affichage du jeu
	 */
	public String toString() {
		String str = "";
		String sep = "______\n";
		
		str += sep;
		for ( int j = 0; j < grid.length; j++) {
			str += "|";
			for ( int i = 0; i < grid[0].length; i++)
				str += grid[i][j] + "|";
			str += "\n" + sep;
		}
		
		return str;
	}

    @Override
    public void add(String name, String id) {
        players[playersCount].setName(name);
        players[playersCount++].setId(id);
        if(playersCount >= 2) {
            server.disalowConnections();
        }
    }

    @Override
    public void sendToAllPlayers(String msg) {
        server.sendToAllClient(msg);
    }

    @Override
    public void sendToPlayer(String msg) {
       server.sendToClient(players[player].getId(), msg);
    }

    @Override
    public void launchGame() {
        //Tant que la partie n'est pas finie, que l'on peut encore jouer
        //et que le nombre de joueur est égal à 2
        while(!win() && canPlay() && playersCount == 2) {
            String msg = "";
            try {
            	System.out.println(playersCount);
                msg = receiveFromPlayer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(processMessage(msg)) {
                sendToAllPlayers(msg);
                changePlayer();
            }
            else {
                sendToPlayer(players[player].getId()+"|ERROR");
            }
            System.out.println(this.toString());
        }
        
        //Envoi du gagnant
        if(playersCount == 2) {
        	changePlayer();
            sendToAllPlayers(players[player].getId()+":win");
        }
    }

    @Override
    public String receiveFromPlayer() throws IOException {
        return server.receiveFromClient(players[player].getId());
    }

    @Override
    public boolean processMessage(String msg) {
        String[] action = msg.split(":");
        if(action[1].equals("disconnected")) {
            playersCount--;
            return false;
        }
        if ( canPlay() ) {
            String[] pos = action[1].split(",");
            int x, y;
            if ( pos.length != 2)
                return false;
            
            x = Integer.parseInt(pos[0]);
            y = Integer.parseInt(pos[1]);
            
            if ( grid[x][y] == 'X' || grid[x][y] == 'O')
                return false;
            
            grid[x][y] = symbols[player];
            return true;
        }
        
        return false;
    }
}
