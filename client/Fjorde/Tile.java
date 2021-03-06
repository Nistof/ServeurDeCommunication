package client.Fjorde;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Tile extends JLabel {
	public final static int IMG_WIDTH = 69;
	public final static int IMG_HEIGHT = 69;
	public final static Color[] PLAYER_COLORS = { new Color(0xd1791b), 
												  new Color(0xdebf9e) };
	
	private String type;
	private int orientation;
	private int xLocation;
	private int yLocation;
	
	private BufferedImage defaultImage;
	
	/**
	 * Initialise un objet de type Tuile 
	 * @param type type de la tuile (Nom du fichier image a charger,...)
	 * @param orientation orientation de la tuile
	 */
	public Tile(String type, int orientation) {
		this.type = type;
		this.orientation = orientation;
		this.xLocation = 0;
		this.yLocation = 0;
		
		if ( type != null && !type.equals("")) {
			this.defaultImage = RotatedTile.getImage(type, orientation, IMG_WIDTH, IMG_HEIGHT);
			this.setIcon(new ImageIcon(defaultImage));
		}
	}	
	
	/**
	 * Dessine l'item sur l'image de la tuile (H = Hutte, F = Champ)
	 * @param item L'item à mettre
	 */
	public void setItem (char item, int player) {
		if (player != 0 && player != 1)
			return ;
		
	    Graphics g = this.defaultImage.getGraphics();
	    g.setColor(PLAYER_COLORS[player]);
	    int center = (IMG_WIDTH-15)/2;
	    //Hutte
	    if(item == 'H') {
	        g.fillRect(center, center, 15, 15);
	        g.fillPolygon(new int[]{center-7, center+7, center+22}, new int[]{center, center-14, center}, 3);
	    }
	    //Champ
	    else if(item == 'F') {
	        g.fillOval(center, center, 15, 15);
	    }
	}
	
	/**
	 * Position X de la tuile
	 * @return Position X de la tuile
	 */
	public int getX() { return this.xLocation; }
	
	/**
	 * Position Y de la tuile
	 * @return Position Y de la tuile
	 */
	public int getY() { return this.yLocation; }
	
	/**
	 * Position absolue de la tuile
	 */
	public void setLocation(int x, int y) {
		this.xLocation = x;
		this.yLocation = y;
	}
	
	/**
	 * Type de la tuile
	 * @return Type de la tuile
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * Position de la tuile par rapport a une autre
	 * @param tile tuile a cote de laquelle placer
	 * @param position cote sur lequel la tuile doit etre placee
	 */
	public void setLocationWithTile(Tile tile, int position) {
		if (tile == null)
			return ;
		
		if ( position == 1 || position == 4) {
			this.xLocation = tile.getX()+(position == 1?IMG_WIDTH:-IMG_WIDTH);
			this.yLocation = tile.getY();
		} else {
			this.xLocation = tile.getX()+(position == 0 || position == 2?IMG_WIDTH/2:-IMG_WIDTH/2);
			this.yLocation = tile.getY()+(position == 2 || position == 3?IMG_HEIGHT/2+IMG_HEIGHT/4:-(IMG_HEIGHT/2+IMG_HEIGHT/4));
		}
	}
}
