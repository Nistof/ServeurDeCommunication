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
	public ClientDiaballik(String ip, int port, boolean variante) {
		try {
			this.ip = InetAddress.getByName(ip);
			this.port = port;
			this.clientSocket = new DatagramSocket();
			this.grid = new char[7][7];
			this.playersName = new HashMap<String, String>();
			
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid.length; j++) {
					this.grid[i][j] = ' ';
				}
			}
			
			if (variante) {
				for (int i = 0; i < grid.length; i++) {
					if (i == 1 || i == 5) {
						this.grid[0][i] = SYMBOLS[1];
						this.grid[6][i] = SYMBOLS[0];
					} else {
						this.grid[0][i] = SYMBOLS[0];
						this.grid[6][i] = SYMBOLS[1];
					}
				}
			} else {
				for (int i = 0; i < grid.length; i++) {
					if (i == 3) {
						this.grid[0][i] = SYMBOLS[0];
						this.grid[6][i] = SYMBOLS[1];
					} else {
						this.grid[0][i] = Character.toLowerCase(SYMBOLS[0]);
						this.grid[6][i] = Character.toLowerCase(SYMBOLS[1]);
					}
					
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("Connexion à l'hote impossible!");
		} catch (IOException e) {
			System.out.println("Erreur de flux");
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
	public void processMessage(String message) throws IOException {
		message = message.trim();

		if (message.equals("gsName_gCid")) {
			// R�cup�ration et envoi du nom du client
			this.clientName = getUserEntry();
			sendMessage(this.clientName);

			// R�cup�ration de l'id du client
			this.clientId = receiveMessage().split(":")[0];
		} else if (message.equals(clientId + ":NAMELIST")) {
			String[] splMsg = receiveMessage().trim().split(":");
			while (!splMsg[1].equals("ENDLIST")) {
				playersName.put(splMsg[1], splMsg[2]);
				splMsg = receiveMessage().trim().split(":");
			}
		} else if (message.equals(clientId + ":START")) { // Demande de saisie
															// par le jeu
			// Symbole du joueur si non d�fini
			if (symbole == -1) {
				boolean isFirstPlayer = true;

				for (int i = 0; i < 3; i++)
					for (int j = 0; j < 3; j++)
						if (grid[i][j] != ' ')
							isFirstPlayer = false;

				this.symbole = (isFirstPlayer) ? 0 : 1;
			}
			int answer = 0;
			do {
				System.out.println("Action : ");
				System.out.println("1.Deplacer la balle");
				System.out.println("2.Deplacer un support");
				System.out.println("3.Finir le tour");
				answer = Integer.parseInt(getUserEntry());
			} while(answer < 1 || answer > 3);
			
			String msg = "";
			int x = 0, y = 0, x2 = 0, y2 = 0;
			if(answer < 3) 
			{
				x = Integer.parseInt(getUserEntry());
				y = Integer.parseInt(getUserEntry());
				msg += ":" + x + "," + y;
				if(answer == 2) {
					x2 = Integer.parseInt(getUserEntry());
					y2 = Integer.parseInt(getUserEntry());
					msg += ":" + x2 + "," + y2;
				}
			}

			sendMessage(this.clientId + ":" + ACTIONS[answer-1] + msg);
		} else if (message.equals(clientId + ":ERROR")) { // Si la saisie est
															// incorrecte
			System.out.println("Saisie incorrecte");
		} else if (message.equals(clientId + ":CANCEL")) {
			System.out.println("Partie annulée");
		} else if (message.equals(clientId + ":END")) {
			return;
		} else {
			String[] splStr = message.trim().split(":");
			if (splStr.length == 3 && !splStr[2].equals("WIN")) { // Placement
																	// des
																	// symboles
																	// sur le
																	// plateau
				int x = Integer.parseInt(splStr[2].split(",")[0]);
				int y = Integer.parseInt(splStr[2].split(",")[1]);

				if (splStr[1].equals(clientId)) {
					this.grid[x][y] = SYMBOLS[symbole];
				} else {
					this.grid[x][y] = SYMBOLS[(symbole + 1) % 2];
				}

				System.out.println(this);
			} else if (splStr.length == 3 && splStr[2].equals("WIN")) { // Si un
																		// joueur
																		// a
																		// gagn�
				System.out.println(playersName.get(splStr[1]) + " gagne!");
			}
		}
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
		if (args.length == 3)
			cm = new ClientDiaballik(args[0], Integer.parseInt(args[1]),
					(args[2].equals("variante")));
		else if (args.length == 2)
			cm = new ClientDiaballik(args[0], Integer.parseInt(args[1]), false);
		if (cm != null) {
			try {
				// R�cup�ration et envoi du nom et r�ception de l'id attribu� au
				// client
				cm.processMessage("gsName_gCid");

				System.out.println(cm);

				String message = null;
				do {
					message = cm.receiveMessage().trim();
					cm.processMessage(message);
				} while (!message.equals(cm.getClientId() + ":END")
						&& !message.equals(cm.getClientId() + ":CANCEL"));

				cm.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
