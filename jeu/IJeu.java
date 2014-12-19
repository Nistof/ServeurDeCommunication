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
     */
    public void sendToAllPlayers(String msg);
    
    /**
     * Envoi un message à un joueur
     * @param msg Message à envoyer
     */
    public void sendToPlayer(String msg);
    
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
     */
    public void launchGame();
}
