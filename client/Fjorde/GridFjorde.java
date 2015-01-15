package client.Fjorde;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.ClientFjorde;

public class GridFjorde extends JPanel implements MouseMotionListener, MouseListener, ActionListener {
	private ClientFjorde client;
	
	private ArrayList<Tile> tiles;
	private ArrayList<PlacementTile> placement;
	
	private JPanel board;
	private boolean isBoardSelected;
	private int cX;
	private int cY;
	
	private Pick closePick;
	private JPanel panelClose;
	
	private Pick openPick;
	private JPanel panelOpen;
	private SelectedTile selected;
	
	private JPanel tileViewer;
	private JPanel hutWindow;
	private JLabel labelHut;
	
	private boolean isInit;
	private int nbHut;
	
	/**
	 * Initialisation du plateau de jeu
	 * @param client client s'occupant de la communication avec le serveur
	 */
	public GridFjorde(ClientFjorde client) {
		this.client = client;
		this.isInit = false;
		this.isBoardSelected = false;
		this.nbHut = 4;
		this.setLayout(null);
		
		//Tuiles
		this.tiles = new ArrayList<Tile>();
		
		//Tuiles de placement
		this.placement = new ArrayList<PlacementTile>();
	}
	
	/**
	 * Initialise le plateau de jeu
	 */
	public void initGrid() {
		if (!isInit) {
			//Plateau de jeu (La ou les tuiles sont placees)
			this.board = new JPanel(null);
			this.board.setBounds( -this.getWidth()/2, -this.getHeight()/2, this.getWidth()*2, this.getHeight()*2);
			this.board.addMouseListener(this);
			this.board.addMouseMotionListener(this);
			
		    //Pioche fermee
			this.panelClose = new JPanel(null);
			this.panelClose.setBounds( this.getWidth()-Tile.IMG_WIDTH-20, 25 + Tile.IMG_HEIGHT, Tile.IMG_WIDTH+20, Tile.IMG_HEIGHT+25);
			this.panelClose.setBackground(new Color(0x7BC5DD));

			JLabel titleClose = new JLabel("Pioche Fermee");
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
			this.selected = new SelectedTile(this);
			this.selected.setLocation(this.getWidth()-Tile.IMG_WIDTH*2, this.getHeight()-Tile.IMG_HEIGHT*2-50);
			this.selected.getToOpenPickButton().addActionListener(this);
			this.selected.getRotateRightButton().addActionListener(this);
			this.selected.getRotateLeftButton().addActionListener(this);
			
			//Nombre de hutte restant au joueur
			this.labelHut = new JLabel("Nombre de hutte : " + nbHut);
			this.labelHut.setBounds( this.getWidth()/2-this.getWidth()/12, 0, this.getWidth()/6, 25);
			this.labelHut.setOpaque(true);
			this.labelHut.setBackground(new Color(0x9DD8DD));
			
			//Confirmation de placement de hutte
			this.hutWindow = new JPanel(null);
			this.hutWindow.setBounds(this.getWidth()/2-this.getWidth()/8, this.getHeight()/2-25,
									 this.getWidth()/4, 50);
			this.hutWindow.setBackground(new Color(0x7BC5DD));
			JLabel hutTitle = new JLabel("Placer une hutte?");
			hutTitle.setBounds(0, 0, this.getWidth()/3, 25);
			hutTitle.setOpaque(true);
			hutTitle.setBackground(new Color(0x9DD8DD));
			this.hutWindow.add(hutTitle);
			JButton valid = new JButton("Oui");
			JButton deny = new JButton("Non");
			valid.setBounds(hutWindow.getWidth()/2-75, 25, 75, 25);
			deny.setBounds(hutWindow.getWidth()/2, 25, 75, 25);
			valid.addActionListener(this);
			deny.addActionListener(this);
			this.hutWindow.add(valid);
			this.hutWindow.add(deny);
			this.hutWindow.setVisible(false);
			
			this.add(selected);
			this.add(panelClose);
			this.add(panelOpen);
			this.add(labelHut);
			this.add(hutWindow);
			this.add(board);
			
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
		
		tiles.get(0).setLocation(this.board.getWidth()/2, this.board.getHeight()/2);
		tiles.get(0).setItem('H',0);
		tiles.get(1).setLocationWithTile(tiles.get(0), 0);
		tiles.get(1).setItem('F',1);
		tiles.get(2).setLocationWithTile(tiles.get(1), 4);
		
		openPick.addTile(tiles.get(3));
		openPick.addTile(tiles.get(4));
		openPick.addTile(tiles.get(5));
		openPick.addTile(tiles.get(6));
		openPick.addTile(tiles.get(7));
		
		this.addPlacementTile(tiles.get(0), 1, 0);
		this.addPlacementTile(tiles.get(0), 2, 1);
		this.addPlacementTile(tiles.get(0), 3, 2);
		this.addPlacementTile(tiles.get(0), 4, 3);
		this.addPlacementTile(tiles.get(0), 1, 4);
		this.addPlacementTile(tiles.get(0), 2, 5);
		this.addPlacementTile(tiles.get(1), 0, 1);
		this.addPlacementTile(tiles.get(1), 1, 2);
		this.addPlacementTile(tiles.get(1), 2, 3);
		this.addPlacementTile(tiles.get(1), 5, 0);
		this.addPlacementTile(tiles.get(1), 0, 4);
		this.addPlacementTile(tiles.get(1), 1, 5);
		this.addPlacementTile(tiles.get(2), 0, 3);
		this.addPlacementTile(tiles.get(2), 3, 0);
		this.addPlacementTile(tiles.get(2), 4, 1);
		this.addPlacementTile(tiles.get(2), 5, 2);
		this.addPlacementTile(tiles.get(2), 3, 4);
		this.addPlacementTile(tiles.get(2), 4, 5);
		
		for (int i = 0; i < 3; i++) {
			tiles.get(i).setBounds(tiles.get(i).getX(), tiles.get(i).getY(), Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
			this.board.add(tiles.get(i));
		}
		
		//this.setColonisationPhase();
	}
	
	/**
	 * Demande au serveur si le joueur peut prendre la piece passee en parametre
	 * @param tileName Nom de la tuile
	 */
	public void keepChoosenTile(String tileName) {
		int keep = -1;
		
		//Envoi de la demande au serveur
		try { 
			keep = client.processMessage("OPICK:" + tileName); 
		} catch ( IOException ex) { ex.printStackTrace(); }
		
		//Si la tuile peut etre prise
		if ( keep == 127 && removeTileViewer()) {
			//on la retire de la pioche ouverte
			if ( openPick.removeTile(tileName)) {
				selected.setSelectedTile(tileName);; //On la place en tant que tuile selectionnee
				updatePlacementTile(); //Affichage des PlacementTile
			}
		}
	}
	
	/**
	 * Retire l'affichage du contenu de la pioche ouverte
	 * @return Vrai si fermeture de la vue des tuiles
	 */
	public boolean removeTileViewer() {
		if ( openPick.closeTileViewer(this)) {
			this.tileViewer = null;
			client.repaint();
			return true;
		}
		return false;
	}
	
	/**
	 * Ajoute une tuile sur le plateau
	 * @param tile tuile a placer
	 */
	public void addTile(Tile tile) {
		if (isInit && tile != null) {
			tile.setBounds(tile.getX(), tile.getY(), Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
			this.tiles.add(tile);
			this.board.add(tile);
		}
	}
	
	/**
	 * Cree une PlacementTile a cote de la tuile passee en parametre
	 * @param neighboor Voisin de la PlacementTile
	 * @param position Cote ou est lie la PlacementTile
	 */
	public void addPlacementTile(Tile neighboor, int position, int orientation) {
		if (neighboor != null) {
			PlacementTile pt = new PlacementTile(neighboor, position, orientation);
			pt.setBounds(pt.getX(), pt.getY(), Tile.IMG_WIDTH, Tile.IMG_HEIGHT);
			pt.setVisible(false);
			pt.addMouseListener(this);
			this.placement.add(pt);
			this.board.add(pt);
		}
	}
	
	/**
	 * Permet de supprimer toutes les PlacementTile visibles
	 */
	private void purgeVisiblePlacementTile() {
		for (PlacementTile t : placement)
				t.setVisible(false);
	}
	
	/**
	 * Met a jour l'affichage des tuiles de placement
	 */
	public void updatePlacementTile() {
		this.purgeVisiblePlacementTile();
		for (PlacementTile t : placement) 
			if ( t.getOrientation() == selected.getOrientation()) 
				t.setVisible(true);
	}
	
	/**
	 * Retire toutes les PlacementTile ajoutees
	 */
	private void removeAllPlacementTile() {
		while ( placement.size() != 0) {
			this.board.remove(placement.get(0));
			placement.remove(0);
		}
		client.repaint();
	}
	
	/**
	 * Rend la fenetre du choix de placement de hutte visible ou non
	 * @param isVisible Etat de la fenetre
	 */
	public void setHutWindowVisible( boolean isVisible) {
		if (isVisible) {
			this.hutWindow.setVisible(true);
		} else {
			this.hutWindow.setVisible(false);
		}
	}
	
	/**
	 * Cache tout les elements ne servant pas lors de la phase de colonisation
	 * et ajoute des MouseListener sur les tuiles afin de pouvoir placer les champs
	 */
	public void setColonisationPhase() {
		this.panelClose.setVisible(false);
		this.panelOpen.setVisible(false);
		this.labelHut.setVisible(false);
		this.selected.setVisible(false);
		
		for (Tile t : tiles)
			t.addMouseListener(this);
		
		client.repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//Afficher le contenu de la pioche ouverte
		if ( e.getSource() == this.openPick && tileViewer == null && !this.selected.hasSelection()) {
			this.tileViewer = this.openPick.viewTiles();
			if (this.tileViewer != null) {
				this.add(tileViewer, 0);
				this.client.repaint();
			}
		}
		//Piocher une piece dans la pioche fermee
		else if ( e.getSource() == this.closePick && !this.closePick.isEmpty() && tileViewer == null) {
			try { client.processMessage("PICK"); } catch ( IOException ex) { ex.printStackTrace(); }
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		if ( e.getSource() instanceof PlacementTile) {
			//Creation et initialisation de la tuile
			PlacementTile pt = (PlacementTile)e.getSource();
			Tile tile = new Tile(selected.getType(), pt.getOrientation());
			tile.setLocation(pt.getX(), pt.getY());
			
			this.addTile(tile);	//Ajout au plateau
			this.removeAllPlacementTile(); //Retrait des PlacementTile
			this.selected.setSelectedTile(null); //Retrait de la selection
			
			//Envoi du placement au serveur
			try { client.processMessage("POSET:"+ pt.getNeighboor().getType() + ":" + pt.getPosition()); } catch ( IOException ex) { ex.printStackTrace(); }
		}
		//Placement d'un champ
		else if ( e.getSource() instanceof Tile) {
			Tile t = (Tile)e.getSource();
			int returnedValue = -1;
			
			//Envoi du placement au serveur
			try { returnedValue = client.processMessage("FIELD:"+ t.getType()); } catch ( IOException ex) { ex.printStackTrace(); }
			
			if ( returnedValue == 127) {
				t.setItem('F',client.getNumPlayer());
				t.removeMouseListener(this);
				client.repaint();
			}
		}
		//Clic sur une zone du plateau
		else if ( e.getSource() == this.board) {
			this.isBoardSelected = true;
			//Position de la souris au clic
			this.cX = e.getX();
			this.cY = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//Clic relache sur le plateau
		if ( e.getSource() == this.board) {
			this.isBoardSelected = false;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//Si il s'agit du bouton d'envoi a la pioche ouverte
		if ( e.getSource() == selected.getToOpenPickButton() && selected.hasSelection()) {
			//Demande au serveur si il est possible d'envoyer a la pioche ouverte
			int returnedValue = -1;
			try {
				returnedValue = this.client.processMessage("SEND_TO_OPICK");
			} catch (IOException ex) { ex.printStackTrace(); }
			
			//Si le serveur a accepte le placement dans la pioche ouverte
			if ( returnedValue == 127 ) {
				this.openPick.addTile(new Tile(selected.getType(), 0));
				this.selected.setSelectedTile(null);
				this.removeAllPlacementTile();
			}
		}
		//Si il s'agit des bouttons de validation pour la hutte
		else if( e.getSource() instanceof JButton) {
			JButton button = (JButton)e.getSource();
			if ( button.getText().equals("Oui")) {
				try {
					this.client.processMessage("HUT:TRUE");
				} catch (IOException ex) { ex.printStackTrace(); }
				this.tiles.get(tiles.size()-1).setItem('H',client.getNumPlayer());
				this.hutWindow.setVisible(false);
				this.nbHut--;
				this.labelHut.setText("Nombre de hutte : " + nbHut);
				client.repaint();
			}
			else {
				try {
					this.client.processMessage("HUT:FALSE");
				} catch (IOException ex) { ex.printStackTrace(); }
				this.hutWindow.setVisible(false);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		//Deplacement du plateau
		if(arg0.getSource() == this.board && isBoardSelected) {
			arg0.translatePoint(arg0.getComponent().getLocation().x-cX, arg0.getComponent().getLocation().y-cY);
			//On ne change la position du plateau que si il ne sort pas de l'ecran
			if ( arg0.getX() < 0  && arg0.getY() < 0 &&
				 arg0.getX() + board.getWidth() > this.getWidth() &&
				 arg0.getY() + board.getHeight() > this.getHeight()) {
				this.board.setLocation(arg0.getX(), arg0.getY());
				client.repaint();
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
}