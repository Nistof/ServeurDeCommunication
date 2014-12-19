package jeu;

import jeu.diaballik.Diaballik;
import jeu.morpion.Morpion;

public class Launcherjava {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        if(args.length < 1) {
            System.out.println("Erreur : vous devez spécifier un jeu à lancer");
            System.exit(1);
        }
        if(args[0].equals("diabiallik")) {
            new Diaballik(false);
        }
        else if(args[0].equals("diabiallik_variante")) {
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
