package client.Fjorde;

import java.util.ArrayList;

import javax.swing.JPanel;

public class GridFjorde extends JPanel {
	private ArrayList<Tile> tiles;
	private Pick closePick;
	private Pick openPick;
	
	private boolean isInit;
	
	public GridFjorde() {
		this.isInit = false;
		this.setLayout(null);
		this.tiles = new ArrayList<Tile>();
	}
	
	public void initGrid() {
		if (!isInit) {
			this.closePick = new Pick(false);
			this.closePick.setBounds( this.getWidth()-Tile.IMG_WIDTH, 20 + Tile.IMG_HEIGHT, Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
			
			this.openPick = new Pick(true);
			this.openPick.setBounds( this.getWidth()-Tile.IMG_WIDTH, 10, Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
			
			this.add(openPick);
			this.add(closePick);
			
			this.createTest();
		}
		
		this.isInit = true;
	}
	
	private void createTest() {
		this.tiles.add(new Tile("MMMMMM", 0));
		this.tiles.add(new Tile("ETMMTE", 1));
		this.tiles.add(new Tile("EEEETE", 2));
		
		this.tiles.add(new Tile("EEEETT", 3));
		this.tiles.add(new Tile("EEETEE", 4));
		
		tiles.get(0).setLocation(this.getWidth()/2, this.getHeight()/2);
		tiles.get(1).setLocationWithTile(tiles.get(0), 0);
		tiles.get(2).setLocationWithTile(tiles.get(1), 4);
		
		openPick.setFrontTile(tiles.get(3));
		closePick.setFrontTile(tiles.get(4));
		
		for (int i = 0; i < 3; i++) {
			tiles.get(i).setBounds(tiles.get(i).getX(), tiles.get(i).getY(), Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
			this.add(tiles.get(i));
		}
		
		JPanel tileViewer = openPick.viewTiles();
		this.add(tileViewer, 0);
	}
	
	public void addTile(Tile tile) {
		if (isInit && tile != null)
			this.tiles.add(tile);
	}
}