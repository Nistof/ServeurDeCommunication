package client.Fjorde;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SelectedTile extends JPanel implements ActionListener {
	private JLabel title;
	private JButton toOpenPick;
	
	private JButton rotateLeft;
	private JButton rotateRight;
	private int orientation;
	
	private String type;
	private JLabel tile;
	
	/**
	 * Initialise un composant indiquant la tuile selectionnee
	 */
	public SelectedTile() {
		this.orientation = 0;
		
		this.setBackground(new Color(0x7BC5DD));
		this.setLayout(null);
		
		this.title = new JLabel("Selection");
		this.toOpenPick = new JButton("Vers pioche ouverte");
		this.rotateLeft = new JButton("<");
		this.rotateRight = new JButton(">");
		this.tile = new JLabel();
		
		this.title.setBackground(new Color(0x9DD8DD));
		this.title.setOpaque(true);
		this.title.setBounds(0, 0, Tile.IMG_WIDTH*2, 25);
		this.toOpenPick.setBounds(0, 25, Tile.IMG_WIDTH*2, 25);
		this.tile.setBounds(Tile.IMG_WIDTH/2, 50+Tile.IMG_HEIGHT/2, Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
		
		this.toOpenPick.setEnabled(false);
		
		this.rotateLeft.setBounds(20, Tile.IMG_HEIGHT*2+20, 42, 25);
		this.rotateRight.setBounds(Tile.IMG_WIDTH*2-60, Tile.IMG_HEIGHT*2+20, 42, 25);
		this.rotateLeft.addActionListener(this);
		this.rotateRight.addActionListener(this);
		this.rotateLeft.setVisible(false);
		this.rotateRight.setVisible(false);
		
		this.setSize(Tile.IMG_WIDTH*2, Tile.IMG_HEIGHT*2+50);
		this.add(title);
		this.add(toOpenPick);
		this.add(tile);
		this.add(rotateLeft);
		this.add(rotateRight);
	}
	
	/**
	 * Definie la tuile selectionnee
	 * @param tile Tuile a selectionner
	 */
	public void setSelectedTile(Tile tile) {
		if ( tile != null) {
			this.type = tile.getType();
			this.tile.setIcon(tile.getIcon());
			this.toOpenPick.setEnabled(true);
			this.rotateLeft.setVisible(true);
			this.rotateRight.setVisible(true);
		} else {
			this.type = "";
			this.tile.setIcon(null);
			this.toOpenPick.setEnabled(false);
			this.rotateLeft.setVisible(false);
			this.rotateRight.setVisible(false);
			this.orientation = 0;
		}
	}

	public String getType() {
		return type;
	}
	/**
	 * Renvoie le status de la selection : si oui ou non une selection est faite
	 * @return Vrai si une tuile est selectionnee
	 */
	public boolean hasSelection() { return tile.getIcon()!=null; }
	
	/**
	 * Renvoie le bouton d'envoi a la pioche ouverte
	 * @return Bouton d'envoi a la pioche ouverte
	 */
	public JButton getToOpenPickButton() { return toOpenPick; }
	
	/**
	 * Renvoie le bouton de rotation vers la gauche
	 * @return Bouton de rotation vers la gauche
	 */
	public JButton getRotateLeftButton() { return rotateLeft; }
	
	/**
	 * Renvoie le bouton de rotation vers la droite
	 * @return Bouton de rotation vers la droite
	 */
	public JButton getRotateRightButton() { return rotateRight; }
	
	/**
	 * Renvoie l'orientation de la piece
	 * @return Orientation de la piece
	 */
	public int getOrientation() { return this.orientation; }

	@Override
	public void actionPerformed(ActionEvent e) {
		//Changement de l'orientation de la pièce
		try {
			//Rotation gauche
			if ( e.getSource() == this.rotateLeft) {
				orientation = ((orientation-1)%6+6)%6;
				this.tile.setIcon(new ImageIcon( RotatedTile.getImage(type, orientation, Tile.IMG_WIDTH, Tile.IMG_WIDTH)));
				
			}
			//Rotation droite
			else if ( e.getSource() == this.rotateRight) {
				orientation = ((orientation+1)%6+6)%6;
				this.tile.setIcon(new ImageIcon( RotatedTile.getImage(type, orientation, Tile.IMG_WIDTH, Tile.IMG_WIDTH)));
			}
		} catch (IOException ex) { }
	}
}
