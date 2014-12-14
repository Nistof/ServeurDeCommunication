package jeu;

public interface IJeu {
    public String getEtatInitial();
    public String getOrdreDuTour();
    public String action ( String action);
    public String Classement ();
    public void add(String id);
    public void sendToAllPlayers(String action);
    public void sendToPlayer(String action);
    public void launchGame();
}
