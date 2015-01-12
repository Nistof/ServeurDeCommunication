package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class ClientDiaballik {
	private final static char[] SYMBOLS = { 'B', 'N' };
	private final static String[] ACTIONS =  {"MOVEB", "MOVES", "ENDTURN"};
	private InetAddress ip;
	private int port;
	private DatagramSocket clientSocket;
	private String clientName;
	private String clientId;
	private HashMap<String, String> playersName;
	private String winner;

	private char[][] grid;
	private int symbole;

	/**
	 * Constructeur du ClientMorpion
	 * 
	 * @param ip
	 *            IP du serveur
	 * @param port
	 *            Port o� le serveur �coute
	 */
	public ClientDiaballik(String ip, int port) {
		try {
			this.ip = InetAddress.getByName(ip);
			this.port = port;
			this.clientSocket = new DatagramSocket();
			this.grid = new char[7][7];
			this.playersName = new HashMap<String, String>();
			this.clientId = "";
			this.clearGrid();
		} catch (UnknownHostException e) {
			System.out.println("Connexion à l'hote impossible!");
		} catch (IOException e) {
			System.out.println("Erreur de flux");
		}
	}

	
	public void clearGrid () {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				this.grid[i][j] = ' ';
			}
		}
	}
	
	/**
	 * R�ception d'un message de la part du serveur
	 * 
	 * @return Message envoy� par le serveur
	 * @throws IOException
	 */
	public String receiveMessage() throws IOException {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);

		// R�cup�ration du message
		this.clientSocket.receive(packet);

		return new String(packet.getData());
	}

	/**
	 * Traitement d'un message
	 * 
	 * @param message
	 *            Message � traiter
	 * @throws IOException
	 */
	public int processMessage(String message) throws IOException {
		message = message.trim();
		String parts[] = message.split(":");
		if(parts[1].equals("OK")) {
			this.clientId = parts[0];
			return 127;
		}
		if(!parts[0].equals(clientId)) {
			return -3;
		} else if (parts[1].equals("NAMELIST")) {
			String[] splMsg = receiveMessage().trim().split(":");
			while (!splMsg[1].equals("ENDLIST")) {
				playersName.put(splMsg[1], splMsg[2]);
				splMsg = receiveMessage().trim().split(":");
			}
			return 127;
		} else if (message.equals(clientId + ":POINTLIST")) {
			String[] splMsg = receiveMessage().trim().split(":");
			while (!splMsg[1].equals("ENDLIST")) {
				String[] coords = splMsg[1].split(",");
				int x = Integer.parseInt(coords[0]), y = Integer.parseInt(coords[1]);
				if(splMsg.length == 3)
				    grid[x][y] = splMsg[2].charAt(0);
				else
				    grid[x][y] = ' ';
				splMsg = receiveMessage().trim().split(":");
			}
			return 3;
		} else if (parts[1].equals("START")) { // Demande de saisie
				return 0;
		} else if (parts[1].equals("ERROR")) { // Si la saisie est
			if(parts[2].equals("ID"))
				return -2;
			else //ERROR:ENTRY
				return -1;
		} else if (parts[1].equals("CANCEL")) {
			return -3;
		} else if (parts[1].equals("END")) {
			winner = playersName.get(parts[2]);
			return 2;
		} else if (parts[1].equals("WIN")) {
			winner = playersName.get(parts[2]);
			return 1;
		}
		return -3;
	}

	/**
	 * Envoi d'un message au serveur
	 * 
	 * @param message
	 *            Message � envoyer
	 * @throws IOException
	 */
	private void sendMessage(String message) throws IOException {
		byte[] data = new byte[1024];
		if(!clientId.equals(""))
			message = clientId + ":" + message;
		data = message.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		// Envoi du message
		this.clientSocket.send(packet);
	}

	/**
	 * R�cup�ration d'une entr�e utilisateur
	 * 
	 * @return Chaine saisie par l'utilisateur
	 * @throws IOException
	 */
	private String getUserEntry() throws IOException {
		BufferedReader buff = new BufferedReader(new InputStreamReader(
				System.in));
		String entry = buff.readLine();
		return entry;
	}

	/**
	 * Renvoie l'id du client
	 * 
	 * @return id du client
	 */
	public String getClientId() {
		return this.clientId;
	}

	/**
	 * Ferme le socket client
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		this.clientSocket.close();
	}

	@Override
	public String toString() {
		String s, sep = "+";

		for (int i = 0; i < grid.length; i++)
			sep += "---+";

		s = sep + "\n";
		for (int i = 0; i < grid.length; i++) {
			s += "|";
			for (int j = 0; j < grid[0].length; j++) {
				s += " " + this.grid[i][j] + " |";
			}
			s += "\n" + sep + "\n";
		}

		return s;
	}

	public static void main(String[] args) {
		ClientDiaballik cm = null;
		boolean endGame = false;
		if (args.length == 2)
			cm = new ClientDiaballik(args[0], Integer.parseInt(args[1]));
		if (cm != null) {
			try {
				// R�cup�ration et envoi du nom et r�ception de l'id attribu� au
				// client
				System.out.print("Saisie du nom : ");
				String name = cm.getUserEntry();
				cm.sendMessage(name);
				cm.setName(name);
				String message = null;
				do {
					
					message = cm.receiveMessage().trim();
					int code = cm.processMessage(message);
					switch(code) {
						case -3: //CANCEL
							System.out.println("Partie annulée");
							endGame = true;
							break;
						case -2: //ERROR:ID
							System.out.println("Vous n'êtes pas le joueur courant");
							break;
						case -1: //ERROR:ENTRY
							System.out.println("Saisie incorrecte");
							break;
						case 0: //START
							int answer = 0;
							do {
								System.out.println("Action : ");
								System.out.println("1.Deplacer la balle");
								System.out.println("2.Deplacer un support");
								System.out.println("3.Finir le tour");
								System.out.print("Entrez votre choix : ");
								answer = Integer.parseInt(cm.getUserEntry());
							} while(answer < 1 || answer > 3);
							
							String msg = "";
							
							int x = 0, y = 0, x2 = 0, y2 = 0;
							if(answer < 3) {
								System.out.print("Entrez le X : ");
								x = Integer.parseInt(cm.getUserEntry());
								System.out.print("Entrez le Y : ");
								y = Integer.parseInt(cm.getUserEntry());
								msg += ":" + x + "," + y;
								if(answer == 2) {
									System.out.print("Entrez la direction (NOSE) : ");
									msg+= ":" + cm.getUserEntry();
								}
								else {
									System.out.print("Entrez le X de destination : ");
									x2 = Integer.parseInt(cm.getUserEntry());
									System.out.print("Entrez le Y de destination : ");
									y2 = Integer.parseInt(cm.getUserEntry());
									msg += ":" + x2 + "," + y2;
								}
							}

							cm.sendMessage(ACTIONS[answer-1] + msg);
							break;
						case 1: //WIN
							System.out.println("Le gagnant est : "  + cm.getWinner());
							endGame = true;
							break;
						case 2: //END -> Utilisé pour l'anti jeu
							System.out.println("Le gagnant par anti-jeu est : " + cm.getWinner());
							endGame = true;
							break;
						case 3:
							System.out.println(cm);
							break;
						default:
							break;
					}
				} while (!endGame);

				cm.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getWinner() {
		return winner;
	}

	private void setName(String name) {
		this.clientName = name;
	}
}
