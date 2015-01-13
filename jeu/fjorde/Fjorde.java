package jeu.fjorde;

import java.io.IOException;

import jeu.IJeu;

public class Fjorde implements IJeu {
	private Board board;
	private FjordePlayer[] players;
	private int currentPlayer;
	private Closed pickClose;
	private Open pickOpen;
	
	
	
	public void draw () {
		players[currentPlayer].draw(pickClose.draw());	
	}
	
	public void draw (int i) {
		players[currentPlayer].draw(pickOpen.draw(i));
	}
	
	public boolean play(int id, int side) {
		this.board.add(id, side, players[currentPlayer].removeFromHand());
		return true;
	}
	
	@Override
	public void add(String name, String id) {
		
	}

	@Override
	public void sendToAllPlayers(String msg) throws IOException {
		
	}

	@Override
	public void sendToPlayer(String msg) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String receiveFromPlayer() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int processMessage(String msg) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void launchGame() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
