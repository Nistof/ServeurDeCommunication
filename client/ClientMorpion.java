package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientMorpion {
	private InetAddress 	ip;
	private int				port;
	private DatagramSocket 	clientSocket;
	private String			clientName;
	private String			clientId;
	
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
		else if (message.equals(clientId + ":START")) {				//Demande de saisie par le jeu
			sendMessage(this.clientId + ":" + getUserEntry());
		}
		else if (message.equals(clientId + ":ERROR")) {				//Si la saisie est incorrecte
			System.out.println("Saisie incorrecte");
		}
		else { 
			String[] splStr = message.split(":");
			if ( splStr.length == 2 && splStr[1].equals("WIN")) {	//Si un joueur a gagné
				if ( splStr[0].equals(clientId)) {
					System.out.println("Vous avez gagné.");
				} else {
					System.out.println("Vous avez perdu.");
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
	
	public static void main(String[] args) {
		if ( args.length == 2) {
			ClientMorpion cm = new ClientMorpion(args[0], Integer.parseInt(args[1]));
			
			try {
				//Récupération et envoi du nom et réception de l'id attribué au client
				cm.processMessage("gsName_gCid");	
	
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
