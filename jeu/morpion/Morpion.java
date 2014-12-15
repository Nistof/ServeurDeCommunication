package jeu.morpion;

import java.io.IOException;

import jeu.IJeu;
import jeu.Joueur;
import server.Server;

public class Morpion implements IJeu {
	private static char[] symbols = {'X', 'O'};
    private int player;
	private int playersCount;
	private Joueur[] players;
	private Server server;
	private char[][] grid;
	
	public Morpion() {
		this.player = 0;
		this.players = new Joueur[2];
		this.players[0] = new Joueur("X");
		this.players[1] = new Joueur("O");
		this.grid = new char[3][3];
		
		for ( int j = 0; j < grid.length; j++)
			for ( int i = 0; i < grid[0].length; i++)
				grid[i][j] = ' ';	
		this.server = new Server(this);
	}
	
	public boolean canPlay() {
		boolean b = false;
		
		for ( int j = 0; j < grid.length; j++)
			for ( int i = 0; i < grid[0].length; i++)
				if ( grid[i][j] == ' ' )
					b = true;
		return b;
	}
	
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
	
	public void changePlayer () { player = (player+1)%2; }
	public char getPlayer () { return (player == 0)?'X':'O'; }
	
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
    public void add(String id) {
        players[playersCount++].setId(id);
        if(playersCount >= 2) {
            server.disalowConnections();
        }
    }

    @Override
    public void sendToAllPlayers(String action) {
        server.sendToAllClient(action);
    }

    @Override
    public void sendToPlayer(String action) {
       server.sendToClient(players[player].getId(), action);
    }

    @Override
    public void launchGame() {
        // TODO Auto-generated method stub
        do {
            String msg = "";
            try {
                msg = receiveFromPlayer();
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
        }while(!win());
    }

    @Override
    public String receiveFromPlayer() throws IOException {
        return server.receiveFromClient(players[player].getId());
    }

    @Override
    public boolean processMessage(String action) {
        if ( canPlay() ) {
            String[] pos = action.split(" ");
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
    
    public static void main(String[] args) {
        new Morpion();
    }
}
