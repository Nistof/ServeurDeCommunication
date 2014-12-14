import java.util.Scanner;

public abstract class CUI {
	public static void main(String[] args) {
		Morpion m = new Morpion();
		String str;
		Scanner sc = new Scanner( System.in);;

		do {
			System.out.println( m );
			
			if ( m.canPlay()) {
				System.out.print(m.getPlayer() + " : ");
				str = sc.nextLine();
				if ( m.action(str) && !m.win())
					m.changePlayer();
			}
		} while ( !m.win() && m.canPlay() );
		sc.close();
		
		System.out.println( m );
		if ( !m.canPlay() && !m.win())
			System.out.println("Partie nulle");
		else
			System.out.println("Gagnant : " + m.getPlayer());
	}
}
