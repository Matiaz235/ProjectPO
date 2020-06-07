package gameOfLife;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GameFrame extends JFrame implements KeyListener
{
	private static final long serialVersionUID = 1L;
	private JPanel topPanel, bottomPanel, leftPanel, rightPanel, rightcenterPanel, rule1Panel, rule2Panel;
	private JSlider jumpSlider, zoomSlider, speedSlider;
	private JComboBox<String> modelsBox;
	private JButton chartButton, clearButton, ofonButton, stepButton, ruleButton;
	static JLabel speedLabel, jumpLabel, zoomLabel, ruleLabel, rule1Label, rule2Label;
	private Thread game;
  
  static final int JUMP_SLIDER_MIN = 1;
  static final int JUMP_SLIDER_MAX = 5;
  static final int JUMP_SLIDER_INIT = 1;
  static final int ZOOM_SLIDER_MIN = 3;
  static final int ZOOM_SLIDER_MAX = 21;
  static final int ZOOM_SLIDER_INIT = 3;
  static final int SPEED_SLIDER_MIN = 0;
  static final int SPEED_SLIDER_MAX = 90;
  static final int SPEED_SLIDER_INIT = 50;
  
	// Colors
	static Color basicColor = new Color(79, 255, 166, 150);
	static Color secondaryColor = new Color(252, 121, 0);

	public static int  jump = 1, speed = 50;
	ComboImageText langComboBox;
	ResourceBundle labels;
	Locale locale;
	TitledBorder titleborder1,titleborder2,titleborder3;
	JPanel[][] topPanelHolder;
	JFrame ruleFrame,chartFrame;
	GameChart gameChart;

	public static boolean IS_ON = false;
	public static int BLOCK_SIZE = 3;
	private GameWorld gb_gameBoard;
	static int languageChange, option;

	static ArrayList<Integer> rule1List = new ArrayList<Integer>();
	static ArrayList<Integer> rule2List = new ArrayList<Integer>();
  
	JPanel ruleButtonPanel;
	JLabel rulesLabel;
	JButton setRuleButton;
	static // table of provided laguages
	Locale[] providedLangs =
	{ new Locale("pl", "PL"), new Locale("en", "GB"), new Locale("ru", "RU"), new Locale("ja", "JP") };

	void changeRulesLabel(ArrayList<Integer> rule1, ArrayList<Integer> rule2)

	{
		 String newLabel="     ";
		
		for (int i = 0; i < rule1.size(); i++)
		{
			newLabel+=Integer.toString(rule1.get(i));
		}
		newLabel+="/";
		for (int i = 0; i < rule2.size(); i++)
		{
			newLabel+=Integer.toString(rule2.get(i));
		}
		rulesLabel.setText(newLabel);
	}

	public GameFrame()
	{

		// default Conway's rules
		rule1List.add(2);
		rule1List.add(3);
		rule2List.add(3);

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setMinimumSize(new Dimension(900, 650));
		this.setSize(900, 650);

		this.setTitle("Our Game Of Life");
		this.setLayout(new BorderLayout());
		this.setFocusable(true);
		this.addKeyListener((KeyListener) this);

		ToolTipManager.sharedInstance().setInitialDelay(300);

		// Languages
		langComboBox = new ComboImageText();
		// czyta z systemu lokalne ustawienia języka
		locale = Locale.getDefault();
		for (int k = 0; k < providedLangs.length; k++)
		{
			if (providedLangs[k].equals(Locale.getDefault()))
			{
				// to jest pochodna JPanel, więc trzeba się zagłębić
				langComboBox.languageList.setSelectedIndex(k);
				locale = providedLangs[k];
				break;
			} else
			{
				// english
				langComboBox.languageList.setSelectedIndex(1);
				locale = providedLangs[1];
			}
		}
		labels = ResourceBundle.getBundle("lang.Languages", locale);

		langComboBox.languageList.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				locale = providedLangs[langComboBox.languageList.getSelectedIndex()];
				labels = ResourceBundle.getBundle("lang.Languages", locale);
				try
				{
					chartButton.setText(labels.getString("chartButtonlabel"));
					clearButton.setText(labels.getString("clearButtonlabel"));
					if (IS_ON == false)
						ofonButton.setText(labels.getString("ofonButtonONlabel"));
					else
						ofonButton.setText(labels.getString("ofonButtonOFFlabel"));

					stepButton.setText(labels.getString("stepButtonlabel"));
					ruleButton.setText(labels.getString("ruleButtonlabel"));
					jumpSlider.setToolTipText(labels.getString("jumpSliderTip"));
					speedSlider.setToolTipText(labels.getString("speedSliderTip"));
					titleborder1.setTitle(labels.getString("jumpSliderTitle"));
					titleborder2.setTitle(labels.getString("speedSliderTitle"));
					titleborder3.setTitle(labels.getString("zoomSliderTitle"));
					try {
					ruleFrame.setTitle(labels.getString("ruleTitle"));
					ruleLabel.setText(labels.getString("ruleTitle2"));
					rule1Label.setText(labels.getString("rule1"));
					rule2Label.setText(labels.getString("rule2"));
					chartFrame.setTitle(labels.getString("chart"));
					setRuleButton.setText(labels.getString("setRuleButtonlabel"));
					}catch(Exception e)
					{
						//System.out.println("No object");
					}
					// Borders need to be repainted
					repaint();

				} catch (MissingResourceException e)
				{
					//System.out.println("No resource");
				}

			}
		});
		// Top Panel
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 4));

		int i = 1;
		int j = 4;
		topPanelHolder = new JPanel[i][j];

		for (int m = 0; m < i; m++)
		{
			for (int n = 0; n < j; n++)
			{
				topPanelHolder[m][n] = new JPanel();
				topPanelHolder[m][n].setBackground(basicColor);
				topPanel.add(topPanelHolder[m][n]);
			}
		}
		// Label with current rules
		rulesLabel = new JLabel();
		rulesLabel.setFont(rulesLabel.getFont().deriveFont(20f));
		changeRulesLabel(rule1List, rule2List);
		topPanelHolder[0][0].setLayout(new GridBagLayout());
		topPanelHolder[0][0].add(rulesLabel);

		jumpSlider = new JSlider(JSlider.HORIZONTAL, JUMP_SLIDER_MIN, JUMP_SLIDER_MAX, JUMP_SLIDER_INIT);
		jumpSlider.setPreferredSize(new Dimension(200, 50));
		jumpSlider.setMajorTickSpacing(1);
		jumpSlider.setMinorTickSpacing(0);
		jumpSlider.setPaintTicks(true);
		jumpSlider.setPaintLabels(true);
		jumpSlider.addChangeListener(new jumpSliderChangeListener());

		jumpSlider.setToolTipText(labels.getString("jumpSliderTip"));

		speedSlider = new JSlider(JSlider.HORIZONTAL, SPEED_SLIDER_MIN, SPEED_SLIDER_MAX, SPEED_SLIDER_INIT);
		speedSlider.setPreferredSize(new Dimension(200, 50));
		speedSlider.setMajorTickSpacing(15);
		speedSlider.setMinorTickSpacing(5);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.addChangeListener(new speedSliderChangeListener());

		speedSlider.setToolTipText(labels.getString("speedSliderTip"));

		topPanelHolder[0][1].add(speedSlider);
		topPanelHolder[0][2].add(jumpSlider);

		Border blueline = BorderFactory.createLineBorder(secondaryColor, 4);
		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		Border sliderframe = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
		sliderframe = BorderFactory.createCompoundBorder(blueline, sliderframe);

		titleborder1 = BorderFactory.createTitledBorder(sliderframe, labels.getString("jumpSliderTitle"));
		titleborder2 = BorderFactory.createTitledBorder(sliderframe, labels.getString("speedSliderTitle"));

		topPanelHolder[0][3].add(langComboBox);
		topPanelHolder[0][1].setBorder(titleborder2);
		topPanelHolder[0][2].setBorder(titleborder1);

		this.add(topPanel, BorderLayout.PAGE_START);

		// Left Panel
		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(2, 1));
		JPanel leftPanel1 = new JPanel();
		JPanel leftPanel2 = new JPanel();
		leftPanel1.setBackground(basicColor);
		leftPanel2.setBackground(basicColor);
		leftPanel.add(leftPanel1);
		leftPanel.add(leftPanel2);

		leftPanel2.setLayout(new GridLayout(10, 1));

		chartButton = new JButton(labels.getString("chartButtonlabel"));

		chartButton.setBackground(secondaryColor);
		chartButton.setForeground(Color.BLACK);
		chartButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
				BorderFactory.createEmptyBorder(0, 0, 0, 0)));
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

		clearButton = new JButton(labels.getString("clearButtonlabel"));

		clearButton.setBackground(secondaryColor);
		clearButton.setForeground(Color.BLACK);
		clearButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
				BorderFactory.createEmptyBorder(0, 0, 0, 0)));
		clearButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		clearButton.setFocusable(false);
		clearButton.addActionListener(new ActionListener()
		{
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
		ofonButton = new JButton(labels.getString("ofonButtonONlabel"));
		ofonButton.setBackground(Color.GREEN);
		ofonButton.setForeground(Color.BLACK);
		ofonButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
				BorderFactory.createEmptyBorder(0, 0, 0, 0)));
		ofonButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		ofonButton.setFocusable(false);
		ofonButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (IS_ON == true)
				{
					IS_ON = false;
					ofonButton.setBackground(Color.GREEN);
					ofonButton.setText(labels.getString("ofonButtonONlabel"));
					setGameBeingPlayed(false);
					stepButton.setEnabled(true);
				}
				else
				{
					IS_ON = true;
					ofonButton.setBackground(Color.RED);
					ofonButton.setText(labels.getString("ofonButtonOFFlabel"));
					setGameBeingPlayed(true);
					stepButton.setEnabled(false);
				}
			}
		});

		ofonButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt)
			{
				ofonButton.setBackground(Color.ORANGE);
			}

			public void mouseExited(java.awt.event.MouseEvent evt)
			{
				if (IS_ON == true)
					ofonButton.setBackground(Color.RED);
				else
					ofonButton.setBackground(Color.GREEN);
			}
		});

		stepButton = new JButton(labels.getString("stepButtonlabel"));
		stepButton.setBackground(secondaryColor);
		stepButton.setForeground(Color.BLACK);
		stepButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
				BorderFactory.createEmptyBorder(0, 0, 0, 0)));
		stepButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		stepButton.setFocusable(false);
		stepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				setGameBeingPlayedOnce(true);
			}
		});

		stepButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt)
			{
				stepButton.setBackground(Color.ORANGE);
			}

			public void mouseExited(java.awt.event.MouseEvent evt)
			{
				stepButton.setBackground(secondaryColor);
			}
		});
		ruleButton = new JButton(labels.getString("ruleButtonlabel"));
		ruleButton.setBackground(secondaryColor);
		ruleButton.setForeground(Color.BLACK);
		ruleButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2),
				BorderFactory.createEmptyBorder(0, 0, 0, 0)));
		ruleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		ruleButton.setFocusable(false);
		ruleButton.addMouseListener(new java.awt.event.MouseAdapter() {
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

		// Right Panel
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		// Right center Panel
		rightcenterPanel = new JPanel();

		rightcenterPanel.setBackground(basicColor);
		titleborder3 = BorderFactory.createTitledBorder(sliderframe, labels.getString("zoomSliderTitle"));
		rightcenterPanel.setBorder(titleborder3);

		rightcenterPanel.setPreferredSize(new Dimension(100, 400));
		zoomSlider = new JSlider(JSlider.VERTICAL, ZOOM_SLIDER_MIN, ZOOM_SLIDER_MAX, ZOOM_SLIDER_INIT);
		zoomSlider.setPreferredSize(new Dimension(80, 420));
		zoomSlider.setMajorTickSpacing(3);
		zoomSlider.setMinorTickSpacing(1);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setPaintLabels(true);
		zoomSlider.addChangeListener(new zoomSliderChangeListener());

		rightcenterPanel.add(zoomSlider);
		rightPanel.add(rightcenterPanel, BorderLayout.CENTER);

		this.add(rightPanel, BorderLayout.LINE_END);

		// Bottom Panel
		bottomPanel = new JPanel();
		bottomPanel.setBackground(basicColor);
		modelsBox = new JComboBox<String>();
		modelsBox.addItem("Brak");				//Tutaj trzeba językowo zrobić a ja się na tym nie znam wiec jak mógłbyś to ogarnąc
		modelsBox.addItem("Szybowiec"); //Glider
		modelsBox.addItem("Dakota");
		modelsBox.addItem("Exploder");
		ListenForComboBox modelListener = new ListenForComboBox();
		modelsBox.addActionListener(modelListener);

		bottomPanel.add(modelsBox);
		this.add(bottomPanel, BorderLayout.PAGE_END);

		// Center Panel
		gb_gameBoard = new GameWorld();
		gb_gameBoard.setPreferredSize(new Dimension(700, 490));
		this.add(gb_gameBoard, BorderLayout.CENTER);
	}

	// Key Listener
	public void keyTyped(KeyEvent e)
	{
		char key = e.getKeyChar();
		if (key == KeyEvent.VK_ESCAPE)
			System.exit(0);
	}

	public void keyPressed(KeyEvent txt)
	{
		// do nothing
	}

	public void keyReleased(KeyEvent txt)
	{
		// do nothing
	}

	//Model Listener
  	class ListenForComboBox implements ActionListener {				
  		public void actionPerformed(ActionEvent e) {
  			gb_gameBoard.setModel(modelsBox.getSelectedIndex());
  		}
  	}
	
	// Slider Listener
	public class jumpSliderChangeListener implements ChangeListener
	{

		@Override
		public void stateChanged(ChangeEvent arg0)
		{
			jump = jumpSlider.getValue();
		}
	}

	public class speedSliderChangeListener implements ChangeListener
	{

		@Override
		public void stateChanged(ChangeEvent arg0)
		{
			if (speedSlider.getValue() == 0)
			{
				speed = 1;
			} else
			{
				speed = speedSlider.getValue();
			}
		}
	}

  public class zoomSliderChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent arg0) {
        	BLOCK_SIZE = zoomSlider.getValue();
        	gb_gameBoard.changeBoardSize();
        }
    }    

	// Rule Listener
	public class ListenForRule implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			ruleFrame = new JFrame();

			try
			{

				ruleFrame.setIconImage(new ImageIcon(GameWorld.class.getResource("graphics/molecular.png")).getImage());

			} catch (Exception ex)
			{
				System.out.println(ex);
				System.out.println("nie udało się wczytać ikony");
			}
			// Icon made by:
			// https://www.flaticon.com/free-icon/molecular_1694420?term=science&page=1&position=53
			ruleFrame.setVisible(true);
			ruleFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			ruleFrame.setSize(800, 200);
			ruleFrame.setLayout(new GridLayout(4, 1));
			ruleFrame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - ruleFrame.getWidth()) / 2,
					(Toolkit.getDefaultToolkit().getScreenSize().height - ruleFrame.getHeight()) / 2);
			ruleFrame.setResizable(false);

			rule1Panel = new JPanel();
			rule2Panel = new JPanel();
			ruleLabel = new JLabel();
			rule1Label = new JLabel();
			rule2Label = new JLabel();

			ruleLabel.setFont(ruleLabel.getFont().deriveFont(22f));
			rule1Label.setFont(rule1Label.getFont().deriveFont(22f));
			rule2Label.setFont(rule2Label.getFont().deriveFont(22f));

			ruleFrame.add(ruleLabel);
			rule1Panel.add(rule1Label);
			ruleFrame.add(rule1Panel);
			rule2Panel.add(rule2Label);
			ruleFrame.add(rule2Panel);

			ruleFrame.setTitle(labels.getString("ruleTitle"));
			ruleLabel.setText(labels.getString("ruleTitle2"));
			rule1Label.setText(labels.getString("rule1"));
			rule2Label.setText(labels.getString("rule2"));

			// rozwiązanie z radio
			JCheckBox[] rule1cbox = new JCheckBox[9];
			JCheckBox[] rule2cbox = new JCheckBox[9];

			for (int i = 0; i < rule1cbox.length; i++)
			{
				rule1cbox[i] = new JCheckBox(Integer.toString(i));
				rule1cbox[i].setVerticalTextPosition(SwingConstants.BOTTOM);
				rule1cbox[i].setHorizontalTextPosition(SwingConstants.CENTER);
				rule1cbox[i].setFocusable(false);

				if (rule1List.contains(i))
					rule1cbox[i].setSelected(true);

			}

			for (int i = 0; i < rule2cbox.length; i++)
			{
				rule2cbox[i] = new JCheckBox(Integer.toString(i));
				rule2cbox[i].setVerticalTextPosition(SwingConstants.BOTTOM);
				rule2cbox[i].setHorizontalTextPosition(SwingConstants.CENTER);
				rule2cbox[i].setFocusable(false);

				if (rule2List.contains(i))
					rule2cbox[i].setSelected(true);
			}
			setRuleButton = new JButton();
			ruleButtonPanel = new JPanel();
			setRuleButton = new JButton(labels.getString("setRuleButtonlabel"));
			setRuleButton.setPreferredSize(new Dimension(100, 30));
			setRuleButton.setBackground(secondaryColor);
			setRuleButton.setForeground(Color.BLACK);
			setRuleButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 2),BorderFactory.createEmptyBorder(0, 0, 0, 0)));
			setRuleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			setRuleButton.setFocusable(false);
			
			setRuleButton.addMouseListener(new java.awt.event.MouseAdapter()
			{
				public void mouseEntered(java.awt.event.MouseEvent evt)
				{
					setRuleButton.setBackground(Color.ORANGE);
				}

				public void mouseExited(java.awt.event.MouseEvent evt)
				{
					setRuleButton.setBackground(secondaryColor);
				}

			});
			
			setRuleButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					rule1List.clear();
					rule2List.clear();
					for (int i = 0; i < rule1cbox.length; i++)
					{
						if (rule1cbox[i].isSelected())
							rule1List.add(i);

						if (rule2cbox[i].isSelected())
							rule2List.add(i);

					}
					if ((rule1List.isEmpty() && rule2List.isEmpty())
							|| (rule1List.isEmpty() && rule2List.contains(0) && rule2List.size() == 1)
							|| (rule2List.isEmpty() && rule1List.contains(0) && rule1List.size() == 1))
					{
						rule1List.clear();
						rule2List.clear();
						rule1List.add(2);
						rule1List.add(3);
						rule2List.add(3);
					}
					
					ruleFrame.dispose();
					changeRulesLabel(rule1List, rule2List);
					repaint();
				}
			});
			ruleFrame.add(ruleLabel);
			rule1Panel.add(rule1Label);
			ruleFrame.add(rule1Panel);
			rule2Panel.add(rule2Label);
			ruleFrame.add(rule2Panel);
			ruleButtonPanel.add(setRuleButton);
			ruleFrame.add(ruleButtonPanel);

			for (int i = 0; i < rule1cbox.length; i++)
			{
				rule1Panel.add(rule1cbox[i]);
				rule2Panel.add(rule2cbox[i]);
			}
		}
	}

	public class ListenForChart implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			gameChart = new GameChart();
			System.out.println(gb_gameBoard.turn +" "+ gb_gameBoard.amount );
		}
	}

	public void setGameBeingPlayed(boolean play) {
		if (play ) {
	        game = new Thread(gb_gameBoard);
	        game.start();
	        } 
		else {
	        game.interrupt();
	    }
    }
    
    public void setGameBeingPlayedOnce(boolean play) {
    	if (play ) {
    		game = new Thread(gb_gameBoard);
    		game.start();
  	    	game.interrupt();
  	    	gb_gameBoard.repaint();
  	    	GameWorld.step = 1;
    	}
    }

	public static void main(String[] args)
	{

		JFrame frame = new GameFrame();
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth()) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2);
		try
		{
			frame.setIconImage(new ImageIcon(GameFrame.class.getResource("graphics/molecular.png")).getImage());

		} catch (Exception ex)
		{
			System.out.println(ex);
			System.out.println("nie udało się wczytać ikony");

		}
		// Icon made by:
		// https://www.flaticon.com/free-icon/molecular_1694420?term=science&page=1&position=53
		frame.setVisible(true);

	}
}
