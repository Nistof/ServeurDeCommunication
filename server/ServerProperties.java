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
	
	/**
	 * Défini les propriétés initiales du serveur. Celles-ci sont 
	 * soit récupérées dans le fichier dont le chemin est passé 
	 * en paramètre, soit un nouveau est fichier "properties" 
	 * est créé et les valeurs par défaut sont attribuées
	 * @param properties chemin du fichier de propriétés
	 */
	public ServerProperties (String properties) {
		this.clientTimeout = DEFAULT_CLIENT_TIMEOUT;
		this.clientMax = DEFAULT_CLIENT_MAX;
		this.serverPort = DEFAULT_SERVER_PORT;
		String[] prop;
		
		//Si le fichier n'existe pas ou qu'aucun fichier
		//n'a été passé en paramètre
		if ( properties == null )
			properties = "properties";
		
		File f = new File(properties);
		if ( !f.exists() ) {
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
	
	/**
	 * Sauvegarde les données dans le fichier de propriétés
	 */
	public void save () {
		String prop = "CLIENT_MAX=" + clientMax + "\n" +
					  "CLIENT_TIMEOUT=" + clientTimeout + "\n" +
					  "SERVER_PORT=" + serverPort;
		
		try {
			FileWriter fw = new FileWriter(new File("properties"), false);
			fw.write(prop);
			fw.flush();
			fw.close();
		} catch ( IOException e ) { e.printStackTrace(); }
	}

	/**
	 * Donne le temps d'attente avant de considérer qu'un client ne
	 * répond pas.
	 * @return Temps d'attente avant de déconnecter un client ne répondant pas.
	 */
	public int getClientTimeout() {
		return clientTimeout;
	}

	/**
	 * Donne le nombre de client maximum accepté simultanément
	 * @return Nombre de client maximum
	 */
	public int getClientMax() {
		return clientMax;
	}

	/**
	 * Donne le port sur lequel le serveur doit pouvoir être accessible.
	 * @return port serveur.
	 */
	public int getServerPort() {
		return serverPort;
	}
}