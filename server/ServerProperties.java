package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class ServerProperties {
	public static final int DEFAULT_CLIENT_MAX = 4;
	public static final int DEFAULT_CLIENT_TIMEOUT = 3000;
	public static final int DEFAULT_SERVER_PORT = 5555;
	
	private String properties; //Chemin du fichier de propriétés
	
	private int clientTimeout;
	private int clientMax;
	private int serverPort;
		
	public ServerProperties (String properties) {
		this.clientTimeout = DEFAULT_CLIENT_TIMEOUT;
		this.clientMax = DEFAULT_CLIENT_MAX;
		this.serverPort = DEFAULT_SERVER_PORT;
		String[] prop;
		
		//Si aucun chemin n'a été passé en paramètre
		if ( properties == null )
			this.properties = "properties";
		else
			this.properties = properties;
		
		//Si le fichier n'existe pas
		File f = new File(properties);
		if ( properties == null || !f.exists() ) {
			this.properties = "properties";
			try {
				f.createNewFile();
			} catch ( IOException e ) { e.printStackTrace(); }
		}
		
		//Lecture du fichier et passage des valeurs
		try {
			Scanner sc = new Scanner(new File(properties));
			
			while (sc.hasNextLine()) {
				prop = sc.nextLine().split("=");
				
				if ( prop[0].equals("CLIENT_MAX"))
					clientMax = Integer.parseInt(prop[1]);
				else if ( prop[0].equals("CLIENT_TIMEOUT"))
					clientTimeout = Integer.parseInt(prop[1]);
				else if ( prop[0].equals("SERVER_PORT"))
					serverPort = Integer.parseInt(prop[1]);
			}
			
			sc.close();
		} catch ( FileNotFoundException e) { e.printStackTrace(); }
	}
	
	public void save () {
		String prop = "CLIENT_MAX=" + clientMax + "\n" +
					  "CLIENT_TIMEOUT=" + clientTimeout + "\n" +
					  "SERVER_PORT=" + serverPort;
		
		try {
			FileWriter fw = new FileWriter(new File("properties", true));
			
		} catch ( IOException e ) { e.printStackTrace(); }
	}

	public int getClientTimeout() {
		return clientTimeout;
	}

	public int getClientMax() {
		return clientMax;
	}

	public int getServerPort() {
		return serverPort;
	}
}