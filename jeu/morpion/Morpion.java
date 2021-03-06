package jeu.morpion;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

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
		this.playersCount = 0;
		this.players = new Player[2];
		this.players[0] = new Player("X");
		this.players[1] = new Player("O");
		this.grid = new char[3][3];
		
		for ( int j = 0; j < grid.length; j++)
			for ( int i = 0; i < grid[0].length; i++)
				grid[i][j] = ' ';	
		try {
            this.server = new Server(this);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        server.log("Ajout du joueur " + name + " avec l'id : " + id);
    }

    @Override
    public void sendToAllPlayers(String msg) throws IOException {
        server.sendToAllClient(msg);
        server.log("Envoi du message : " + msg + " à tous les joueurs");
    }

    @Override
    public void sendToPlayer(String msg) throws IOException {
       server.sendToClient(players[player].getId(), msg);
       server.log("Envoi du message : " + msg + " au joueur " + players[player].getId());
    }

    @Override
    public void launchGame() throws IOException {
        //Tant que la partie n'est pas finie, que l'on peut encore jouer
        //et que le nombre de joueur est égal à 2
    	sendToAllPlayers(":NAMELIST");
    	for(int i = 0 ; i < players.length; i++)  {
    		sendToAllPlayers(":" + players[i].getId() + ":" + players[i].getName() );
    	}
    	sendToAllPlayers(":ENDLIST");
        while(!win() && canPlay() && playersCount == 2) {
            String msg = "";
            try {
            	sendToPlayer(":START");
                msg = receiveFromPlayer();
            } catch (SocketTimeoutException e) {
                playersCount--;
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(processMessage(msg) == 1) {
                sendToAllPlayers(":" + msg);
                changePlayer();
            }
            else {
                sendToPlayer(":ERROR");
            }
            System.out.println(this.toString());
        }
        
        //Envoi du gagnant
        if(playersCount == 2) {
        	changePlayer();
            sendToAllPlayers(":WIN:" + players[player].getId());
        }
        else {
        	sendToAllPlayers(":CANCEL");
        }
    }

    @Override
    public String receiveFromPlayer() throws IOException, SocketTimeoutException {
        String s = server.receive();
        server.log("Message reçu d'un client : " + s);
        return s;
    }

    @Override
    public int processMessage(String msg) {
        String[] action = msg.split(":");
        if(!players[player].getId().equals(action[0])) {
            return 0;
        }
        if ( canPlay() ) {
            String[] pos = action[1].split(",");
            int x = 0, y = 0;
            if ( pos.length != 2)
                return 0;
            
            
            x = Integer.parseInt(pos[0].trim());
            y = Integer.parseInt(pos[1].trim());
            
            if ( x < 0 || x > 2 || y < 0 || y > 2 || grid[x][y] == 'X' || grid[x][y] == 'O')
                return 0;
            
            grid[x][y] = symbols[player];
            return 1;
        }
        
        return 0;
    }
}
