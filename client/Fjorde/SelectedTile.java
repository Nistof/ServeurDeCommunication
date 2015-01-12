package client.Fjorde;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SelectedTile extends JPanel {
	private JLabel title;
	private JButton toOpenPick;
	
	private String type;
	private JLabel tile;
	
	public SelectedTile() {
		this.setBackground(new Color(0x7BC5DD));
		this.setLayout(null);
		
		this.title = new JLabel("Selection");
		this.toOpenPick = new JButton("Vers pioche ouverte");
		this.tile = new JLabel();
		
		this.title.setBackground(new Color(0x9DD8DD));
		this.title.setOpaque(true);
		this.title.setBounds(0, 0, Tile.IMG_WIDTH*2, 25);
		this.toOpenPick.setBounds(0, 25, Tile.IMG_WIDTH*2, 25);
		this.tile.setBounds(Tile.IMG_WIDTH/2, 50+Tile.IMG_HEIGHT/2, Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
		
		this.setSize(Tile.IMG_WIDTH*2, Tile.IMG_HEIGHT*2+50);
		this.add(title);
		this.add(toOpenPick);
		this.add(tile);
	}
	
	public void setSelectedTile(Tile tile) {
		if ( tile != null) {
			this.type = tile.getType();
			this.tile.setIcon(tile.getIcon());
		} else {
			this.type = "";
			this.tile.setIcon(null);
		}
	}
	
	public boolean hasSelection() { return tile.getIcon()!=null; }
}
