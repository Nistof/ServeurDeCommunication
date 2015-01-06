package server;

import java.net.InetAddress;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 12-03-2014
 */

public class Client {
    private InetAddress ip;
    private int port;
    private String cid; // Unique identifier
    
    public Client (InetAddress ip, int port) {
        this.cid = genererId();
        this.ip = ip;
        this.port = port;
    }

    /**
     * Génère un identifiant aléatoire pour le client
     * @return Identifiant du client
     */
    private String genererId() {
        StringBuilder id = new StringBuilder();
        int val;
        
        for ( int i = 0; i < 8; i++) {
            val = (int)(Math.random()*94+33);
            if((char)val == ':')
                i--;
            else
                id.append((char)val);
            
        }
        
        return id.toString();
    }

    /**
     * Donne l'identifiant du client
     * @return Identifiant du client
     */
    public String getCid() {
        return cid;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
