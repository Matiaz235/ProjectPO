package gameOfLife;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GameWorld extends JPanel {
	int size = 10;
	boolean[][] world = new boolean[GameFrame.xP/size][GameFrame.yP/size];
	
	public GameWorld() {
	
		setSize(710, 520);

		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		setBackground(Color.LIGHT_GRAY);
		
		g.setColor(Color.black);
		
		grid(g);
	}
	
	private void grid(Graphics g) {
		//for( int i=0; i<GameFrame.xP; i++) {
		//	g.drawLine(i*size, 0, i*size, GameFrame.yP);
		//}
		//for( int k=0; k<GameFrame.yP && k<GameFrame.xP; k++) {
		//		g.drawLine(0, k*size, GameFrame.xP, k*size);		
		//	}
			// g.drawLine(0, 0, GameFrame.xP, GameFrame.yP); //test
		for( int i=0; i<world.length; i++) {
				g.drawLine(i*size, 0, i*size, GameFrame.yP);
				g.drawLine(0, i*size, GameFrame.xP, i*size);
		}
		
	}

}
