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
	
	public ClientMorpion(String ip, int port) {
		try {
			this.ip = InetAddress.getByName(ip);
			this.port = port;
			this.clientSocket = new DatagramSocket();
			
			//Récupération et envoi du nom et réception de l'id attribué au client
			processMessage("gsName_gCid");	
			
			String message = null;
			do {
				message = receiveMessage();
				processMessage(message);
			} while (!message.equals("END"));
			
			this.clientSocket.close();
		} 
		catch (UnknownHostException e) { }
		catch (IOException e) { }
	}
	
	private String receiveMessage() throws IOException {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		//Récupération du message
		this.clientSocket.receive(packet);
		
		return new String(packet.getData());
	}
	
	private boolean processMessage(String message) throws IOException {
		if (message.equals("getAndSendName")) {
			//Récupération et envoi du nom du client
			this.clientName = getUserEntry();
			sendMessage(this.clientName);
			this.clientId = receiveMessage().split(":")[0];
		}
		else if (message.equals("")) {
			
		}
		
		return true;
	}
	
	private void sendMessage(String message) throws IOException {
		byte[] data = new byte[1024];
		data = message.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		
		//Envoi du message
		this.clientSocket.send(packet);
	}
	
	private String getUserEntry() throws IOException {
		BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
		String entry = buff.readLine();
		return entry;
	}
	
	public static void main(String[] args) {
		if ( args.length == 2)
			new ClientMorpion(args[0], Integer.parseInt(args[1]));
	}
}
