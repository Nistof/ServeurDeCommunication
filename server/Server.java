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
	
	/**
	 * Construit un nouvel objet de type Server s'occupant d'ouvrir
	 * le socket serveur au port (5555 par défaut) défini dans le fichier "properties"
	 * ainsi que le nombre maximum de connexion simultanées défini (4 par défaut).
	 */
	public Server() {
		this.serverProperties = new ServerProperties("properties");
		
		try {
			this.serverSocket = new ServerSocket( serverProperties.getServerPort(), serverProperties.getClientMax() );
		} catch ( IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * Sauvegarde les propriétés et ferme le socket serveur ouvert à la création de l'objet.
	 * @throws IOException Le socket serveur est déjà fermé.
	 */
	public void close() throws IOException {
		this.serverProperties.save();
		this.serverSocket.close();
	}
	
	/**
	 * Renvoie vrai si le socket serveur est fermé.
	 * @return vrai si le socket serveur est fermé, faux sinon.
	 */
	public boolean isClosed() {
		return this.serverSocket.isClosed();
	}
	
	/**
	 * Donne l'adresse IP de la machine sur laquelle le serveur est démarré.
	 * @return adresse IP du serveur.
	 */
	public InetAddress getIPAdress() {
		try {
			return InetAddress.getLocalHost();
		} catch ( UnknownHostException e ) { e.printStackTrace(); }
		return null;
	}
	
	/**
	 * Donne le port utilisé par le serveur.
	 * @return port du serveur.
	 */
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
