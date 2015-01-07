package jeu.fjorde;

public enum Type {
    TERRE_ARABLE("T"),
    MONTAGNE("M"),
    EAU("E");
    
    private String id;
    
    Type(String id){
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
    	return this.id;
    }
}