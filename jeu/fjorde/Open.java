package jeu.fjorde;

public interface Open {
	public Tile getTile(int i);
	public Tile draw(int i);
	public int getSize();
	public void add(Tile t);
}