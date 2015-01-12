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
	
	public Tile(String type, int orientation) {
		this.type = type;
		this.orientation = orientation;
		this.xLocation = 0;
		this.yLocation = 0;
		
		try {
			this.defaultImage = ImageIO.read(new File("./client/Fjorde/images/" + type + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.setIcon(new ImageIcon(defaultImage));
	}	
	
	public int getX() { return this.xLocation; }
	public int getY() { return this.yLocation; }
	
	public void setLocation(int x, int y) {
		this.xLocation = x;
		this.yLocation = y;
	}
	
	public String getType() {
		return this.type;
	}
	
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
