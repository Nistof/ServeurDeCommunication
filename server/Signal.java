package server;

public enum Signal {
	//Signaux de la partie m�tier
	BUSINESS_KEEP_INFOS 		( 0x00 ),	//Demande des clients connect�s
	BUSINESS_MSG 				( 0x01 ),	//Envoi d'un message
	BUSINESS_GIVE_HAND			( 0x02 ),	//Ecoute d'un client en particulier
	BUSINESS_CONNECTION_ALLOWED	( 0x05 ),	//Autorise les clients � se connecter
	BUSINESS_CONNECTION_DENIED	( 0x06 ),	//Interdire les clients de se connecter
	BUSINESS_WRONG_SIGNAL		( 0x07, "Business : Signal rejected : Permission denied" ),	//Utilisation d'un signal non autoris�
	BUSINESS_WRONG_MSG 			( 0x08, "Business : Wrong message" ),	//Message refus� (Mauvais format)
	BUSINESS_WRONG_CLIENT_ID	( 0x09, "Business : Wrong client id : Message not transmitted"),	//Utilisation d'un id de client inexistant
	
	//Signaux des clients
	CLIENT_MSG 					( 0x11 ),	//Envoi d'un message
	CLIENT_CONNECTION_SUCCESS	( 0x15 ),	//Connection r�ussie
	CLIENT_CONNECTION_FAILED	( 0x16, "Client : Connection failed" ),	//Connection non r�ussie
	CLIENT_WRONG_SIGNAL			( 0x17, "Client : Signal rejected : Permission denied" ),	//Utilisation d'un signal non autoris�
	CLIENT_WRONG_MSG 			( 0x18, "Client : Wrong message" ),	//Message refus� (Mauvais format)
	CLIENT_TIMEOUT 				( 0x19, "Client : Timeout : Disconnected" ),	//Client ne r�pondant pas
	
	//Signaux du serveur
	SERVER_SEND_INFOS			( 0x20 ),	//R�ponse � un signal "BUSINESS_KEEP_INFOS" si il y a des clients connect�s
	SERVER_SEND_CLIENT_ID		( 0x21 ),	//ID attribu� au client par le serveur
	SERVER_MSG_TRANSMITTED		( 0x22 ),	//Envoy� pour confirmer que le message a bien �t� transmit
	SERVER_NO_CLIENT			( 0x28, "Server : No client connected" ),	//R�ponse � un signal "BUSINESS_KEEP_INFOS" si aucun clients n'est connect�
	SERVER_CLOSED 				( 0x29, "Server : Closed" );	//Socket ferm� -> Envoy� � tout les clients avant la fermeture
	
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
