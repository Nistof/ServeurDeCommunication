package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

    public void envoyer (String msg) {
        this.writer.print(msg);
    }
    
    public String recevoir () throws IOException {
        return this.reader.readLine();
    }

    private String genererId() {
        StringBuilder id = new StringBuilder();
        int val;
        char c;
        
        for ( int i = 0; i < 8; i++) {
            val = (int)(Math.random()*95+33);
            id.append((char)val);
            
        }
        
        System.out.println(id);
        
        return id.toString();
    }

    public String getId() {
        return id;
    }
    
    public static void main(String[] args) {
        Client c =  new Client(null);
        
    }
}
