package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.Fjorde.GridFjorde;
import client.Fjorde.Tile;

public class ClientFjorde extends JFrame implements ActionListener, MouseListener, FocusListener {
	private static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	private InetAddress 	ip;
	private short			port;
	private DatagramSocket 	clientSocket;
	private String			clientId;
	
	private JButton quitButton;
	private GridFjorde grid;
	
	private JPanel panel;
	private JPanel wait;
	
	private JPanel connexion;
	private JLabel connexionTitle;
	private JTextField serverIp;
	private JTextField serverPort;
	private JButton sendConnexion;
	
	private int numPlayer;
	private boolean isInit;
	private String tileName;

	/**
	 * Initialisation du client (Fenetre et dependances a la communication avec le serveur)
	 */
	public ClientFjorde() {
		this.setTitle("Fjorde");
		this.setLocation(0, 0);
		this.goFullScreen();
		this.numPlayer = 0;
		this.isInit = false;
		this.tileName = null;
		
		this.panel = new JPanel();
		this.panel.setLayout(null);
		this.panel.setBounds(0, 0, this.getWidth(), this.getHeight());
		
		//Panel de connexion
		this.connexion = new JPanel(null);
		this.connexion.setBounds(this.getWidth()/2-this.getWidth()/6, this.getHeight()/2-this.getHeight()/4, 
								 this.getWidth()/3, this.getWidth()/4);
		this.connexion.setOpaque(true);
		this.connexion.setBackground(new Color(0x7BC5DD));
		this.connexionTitle = new JLabel("Connexion");
		this.connexionTitle.setOpaque(true);
		this.connexionTitle.setBackground(new Color(0x9DD8DD));
		this.connexionTitle.setBounds(0, 0, connexion.getWidth(), 25);
		JLabel labelIp = new JLabel("IP du serveur :");
		labelIp.setBounds(connexion.getWidth()/2-connexion.getWidth()/8, 50, connexion.getWidth()/4, 25);
		JLabel labelPort = new JLabel("Port :");
		labelPort.setBounds(connexion.getWidth()/2-connexion.getWidth()/8, 125, connexion.getWidth()/4, 25);
		this.serverIp = new JTextField();
		this.serverIp.setBounds(connexion.getWidth()/2-connexion.getWidth()/6, 75, connexion.getWidth()/3, 25);
		this.serverPort = new JTextField();
		this.serverPort.setBounds(connexion.getWidth()/2-connexion.getWidth()/6, 150, connexion.getWidth()/3, 25);
		this.sendConnexion = new JButton("Connexion");
		this.sendConnexion.setBounds(connexion.getWidth()/2-connexion.getWidth()/8, 200, connexion.getWidth()/4, 25);
		this.sendConnexion.addActionListener(this);
		
		this.connexion.add(this.connexionTitle);
		this.connexion.add(labelIp);
		this.connexion.add(serverIp);
		this.connexion.add(labelPort);
		this.connexion.add(serverPort);
		this.connexion.add(sendConnexion);
		this.add(connexion);
		
		//----------------//
		// Bouton quitter
		this.quitButton = new JButton("Quitter");
		this.quitButton.setBounds( 0, 0, 100, 30);
		this.quitButton.addActionListener(this);
		this.quitButton.setAlignmentX(0.0f);
		this.quitButton.setAlignmentY(0.0f);
		this.panel.add(quitButton,0);
		this.add(panel);
		
		this.setVisible(true);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.repaint();
	}
	
	/**
	 * Mettre ou retirer l'attente au joueur
	 * @param isWaiting Etat de l'attente
	 */
	public void playerWait(boolean isWaiting) {
		this.wait.setVisible(isWaiting);
		this.repaint();
	}
	
	/**
	 * Ajoute les differents elements a la fenetre
	 */
	private void setFrame() {
		// Dimensions de la fenetre
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//Ecran d'attente
		JLabel textWait = new JLabel("En attente du serveur...");
		textWait.setForeground(Color.BLACK);
		textWait.setBounds( (this.getWidth()/2)-25, this.getHeight()/2-10,
							this.getWidth(), 20);
		
		this.wait = new JPanel();
		this.wait.setLayout(null);
		this.wait.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.wait.setOpaque(true);
		this.wait.setBackground(new Color(0xAA7BC5DD, true));
		this.wait.add(textWait);
		this.wait.setVisible(false);
		
		//Rendre le panel bloquant (Le joueur ne peut ainsi effectuer aucune action)
		this.wait.addFocusListener(this);
		this.wait.addMouseListener(this);
		this.panel.add(wait);
		
		// Plateau de jeu
		this.grid = new GridFjorde(this);
		this.grid.setBounds( 0, 0, screenSize.width, screenSize.height);
		this.grid.initGrid();
		this.grid.setAlignmentX(0.0f);
		this.grid.setAlignmentY(0.0f);
		this.panel.add(grid);
		
		this.repaint();
		this.isInit = true;
	}
	
	/**
	 * Initialise la connexion au serveur
	 * @return Vrai si la connexion a ete effectuee ( Avec un numero de port positif)
	 * @throws IOException
	 */
	private boolean initConnexion() throws IOException {	
		try {
			this.ip = InetAddress.getByName(this.serverIp.getText());
			this.port = Short.parseShort(this.serverPort.getText());
		} 
		catch (UnknownHostException ex) { throw new IOException(); }
		catch (NumberFormatException ex) { throw new IOException(); }
		
		this.clientSocket = new DatagramSocket();
				
		return (this.port > 0);
	}
	
	/**
	 * Passe la fenetre en mode plein ecran
	 */
	private void goFullScreen() {
		if ( !this.isUndecorated())
			this.setUndecorated(true);	
		device.setFullScreenWindow(this);
	}
	
	public void setTileName(String name) { this.tileName = name; }
	public boolean isInit() { return this.isInit; }
	
	//-------------------------------------//
	//    MESSAGES RECUS PAR LE CLIENT     //
	//-------------------------------------//
	//PLAYER:NUMERO                        //
	//HUT                                  //
	//POSET:NEIGHBOOR:POSITION:TILE:YES/NO //
	//FIELD:TILE                           //
	//PICK:TILE                            //
	//OPICK:YES/NO                         //
	//SEND_TO_OPICK:YES/NO                 //
	//PLACEMENTLIST                        //
	// * -> NEIGHBOOR:POSITION             //
	// * -> ENDLIST                        //
	//COLONIZATION                         //
	//END                                  //
	//WIN                                  //
	//EGALITE                              //
	//CANCEL                               //
	//-------------------------------------//
	
	/**
	 * Reception d'un message de la part du serveur
	 * @return Message envoye par le serveur
	 * @throws IOException
	 */
	public String receiveMessage() throws IOException {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		//Recuperation du message
		this.clientSocket.receive(packet);
		
		return new String(packet.getData());
	}
	
	
	//--------------------------------//
	// MESSAGES A ENVOYER AU SERVEUR  //
	//--------------------------------//
	//OPICK:TILE                      //
	//PICK                            //
	//SEND_TO_OPICK                   //
	//REQUEST_PLACEMENT:ORIENTATION   //
	//POSET:TILE:POSITION             //
	//HUT:YES ou HUT:NO               //
	//FIELD:TILE                      //
	//--------------------------------//
	
	/**
	 * Envoi d'un message au serveur
	 * @param message Message a envoyer
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException {
		byte[] data = new byte[1024];
		message = this.clientId + ":" + message;
		data = message.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		//Envoi du message
		this.clientSocket.send(packet);
	}
	
	/**
	 * Traitement d'un message
	 * @param message Message a traiter
	 * @throws IOException
	 */
	public int processMessage(String message) throws IOException {
		message = message.trim();
		String splStr[] = message.split(":");
		
		if (!splStr[0].equals(this.clientId))
			return -1;
		
		/* MESSAGES RECUS PAR LE CLIENT */
		if(splStr[1].equals("OK")) {
			this.clientId = splStr[0];
			return 127;
		}
		//PLAYER:NUMERO                        //
		else if (splStr.length == 3 && splStr[1].equals("PLAYER")) {
			this.numPlayer = Integer.parseInt(splStr[2]);
			return 127;
		}
		//START                                //
		else if (splStr.length == 2 && splStr[1].equals("START")) {
			this.playerWait(false);
			return 2;
		}
		//ENDTURN                              //
		else if (splStr.length == 2 && splStr[1].equals("ENDTURN")) {
			this.playerWait(true);
			return 3;
		}
		//HUT                                  //
		else if (splStr.length == 2 && splStr[1].equals("HUT")) {
			this.grid.setHutWindowVisible(true);
			return 127;
		}
		//POSET:TILE:ORIENTAION
		else if (splStr.length == 4 && splStr[1].equals("POSET")) {
			Tile t = new Tile(splStr[2], Integer.parseInt(splStr[3]));
			t.setLocation(grid.getBoardSize().width/2, grid.getBoardSize().height/2);
			return 127;
		}
		//POSET:NEIGHBOOR:POSITION:TILE:ORIENTAION:YES/NO //
		else if (splStr.length == 7 && splStr[1].equals("POSET")) {
			if ( this.wait.isVisible()) {
				Tile t = new Tile(splStr[4], Integer.parseInt(splStr[5]));
				t.setItem('H', numPlayer);
				Tile neighboor = grid.getTile(splStr[2]);
				t.setLocationWithTile(neighboor, Integer.parseInt(splStr[3]));
				this.grid.addTile(t);
			}
			return 127;
		}
		//FIELD:TILE                           //
		else if (splStr.length == 3 && splStr[1].equals("FIELD")) {
			int player = (!this.wait.isVisible()?this.numPlayer:(this.numPlayer+1)%2);
			Tile t = grid.getTile(splStr[2]);
			t.setItem('F', player);
			t.removeMouseListener(this);
			this.repaint();
			return 127;
		}
		//PICK:TILE                            //
		else if (splStr.length == 3 && splStr[1].equals("PICK")) {
			this.grid.pickedTile(splStr[2]);
			return 127;
		}
		//OPICK:YES/NO                         //
		else if (splStr.length == 3 && splStr[1].equals("OPICK")) {
			if (splStr[2].equals("YES")) {
				grid.pickFromOpenPick(tileName); //On place la tuile en tant que tuile selectionnee
				this.tileName = null;
				return 3;
			}
			else {
				return -1;
			}
		}
		//SEND_TO_OPICK:YES/NO                 //
		else if (splStr.length == 3 && splStr[1].equals("SEND_TO_OPICK")) {
			if (splStr[2].equals("YES")) {
				grid.sendToOpenPick();
				return 4;
			}
			else {
				return -1;
			}
		}
		//PLACEMENTLIST                        //
		// * -> NEIGHBOOR:POSITION             //
		// * -> ENDLIST                        //
		else if (splStr.length == 2 && splStr[1].equals("PLACEMENTLIST")) {
			do {
				String[] tile = this.receiveMessage().trim().split(":");
				if (tile.length == 2 && tile[1].equals("ENDLIST")) {
					return 127;
				}
				//Ajout des tuiles de placement
				else if (tile.length == 3) {
					this.grid.addPlacementTile(grid.getTile(tile[1]), Integer.parseInt(tile[2]));
				}
			} while (true);
		}
		//COLONIZATION                         //
		else if (splStr.length == 2 && splStr[1].equals("COLONIZATION")) {
			this.grid.setColonizationPhase();
			return 127;
		}
		//END:SCORE1:score:SCORE2:score        //
		else if (splStr.length == 6 && splStr[1].equals("WIN")) {
			//TODO : Traitement
			return 0;
		}
		//WIN:ID                               //
		else if (splStr.length == 3 && splStr[1].equals("WIN")) {
			return 1;
		}
		//EGALITE                              //
		//CANCEL                               //
		else if (splStr.length == 2 && (splStr[1].equals("EGALITE") || splStr[1].equals("CANCEL"))) {
			return 1;
		}
		
		return -1;
	}
	
	/**
	 * Retourne le numero du joueur (0 ou 1)
	 * @return Numero du joueur
	 */
	public int getNumPlayer() {
		return this.numPlayer;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.quitButton)
			System.exit(0);
		if (e.getSource() == this.sendConnexion) {
			boolean isConnected = false;
			try { isConnected = this.initConnexion(); } 
			catch (IOException ex) { isConnected = false; }
			
			if (isConnected) {
				this.connexion.setVisible(false);
				this.setFrame();
				this.playerWait(true);
			}
			else {
				this.connexionTitle.setText("Connexion : Saisie invalide");
				this.serverIp.setText("");
				this.serverPort.setText("");
			}
			
		}
	}

	@Override
	public void focusGained(FocusEvent arg0) {}

	@Override
	public void focusLost(FocusEvent arg0) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	public static void main(String[] args) {
		ClientFjorde cf = new ClientFjorde();
		while ( !cf.isInit()) {} //Attente que le client soit connecte
		
		try {
			cf.sendMessage("CLIENT");//Nom pour le serveur
			String message;
			boolean endGame = false;
			int returnedValue;
			do {
				message = cf.receiveMessage();
				returnedValue = cf.processMessage(message);
				switch (returnedValue) {
					case 1://WIN EGALITE CANCEL
						endGame = true;
						break;
				}
			} while (!endGame);
		}
		catch (IOException ex) {}
	}
}
