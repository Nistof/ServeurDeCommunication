package jeu;

import jeu.diaballik.Diaballik;
import jeu.morpion.Morpion;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 12-03-2014
 */

public class Launcher {
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Erreur : vous devez spécifier un jeu à lancer");
            System.exit(1);
        }
        if(args[0].equals("diaballik")) {
            new Diaballik(false);
        }
        else if(args[0].equals("diaballik_variante")) {
            new Diaballik(true);
        }
        else if (args[0].equals("morpion")) {
            new Morpion();
        }
        else {
            System.out.println("Jeu inconnu");
        }
    }

}
