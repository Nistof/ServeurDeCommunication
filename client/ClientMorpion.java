package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientMorpion {
	private final static char[] SYMBOLES = { 'X', 'Y'};
	
	private InetAddress 	ip;
	private int				port;
	private DatagramSocket 	clientSocket;
	private String			clientName;
	private String			clientId;
	
	private char[][]		plateau;
	private int				symbole;
	
	/**
	 * Constructeur du ClientMorpion
	 * @param ip	IP du serveur
	 * @param port	Port où le serveur écoute
	 */
	public ClientMorpion(String ip, int port) {
		try {
			this.ip = InetAddress.getByName(ip);
			this.port = port;
			this.clientSocket = new DatagramSocket();
			this.plateau = new char[3][3];
			this.symbole = -1;
		} 
		catch (UnknownHostException e) { System.out.println("Connexion à l'hote impossible!"); }
		catch (IOException e) { System.out.println("Erreur de flux"); }
	}
	
	/**
	 * Réception d'un message de la part du serveur
	 * @return Message envoyé par le serveur
	 * @throws IOException
	 */
	public String receiveMessage() throws IOException {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		//Récupération du message
		this.clientSocket.receive(packet);
		
		return new String(packet.getData());
	}
	
	/**
	 * Traitement d'un message
	 * @param message Message à traiter
	 * @throws IOException
	 */
	public void processMessage(String message) throws IOException {
		if (message.equals("getAndSendName")) {
			//Récupération et envoi du nom du client
			this.clientName = getUserEntry();
			sendMessage(this.clientName);
			
			//Récupération de l'id du client
			this.clientId = receiveMessage().split(":")[0];
		}
		else if (message.equals(clientId + ":START")) {		//Demande de saisie par le jeu
			//Symbole du joueur si non défini
			if ( symbole == -1 ) {
				boolean isFirstPlayer = true;
				
				for (int i = 0; i < 3; i++)
					for (int j = 0; j < 3; j++)
						if (plateau[i][j] != ' ')
							isFirstPlayer = false;
				
				this.symbole = (isFirstPlayer)?0:1;
			}
			
			sendMessage(this.clientId + ":" + getUserEntry());
		}
		else if (message.equals(clientId + ":ERROR")) {		//Si la saisie est incorrecte
			System.out.println("Saisie incorrecte");
		}
		else { 
			String[] splStr = message.split(":");
			if( splStr.length == 2) { //Placement des symboles sur le plateau
				int x = Integer.parseInt(splStr[1].split(",")[0].trim());
				int y = Integer.parseInt(splStr[1].split(",")[1].trim());
				
				if ( splStr[0].equals(clientId)) {
					this.plateau[x][y] = SYMBOLES[symbole];
				} else {
					this.plateau[x][y] = SYMBOLES[(symbole+1)%2];
				}
				
				System.out.println(this);
			} else if ( splStr.length == 3 && splStr[2].equals("WIN")) {	//Si un joueur a gagné
				if ( splStr[1].equals(clientId)) {
					System.out.println(SYMBOLES[symbole] + " gagne!");
				} else {
					System.out.println(SYMBOLES[(symbole+1)%2] + " gagne!");
				}
			}
		}
	}
	
	/**
	 * Envoi d'un message au serveur
	 * @param message Message à envoyer
	 * @throws IOException
	 */
	private void sendMessage(String message) throws IOException {
		byte[] data = new byte[1024];
		data = message.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		
		//Envoi du message
		this.clientSocket.send(packet);
	}
	
	/**
	 * Récupération d'une entrée utilisateur
	 * @return Chaine saisie par l'utilisateur
	 * @throws IOException 
	 */
	private String getUserEntry() throws IOException {
		BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
		String entry = buff.readLine();
		return entry;
	}
	
	/**
	 * Renvoie l'id du client
	 * @return id du client
	 */
	public String getClientId () { return this.clientId; }
	
	/**
	 * Ferme le socket client
	 * @throws IOException
	 */
	public void close () throws IOException {
		this.clientSocket.close();
	}
	
	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++)
				str += plateau[i][j];
			str += "\n";
		}
		
		return str;
	}

	public static void main(String[] args) {
		if ( args.length == 2) {
			ClientMorpion cm = new ClientMorpion(args[0], Integer.parseInt(args[1]));
			
			try {
				//Récupération et envoi du nom et réception de l'id attribué au client
				cm.processMessage("gsName_gCid");	
				System.out.println(cm);
				
				String message = null;
				do {
					message = cm.receiveMessage();
					cm.processMessage(message);
				} while (!message.equals(cm.getClientId() + ":END") || !message.equals(cm.getClientId() + ":CANCEL"));
				
				cm.close();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
}
