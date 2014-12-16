package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class ServerProperties {
	public static final int DEFAULT_CLIENT_MAX = 4;
	public static final int DEFAULT_CLIENT_TIMEOUT = 3000;
	public static final int DEFAULT_CLIENT_DELAY = 10000;
	public static final int DEFAULT_TIME_WAIT = 2000;
	public static final int DEFAULT_SERVER_PORT = 5555;
	
	private String properties; //Chemin du fichier de propriétés
	
	private int clientTimeout;
	private int clientMax;
	private int clientDelay;
	private int timeWait;
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
		this.clientDelay = DEFAULT_CLIENT_DELAY;
	    this.timeWait = DEFAULT_TIME_WAIT;
		this.serverPort = DEFAULT_SERVER_PORT;
		String prop, value;
		
		//Si le fichier n'existe pas ou qu'aucun fichier
		//n'a été passé en paramètre
		if ( properties == null )
			properties = "properties.xml";
		
		File f = new File(properties);
		if ( !f.exists() ) {
			this.properties = properties;
			try {
				f.createNewFile();
			} catch ( IOException e ) { e.printStackTrace(); }
		}
		
		//Lecture du fichier et passage des valeurs
		try {
			Scanner sc = new Scanner(new File(properties));

			while (sc.hasNextLine()) {
				prop = sc.nextLine();
				if ( prop.contains("property")) {
				    value = prop.substring( prop.indexOf(">")+1, prop.indexOf("<", prop.indexOf(">")));
				    prop = prop.substring( prop.indexOf("name=\"")+6, prop.indexOf("\"", prop.indexOf("\"")+1));
				} else {
				    value = "";
				}
				
				if ( prop.equals("CLIENT_MAX"))
					clientMax = Integer.parseInt(value);
				else if ( prop.equals("CLIENT_TIMEOUT"))
					clientTimeout = Integer.parseInt(value);
				else if ( prop.equals("CLIENT_DELAY"))
				    clientDelay = Integer.parseInt(value);
				else if ( prop.equals("TIME_WAIT"))
				    timeWait = Integer.parseInt(value);
				else if ( prop.equals("SERVER_PORT"))
					serverPort = Integer.parseInt(value);
			}
			
			sc.close();
		} catch ( FileNotFoundException e) { e.printStackTrace(); }
	}
	
	/**
	 * Sauvegarde les données dans le fichier de propriétés
	 */
	public void save () {
		String prop = "<properties>\n" +
                	  "\t<property name=\"CLIENT_MAX\">" + clientMax + "</property>\n" +
                	  "\t<property name=\"CLIENT_TIMEOUT\">" + clientTimeout + "</property>\n" +
                	  "\t<property name=\"CLIENT_DELAY\">" + clientDelay + "</property>\n" +
                	  "\t<property name=\"TIME_WAIT\">" + timeWait + "</property>\n" +
                	  "\t<property name=\"SERVER_PORT\">" + serverPort + "</property>\n" +
                	  "</properties>";
		
		try {
			FileWriter fw = new FileWriter(new File(properties), false);
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

    public String getProperties() {
        return properties;
    }

    public int getClientDelay() {
        return clientDelay;
    }

    public int getTimeWait() {
        return timeWait;
    }
	
	
}