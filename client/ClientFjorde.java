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
import javax.swing.OverlayLayout;

import client.Fjorde.GridFjorde;

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

	/**
	 * Initialisation du client (Fenetre et dependances a la communication avec le serveur)
	 */
	public ClientFjorde() {
		this.setTitle("Fjorde");
		this.setLocation(0, 0);
		this.goFullScreen();
		this.numPlayer = 1;
		
		this.panel = new JPanel();
		this.panel.setLayout(new OverlayLayout(panel));
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
		
		//----------------
		// Bouton quitter
		this.quitButton = new JButton("Quitter");
		this.quitButton.addActionListener(this);
		this.quitButton.setAlignmentX(0.0f);
		this.quitButton.setAlignmentY(0.0f);
		this.panel.add(quitButton,0);
		this.add(panel);
		
		this.setVisible(true);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		//this.playerWait(true);
		this.repaint();
	}
	
	/**
	 * Mettre ou retirer l'attente au joueur
	 * @param isWaiting Etat de l'attente
	 */
	public void playerWait(boolean isWaiting) {
		if (isWaiting) {
			this.add(wait,0);
			this.remove(quitButton);
			this.add(quitButton, 0);
			this.repaint();
		} else {
			this.remove(wait);
		}
	}
	
	/**
	 * Ajoute les differents elements a la fenetre
	 */
	private void setFrame() {
		// Dimensions de la fenetre
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//Ecran d'attente
		JLabel textWait = new JLabel("En attente du serveur...");
		textWait.setForeground(Color.black);
		textWait.setBounds( (this.getWidth()/2)-25, this.getHeight()/2-10,
							this.getWidth(), 20);
		
		this.wait = new JPanel();
		this.wait.setLayout(null);
		this.wait.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.wait.setOpaque(true);
		this.wait.setBackground(new Color(0xAA7BC5DD, true));
		this.wait.add(textWait);
		
		//Rendre le panel bloquant (Le joueur ne peut ainsi effectuer aucune action)
		this.wait.addFocusListener(this);
		this.wait.addMouseListener(this);
		
		// Plateau de jeu
		this.grid = new GridFjorde(this);
		this.grid.setBounds( 0, 0, screenSize.width, screenSize.height);
		this.grid.initGrid();
		this.grid.setAlignmentX(0.0f);
		this.grid.setAlignmentY(0.0f);
		this.panel.add(grid);
		
		this.repaint();
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
	
	/**
	 * Envoi d'un message au serveur
	 * @param message Message a envoyer
	 * @throws IOException
	 */
	private void sendMessage(String message) throws IOException {
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
		String splStr[] = message.trim().split(":");
		
		/* MESSAGES RECUS PAR LE CLIENT */
		//START
		if (splStr.length == 2 && splStr[1].equals("START")) {
			this.playerWait(false);
			return 0;
		}
		//ENDTURN
		else if (splStr.length == 2 && splStr[1].equals("ENDTURN")) {
			this.playerWait(true);
			return 0;
		}
		//HUT
		else if (splStr.length == 2 && splStr[1].equals("HUT")) {
			this.grid.setHutWindowVisible(true);
			return 0;
		}
		//POSET
		else if (splStr.length == 5 && splStr[1].equals("POSET")) {
			
			return 0;
		}
		/* MESSAGES A ENVOYER AU SERVEUR */
		//OPICK:NOM_PIECE
		else if (splStr.length == 2 && splStr[0].equals("OPICK")) {
			System.out.println("OpenPick -> send");
			//return -1; //Erreur d'entree
			return 127; //OK
		}
		//PICK
		else if (splStr.length == 1 && splStr[0].equals("PICK")) {
			System.out.println("ClosePick -> send");
			//return -1; //Erreur d'entree
			return 127; //OK
		}
		//SEND_TO_OPICK
		else if (splStr.length == 1 && splStr[0].equals("SEND_TO_OPICK")) {
			System.out.println("SelectedTile -> send");
			//return -1; //Erreur d'entree
			return 127; //OK
		}
		//POSET
		else if (splStr.length == 3 && splStr[0].equals("POSET")) {
			return 127;
		}
		//HUT
		else if (splStr.length == 2 && splStr[0].equals("HUT")) {
			return 127;
		}
		//FIELD
		else if (splStr.length == 2 && splStr[0].equals("FIELD")) {
			return 127;
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
		new ClientFjorde();
	}
}
