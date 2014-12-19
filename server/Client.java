package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
    private Socket sock; 
    private String id; // Unique identifier
    private PrintWriter writer;
    private BufferedReader reader;
    
    
    public Client (Socket sock) {
        this.sock = sock;
        this.id = genererId();
        try {
            this.writer = new PrintWriter(this.sock.getOutputStream());
            this.reader = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Envoi d'un message au client
     * @param msg Message à envoyer
     */
    public void send (String msg) {
        this.writer.print(msg);
        this.writer.flush();
    }
    
    /**
     * Recevoir un message du client
     * @return Message du client
     * @throws IOException Flux d'entrée fermé
     */
    public String receive () throws IOException {
        return this.reader.readLine();
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
    public String getId() {
        return id;
    }
    
}
