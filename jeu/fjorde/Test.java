package jeu.fjorde;

public class Test {
	private Board board;
	private FjordePlayer player1;
	private FjordePlayer player2;
	private Pick pickClose;
	private Pick pickOpen;
	
	public Test(){
		board = new Board();
		pickClose = new Pick(true);
		pickOpen = new Pick(false);
		
		player1 = new FjordePlayer("PAUL","BLANC",pickClose);
		player2 = new FjordePlayer("JACK","NOIR",pickClose);
	}
	
	public String display(){
		System.out.println(board.getNbHut(player1));
		return board.boardDraw();
	}
	
	public void tour(){
		player1.put(board,pickClose);
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		
		System.out.println(t.display());
		for(int i=0; i<2; i++){ t.tour();System.out.println(t.display()); }
	}

}
