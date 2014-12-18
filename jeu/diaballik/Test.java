package jeu.diaballik;

import java.util.Scanner;


public class Test {
	
	private static Scanner sc;
	private Boolean b;
	private Diaballik d;
	private String c,src,dir,dest;
	private int cptD,cptB;
	
	public Test(){
		sc = new Scanner(System.in);
	    b = sc.nextBoolean();
		d = new Diaballik(b);
	}
	public boolean aGagne(){
		return d.win();
	}
	public boolean antijeu(DiaballikPlayer j){
		if(d.isBlocked(j)){ return true; }
		return false;
	}
	
	public void tour(DiaballikPlayer joueur){
		cptD = 0;
		cptB = 0;
		for(int i=0; i<3; i++){
			System.out.println( d.toString() );
			
			if(cptD<2){
				if(cptB<1){
					System.out.print ( "Joueur "+ joueur.getColor() +" (D/B/Q) :" );
					c = sc.nextLine();
					while(! c.equals("D") && ! c.equals("B") && ! c.equals("Q")){c = sc.nextLine();}
				}
				else {
					System.out.print ( "Joueur "+ joueur.getColor() +" (D/Q) :" );
					c = sc.nextLine();
					while(! c.equals("D") && ! c.equals("Q")){c = sc.nextLine();}
				}
					
				if(c.equals("D")){
					System.out.println("Veuillez saisir un support (x,y) : ");
					src = sc.nextLine();
					System.out.println("Veuillez saisir la direction (N,S,E,O) : ");
					dir = sc.nextLine();
					while(! dir.equals("N") && ! dir.equals("S") && ! dir.equals("E") && ! dir.equals("O")){dir = sc.nextLine();}
					
					while (! d.moveS(joueur.getColor(),src,dir)){
						System.out.println("Veuillez saisir un support (x,y) : ");
						src = sc.nextLine();
						System.out.println("Veuillez saisir la direction (N,S,E,O) : ");
						dir = sc.nextLine();
						while(! dir.equals("N") && ! dir.equals("S") && ! dir.equals("E") && ! dir.equals("O")){dir = sc.nextLine();}					
					}
					cptD++;
				}
			}
			else{ 
				if(cptB<1){
					System.out.print ( "Joueur "+ joueur.getColor() +" (B/Q) :" );
					c = sc.nextLine();
					while(! c.equals("B") && ! c.equals("Q")){c = sc.nextLine();}
				}
				else {
					System.out.print ( "Joueur "+ joueur.getColor() +" (Q) :" );
					c = sc.nextLine();
					while(! c.equals("Q")){c = sc.nextLine();}
				} 
			}	
			if(c.equals("B") && cptB <1){	
				System.out.println("Veuillez saisir un support de destination (x,y) : ");
				dest = sc.nextLine();
		
				while (! d.moveB(joueur.getColor(),dest)){
					System.out.println("Veuillez saisir un support de destination (x,y) : ");
					dest = sc.nextLine();
				}
				cptB++;
				
			}
			if( i>=1 && c.equals("Q")){break;}
		}
	}
	
	public static void main(String[] args) {	
		System.out.print ( "Voulez vous jouer avec la situation 2 :" );
		Test t = new Test();
		DiaballikPlayer j1 = new DiaballikPlayer("J1","Blanc");
		DiaballikPlayer j2 = new DiaballikPlayer("J2","Noir");
		
		while (!t.aGagne())
		{		
			if(t.antijeu(j2)){ 
				System.out.println("Pr�sence d'antijeu du joueur 2 : voulez vous le d�clarer ?");
				 boolean b = sc.nextBoolean();
				 if(b){	break; }
			}
			t.tour(j1);
			if(t.aGagne()){ break; }
			if(t.antijeu(j1)){ 
				System.out.println("Pr�sence d'antijeu du joueur 1 : voulez vous le d�clarer ?");
				 boolean b = sc.nextBoolean();
				 if(b){	break; }
			}
			t.tour(j2);
		}
	}
}