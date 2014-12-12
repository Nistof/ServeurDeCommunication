package jeu;

public class Diaballik {
	private Support[][] plateau;
	private Joueur[] tabJoueur;
	private Joueur joueurCourant;
	
	public Diaballik( boolean variante ) {
		this.tabJoueur = new Joueur[2];
		this.tabJoueur[0] = new Joueur( "Joueur 1", "Blanc" );
		this.tabJoueur[1] = new Joueur( "Joueur 2", "Noir" );
		this.joueurCourant = tabJoueur[0];
		
		this.plateau = new Support[7][7];
		if(variante) {
			//completer
		} else {
			for( int i=0; i<plateau.length; i++ ) {
				this.plateau[0][i] = tabJoueur[0].getSupport(i);
				this.plateau[6][i] = tabJoueur[1].getSupport(i);
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
		Diaballik d = new Diaballik(false);
		System.out.println( d.toString() );
	}
}