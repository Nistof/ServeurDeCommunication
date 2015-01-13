package client.Fjorde;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Pick extends JLabel implements MouseListener, ActionListener {
	GridFjorde grid;
	
	private LinkedHashMap<String, JLabel> tileList;
	private boolean isOpen;
	
	private boolean isClose;//Rend la pioche inutilisable
	private boolean isEmpty;//Indique si la pioche est vide
	
	private BufferedImage closedPick;
	private BufferedImage nullPick;
	
	private JPanel panelTiles;
	private boolean panelTileIsOpen;
	
	/**
	 * Initialisation de la pioche
	 * @param grid Liaison au plateau de jeu pour communications
	 * @param isOpen Pioche ouverte ou fermee
	 */
	public Pick(GridFjorde grid, boolean isOpen) {
		try {
			this.closedPick = ImageIO.read(new File("./client/Fjorde/images/close.png"));
			this.nullPick = ImageIO.read(new File("./client/Fjorde/images/null.png"));
		} catch (IOException e) {}
		
		this.grid = grid;
		this.tileList = new LinkedHashMap<String, JLabel>();
		this.isOpen = isOpen;
		this.isClose = false;
		this.isEmpty = true;
		
		this.panelTileIsOpen = false;
		
		this.addTile(null);		
	}
	
	/**
	 * Ferme l'acces a la pioche
	 */
	public void close() { this.isClose = true; }
	
	/**
	 * Ajout d'une tuile dans la pioche
	 * @param tile Tuile a ajouter
	 */
	public void addTile(Tile tile) {
		//Si la pioche a été vérouillée
		if (isClose)
			return ;
		
		//Si il s'agit d'une pioche ouverte
		if (isOpen && tile != null) {
			JLabel tmp = new JLabel(tile.getIcon());
			tmp.addMouseListener(this);
			this.tileList.put( tile.getType(), tmp);
		}
		
		//Apparence de la pioche
		if (tile != null) {
			this.isEmpty = false;
			if(isOpen) {
				this.setIcon(tile.getIcon());
			}
			else {
				this.setIcon(new ImageIcon(closedPick));
			}
		} else {
			this.isEmpty = true;
			this.setIcon(new ImageIcon(nullPick));
		}
	}
	
	/**
	 * Retrait d'une tuile de la pioche
	 * @param tile Nom de la tuile a retirer
	 * @return Vrai si la tuile a ete retiree
	 */
	public boolean removeTile (String tile) {
		if (isClose || !isOpen)
			return false;
		
		//Retrait de la tuile dans la pioche ouverte si elle existe
		if (isOpen) {
			if ( tileList.containsKey(tile)) {
				tileList.remove(tile);
				if (tileList.size() == 0)
					this.setIcon(new ImageIcon(nullPick));
				else
					this.setIcon(new ArrayList<JLabel>(tileList.values()).get(tileList.size()-1).getIcon());
				return true;
			}
				
		}
		
		return false;
	}
	
	public JPanel viewTiles() {
		//Si la pioche a été vérouillée
		if (isClose || panelTileIsOpen)
			return null;
		
		//Si il s'agit d'une pioche ouverte
		if (isOpen && tileList.size() != 0) {
			// Dimensions de la fenêtre
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int nbTileColumn = screenSize.width/Tile.IMG_WIDTH - 2;
			int nbLine;
			
			for (nbLine = 0; nbLine*nbTileColumn < tileList.size(); nbLine++);
			
			panelTiles = new JPanel();
			
			//Apparence de la fenêtre
			JButton cancel = new JButton("Fermer");
			JLabel title = new JLabel("Pioche ouverte");
			
			cancel.setBounds(nbTileColumn*Tile.IMG_WIDTH/2, 
							0, 
							nbTileColumn*Tile.IMG_WIDTH/2, 
							25);
			cancel.addActionListener(this);
			title.setBounds(0, 
							0, 
							nbTileColumn*Tile.IMG_WIDTH/2, 
							25);
			title.setOpaque(true);
			title.setBackground(new Color(0x9DD8DD));
			
			panelTiles.setBounds(screenSize.width/2-nbTileColumn*Tile.IMG_WIDTH/2, 
								(screenSize.height/2-nbLine*Tile.IMG_HEIGHT/2), 
								nbTileColumn*Tile.IMG_WIDTH, 
								nbLine*Tile.IMG_HEIGHT+25);
			
			panelTiles.setBackground(new Color(0x7BC5DD));
			
			panelTiles.add(cancel);
			panelTiles.add(title);
			
			//Affichage des tuiles dans la fenêtre
			int ligne = 0,
				colonne = 0;
			JLabel tileTmp;
			for (String str : tileList.keySet()) {
				tileTmp = tileList.get(str);
				tileTmp.setBounds( Tile.IMG_WIDTH*colonne, Tile.IMG_HEIGHT*ligne+25, 
									Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
				panelTiles.add(tileTmp);
				
				if (colonne%nbTileColumn == 0 && colonne != 0) {
					++ligne;
					colonne = 0;
				}
				++colonne;
			}
					
			this.panelTileIsOpen = true;
			return panelTiles;
		}
		
		return null;
	}
	
	/**
	 * Ferme le TileViewer si celui-ci a ete ouvert 
	 * @param panelLocation Composant ou a ete ajoute le TileViewer
	 * @return Vrai si le TileViewer a ete retire
	 */
	public boolean closeTileViewer(JPanel panelLocation) {
		if (!panelTileIsOpen)
			return false;
		panelLocation.remove(panelTiles);
		this.panelTiles.invalidate();
		this.panelTiles = null;
		this.panelTileIsOpen = false;
		
		return true;
	}
	
	/**
	 * Définie si une pioche est vide ou non
	 * @param isEmpty Vrai si la pioche doit apparaitre vide
	 */
	public void setIsEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
		if (isEmpty)
			this.addTile(null);
		else
			this.addTile(new Tile("",0));
	}
	
	/**
	 * Retourne l'etat de la pioche (si elle est vide ou non)
	 * @return Vrai si la pioche est vide
	 */
	public boolean isEmpty() { return this.isEmpty; }
	
	/**
	 * Renvoie le nom de la tuile dont le JLabel est passe en parametre
	 * @param tile JLabel de la tuile dont on souhaite recuperer le nom
	 * @return nom de la tuile
	 */
	private String getTileName(JLabel tile) {
		for (String str : tileList.keySet())
			if (tileList.get(str).equals(tile))
				return str;
		return null;				
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//Selection des tuiles dans le TileViewer
		if (e.getSource() instanceof JLabel) {
			JLabel tmp = (JLabel)e.getSource();
			tmp.setOpaque(true);
			tmp.setBackground(new Color(0xAA8080));
			
		}		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		//Selection des tuiles dans le TileViewer
		if (e.getSource() instanceof JLabel) {
			JLabel tmp = (JLabel)e.getSource();
			tmp.setBackground(null);
		}		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//Selection des tuiles dans le TileViewer
		if (e.getSource() instanceof JLabel) {
			JLabel tmp = (JLabel)e.getSource();
			String str = getTileName(tmp);
			tmp.setBackground(null);
			
			//Si le choix n'est pas nul, envoi au plateau pour traitement
			if ( str != null)
				grid.keepChoosenTile(str);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Appui sur le bouton annuler du TileViewer
		grid.removeTileViewer();
	}
}
