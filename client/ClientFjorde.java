package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.OverlayLayout;

import client.Fjorde.GridFjorde;

public class ClientFjorde extends JFrame implements ActionListener {
	private InetAddress 	ip;
	private int				port;
	private DatagramSocket 	clientSocket;
	private String			clientId;
	
	private JButton quitButton;
	private GridFjorde grid;
	
	private JPanel panel;

	public ClientFjorde() {
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		
		this.panel = new JPanel();
		this.panel.setLayout(new OverlayLayout(panel));
		
		this.setTitle("Fjorde");
		this.setLocation(0, 0);
		this.goFullScreen();
		this.setFrame();
		c.add(panel, BorderLayout.CENTER);
		
		this.setVisible(true);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Ajoute les différents éléments à la fenêtre
	 */
	private void setFrame() {
		// Dimensions de la fenêtre
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
	 * Passe la fenêtre en mode plein écran
	 */
	private void goFullScreen() {
		if ( !this.isUndecorated())
			this.setUndecorated(true);	
		this.setExtendedState(MAXIMIZED_BOTH);
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.quitButton)
			System.exit(0);
	}
	
	/**
	 * Rï¿½ception d'un message de la part du serveur
	 * @return Message envoyï¿½ par le serveur
	 * @throws IOException
	 */
	public String receiveMessage() throws IOException {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		//Rï¿½cupï¿½ration du message
		this.clientSocket.receive(packet);
		
		return new String(packet.getData());
	}
	
	/**
	 * Envoi d'un message au serveur
	 * @param message Message ï¿½ envoyer
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
	 * @param message Message ï¿½ traiter
	 * @throws IOException
	 */
	public boolean processMessage(String message) throws IOException {
		message = message.trim();
		//OPICK:NOM_PIECE
		//PICK
		
		if (message.split(":")[0].equals("OPICK")) {
			System.out.println("OpenPick -> send");
		}
		else if (message.split(":")[0].equals("PICK")) {
			System.out.println("ClosePick -> send");
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		new ClientFjorde();
	}
}
