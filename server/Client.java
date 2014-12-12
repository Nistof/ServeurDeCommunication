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
        this.id = generateId();
        try {
            this.writer = new PrintWriter(sock.getOutputStream());
            this.reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void send (String msg) {
        writer.print(msg);
    }
    
    public String receive () throws IOException {
        return reader.readLine();
    }

    private String generateId() {
        // TODO Auto-generated method stub
        return null;
    }
}
