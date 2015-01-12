package client.Fjorde;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import client.ClientFjorde;

public class GridFjorde extends JPanel implements MouseListener {
	private ClientFjorde client;
	
	private ArrayList<Tile> tiles;
	
	private Pick closePick;
	private JPanel panelClose;
	
	private Pick openPick;
	private JPanel panelOpen;
	private SelectedTile selected;
	
	private JPanel tileViewer;
	
	private boolean isInit;
	
	public GridFjorde(ClientFjorde client) {
		this.client = client;
		this.isInit = false;
		this.setLayout(null);
		this.tiles = new ArrayList<Tile>();
	}
	
	public void initGrid() {
		if (!isInit) {
			//Pioche fermée
			this.panelClose = new JPanel(null);
			this.panelClose.setBounds( this.getWidth()-Tile.IMG_WIDTH-20, 25 + Tile.IMG_HEIGHT, Tile.IMG_WIDTH+20, Tile.IMG_HEIGHT+25);
			this.panelClose.setBackground(new Color(0x7BC5DD));
			
			JLabel titleClose = new JLabel("Pioche Fermée");
			titleClose.setOpaque(true);
			titleClose.setBackground(new Color(0x9DD8DD));
			titleClose.setBounds(0, 0, Tile.IMG_WIDTH+20, 25);
			
			this.closePick = new Pick( this, false);
			this.closePick.setBounds( 10, 25, Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
			this.closePick.setIsEmpty(false);
			this.closePick.addMouseListener(this);
			
			this.panelClose.add(titleClose);
			this.panelClose.add(closePick);
			
			//Pioche ouverte
			this.panelOpen = new JPanel(null);
			this.panelOpen.setBounds( this.getWidth()-Tile.IMG_WIDTH-20, 0, Tile.IMG_WIDTH+20, Tile.IMG_HEIGHT+25);
			this.panelOpen.setBackground(new Color(0x7BC5DD));
			
			JLabel titleOpen = new JLabel("Pioche Ouverte");
			titleOpen.setOpaque(true);
			titleOpen.setBackground(new Color(0x9DD8DD));
			titleOpen.setBounds(0, 0, Tile.IMG_WIDTH+20, 25);
			
			this.openPick = new Pick( this, true);
			this.openPick.setBounds( 10, 25, Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
			this.openPick.addMouseListener(this);
			
			this.panelOpen.add(titleOpen);
			this.panelOpen.add(openPick);
			
			//Selection
			this.selected = new SelectedTile();
			this.selected.setLocation(this.getWidth()-Tile.IMG_WIDTH*2, this.getHeight()-Tile.IMG_HEIGHT*2-50);
			
			this.add(selected);
			this.add(panelClose);
			this.add(panelOpen);
			
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
		this.tiles.add(new Tile("MMMMTT", 5));
		this.tiles.add(new Tile("MMMMTM", 0));
		this.tiles.add(new Tile("MMMTTM", 1));
		this.tiles.add(new Tile("MMTMTT", 2));
		
		tiles.get(0).setLocation(this.getWidth()/2, this.getHeight()/2);
		tiles.get(1).setLocationWithTile(tiles.get(0), 0);
		tiles.get(2).setLocationWithTile(tiles.get(1), 4);
		
		openPick.addTile(tiles.get(3));
		openPick.addTile(tiles.get(4));
		openPick.addTile(tiles.get(5));
		openPick.addTile(tiles.get(6));
		openPick.addTile(tiles.get(7));
		
		selected.setSelectedTile(tiles.get(8));
		
		for (int i = 0; i < 3; i++) {
			tiles.get(i).setBounds(tiles.get(i).getX(), tiles.get(i).getY(), Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
			this.add(tiles.get(i));
		}
	}
	
	public void keepChoosenTile(String tileName) {
		boolean b = false;
		try { 
			b = client.processMessage("OPICK:" + tileName); 
		} catch ( IOException ex) { ex.printStackTrace(); }
		
		if ( b && removeTileViewer())
			openPick.removeTile(tileName);
	}
	
	public boolean removeTileViewer() {
		if ( openPick.closeTileViewer(this)) {
			this.tileViewer = null;
			client.repaint();
			return true;
		}
		return false;
	}
	
	public void addTile(Tile tile) {
		if (isInit && tile != null)
			this.tiles.add(tile);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if ( e.getSource() == this.openPick && tileViewer == null) {
			this.tileViewer = this.openPick.viewTiles();
			if (this.tileViewer != null) {
				this.add(tileViewer, 0);
				this.client.repaint();
			}
		} else if ( e.getSource() == this.closePick && !this.closePick.isEmpty() && tileViewer == null) {
			try { client.processMessage("PICK"); } catch ( IOException ex) { ex.printStackTrace(); }
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}