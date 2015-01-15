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

public class Test {
	private Fjorde fjorde;
	
	
	public Test(){
	    fjorde = new Fjorde();
	}
	
	public String display(){
		return fjorde.toString();
	}
	
	public void tourD(){
	    fjorde.draw();
		fjorde.play(0, 1);
	}
	
	public void tourC(){
		fjorde.draw();
		fjorde.play(0, 0);
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		
		//System.out.println(t.display());
		//for(int i=0; i<2; i++){ 
			t.tourD();
			t.tourC();
			//System.out.println(t.display()); 
		//}
		System.out.println(t.display());
		
	}

}
