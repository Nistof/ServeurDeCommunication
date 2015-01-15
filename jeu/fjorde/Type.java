package jeu.fjorde;

/**
 * 
 * @author Julien DELAFENESTRE
 * @author Thomas MARECAL
 * @author Florian MARTIN
 * @author Thibaut QUENTIN
 * @author Sarah QULORE
 * @version 0.1, 01-05-2015
 */

public enum Type {
    TERRE_ARABLE('T'),
    MONTAGNE('M'),
    EAU('E');
    
    private char id;
    
    Type(char id){
    	this.id = id;
    }
    
    public static Type getTypeById (char id) {
        switch(id) {
            case 'T':
                return Type.TERRE_ARABLE;
            case 'M':
                return Type.MONTAGNE;
            case 'E':
                return Type.EAU;
            default:
                return null;
        }
    }
    
    public String toString(){
    	return "" + this.id;
    }

    public char getCode() {
        return this.id;
    }
}