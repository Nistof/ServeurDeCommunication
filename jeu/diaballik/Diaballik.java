package jeu.diaballik;

import java.io.IOException;

import jeu.IJeu;
import server.Server;

public class Diaballik implements IJeu{
    private static String[] couleurs = {"Blanc", "Noir"};
	private Support[][] plateau;
	private JoueurDiaballik[] tabJoueurs;
	private int joueurCourant;
    private Server server;
    private int nbJoueur;
	
	public Diaballik( boolean variante) {
	    this.tabJoueurs = new JoueurDiaballik[2];
		this.tabJoueurs[0] = new JoueurDiaballik( "Joueur 1", "Blanc" );
		this.tabJoueurs[1] = new JoueurDiaballik( "Joueur 2", "Noir" );
		this.joueurCourant = 0;
		
		this.plateau = new Support[7][7];
		if(variante) {
			for( int i=0; i<plateau.length; i++ ) {
				if(i==1 || i == 5){
					this.plateau[0][i] = tabJoueurs[1].getSupport(i);
					this.plateau[6][i] = tabJoueurs[0].getSupport(i);
				}
				else{
					this.plateau[0][i] = tabJoueurs[0].getSupport(i);
					this.plateau[6][i] = tabJoueurs[1].getSupport(i);
				}
			}
		} else {
			for( int i=0; i<plateau.length; i++ ) {
				this.plateau[0][i] = tabJoueurs[0].getSupport(i);
				this.plateau[6][i] = tabJoueurs[1].getSupport(i);
			}
		}
		//this.server = new Server(this);
	}

	public boolean deplacerB(String coul, String dest){
		String[] s = dest.split(",");
		int x=0,y=0;
		int[] p = new int[2];
		for(int i=0; i<2; i++){ p[i] =Integer.parseInt(s[i]); } 
		
		for(int i=0; i<plateau.length; i++){
			for(int j=0; j<plateau.length; j++){
				if(plateau[i][j] != null && plateau[i][j].getABalle() && plateau[i][j].getCouleur().equals(coul)){
					x=i;
					y=j;
				}
			}			
		}
		for(int i=1; i<plateau.length; i++){
			if ( x-i>=0 && x-i<7 && y-i>=0 && y-i<7 && plateau[x - i][y - i] == plateau[p[0]][p[1]] && plateau[x - i][y - i].getCouleur().equals(coul)){
				plateau[p[0]][p[1]].setABalle();
				plateau[x][y].setABalle();
				return true;
			}
			if ( x-i>=0 && x-i<7 && plateau[x - i][y] == plateau[p[0]][p[1]] && plateau[x - i][y].getCouleur().equals(coul)){
				plateau[p[0]][p[1]].setABalle();
				plateau[x][y].setABalle();
				return true;
			}
			if ( x-i>=0 && x-i<7 && y+i<7 && y+i>=0 && plateau[x - i][y + i] == plateau[p[0]][p[1]] && plateau[x - i][y + i].getCouleur().equals(coul)){
				plateau[p[0]][p[1]].setABalle();
				plateau[x][y].setABalle();
				return true;
			}
			if ( y-i<7 && y-i>=0 && plateau[x][y - i] == plateau[p[0]][p[1]] && plateau[x][y - i].getCouleur().equals(coul)){
				plateau[p[0]][p[1]].setABalle();
				plateau[x][y].setABalle();
				return true;
			}
			if ( y+i<7 && y+i>=0 && plateau[x][y + i] == plateau[p[0]][p[1]] && plateau[x][y + i].getCouleur().equals(coul)){
				plateau[p[0]][p[1]].setABalle();
				plateau[x][y].setABalle();
				return true;
			}		
			if (  x+i>=0 && x+i<7 && y-i<7 && y-i>=0 && plateau[x + i][y - i] == plateau[p[0]][p[1]] && plateau[x + i][y - i].getCouleur().equals(coul)){
				plateau[p[0]][p[1]].setABalle();
				plateau[x][y].setABalle();
				return true;
			}
			if (  x+i>=0 && x+i<7 && plateau[x + i][y] == plateau[p[0]][p[1]] && plateau[x + i][y].getCouleur().equals(coul)){
				plateau[p[0]][p[1]].setABalle();
				plateau[x][y].setABalle();
				return true;
			}
			if ( x+i>=0 && x+i<7 && y+i<7 && y+i>=0 && plateau[x + i][y + i] == plateau[p[0]][p[1]] && plateau[x + i][y + i].getCouleur().equals(coul)){
				plateau[p[0]][p[1]].setABalle();
				plateau[x][y].setABalle();
				return true;
			
			}			
		}
		return false;
	}
	
	public boolean deplacerS(String coul, String src, String dir){		
		String[] j = src.split(",");
		int[] p = new int[2];
		for(int i=0; i<2; i++){ p[i] =Integer.parseInt(j[i]); } 
		if(plateau[p[0]][p[1]].getABalle()){ return false; }
		if(p[0] != 0 && dir.equals("N") && plateau[p[0]][p[1]].getCouleur().equals(coul)){
			plateau[p[0]-1][p[1]] = plateau[p[0]][p[1]];
			plateau[p[0]][p[1]] = null;
			return true;
		}
		if(p[0] != 6 && dir.equals("S") && plateau[p[0]][p[1]].getCouleur().equals(coul)){
			plateau[p[0]+1][p[1]] = plateau[p[0]][p[1]];
			plateau[p[0]][p[1]] = null;
			return true;
		}
		if(p[1] != 6 && dir.equals("E") && plateau[p[0]][p[1]].getCouleur().equals(coul)){
			plateau[p[0]][p[1]+1] = plateau[p[0]][p[1]];
			plateau[p[0]][p[1]] = null;
			return true;
		}
		if(p[1] != 0 &&dir.equals("O") && plateau[p[0]][p[1]].getCouleur().equals(coul)){	
			plateau[p[0]][p[1]-1] = plateau[p[0]][p[1]];
			plateau[p[0]][p[1]] = null;
			return true;
		}
		return false;
	}
	
	public boolean aGagne(){
		for(int i=0; i<plateau.length; i++){
			if( plateau[0][i]!=null && plateau[0][i].getABalle() && plateau[0][i].getCouleur().equals("Noir") ||
					plateau[6][i]!=null && plateau[6][i].getABalle() && plateau[6][i].getCouleur().equals("Blanc"))
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
					if( plateau[i][j].getABalle() ){
						if( plateau[i][j].getCouleur().charAt(0) == 'N' ){
								s += " n |";
						}
						if( plateau[i][j].getCouleur().charAt(0) == 'B' ){
							s += " b |";
						}
					}
					
					else
						s += " " + plateau[i][j].getCouleur().charAt(0) + " |";
				}
			}
			s += "\n" + sep + "\n";
		}
		
		return s;
	}
	
	public static void main( String[] args ) {
		Diaballik d = new Diaballik(false);
	}

	@Override
    public void add(String id) {
        tabJoueurs[nbJoueur++].setId(id);
        if(nbJoueur >= 2) {
            server.disalowConnections();
        }
    }

    @Override
    public void sendToAllPlayers(String action) {
        server.sendToAllClient(action);
    }
    
    public void sendToPlayer(String action) {
        server.sendToClient(tabJoueurs[joueurCourant].getNom(), action);
    }

    public String receiveFromPlayer() throws IOException {
        return server.receiveFromClient(tabJoueurs[joueurCourant].getId());
    }
    @Override
    public void launchGame() {
        // TODO Auto-generated method stub
        sendToAllPlayers("Bienvenue dans Diaballik\n");
        try {
            System.out.println("Joueur 1 : " + receiveFromPlayer());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean processMessage(String action) {
        // TODO Auto-generated method stub
        return false;
    }
    
}