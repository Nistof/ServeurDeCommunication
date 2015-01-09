package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.OverlayLayout;

import client.Fjorde.GridFjorde;

public class ClientFjorde extends JFrame implements ActionListener {
	private JButton quitButton;
	private GridFjorde grid;
	
	private JPanel panel;

	public ClientFjorde() {
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		
		this.panel = new JPanel();
		this.panel.setLayout(new OverlayLayout(panel));
		
		this.setTitle("Fjorde");
		this.setLocation(0, 0);
		this.goFullScreen();
		
		//this.panel.setLayout(new OverlayLayout(panel));
		this.setFrame();
		c.add(panel, BorderLayout.CENTER);
		
		this.setVisible(true);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Ajoute les différents éléments à la fenêtre
	 */
	private void setFrame() {
		// Dimensions de la fenêtre
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Bouton quitter
		this.quitButton = new JButton("Quitter");
		this.quitButton.addActionListener(this);
		this.quitButton.setAlignmentX(0.0f);
		this.quitButton.setAlignmentY(0.0f);
		this.panel.add(quitButton);
		
		// Plateau de jeu
		this.grid = new GridFjorde();
		this.grid.setBounds( 0, 0, screenSize.width, screenSize.height);
		this.grid.initGrid();
		this.grid.setAlignmentX(0.0f);
		this.grid.setAlignmentY(0.0f);
		this.panel.add(grid);
	}
	
	/**
	 * Passe la fenêtre en mode plein écran
	 */
	private void goFullScreen() {
		if ( !this.isUndecorated())
			this.setUndecorated(true);	
		this.setExtendedState(MAXIMIZED_BOTH);
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.quitButton)
			System.exit(0);
	}
	
	public static void main(String[] args) {
		new ClientFjorde();
	}
}
