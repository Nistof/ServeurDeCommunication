package jeu.fjorde;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 01-05-2015
 */

public class Test {
	private Fjorde fjorde;
	private Board board;
	private FjordePlayer player1;
	private FjordePlayer player2;
	private Closed pickClose;
	private Open pickOpen;
	
	public Test(){
		board = new Board();
		pickClose = new Pick(true);
		pickOpen = new Pick(false);
		
		player1 = new FjordePlayer("PAUL","BLANC",pickClose);
		player2 = new FjordePlayer("JACK","NOIR",pickClose);
	}
	
	public String display(){
		return board.toString();
	}
	
	public void tourD(){
		fjorde.play(0, 1);
	}
	
	public void tourC(){
		
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		
		System.out.println(t.display());
		//for(int i=0; i<2; i++){ 
			t.tourD();
			System.out.println(t.display()); 
		//}
		System.out.println(t.display());
		
	}

}
