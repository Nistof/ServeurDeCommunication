package server;

public enum Signal {
	//Signaux de la partie métier
	BUSINESS_KEEP_INFOS 		( 0x00 ),	//Demande des clients connectés
	BUSINESS_MSG 				( 0x01 ),	//Envoi d'un message
	BUSINESS_GIVE_HAND			( 0x02 ),	//Ecoute d'un client en particulier
	BUSINESS_CONNECTION_ALLOWED	( 0x05 ),	//Autorise les clients à se connecter
	BUSINESS_CONNECTION_DENIED	( 0x06 ),	//Interdire les clients de se connecter
	
	//Signaux des clients
	CLIENT_MSG 					( 0x10),	//Envoi d'un message
	CLIENT_TIMEOUT 				( 0x11, "Client : Timeout : Disconnected" ),	//Client ne répondant pas
	
	//Signaux du serveur
	SERVER_SEND_INFOS			( 0x20 ),	//Réponse à un signal "BUSINESS_KEEP_INFOS" si il y a des clients connectés
	SERVER_SEND_CLIENT_ID		( 0x21 ),	//ID attribué au client par le serveur
	SERVER_MSG_TRANSMITTED		( 0x22 ),	//Envoyé pour confirmer que le message a bien été transmit
	SERVER_WRONG_CLIENT_ID		( 0x23, "Server : Wrong client id : Message not transmitted"),	//Utilisation d'un id de client inexistant
	SERVER_WRONG_SIGNAL			( 0x24, "Server : Signal rejected : Permission denied" ), //Utilisation d'un signal non autorisé
	SERVER_WRONG_MSG 			( 0x25, "Server : Wrong message" ),	//Message refusé (Mauvais format)
	SERVER_NO_CLIENT			( 0x26, "Server : No client connected" ),	//Réponse à un signal "BUSINESS_KEEP_INFOS" si aucun clients n'est connecté
	SERVER_CLOSED 				( 0x27, "Server : Closed" );	//Socket fermé -> Envoyé à tout les clients avant la fermeture
	
	private int code;
	private String infos;
	
	private Signal (int code) {
		this.code = code;
		this.infos = "";
	}
	
	private Signal (int code, String infos) {
		this.code = code;
		this.infos = infos;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String toString() {
		return String.format("0x%02X", this.code) + " - " + this.infos;
	}
}
