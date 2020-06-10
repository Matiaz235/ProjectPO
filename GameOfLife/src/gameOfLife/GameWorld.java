package gameOfLife;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.swing.*;

class GameWorld extends JPanel implements ComponentListener, MouseListener, Runnable
{

	private static final long serialVersionUID = 1L;
	private Dimension gameBoardSize = new Dimension();
	private ArrayList<Point> point = new ArrayList<Point>(0);
	public static int step = 1, turn = 0, amount = 0;

	// Colors
	static Color secondaryColor = new Color(252, 121, 0);

	public void saveCells(String name)
	{

		boolean[][] gameBoard = new boolean[gameBoardSize.width + 2][gameBoardSize.height + 2];
		for (Point current : point)
		{
			gameBoard[current.x + 1][current.y + 1] = true;
		}
		try
		{
			// System.out.println(new File(".").getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(new FileWriter("src/saves/" + name + ".txt"));

			for (int row = 1; row < gameBoard.length - 1; row++)
			{
				for (int j = 1; j < gameBoard[row].length - 1; j++)
				{
					if (gameBoard[row][j])
					{
						bw.write("1");

					} else
					{
						bw.write("0");

					}
				}
				bw.newLine();
			}

			bw.close();
		} catch (IOException e)
		{
			System.out.println("bład zapisu");
			e.printStackTrace();
		}
	}

	void loadCells(String savename)
	{
		BufferedReader br;
		try
		{
			br = new BufferedReader(new FileReader(savename));
			ArrayList<Point> loadedCells = new ArrayList<Point>(0);

			String row;
			int j = 0;

			while ((row = br.readLine()) != null)
			{

				for (int i = 0; i < row.length(); i++)
				{

					if (row.charAt(i) == '1')
					{
						loadedCells.add(new Point(j, i));

					}

				}

				j++;
			}
			resetBoard();
			point.addAll(loadedCells);
			repaint();

		} catch (FileNotFoundException e)
		{
			System.out.println("bład odczytu 1");
			e.printStackTrace();
		} catch (IOException e)
		{
			System.out.println("bład odczytu 2");
			e.printStackTrace();
		}

	}

	public GameWorld()
	{
		this.addComponentListener(this);
		this.addMouseListener(this);
		// series = new XYSeries("Population");
	}

	public void changeBoardSize()
	{
		repaint();
	}

	private void updateArraySize()
	{
		ArrayList<Point> removeList = new ArrayList<Point>(0);
		for (Point current : point)
		{
			if ((current.x > gameBoardSize.getWidth() - 1) || (current.y > gameBoardSize.getHeight() - 1))
			{
				removeList.add(current);
			}
		}
		point.removeAll(removeList);
		repaint();
	}

	public void addPoint(int x, int y)
	{
		if (!point.contains(new Point(x, y)))
		{
			point.add(new Point(x, y));

		}
		repaint();

	}

	public void removePoint(int x, int y)
	{
		if (point.contains(new Point(x, y)))
		{
			point.remove(new Point(x, y));

		}
		repaint();
	}

	public void changePointWithMouse(MouseEvent me)
	{
		int x = me.getPoint().x / GameFrame.BLOCK_SIZE - 1;
		int y = me.getPoint().y / GameFrame.BLOCK_SIZE - 1;
		if ((x >= 0) && (x < gameBoardSize.width) && (y >= 0) && (y < gameBoardSize.height))
		{

			if (point.contains(new Point(x, y)))
			{
				removePoint(x, y);
			} else
			{
				addPoint(x, y);
			}
		}
	}

	public void resetBoard()
	{
		point.clear();

	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		try
		{
			for (Point newPoint : point)
			{
				// Draw new point
				// g.setColor(secondaryColor);
				g.setColor(Color.ORANGE);
				g.fillRect(GameFrame.BLOCK_SIZE + (GameFrame.BLOCK_SIZE * newPoint.x),
						GameFrame.BLOCK_SIZE + (GameFrame.BLOCK_SIZE * newPoint.y), GameFrame.BLOCK_SIZE,
						GameFrame.BLOCK_SIZE);
			}
		} catch (ConcurrentModificationException cme)
		{
		}
		// Setup grid
		g.setColor(Color.BLACK);
		for (int i = 0; i <= gameBoardSize.width; i++)
		{
			g.drawLine(((i * GameFrame.BLOCK_SIZE) + GameFrame.BLOCK_SIZE), GameFrame.BLOCK_SIZE,
					(i * GameFrame.BLOCK_SIZE) + GameFrame.BLOCK_SIZE,
					GameFrame.BLOCK_SIZE + (GameFrame.BLOCK_SIZE * gameBoardSize.height));
		}
		for (int i = 0; i <= gameBoardSize.height; i++)
		{
			g.drawLine(GameFrame.BLOCK_SIZE, ((i * GameFrame.BLOCK_SIZE) + GameFrame.BLOCK_SIZE),
					GameFrame.BLOCK_SIZE * (gameBoardSize.width + 1),
					((i * GameFrame.BLOCK_SIZE) + GameFrame.BLOCK_SIZE));
		}
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		gameBoardSize = new Dimension((-2) + getWidth() / GameFrame.BLOCK_SIZE,
				(-2) + getHeight() / GameFrame.BLOCK_SIZE);
		updateArraySize();
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
	}

	@Override
	public void componentHidden(ComponentEvent e)
	{
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		changePointWithMouse(e);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void run()
	{
		boolean[][] gameBoard = new boolean[gameBoardSize.width + 2][gameBoardSize.height + 2];
		for (Point current : point)
		{
			gameBoard[current.x + 1][current.y + 1] = true;
		}
		ArrayList<Point> survivingCells = new ArrayList<Point>(0);
		for (int i = 1; i < gameBoard.length - 1; i++)
		{
			for (int j = 1; j < gameBoard[0].length - 1; j++)
			{
				int neighbors = 0;
				if (gameBoard[i - 1][j - 1])
				{
					neighbors++;
				}
				if (gameBoard[i - 1][j])
				{
					neighbors++;
				}
				if (gameBoard[i - 1][j + 1])
				{
					neighbors++;
				}
				if (gameBoard[i][j - 1])
				{
					neighbors++;
				}
				if (gameBoard[i][j + 1])
				{
					neighbors++;
				}
				if (gameBoard[i + 1][j - 1])
				{
					neighbors++;
				}
				if (gameBoard[i + 1][j])
				{
					neighbors++;
				}
				if (gameBoard[i + 1][j + 1])
				{
					neighbors++;
				}
				if (gameBoard[i][j])
				{
					// Alive
					if (GameFrame.rule1List.contains(neighbors))
					{
						survivingCells.add(new Point(i - 1, j - 1));
					}
				} else
				{
					// Dead
					if (GameFrame.rule2List.contains(neighbors))
					{
						survivingCells.add(new Point(i - 1, j - 1));
					}
				}
			}
		}
		resetBoard();

		point.addAll(survivingCells);

		repaint();

		try
		{
			Thread.sleep(1000 - 10 * GameFrame.speed);
			run();
		} catch (InterruptedException ex)
		{
		}
	}
}