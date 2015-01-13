package client.Fjorde;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Tile extends JLabel {
	public final static int IMG_WIDTH = 69;
	public final static int IMG_HEIGHT = 69;
	
	private String type;
	private int orientation;
	private int xLocation;
	private int yLocation;
	
	private BufferedImage defaultImage;
	private BufferedImage transformedImage;
	
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
		
		if ( !type.equals("")) {
			try {
				this.defaultImage = ImageIO.read(new File("./client/Fjorde/images/" + type + ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.setIcon(new ImageIcon(defaultImage));
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
		if ( position == 1 || position == 4) {
			this.xLocation = tile.getX()+(position == 1?IMG_WIDTH:-IMG_WIDTH);
			this.yLocation = tile.getY();
		} else {
			this.xLocation = tile.getX()+(position == 0 || position == 2?IMG_WIDTH/2:-IMG_WIDTH/2);
			this.yLocation = tile.getY()+(position == 2 || position == 3?IMG_HEIGHT/2+IMG_HEIGHT/4:-(IMG_HEIGHT/2+IMG_HEIGHT/4));
		}
	}
}
