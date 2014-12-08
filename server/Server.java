package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 12/03/2014
 */
public class Server {
	private ServerSocket serverSocket;
	private ServerProperties serverProperties;
	
	public Server() {
		this.serverProperties = new ServerProperties("properties");
		
		try {
			this.serverSocket = new ServerSocket( serverProperties.getServerPort(), serverProperties.getClientMax() );
		} catch ( IOException e) { e.printStackTrace(); }
	}
	
	public void close() throws IOException {
		this.serverProperties.save();
		this.serverSocket.close();
	}
	
	public boolean isClosed() {
		return this.serverSocket.isClosed();
	}
	
	public InetAddress getIPAdress() {
		try {
			return InetAddress.getLocalHost();
		} catch ( UnknownHostException e ) { e.printStackTrace(); }
		return null;
	}
	
	public int getServerPort() {
		return serverProperties.getServerPort();
	}
	
	public static void main(String[] args) {
		System.out.println("Démarrage du serveur ...");
		Server serv = new Server();
		System.out.println("Serveur démarré à l'adresse : " + serv.getIPAdress() + ":" + serv.getServerPort());	
		try { serv.close(); } catch ( IOException e ) {}
		System.out.println("Serveur fermé");
	}
}
