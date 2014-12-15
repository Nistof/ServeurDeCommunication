package jeu.diaballik;

import java.util.Scanner;


public class Test {
	
	private Scanner sc;
	private Boolean b;
	private Diaballik d;
	private String c,src,dir,dest;
	private int cpt;
	
	public Test(){
		sc = new Scanner(System.in);
	    b = sc.nextBoolean();
		d = new Diaballik(b);
	}
	public boolean aGagne(){
		return d.aGagne();
	}
	
	public void tour(JoueurDiaballik joueur){
		cpt = 0;
		for(int i=0; i<3; i++){
			System.out.println( d.toString() );
			
			if(cpt<2){
				System.out.print ( "Joueur "+ joueur.getCouleur() +" (D/B/Q) :" );
				c = sc.nextLine();
				while(! c.equals("D") && ! c.equals("B") && ! c.equals("Q")){c = sc.nextLine();}
				
				if(c.equals("D")){
					System.out.println("Veuillez saisir un support (x,y) : ");
					src = sc.nextLine();
					System.out.println("Veuillez saisir la direction (N,S,E,O) : ");
					dir = sc.nextLine();
					while(! dir.equals("N") && ! dir.equals("S") && ! dir.equals("E") && ! dir.equals("O")){dir = sc.nextLine();}
					
					while (! d.deplacerS(joueur.getCouleur(),src,dir)){
						System.out.println("Veuillez saisir un support (x,y) : ");
						src = sc.nextLine();
						System.out.println("Veuillez saisir la direction (N,S,E,O) : ");
						dir = sc.nextLine();
						while(! dir.equals("N") && ! dir.equals("S") && ! dir.equals("E") && ! dir.equals("O")){dir = sc.nextLine();}					
					}
					cpt++;
				}
			}
			else{ 
				System.out.print ( "Joueur "+ joueur.getCouleur() +" (B/Q) :" );
				c = sc.nextLine();
				while(! c.equals("B") && ! c.equals("Q")){c = sc.nextLine();}
			}
			if(c.equals("B")){
				System.out.println("Veuillez saisir un support de destination (x,y) : ");
				dest = sc.nextLine();
		
				while (! d.deplacerB(joueur.getCouleur(),dest)){
					System.out.println("Veuillez saisir un support de destination (x,y) : ");
					dest = sc.nextLine();
				}
			}
			if(i>=1 && c.equals("Q") || d.aGagne()){break;}
		}
	}
	
	public static void main(String[] args) {
		System.out.print ( "Voulez vous jouer avec la situation 2 :" );
		Test t = new Test();
		JoueurDiaballik j1 = new JoueurDiaballik("J1","Blanc");
		JoueurDiaballik j2 = new JoueurDiaballik("J2","Noir");
		
		while (!t.aGagne())
		{		
			t.tour(j1);
			t.tour(j2);
		}
	}
}