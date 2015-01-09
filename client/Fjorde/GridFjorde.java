package client.Fjorde;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GridFjorde extends JPanel {
	ArrayList<Tile> tiles;
	
	public GridFjorde() {
		this.setLayout(null);
		this.tiles = new ArrayList<Tile>();
	}
	
	public void createTest() {
		this.tiles.add(new Tile("MMMMMM", 0));
		this.tiles.add(new Tile("ETMMTE", 2));
		this.tiles.add(new Tile("EEEETE", 5));
		
		tiles.get(0).setLocation(this.getWidth()/2, this.getHeight()/2);
		tiles.get(1).setLocationWithTile(tiles.get(0), 0);
		tiles.get(2).setLocationWithTile(tiles.get(1), 2);
		
		for (Tile t : tiles) {
			JLabel tile = t.getGraphicTile();
			tile.setBounds(t.getX(), t.getY(), Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
			System.out.println(t.getX() + ":" + t.getY());
			this.add(tile);
		}
	}
	
	public void addTile(Tile tile) {
		if (tile != null)
			this.tiles.add(tile);
	}
}
