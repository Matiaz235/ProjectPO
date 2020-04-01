package gameOfLife;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GameWorld extends JPanel {
	int size = 10;
	boolean[][] world = new boolean[GameFrame.xP/size][GameFrame.yP/size];
	
	public GameWorld() {
	
		setSize(GameFrame.xP, GameFrame.yP);
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		setBackground(Color.LIGHT_GRAY);
		
		g.setColor(Color.black);
		
		grid(g);
	}
	
	private void grid(Graphics g) {
		for( int i=0; i<GameFrame.xP; i++) {
			for( int k=0; k<GameFrame.yP; k++) {
				g.drawLine(0, k*size, GameFrame.xP, k*size);
				g.drawLine(i*size, 0, i*size, GameFrame.yP);
			}
		}
		
		
	}

}
