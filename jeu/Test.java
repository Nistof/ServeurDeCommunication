package jeu;

import java.util.Scanner;

public class Test {

	private static Scanner sc;
	
	public static void main(String[] args) {
		sc = new Scanner(System.in);
		System.out.print ( "Voulez vous jouer avec la situation 2 :" );
		Boolean b = sc.nextBoolean();
	    
		Diaballik d = new Diaballik(b);
		String c,src,dir,dest;
		int cpt;
		
		while (true)
		{		
			cpt = 0;
			for(int i=0; i<3; i++){
				System.out.println( d.toString() );
				
				//Probleme au premier tour : le joueur blanc passe un tour (surment un probleme de scanner)
				
				if(i<2){
					System.out.print ( "Joueur Blanc (D/B/Q) :" );
					c = sc.nextLine();
					
					if(c.equals("D")){
						System.out.println("Veuillez saisir un support (x,y) : ");
						src = sc.nextLine();
						System.out.println("Veuillez saisir la direction (N,S,E,O) : ");
						dir = sc.nextLine();
			
						d.deplacerS(src,dir);
						cpt++;
					}
				}
				else{ 
					System.out.print ( "Joueur Blanc (B/Q) :" );
					c = sc.nextLine();
				}
				if(c.equals("B")){
					System.out.println("Veuillez saisir un support de destination (x,y) : ");
					dest = sc.nextLine();
			
					d.deplacerB(dest);
				}
				if(c.equals("Q")){break;}
			}
			
			cpt=0;
			for(int i=0; i<3; i++){
				System.out.println( d.toString() );
				
				if(cpt<2){
					System.out.print ( "Joueur Noir (D/B/Q) :" );
					c = sc.nextLine();
					
					if(c.equals("D")){
						System.out.println("Veuillez saisir un support (x,y) : ");
						src = sc.nextLine();
						System.out.println("Veuillez saisir la direction (N,S,E,O) : ");
						dir = sc.nextLine();
			
						d.deplacerS(src,dir);
						cpt++;
					}
				}
				else{ 
					System.out.print ( "Joueur Noir (B/Q) :" );
					c = sc.nextLine();
				}
				if(c.equals("B")){
					System.out.println("Veuillez saisir un support de destination (x,y) : ");
					dest = sc.nextLine();
			
					d.deplacerB(dest);
				}
				if(c.equals("Q")){break;}
			}
		}
	}

}
