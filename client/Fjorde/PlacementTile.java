package client.Fjorde;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class PlacementTile extends JLabel implements MouseListener {
	private ImageIcon image;
	private ImageIcon imageSelected;
	private int orientation;
	
	private Tile neighboor;
	private int position;
	
	/**
	 * Initialisation d'une PlacementTile
	 * @param neighboor Voisin de la tuile de placement
	 * @param position Cote sur laquelle la positionTile sera lie
	 * @param orientation Orientation necessaire au placement
	 */
	public PlacementTile(Tile neighboor, int position, int orientation) {
		this.orientation = orientation;
		this.neighboor = neighboor;
		this.position = position;
		
		this.addMouseListener(this);
		this.image = new ImageIcon(RotatedTile.getImage("PPPPPP", 0, Tile.IMG_WIDTH, Tile.IMG_HEIGHT)); //PositionTile not selected
		this.imageSelected = new ImageIcon(RotatedTile.getImage("SSSSSS", 0, Tile.IMG_WIDTH, Tile.IMG_HEIGHT)); //PositionTile selected
		this.setLocationWithTile(neighboor, position);
		this.setIcon(this.image);
	}
	
	/**
	 * Renvoie l'orientation necessaire de la piece selectionnee pour se placer a sa position
	 * @return Orientation necessaire au placement
	 */
	public int getOrientation() { return this.orientation; }
	
	/**
	 * Position de la tuile par rapport a une autre
	 * @param tile tuile a cote de laquelle placer
	 * @param position cote sur lequel la tuile doit etre placee
	 */
	private void setLocationWithTile(Tile tile, int position) {
		int xLocation, yLocation;
		if ( position == 1 || position == 4) {
			xLocation = tile.getX()+(position == 1?Tile.IMG_WIDTH:-Tile.IMG_WIDTH);
			yLocation = tile.getY();
		} else {
			xLocation = tile.getX()+(position == 0 || position == 2?Tile.IMG_WIDTH/2:-Tile.IMG_WIDTH/2);
			yLocation = tile.getY()+(position == 2 || position == 3?Tile.IMG_HEIGHT/2+Tile.IMG_HEIGHT/4:-(Tile.IMG_HEIGHT/2+Tile.IMG_HEIGHT/4));
		}
		
		this.setBounds(xLocation, yLocation, Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
	}
	
	/**
	 * Retourne le voisin de cette PlacementTile
	 * @return Voisin
	 */
	public Tile getNeighboor() { return this.neighboor; }
	
	/**
	 * Retourne la position a laquelle est li�e la PlacementTile sur le voisin
	 * @return Position a laquelle est li�e la PlacementTile sur le voisin
	 */
	public int getPosition() { return this.position; }

	@Override
	public void mouseClicked(MouseEvent arg0) { }

	@Override
	public void mouseEntered(MouseEvent arg0) {
		this.setIcon(this.imageSelected);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		this.setIcon(this.image);
	}

	@Override
	public void mousePressed(MouseEvent arg0) { }

	@Override
	public void mouseReleased(MouseEvent arg0) { }
}
