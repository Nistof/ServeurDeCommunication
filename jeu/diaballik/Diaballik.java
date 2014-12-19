package jeu.diaballik;

import java.io.IOException;

import jeu.IJeu;
import server.Server;

public class Diaballik implements IJeu{
	private Support[][] plateau;
	private DiaballikPlayer[] tabPlayer;
	private int currentPlayer;
    private Server server;
    private int nbJoueur;
    private boolean endTurn;
	
	public Diaballik( boolean variante) {
	    this.tabPlayer = new DiaballikPlayer[2];
		this.tabPlayer[0] = new DiaballikPlayer( "Joueur 1", "Blanc" );
		this.tabPlayer[1] = new DiaballikPlayer( "Joueur 2", "Noir" );
		this.currentPlayer = 0;
		
		this.plateau = new Support[7][7];
		if(variante) {
			for( int i=0; i<plateau.length; i++ ) {
				if(i==1 || i == 5){
					this.plateau[0][i] = tabPlayer[1].getSupport(i);
					this.plateau[6][i] = tabPlayer[0].getSupport(i);
				}
				else{
					this.plateau[0][i] = tabPlayer[0].getSupport(i);
					this.plateau[6][i] = tabPlayer[1].getSupport(i);
				}
			}
		} else {
			for( int i=0; i<plateau.length; i++ ) {
				this.plateau[0][i] = tabPlayer[0].getSupport(i);
				this.plateau[6][i] = tabPlayer[1].getSupport(i);
			}
		}
		this.server = new Server(this);
	}

	public boolean moveB(String color, String dest){
		String[] s = dest.split(",");
		int cpt=0,x=0,y=0;
		int[] p = new int[2];
		for(int i=0; i<2; i++){ p[i] =Integer.parseInt(s[i]); } 
		
		if(plateau[p[0]][p[1]]!=null){
			for(int i=0; i<plateau.length; i++){
				for(int j=0; j<plateau.length; j++){
					if(plateau[i][j] != null && plateau[i][j].getHaveBall() && plateau[i][j].getColor().equals(color)){
						x=i;
						y=j;
					}
				}			
			}
			for(int i=1; i<plateau.length; i++){
				if ( x-i>=0 && x-i<7 && y-i>=0 && y-i<7 && plateau[x - i][y - i] == plateau[p[0]][p[1]] && plateau[x - i][y - i].getColor().equals(color)){
					cpt=0;
					for(int j=1; j<plateau.length; j++){
						if(x-i+j<x && y-i+j<y && ! plateau[x-i+j][y-i+j].getColor().equals(color)){
							cpt++;
						}
					}
					if(cpt==0){
						plateau[p[0]][p[1]].setHaveBall();
						plateau[x][y].setHaveBall();
						return true;
					}
				}
				if ( x-i>=0 && x-i<7 && plateau[x - i][y] == plateau[p[0]][p[1]] && plateau[x - i][y].getColor().equals(color)){
					cpt=0;
					for(int j=1; j<plateau.length; j++){
						if(x-i+j<x && ! plateau[x-i+j][y].getColor().equals(color)){
							cpt++;
						}
					}
					if(cpt==0){
						plateau[p[0]][p[1]].setHaveBall();
						plateau[x][y].setHaveBall();
						return true;
					}
				}
				if ( x-i>=0 && x-i<7 && y+i<7 && y+i>=0 && plateau[x - i][y + i] == plateau[p[0]][p[1]] && plateau[x - i][y + i].getColor().equals(color)){
					cpt=0;
					for(int j=1; j<plateau.length; j++){
						if( x-i+j<x && y+i-j>y && ! plateau[x-i+j][y+i-j].getColor().equals(color)){
							cpt++;
						}
					}
					if(cpt==0){
						plateau[p[0]][p[1]].setHaveBall();
						plateau[x][y].setHaveBall();
						return true;
					}
				}
				if ( y-i<7 && y-i>=0 && plateau[x][y - i] == plateau[p[0]][p[1]] && plateau[x][y - i].getColor().equals(color)){
					cpt=0;
					for(int j=1; j<plateau.length; j++){
						if( y-i+j<y && ! plateau[x][y-i+j].getColor().equals(color)){
							cpt++;
						}
					}
					if(cpt==0){
						plateau[p[0]][p[1]].setHaveBall();
						plateau[x][y].setHaveBall();
						return true;
					}
				}
				if ( y+i<7 && y+i>=0 && plateau[x][y + i] == plateau[p[0]][p[1]] && plateau[x][y + i].getColor().equals(color)){
					cpt=0;
					for(int j=1; j<plateau.length; j++){
						if( y+i-j>y && ! plateau[x][y+i-j].getColor().equals(color)){
							cpt++;
						}
					}
					if(cpt==0){
						plateau[p[0]][p[1]].setHaveBall();
						plateau[x][y].setHaveBall();
						return true;
					}
				}		
				if (  x+i>=0 && x+i<7 && y-i<7 && y-i>=0 && plateau[x + i][y - i] == plateau[p[0]][p[1]] && plateau[x + i][y - i].getColor().equals(color)){
					cpt=0;
					for(int j=1; j<plateau.length; j++){
						if(x+i-j>x && y-i+j<y && ! plateau[x+i-j][y-i+j].getColor().equals(color)){
							cpt++;
						}
					}
					if(cpt==0){
						plateau[p[0]][p[1]].setHaveBall();
						plateau[x][y].setHaveBall();
						return true;
					}
				}
				if (  x+i>=0 && x+i<7 && plateau[x + i][y] == plateau[p[0]][p[1]] && plateau[x + i][y].getColor().equals(color)){
					cpt=0;
					for(int j=1; j<plateau.length; j++){
						if(x+i-j>x && ! plateau[x+i-j][y].getColor().equals(color)){
							cpt++;
						}
					}
					if(cpt==0){
						plateau[p[0]][p[1]].setHaveBall();
						plateau[x][y].setHaveBall();
						return true;
					}
				}
				if ( x+i>=0 && x+i<7 && y+i<7 && y+i>=0 && plateau[x + i][y + i] == plateau[p[0]][p[1]] && plateau[x + i][y + i].getColor().equals(color)){
					cpt=0;
					for(int j=1; j<plateau.length; j++){
						if(x+i-j>x && y+i-j>y && ! plateau[x+i-j][y+i-j].getColor().equals(color)){
							cpt++;
						}
					}
					if(cpt==0){
						plateau[p[0]][p[1]].setHaveBall();
						plateau[x][y].setHaveBall();
						return true;
					}
				
				}			
			}
		}
		return false;
	}
	
	public boolean moveS(String color, String src, String dir){		
		String[] j = src.split(",");
		int[] p = new int[2];
		for(int i=0; i<2; i++){ p[i] =Integer.parseInt(j[i]); } 
		if(plateau[p[0]][p[1]].getHaveBall()){ return false; }
		if(p[0] != 0 && dir.equals("N") && plateau[p[0]][p[1]].getColor().equals(color) && plateau[p[0]-1][p[1]]==null){
			plateau[p[0]-1][p[1]] = plateau[p[0]][p[1]];
			plateau[p[0]][p[1]] = null;
			return true;
		}
		if(p[0] != 6 && dir.equals("S") && plateau[p[0]][p[1]].getColor().equals(color) && plateau[p[0]+1][p[1]]==null){
			plateau[p[0]+1][p[1]] = plateau[p[0]][p[1]];
			plateau[p[0]][p[1]] = null;
			return true;
		}
		if(p[1] != 6 && dir.equals("E") && plateau[p[0]][p[1]].getColor().equals(color) && plateau[p[0]][p[1]+1]==null){
			plateau[p[0]][p[1]+1] = plateau[p[0]][p[1]];
			plateau[p[0]][p[1]] = null;
			return true;
		}
		if(p[1] != 0 &&dir.equals("O") && plateau[p[0]][p[1]].getColor().equals(color) && plateau[p[0]][p[1]-1]==null){	
			plateau[p[0]][p[1]-1] = plateau[p[0]][p[1]];
			plateau[p[0]][p[1]] = null;
			return true;
		}
		return false;
	}
	
    public boolean isBlocked(DiaballikPlayer player){
		int c1,c2,x;
		for(int i=0; i<plateau.length; i++){
			if(plateau[i][0] != null){
				c1=1;
				c2=0;
				x=i; 
				if(x-1>=0 && plateau[x-1][0] != null && !plateau[x-1][0].getColor().equals(player.getColor())){ c2++; }
				if(x+1<7 && plateau[x+1][0] != null && !plateau[x+1][0].getColor().equals(player.getColor())){ c2++; }
					
				for(int j=1; j<plateau.length; j++){
					if(plateau[x][j] != null && plateau[x][j].getColor().equals(player.getColor())){
						c1++;
						if(x-1>=0 && plateau[x-1][j] != null && !plateau[x-1][j].getColor().equals(player.getColor())){ c2++; }
						if(x+1<7 && plateau[x+1][j] != null && !plateau[x+1][j].getColor().equals(player.getColor())){ c2++; }	
					}
					if(x-1>=0 && plateau[x-1][j] != null && plateau[x-1][j].getColor().equals(player.getColor())){
						x=x-1; 
						c1++;
						if(x-1>=0 && plateau[x-1][j] != null && !plateau[x-1][j].getColor().equals(player.getColor())){ c2++; }
						if(x+1<7 && plateau[x+1][j] != null && !plateau[x+1][j].getColor().equals(player.getColor())){ c2++; }
					}
					if(x+1<7 && plateau[x+1][j] != null && plateau[x+1][j].getColor().equals(player.getColor())){
						x=x+1; 
						c1++;
						if(x-1>=0 && plateau[x-1][j] != null && !plateau[x-1][j].getColor().equals(player.getColor())){ c2++; }
						if(x+1<7 && plateau[x+1][j] != null && !plateau[x+1][j].getColor().equals(player.getColor())){ c2++; }
					}
				}
				if(c1 == plateau.length && c2>=3){ 
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean win(){
		for(int i=0; i<plateau.length; i++){
			if( plateau[0][i]!=null && plateau[0][i].getHaveBall() && plateau[0][i].getColor().equals("Noir") ||
					plateau[6][i]!=null && plateau[6][i].getHaveBall() && plateau[6][i].getColor().equals("Blanc"))
			{
				return true;
			}
		}
		return false;
	}
	
	public String toString() {		
		String s, sep="+";
		
		for( int i=0; i<plateau.length; i++ )
			sep += "---+"; 
		
		s = sep + "\n";
		for( int i=0; i<plateau.length; i++ ) {
			s += "|";
			for( int j=0; j<plateau[0].length; j++ ) {
				if( plateau[i][j] == null )
					s += "   |";
				else {
					if( plateau[i][j].getHaveBall() ){
						if( plateau[i][j].getColor().charAt(0) == 'N' ){
								s += " n |";
						}
						if( plateau[i][j].getColor().charAt(0) == 'B' ){
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
	    tabPlayer[nbJoueur++].setName(name);
        tabPlayer[nbJoueur++].setId(id);
        if(nbJoueur >= 2) {
            server.disalowConnections();
        }
    }

    @Override
    public void sendToAllPlayers(String action) {
        server.sendToAllClient(action);
    }
    
    public void sendToPlayer(String action) {
        server.sendToClient(tabPlayer[currentPlayer].getNom(), action);
    }

    public String receiveFromPlayer() throws IOException {
        return server.receiveFromClient(tabPlayer[currentPlayer].getId());
    }

    @Override
    public void launchGame() {
        // TODO Auto-generated method stub
        System.out.println(toString());
        int countTurn = 0;
        do {
            if(endTurn) {
                changePlayer();
                endTurn = !endTurn;
            }
            String msg = "";
            try {
                msg = receiveFromPlayer();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(processMessage(msg)) {
                sendToAllPlayers(msg);
                countTurn++;
                if(countTurn == 3)
                    finishTurn();
                System.out.println(toString());
            }
            else {
                sendToPlayer(tabPlayer[currentPlayer].getId()+"|ERROR");
            }
        }while(!win());
    }
    
    private void changePlayer() {
        currentPlayer = 1 - currentPlayer;
    }

    private void finishTurn() {
        endTurn = true;
    }

    @Override
     public boolean processMessage(String msg) {
        String[] action = msg.split(":");
        boolean b = false;
        if(action[1].equals("deplacerB")) {
            b = moveB(tabPlayer[currentPlayer].getColor(), action[2]);
        }
        else if(action[1].equals("deplacerS")) {
            b = moveS(tabPlayer[currentPlayer].getColor(),action[2], action[3]);
        }
        else if(action[1].equals("finTour")) {
            b = true;
        }
        return b;
    }
    
}
