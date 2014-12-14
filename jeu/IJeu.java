package jeu;

public interface IJeu {
    public void add(String id);
    public void sendToAllPlayers(String action);
    public void sendToPlayer(String action);
    public void launchGame();
}
