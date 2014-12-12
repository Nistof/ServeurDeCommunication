package jeu;

import java.util.HashSet;

import server.Server;

public class Diaballik implements IJeu{
    private static String[] couleurs = {"Blanc", "Noir"};
	private Support[][] plateau;
	private Joueur[] tabJoueurs;
	private Joueur joueurCourant;
    private Server server;
    private int nbJoueur;
	
	public Diaballik( boolean variante, Server server ) {
		this.server = server;
	    this.tabJoueurs = new Joueur[2];
		this.tabJoueurs[0] = new Joueur( "Joueur 1", "Blanc" );
		this.tabJoueurs[1] = new Joueur( "Joueur 2", "Noir" );
		this.joueurCourant = tabJoueurs[0];
		
		this.plateau = new Support[7][7];
		if(variante) {
			//completer
		} else {
			for( int i=0; i<plateau.length; i++ ) {
				this.plateau[0][i] = tabJoueurs[0].getSupport(i);
				this.plateau[6][i] = tabJoueurs[1].getSupport(i);
			}
		}
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
					//modifier
					if( plateau[i][j].getABalle() )
						s += " 0 |";
					else
						s += " " + plateau[i][j].getCouleur().charAt(0) + " |";
				}
			}
			s += "\n" + sep + "\n";
		}
		
		return s;
	}
	
	public static void main( String[] args ) {
		Diaballik d = new Diaballik(false,null);
		System.out.println( d.toString() );
	}

    @Override
    public String getEtatInitial() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getOrdreDuTour() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String action(String action) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String Classement() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void add(String id) {
        if(nbJoueur < 2) {
            tabJoueurs[nbJoueur] =  new Joueur(id,couleurs[nbJoueur]);
        }
    }
}