package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;

import jeu.IJeu;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 12-03-2014
 */

public class Server {
	private ServerSocket serverSocket;
	private ServerProperties serverProperties;
	private HashMap<String, Client> clients;
	private IJeu jeu;
    private ConnectionManager cm;
	
	/**
	 * Construit un nouvel objet de type Server s'occupant d'ouvrir
	 * le socket serveur au port (5555 par défaut) défini dans le fichier "properties"
	 * ainsi que le nombre maximum de connexion simultanées défini (4 par défaut).
	 */
	public Server(IJeu jeu) {
		this.serverProperties = new ServerProperties("properties.xml");
		this.clients = new HashMap<String, Client>();
		this.jeu = jeu;
		try {
			this.serverSocket = new ServerSocket( serverProperties.getServerPort(), serverProperties.getClientMax() );
			this.serverSocket.setSoTimeout(serverProperties.getClientTimeout());
		} catch ( IOException e) { e.printStackTrace(); }
		
	    this.cm = new ConnectionManager(this);
	    System.out.println("Serveur démarré à l'adresse : " + this.getIPAdress() + ":" + this.getServerPort()); 
	}
	
	/**
	 * Lance le jeu.
	 */
	public void launchServer () {
	    jeu.launchGame();
	    this.close();
	}
	
	/**
     * Sauvegarde les propriétés et ferme le socket serveur ouvert à la création de l'objet.
     * @throws IOException Le socket serveur est déjà fermé.
     */
	public void close() {
		this.serverProperties.save();
		try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
	
	/**
	 * Transmet un message à un client
	 * @param id Identifiant du client
	 * @param msg Message à transmettre au client
	 */
	public void sendToClient (String id, String msg) {
	    clients.get(id).send(msg);
	}
	
	/**
	 * Attend la réception d'un message d'un client et renvoie une chaine de la forme
	 * id:message
	 * @param id Identifiant du client
	 * @return "id:Message envoyé par le client"
	 * @throws IOException Si il est impossible de recevoir le message (Flux d'entrée fermé)
	 */
	public String receiveFromClient (String id) throws IOException {
	    String msg = "";
	    
	    //Attente avant la réception d'un message
	    try {
            Thread.sleep(serverProperties.getTimeWait());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	    
	    //Récupération d'un message
	    msg = clients.get(id).receive();
	    
	    //Si le message est null (Le client est déconnecté)
	    if(msg == null)
	        msg = ":disconnected";
	        
	    return  id+msg;
	}
	
	/**
	 * Transmet un message à tout les clients
	 * @param msg Message à transmettre aux clients
	 */
	public void sendToAllClient (String msg) {
	    for(String c : clients.keySet()) {
	        clients.get(c).send(msg);
	    }
	}
	
	/**
	 * Ajoute un client à partir d'un socket renvoyé par le ConnectionManager
	 * @param socket Socket du client
	 * @throws IOException Flux d'entrée fermé
	 */
    public void add(Socket socket) throws IOException {
        Client c = new Client(socket);
        String name = "";
        clients.put(c.getId(), c);
        sendToClient(c.getId(), c.getId()+":name");
        name = receiveFromClient(c.getId());
        jeu.add(name, c.getId());
        sendToClient(c.getId(), c.getId()+":ok");
    }
    
    /**
     * Interdire toutes les nouvelles connexions au serveur.
     */
    public void disalowConnections() {
        cm.toggleConnect();
    }
    
    private class ConnectionManager extends Thread {
        private Server server;
        private boolean connect;
        
        ConnectionManager (Server server) {
            this.server = server;
            this.connect = true;
            start();
        }
        
        /**
         * Autorise ou non les nouvelles connexions
         */
        public void toggleConnect() {
            connect = !connect;
        }

        public void run () {
            while(connect) {
                try {
                    Socket tmp = server.serverSocket.accept();
                    server.add(tmp);
                } catch (SocketTimeoutException s) {
                    toggleConnect();
                    launchServer();
                } catch (IOException i) {
                    i.printStackTrace();
                } 
            }
            server.launchServer();
        }
    }
}
