package gameOfLife;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class GameWorld extends JPanel implements ComponentListener, MouseListener, Runnable {
	
    private Dimension gameBoardSize = null;
    private ArrayList<Point> point = new ArrayList<Point>(0);
    
    public GameWorld() {
        this.addComponentListener(this);
        this.addMouseListener(this);
    }
    
    private void updateArraySize() {
        ArrayList<Point> removeList = new ArrayList<Point>(0);
        for (Point current : point) {
            if ((current.x > gameBoardSize.getWidth()-1) || (current.y > gameBoardSize.getHeight()-1)) {
                removeList.add(current);
            }
        }
        point.removeAll(removeList);
        repaint();
    }
    
    public void addPoint(int x, int y) {
        if (!point.contains(new Point(x,y))) {
            point.add(new Point(x,y));
        } 
        repaint();
    }
    
    public void addPoint(MouseEvent me) {
        int x = me.getPoint().x/GameFrame.BLOCK_SIZE-1;
        int y = me.getPoint().y/GameFrame.BLOCK_SIZE-1;
        if ((x >= 0) && (x < gameBoardSize.width) && (y >= 0) && (y < gameBoardSize.height)) {
            addPoint(x,y);
        }
    }
    
    
    public void resetBoard() {
        point.clear();
    }
    
    public void randomlyFillBoard(int percent) {
        for (int i=0; i<gameBoardSize.width; i++) {
            for (int j=0; j<gameBoardSize.height; j++) {
                if (Math.random()*100 < percent) {
                    addPoint(i,j);
                }
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            for (Point newPoint : point) {
                // Draw new point
                g.setColor(Color.ORANGE);
                g.fillRect(GameFrame.BLOCK_SIZE + (GameFrame.BLOCK_SIZE*newPoint.x), GameFrame.BLOCK_SIZE + (GameFrame.BLOCK_SIZE*newPoint.y), GameFrame.BLOCK_SIZE, GameFrame.BLOCK_SIZE);
            }
        } catch (ConcurrentModificationException cme) {}
        // Setup grid
        g.setColor(Color.BLACK);
        for (int i=0; i<=gameBoardSize.width; i++) {
            g.drawLine(((i*GameFrame.BLOCK_SIZE)+GameFrame.BLOCK_SIZE), GameFrame.BLOCK_SIZE, (i*GameFrame.BLOCK_SIZE)+GameFrame.BLOCK_SIZE, GameFrame.BLOCK_SIZE + (GameFrame.BLOCK_SIZE*gameBoardSize.height));
        }
        for (int i=0; i<=gameBoardSize.height; i++) {
            g.drawLine(GameFrame.BLOCK_SIZE, ((i*GameFrame.BLOCK_SIZE)+GameFrame.BLOCK_SIZE), GameFrame.BLOCK_SIZE*(gameBoardSize.width+1), ((i*GameFrame.BLOCK_SIZE)+GameFrame.BLOCK_SIZE));
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        // Setup the game board size with proper boundries
        gameBoardSize = new Dimension((-2)+getWidth()/GameFrame.BLOCK_SIZE, (-2)+getHeight()/GameFrame.BLOCK_SIZE);
        updateArraySize();
    }
    @Override
    public void componentMoved(ComponentEvent e) {}
    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {
        addPoint(e);
    }
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}


    @Override
    public void run() {
        boolean[][] gameBoard = new boolean[gameBoardSize.width+2][gameBoardSize.height+2];
        for (Point current : point) {
            gameBoard[current.x+1][current.y+1] = true;
        }
        ArrayList<Point> survivingCells = new ArrayList<Point>(0);
        for (int i=1; i<gameBoard.length-1; i++) {
            for (int j=1; j<gameBoard[0].length-1; j++) {
                int neighbors = 0;
                if (gameBoard[i-1][j-1]) { neighbors++; }
                if (gameBoard[i-1][j])   { neighbors++; }
                if (gameBoard[i-1][j+1]) { neighbors++; }
                if (gameBoard[i][j-1])   { neighbors++; }
                if (gameBoard[i][j+1])   { neighbors++; }
                if (gameBoard[i+1][j-1]) { neighbors++; }
                if (gameBoard[i+1][j])   { neighbors++; }
                if (gameBoard[i+1][j+1]) { neighbors++; }
                if (gameBoard[i][j]) {
                    //Alive
                    if ((neighbors == GameFrame.rule21) || (neighbors == GameFrame.rule22)) {
                        survivingCells.add(new Point(i-1,j-1));
                    } 
                } else {
                    //Dead
                    if (neighbors == GameFrame.rule) {
                        survivingCells.add(new Point(i-1,j-1));
                    }
                }
            }
        }
        resetBoard();
        point.addAll(survivingCells);
        repaint();
        try {
            Thread.sleep(100*GameFrame.jump);
            run();
        } catch (InterruptedException ex) {}
    }
}
