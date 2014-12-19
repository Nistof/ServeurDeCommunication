package jeu;

import java.io.IOException;

public interface IJeu {
    public void add(String name, String id);
    public void sendToAllPlayers(String msg);
    public void sendToPlayer(String msg);
    public String receiveFromPlayer() throws IOException;
    public boolean processMessage(String msg);
    public void launchGame();
}
