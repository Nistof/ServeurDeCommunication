package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

import client.Fjorde.GridFjorde;

public class ClientFjorde extends JFrame implements ActionListener {
	private static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	
	private InetAddress 	ip;
	private int				port;
	private DatagramSocket 	clientSocket;
	private String			clientId;
	
	private JButton quitButton;
	private GridFjorde grid;
	
	private JPanel panel;
	private JPanel wait;

	/**
	 * Initialisation du client (Fenetre et dependances a la communication avec le serveur)
	 */
	public ClientFjorde() {
		this.goFullScreen();
		
		this.panel = new JPanel();
		this.panel.setLayout(new OverlayLayout(panel));
		this.panel.setBounds(0, 0, this.getWidth(), this.getHeight());
		
		//Ecran d'attente
		JLabel textWait = new JLabel("En attente du serveur...");
		textWait.setForeground(Color.black);
		textWait.setBounds( (this.getWidth()/2)-25, this.getHeight()/2-10,
							this.getWidth(), 20);
		
		this.wait = new JPanel();
		this.wait.setLayout(null);
		this.wait.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.wait.setOpaque(true);
		this.wait.setBackground(new Color(0x7BC5DDAA, true));
		this.wait.add(textWait);
		
		this.setTitle("Fjorde");
		this.setLocation(0, 0);
		this.setFrame();
		this.add(panel);
		
		this.setVisible(true);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.playerWait(true);
	}
	
	public void playerWait(boolean isWaiting) {
		if (isWaiting) {
			this.add(wait,0);
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
		
		// Bouton quitter
		this.quitButton = new JButton("Quitter");
		this.quitButton.addActionListener(this);
		this.quitButton.setAlignmentX(0.0f);
		this.quitButton.setAlignmentY(0.0f);
		this.panel.add(quitButton);
		
		// Plateau de jeu
		this.grid = new GridFjorde(this);
		this.grid.setBounds( 0, 0, screenSize.width, screenSize.height);
		this.grid.initGrid();
		this.grid.setAlignmentX(0.0f);
		this.grid.setAlignmentY(0.0f);
		this.panel.add(grid);
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
		
		//START
		if (splStr.length == 2 && splStr[1].equals("START")) {
			return 0;
		}
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
		
		return 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.quitButton)
			System.exit(0);
	}
	
	public static void main(String[] args) {
		new ClientFjorde();
	}
}
