package client.Fjorde;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Pick extends JLabel {
	private Tile frontTile;
	private ArrayList<Tile> tileList;
	private boolean isOpen;
	
	private boolean isClose;//Rend la pioche inutilisable
	
	private BufferedImage closedPick;
	private BufferedImage nullPick;
	
	private Rectangle normalBounds;
	private JPanel panelTiles;
	private boolean panelTileIsOpen;
	
	public Pick(boolean isOpen) {
		try {
			this.closedPick = ImageIO.read(new File("./client/Fjorde/images/close.png"));
			this.nullPick = ImageIO.read(new File("./client/Fjorde/images/null.png"));
		} catch (IOException e) {}
		
		this.tileList = new ArrayList<Tile>();
		this.isOpen = isOpen;
		this.isClose = false;
		
		this.panelTileIsOpen = false;
		
		this.setFrontTile(null);		
	}
	
	public void close() { this.isClose = true; }
	
	public void setFrontTile(Tile tile) {
		//Si la pioche a été vérouillée
		if (isClose)
			return ;
		
		//Si il s'agit d'une pioche ouverte
		if (isOpen && tile != null)
			this.tileList.add(tile);
		this.frontTile = tile;
		
		//Apparence de la pioche
		if (tile != null) {
			if(isOpen) {
				this.setIcon(tile.getIcon());
			}
			else {
				this.setIcon(new ImageIcon(closedPick));
			}
		} else {
			this.setIcon(new ImageIcon(nullPick));
		}
	}
	
	public JPanel viewTiles() {
		//Si la pioche a été vérouillée
		if (isClose || panelTileIsOpen)
			return null;
		
		//Si il s'agit d'une pioche ouverte
		if (isOpen && tileList.size() != 0) {
			System.out.println("Open!");
			// Dimensions de la fenêtre
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int nbTileColumn = screenSize.width/Tile.IMG_WIDTH - 2;
			int nbLine;
			
			for (nbLine = 0; nbLine*nbTileColumn < tileList.size(); nbLine++);
			
			panelTiles = new JPanel(new GridLayout(nbLine, nbTileColumn));
			panelTiles.setBounds(screenSize.width/2-nbTileColumn*Tile.IMG_WIDTH/2, 
								screenSize.height/2-nbLine*Tile.IMG_HEIGHT/2, 
								nbTileColumn*Tile.IMG_WIDTH, 
								nbLine*Tile.IMG_HEIGHT);
			
			panelTiles.setBackground(new Color(0x7BC5DD));
			
			for (Tile t : tileList)
				panelTiles.add(t);
					
			this.panelTileIsOpen = true;
			return panelTiles;
		}
		
		return null;
	}
	
	public boolean closeTileViewer(JPanel panelLocation) {
		if (!panelTileIsOpen)
			return false;
		
		panelLocation.remove(panelTiles);
		this.panelTiles.invalidate();
		this.panelTiles = null;
		this.panelTileIsOpen = false;
		
		return true;
	}
}
