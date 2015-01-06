package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
    private DatagramSocket serverSocket;
    private byte[] receivedData;
    private byte[] sentData;
    private ServerProperties serverProperties;
    private HashMap<String, Client> clients;
    private IJeu jeu;
    private ConnectionManager cm;
    private boolean allowConnection;

    /**
     * Construit un nouvel objet de type Server s'occupant d'ouvrir le socket
     * serveur au port (5555 par défaut) défini dans le fichier "properties"
     * ainsi que le nombre maximum de connexion simultanées défini (4 par
     * défaut).
     */
    public Server(IJeu jeu) {
        this.serverProperties = new ServerProperties("properties.xml");
        this.clients = new HashMap<String, Client>();
        this.jeu = jeu;
        receivedData = new byte[1024];
        sentData = new byte[1024];
        try {
            this.serverSocket = new DatagramSocket(
                    serverProperties.getServerPort());
            this.serverSocket.setSoTimeout(serverProperties.getClientTimeout());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Serveur démarré à l'adresse : "
                + this.getIPAdress() + ":" + this.getServerPort());
        this.cm = new ConnectionManager(this);
    }

    /**
     * Lance le jeu.
     * @throws IOException 
     */
    public void launchServer() throws IOException {
        jeu.launchGame();
        this.close();
    }

    /**
     * Sauvegarde les propriétés et ferme le socket serveur ouvert à la création
     * de l'objet.
     * 
     * @throws IOException
     *             Le socket serveur est déjà fermé.
     */
    public void close() {
        this.serverProperties.save();
        this.serverSocket.close();
    }

    /**
     * Renvoie vrai si le socket serveur est fermé.
     * 
     * @return vrai si le socket serveur est fermé, faux sinon.
     */
    public boolean isClosed() {
        return this.serverSocket.isClosed();
    }

    /**
     * Donne l'adresse IP de la machine sur laquelle le serveur est démarré.
     * 
     * @return adresse IP du serveur.
     */
    public InetAddress getIPAdress() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Donne le port utilisé par le serveur.
     * 
     * @return port du serveur.
     */
    public int getServerPort() {
        return serverProperties.getServerPort();
    }

    /**
     * Transmet un message à un client
     * 
     * @param id
     *            Identifiant du client
     * @param msg
     *            Message à transmettre au client
     * @throws IOException 
     */
    public void sendToClient(String id, String msg) throws IOException {
        Client c = clients.get(id);
        String data = id + ":" + msg;
        sentData = data.getBytes();
        serverSocket.send(new DatagramPacket(sentData, sentData.length, c.getIp(), c.getPort()));
    }

    /**
     * Attend la réception public void disalowConnections() {
     * cm.toggleConnect(); }d'un message d'un client et renvoie une chaine de la
     * forme id:message
     * 
     * @param id
     *            Identifiant du client
     * @return "id:Message envoyé par le client"
     * @throws IOException
     *             Si il est impossible de recevoir le message (Flux d'entrée
     *             fermé)
     */
    public String receive() throws IOException {
        DatagramPacket dp = receivePacket();
        // Attente avant la réception d'un message
        try {
            Thread.sleep(serverProperties.getTimeWait());
        } catch (InterruptedException e) {

        }
        return new String(dp.getData());
    }

    private DatagramPacket receivePacket() throws IOException {
        DatagramPacket dp = new DatagramPacket(receivedData, receivedData.length);
        serverSocket.receive(dp);
        return dp;
    }

    /**
     * Transmet un message à tout les clients
     * 
     * @param msg
     *            Message à transmettre aux clients
     * @throws IOException 
     */
    public void sendToAllClient(String msg) throws IOException {
        for (String k : clients.keySet()) {
            Client c = clients.get(k);
            sendToClient(c.getCid(),msg);
        }
    }

    /**
     * Ajoute un client à partir d'un socket renvoyé par le ConnectionManager
     * 
     * @param datagramPacket
     * @param socket
     *            Socket du client
     * @throws IOException
     *             Flux d'entrée fermé
     */
    public void add(DatagramPacket datagramPacket) throws IOException {
        Client c = null;
        boolean permitAdd = true;
        for (String cid : clients.keySet()) {
            c = clients.get(cid);
            if (c.getIp().equals(datagramPacket.getAddress())
                    && c.getPort() == datagramPacket.getPort()) {
                permitAdd = false;
                break;
            }
        }
        if (permitAdd) {
            c = new Client(datagramPacket.getAddress(),
                    datagramPacket.getPort());
            clients.put(c.getCid(), c);
            System.out.println(new String(datagramPacket.getData()));
            jeu.add(new String(datagramPacket.getData()), c.getCid());
            sendToClient(c.getCid(), ":OK");
        }
        else {
            sendToClient(c.getCid(), ":ERROR");
        }
    }                // TODO Auto-generated catch block

    public void disalowConnections() {
        allowConnection = false;
    }
    
    private class ConnectionManager extends Thread {
        private Server server;
        
        public ConnectionManager (Server server) {
            this.server = server;
            start();
        }
        
        public void run () {
            allowConnection = true;
            while (clients.size() < serverProperties.getClientMax() && allowConnection) {
                try {
                    server.add(server.receivePacket());
                } catch (Exception e) {
                    e.printStackTrace();
                    server.disalowConnections();
                }
            }
            try {
                server.launchServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
