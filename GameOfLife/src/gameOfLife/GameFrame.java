package gameOfLife;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GameFrame extends JFrame implements KeyListener {
	
	private JPanel topPanel, bottomPanel, leftPanel, rightPanel, centerPanel, righttopPanel, rightcenterPanel, rule1Panel, rule2Panel;
	GameWorld gameworld;
    private JSlider jumpSlider, zoomSlider, speedSlider;
    private JComboBox<String> modelsBox, languageBox, rule1Box, rule2Box;
    private JButton chartButton, clearButton, ofonButton, stepButton, ruleButton;
    static JLabel speedLabel, jumpLabel, zoomLabel, ruleLabel, rule1Label, rule2Label; 
    
    static final int JUMP_SLIDER_MIN = 1;
    static final int JUMP_SLIDER_MAX = 5;
    static final int JUMP_SLIDER_INIT = 1;
    static final int ZOOM_SLIDER_MIN = 1;
    static final int ZOOM_SLIDER_MAX = 16;
    static final int ZOOM_SLIDER_INIT = 10;
    static final int SPEED_SLIDER_MIN = 1;
    static final int SPEED_SLIDER_MAX = 10;
    static final int SPEED_SLIDER_INIT = 1;

    //Colors
    Color basicColor = new Color(79, 255, 166,150);
    Color secondaryColor = new Color(252, 121, 0);

    public static int language = 0, jump, speed, rule, rule21, rule22;

    public static boolean IS_ON = false;
    public static int BLOCK_SIZE = 10;
    private GameWorld gb_gameBoard;
	private Thread game;
    
    public GameFrame() {
    	 this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
         this.setSize(898,666);
         this.setTitle("Our Game Of Life");
         this.setLayout(new BorderLayout());
         this.setFocusable(true);
         this.addKeyListener((KeyListener) this);

         ToolTipManager.sharedInstance().setInitialDelay(300);
         
          //Top Panel
         topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 4));

		int i = 1;
		int j = 4;
		JPanel[][] topPanelHolder = new JPanel[i][j];

		for (int m = 0; m < i; m++)
		{
			for (int n = 0; n < j; n++)
			{
				topPanelHolder[m][n] = new JPanel();
				topPanelHolder[m][n].setBackground(basicColor);
				topPanel.add(topPanelHolder[m][n]);
			}
		}

		jumpSlider = new JSlider(JSlider.HORIZONTAL, JUMP_SLIDER_MIN, JUMP_SLIDER_MAX, JUMP_SLIDER_INIT);
		jumpSlider.setPreferredSize(new Dimension(200, 50));
		jumpSlider.setMajorTickSpacing(1);
		jumpSlider.setMinorTickSpacing(0);
		jumpSlider.setPaintTicks(true);
		jumpSlider.setPaintLabels(true);
		jumpSlider.addChangeListener(new jumpSliderChangeListener());

		jumpSlider.setToolTipText("Wybierz o ile kroków ma przeskakiwaæ animacja w ka¿dej generacji.");

		speedSlider = new JSlider(JSlider.HORIZONTAL, SPEED_SLIDER_MIN, SPEED_SLIDER_MAX, SPEED_SLIDER_INIT);
		speedSlider.setPreferredSize(new Dimension(200, 50));
		speedSlider.setMajorTickSpacing(1);
		speedSlider.setMinorTickSpacing(0);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.addChangeListener(new speedSliderChangeListener());

		speedSlider.setToolTipText("Wybierz szybkoœæ wyœwietlania animacji.");


		topPanelHolder[0][1].add(speedSlider);
		topPanelHolder[0][2].add(jumpSlider);

		Border blueline = BorderFactory.createLineBorder(secondaryColor,4);
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border sliderframe = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
		sliderframe = BorderFactory.createCompoundBorder(blueline, sliderframe);


		TitledBorder titleborder1 = BorderFactory.createTitledBorder(sliderframe, "Prêdkoœæ");
		TitledBorder titleborder2 = BorderFactory.createTitledBorder(sliderframe, "Skok");

		topPanelHolder[0][1].setBorder(titleborder1);
		topPanelHolder[0][2].setBorder(titleborder2);

		this.add(topPanel, BorderLayout.PAGE_START);
         
         //Left Panel
        leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(2, 1));
		JPanel leftPanel1 = new JPanel();
		JPanel leftPanel2 = new JPanel();
		leftPanel1.setBackground(basicColor);
		leftPanel2.setBackground(basicColor);
		leftPanel.add(leftPanel1);
		leftPanel.add(leftPanel2);
		
		leftPanel2.setLayout(new GridLayout(10,1));

		chartButton = new JButton("Wykres");

		chartButton.setBackground(secondaryColor);
		chartButton.setForeground(Color.BLACK);
		chartButton.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2),
				BorderFactory.createEmptyBorder(0,0,0,0)));	
		chartButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		chartButton.setFocusable(false);
		chartButton.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseEntered(java.awt.event.MouseEvent evt)
			{
				chartButton.setBackground(Color.ORANGE);
			}

			public void mouseExited(java.awt.event.MouseEvent evt)
			{
				chartButton.setBackground(secondaryColor);
			}
		});

		clearButton = new JButton("Czyœæ");

		clearButton.setBackground(secondaryColor);
		clearButton.setForeground(Color.BLACK);
		clearButton.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2),
				BorderFactory.createEmptyBorder(0,0,0,0)));
		clearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		clearButton.setFocusable(false);
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				gb_gameBoard.resetBoard();
	            gb_gameBoard.repaint();
			}
		});
		clearButton.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseEntered(java.awt.event.MouseEvent evt)
			{
				clearButton.setBackground(Color.ORANGE);
			}

			public void mouseExited(java.awt.event.MouseEvent evt)
			{
				clearButton.setBackground(secondaryColor);
			}
		});
		ofonButton = new JButton("START");
		ofonButton.setBackground(Color.GREEN);
		ofonButton.setForeground(Color.BLACK);
		ofonButton.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2),
				BorderFactory.createEmptyBorder(0,0,0,0)));
		ofonButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		ofonButton.setFocusable(false);
		ofonButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(IS_ON==true)
				{
					IS_ON=false;
					ofonButton.setBackground(Color.GREEN);
					ofonButton.setText("START");
				}
				else
				{
					IS_ON=true;
					ofonButton.setBackground(Color.RED);
					ofonButton.setText("STOP");
				}
			}
			});
		
		ofonButton.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseEntered(java.awt.event.MouseEvent evt)
			{
				ofonButton.setBackground(Color.ORANGE);
			}

			public void mouseExited(java.awt.event.MouseEvent evt)
			{
				if(IS_ON==true)
					ofonButton.setBackground(Color.RED);
				else
					ofonButton.setBackground(Color.GREEN);
			}
		});
		

		stepButton = new JButton("Krok w przód");
		stepButton.setBackground(secondaryColor);
		stepButton.setForeground(Color.BLACK);
		stepButton.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2),
				BorderFactory.createEmptyBorder(0,0,0,0)));
		stepButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		stepButton.setFocusable(false);
		stepButton.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseEntered(java.awt.event.MouseEvent evt)
			{
				stepButton.setBackground(Color.ORANGE);
			}

			public void mouseExited(java.awt.event.MouseEvent evt)
			{
				stepButton.setBackground(secondaryColor);
			}
		});
		ruleButton = new JButton("Zasady");
		ruleButton.setBackground(secondaryColor);
		ruleButton.setForeground(Color.BLACK);
		ruleButton.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2),
				BorderFactory.createEmptyBorder(0,0,0,0)));
		ruleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		ruleButton.setFocusable(false);
		ruleButton.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mouseEntered(java.awt.event.MouseEvent evt)
			{
				ruleButton.setBackground(Color.ORANGE);
			}

			public void mouseExited(java.awt.event.MouseEvent evt)
			{
				ruleButton.setBackground(secondaryColor);
			}
		});
		ListenForRule lRule = new ListenForRule();
		ruleButton.addActionListener(lRule);
		ListenForChart lChart = new ListenForChart();
		chartButton.addActionListener(lChart);

		leftPanel2.add(chartButton);
		leftPanel2.add(Box.createRigidArea(new Dimension(0, 20)));
		leftPanel2.add(ruleButton);
		leftPanel2.add(Box.createRigidArea(new Dimension(0, 20)));
		leftPanel2.add(stepButton);
		leftPanel2.add(Box.createRigidArea(new Dimension(0, 20)));
		leftPanel2.add(clearButton);
		leftPanel2.add(Box.createRigidArea(new Dimension(0, 20)));
		leftPanel2.add(ofonButton);
		
		leftPanel.setPreferredSize(new Dimension(90, 500));
		this.add(leftPanel, BorderLayout.LINE_START);
		
         
         //Right Panel
         rightPanel = new JPanel();
         rightPanel.setLayout(new BorderLayout());
         	//Right top Panel
         righttopPanel = new JPanel();
	 righttopPanel.setBackground(basicColor);
         languageBox = new JComboBox<String>();
 		 languageBox.setPreferredSize(new Dimension(70,30));
 		 languageBox.addItem("PL");
 		 languageBox.addItem("ENG");
// 		 ListenForLanguageBox lLanguage = new ListenForLanguageBox();
// 		 languageBox.addActionListener(lLanguage);
 		 
 		 righttopPanel.add(languageBox);
 		 rightPanel.add(righttopPanel, BorderLayout.PAGE_START);
         	//Right center Panel
         rightcenterPanel = new JPanel();
	 rightcenterPanel.setBackground(basicColor);
	 TitledBorder titleborder3 = BorderFactory.createTitledBorder(sliderframe, "Rozmiar");
   	 rightcenterPanel.setBorder(titleborder3);
         rightcenterPanel.setPreferredSize(new Dimension(50,400));
         zoomSlider = new JSlider(JSlider.VERTICAL, ZOOM_SLIDER_MIN, ZOOM_SLIDER_MAX, ZOOM_SLIDER_INIT);
         zoomSlider.setPreferredSize(new Dimension(50,420));
         zoomSlider.setMajorTickSpacing(3);
         zoomSlider.setMinorTickSpacing(1);
         zoomSlider.setPaintTicks(true);
         zoomSlider.setPaintLabels(true);
	    
         rightcenterPanel.add(zoomSlider);
         rightPanel.add(rightcenterPanel, BorderLayout.CENTER);
         
         this.add(rightPanel, BorderLayout.LINE_END);
         	

         //Bottom Panel
         bottomPanel = new JPanel();
	 bottomPanel.setBackground(basicColor);
         modelsBox = new JComboBox<String>();
 		 modelsBox.addItem("Brak");

 		 bottomPanel.add(modelsBox);
 		 this.add(bottomPanel, BorderLayout.PAGE_END);
 		 
 		// Center Panel
 	    centerPanel = new JPanel();
 	    gb_gameBoard = new GameWorld();
 	    gb_gameBoard.setPreferredSize(new Dimension(700, 490));
 	    centerPanel.add(gb_gameBoard);
 	    this.add(centerPanel, BorderLayout.CENTER);
    }
    
    public void setGameBeingPlayed(boolean IS_ON) {
		if (IS_ON == false) {
	        game = new Thread(gb_gameBoard);
	        game.start();
	        } 
		else {
	        game.interrupt();
	    }
    }
    
    
    
    //Key Listener
    public void keyTyped(KeyEvent e) {
    	char key = e.getKeyChar();
        if (key == KeyEvent.VK_ESCAPE) System.exit(0);
    }
    
    public void keyPressed(KeyEvent txt) {
        //do nothing
    }

    public void keyReleased(KeyEvent txt) {
        //do nothing
    }
    
    
    //Slider Listener
    public class jumpSliderChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent arg0) {
        	jump = jumpSlider.getValue();            
        }
    }
    
    public class speedSliderChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent arg0) {
        	speed = speedSlider.getValue();
            
        }
    }
    
    //Language Listener
/*    private class ListenForLanguageBox implements ActionListener {				
		public void actionPerformed(ActionEvent e) {
			if (languageBox.getSelectedIndex() == 0) {
				language=0;
				chartButton.setText("Wykres");
				clearButton.setText("Czysc"); 
				stepButton.setText("Nastepny");
				ruleButton.setText("Zasady");
				modelsBox.removeAllItems();
				modelsBox.addItem("Brak");
				zoomLabel.setText("Rozmiar");
				jumpLabel.setText("Skok");
				speedLabel.setText("Predkosc");
//				if(IS_ON == false) {					
//					ofonButton.setText("START");
//				}
//				if(IS_ON == true) {
//					ofonButton.setText("STOP");
//				}
			}
			if (languageBox.getSelectedIndex() == 1) {
				language=1;
				chartButton.setText("Chart");
				clearButton.setText("Clear"); 
				stepButton.setText("Next");
				ruleButton.setText("Rules");
				modelsBox.removeAllItems();
				modelsBox.addItem("None");
				zoomLabel.setText("Size");
				jumpLabel.setText("Jump");
				speedLabel.setText("Speed");
//				if(IS_ON == false) {						
//					ofonButton.setText("ON");
//				}
//				if (IS_ON == true) {
//					ofonButton.setText("OFF");
//				}
			}
		}
	}*/
    
    
    	//Rule Listener
    public class ListenForRule implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	JFrame ruleFrame = new JFrame();
            ruleFrame.setVisible(true);
            ruleFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            ruleFrame.setSize(800,200);
            ruleFrame.setLayout(new GridLayout(3,1));
            ruleFrame.setResizable(false);
            rule1Panel = new JPanel();
            rule2Panel = new JPanel();
            ruleLabel = new JLabel(); 
            rule1Label = new JLabel(); 
            rule2Label = new JLabel();
            ruleLabel.setFont(ruleLabel.getFont().deriveFont(22f));
            rule1Label.setFont(rule1Label.getFont().deriveFont(22f));
            rule2Label.setFont(rule2Label.getFont().deriveFont(22f));
            if (language==0) {
            	ruleFrame.setTitle("Zasady");
            	ruleLabel.setText("    Zasady gry w ¿ycie :");
                rule1Label.setText("1.W nastepnej turze martwa komórka o¿ywa jesli ma");
                rule2Label.setText("2.Komórka umiera jezeli liczba jej sasiadów nie wynosi");
            }
            if (language==1) {
            	ruleFrame.setTitle("Rules");
            	ruleLabel.setText("    Rules of the game in life");
                rule1Label.setText("1.In the next turn, the dead cell comes alive if it has");
                rule2Label.setText("2.A cell dies if the number of neighbors is not");
            }
            
            rule1Box = new JComboBox<String>();
    		rule1Box.addItem("1");
    		rule1Box.addItem("2");
    		rule1Box.addItem("3");
    		rule1Box.addItem("4");
    		rule1Box.addItem("5");
    		rule1Box.addItem("6");
    		rule1Box.addItem("7");
    		rule1Box.addItem("8");
    		rule1Box.setSelectedIndex(2);
    		rule2Box = new JComboBox<String>();
    		rule2Box.addItem("3 or 2");
    		ListenForRule1Box lRule1 = new ListenForRule1Box();
    		languageBox.addActionListener(lRule1);
    		ListenForRule2Box lRule2 = new ListenForRule2Box();
    		languageBox.addActionListener(lRule2);
            
            ruleFrame.add(ruleLabel);
            rule1Panel.add(rule1Label);
            rule1Panel.add(rule1Box);
            ruleFrame.add(rule1Panel);
            rule2Panel.add(rule2Label);
            rule2Panel.add(rule2Box);
            ruleFrame.add(rule2Panel);
            
        }
    }
    
    private class ListenForRule1Box implements ActionListener {				
		public void actionPerformed(ActionEvent e) {
			if (rule1Box.getSelectedIndex() == 0) {
				rule=1;
			}
			if (rule1Box.getSelectedIndex() == 1) {
				rule=2;
			}
			if (rule1Box.getSelectedIndex() == 2) {
				rule=3;
			}
			if (rule1Box.getSelectedIndex() == 3) {
				rule=4;
			}
			if (rule1Box.getSelectedIndex() == 4) {
				rule=5;
			}
			if (rule1Box.getSelectedIndex() == 5) {
				rule=6;
			}
			if (rule1Box.getSelectedIndex() == 6) {
				rule=7;
			}
			if (rule1Box.getSelectedIndex() == 7) {
				rule=8;
			}
		}
	}
    
    private class ListenForRule2Box implements ActionListener {				
		public void actionPerformed(ActionEvent e) {
			if (rule1Box.getSelectedIndex() == 0) {
				rule21 = 2;
				rule22 = 3;
			}
		}
	}
    
    public class ListenForChart implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	JFrame chartFrame = new JFrame();
            chartFrame.setVisible(true);
            chartFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            chartFrame.setSize(500,400);
            if (language==0) {
            	chartFrame.setTitle("Wykres");
            	
            }
            if (language==1) {
            	chartFrame.setTitle("Chart");
            }
            
        }
    }
    

    
    
    

    public static void main(String[] args) {
    	JFrame frame = new GameFrame();
    	frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth())/2, (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight())/2); 
    	try {
		 	frame.setIconImage(new ImageIcon(GameFrame.class.getResource("graphics/molecular.png")).getImage());
		 	System.out.println("uda³o siê");
		  } catch (Exception ex) {
		    System.out.println(ex);
		    System.out.println("nie uda³o siê");
		  }
	    //Icon made by:
	    //https://www.flaticon.com/free-icon/molecular_1694420?term=science&page=1&position=53
       frame.setVisible(true);
    }

}