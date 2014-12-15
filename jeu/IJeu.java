package jeu;

import java.io.IOException;

public interface IJeu {
    public void add(String id);
    public void sendToAllPlayers(String action);
    public void sendToPlayer(String action);
    public String receiveFromPlayer() throws IOException;
    public boolean processMessage(String action);
    public void launchGame();
}
