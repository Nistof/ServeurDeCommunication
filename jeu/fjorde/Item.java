package jeu.fjorde;

public enum Item {
    HUTTE,
    CHAMP;
    
    public static Item getItemById (char c) {
    	if(c == 'H')
    		return Item.HUTTE;
    	else if(c == 'C')
    		return Item.CHAMP;
		return null;
    }
}
