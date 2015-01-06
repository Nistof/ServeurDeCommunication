package jeu;

import java.io.IOException;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 12-03-2014
 */

public interface IJeu {
    /**
     * Ajoute un client
     * @param name Nom du client
     * @param id Identifiant du client
     */
    public void add(String name, String id);
    
    /**
     * Envoi un message à tout les joueurs
     * @param msg Message à envoyer
     * @throws IOException 
     */
    public void sendToAllPlayers(String msg) throws IOException;
    
    /**
     * Envoi un message à un joueur
     * @param msg Message à envoyer
     * @throws IOException 
     */
    public void sendToPlayer(String msg) throws IOException;
    
    /**
     * Reçoit un message d'un joueur
     * @return Message du joueur
     * @throws IOException Flux d'entrée fermé
     */
    public String receiveFromPlayer() throws IOException;
    
    /**
     * Traitement du message
     * @param msg Message à traiter
     * @return Vrai quand le message est valide
     */
    public boolean processMessage(String msg);
    
    /**
     * Lance le jeu
     * @throws IOException 
     */
    public void launchGame() throws IOException;
}
