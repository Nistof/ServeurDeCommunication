package jeu.fjorde;

public enum Type {
    TERRE_ARABLE,
    MONTAGNE,
    EAU;

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
}