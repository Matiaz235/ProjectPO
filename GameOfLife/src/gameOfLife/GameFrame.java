package gameOfLife;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GameFrame extends JFrame implements KeyListener {
	
	private JPanel topPanel, bottomPanel, leftPanel, rightPanel, jumpPanel, speedPanel, righttopPanel, rightcenterPanel, rule1Panel, rule2Panel;
	GameWorld gameworld;
    private JSlider jumpSlider, zoomSlider, speedSlider;
    private JComboBox<String> modelsBox, languageBox, rule1Box, rule2Box;
    private JButton chartButton, clearButton, ofonButton, stepButton, ruleButton;
    static JLabel speedLabel, jumpLabel, zoomLabel, ruleLabel, rule1Label, rule2Label; 
    
    static final int JUMP_SLIDER_MIN = 0;
    static final int JUMP_SLIDER_MAX = 100;
    static final int JUMP_SLIDER_INIT = 0;
    static final int ZOOM_SLIDER_MIN = 0;
    static final int ZOOM_SLIDER_MAX = 100;
    static final int ZOOM_SLIDER_INIT = 0;
    static final int SPEED_SLIDER_MIN = 0;
    static final int SPEED_SLIDER_MAX = 100;
    static final int SPEED_SLIDER_INIT = 0;
    
    public static int language, jump, speed, xP, yP, rule;
    public static boolean offon = false;
    
    public GameFrame() {
    	 this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
         this.setSize(898,666);
         this.setTitle("Our Game Of Life");
         this.setLayout(new BorderLayout());
         this.setFocusable(true);
         this.addKeyListener((KeyListener) this);
         this.setResizable(false);									//To usun¹æ gdy naprawiony zostanie GameWorld
         
         
         //Top Panel
         topPanel = new JPanel();
         topPanel.setLayout(new FlowLayout());
         jumpPanel = new JPanel();
        // jumpPanel.setLayout(new GridLayout(2,1));
         speedPanel = new JPanel();
        // speedPanel.setLayout(new GridLayout(2,1));
         
         jumpSlider = new JSlider(JSlider.HORIZONTAL, JUMP_SLIDER_MIN, JUMP_SLIDER_MAX, JUMP_SLIDER_INIT);
         jumpSlider.setPreferredSize(new Dimension(300,50));
         jumpSlider.setMajorTickSpacing(20);
         jumpSlider.setMinorTickSpacing(5);
         jumpSlider.setPaintTicks(true);
         jumpSlider.setPaintLabels(true);
         jumpSlider.addChangeListener(new jumpSliderChangeListener());
         
         jumpLabel = new JLabel(); 
         jumpLabel.setText("Skok");
         
         speedSlider = new JSlider(JSlider.HORIZONTAL, SPEED_SLIDER_MIN, SPEED_SLIDER_MAX, SPEED_SLIDER_INIT);
         speedSlider.setPreferredSize(new Dimension(300,50));
         speedSlider.setMajorTickSpacing(20);
         speedSlider.setMinorTickSpacing(5);
         speedSlider.setPaintTicks(true);
         speedSlider.setPaintLabels(true);
         speedSlider.addChangeListener(new speedSliderChangeListener());
         
         speedLabel = new JLabel(); 
         speedLabel.setText("Predkosc");
         
         speedPanel.add(speedLabel);
         speedPanel.add(speedSlider);
         jumpPanel.add(jumpLabel);
         jumpPanel.add(jumpSlider);
         topPanel.add(speedPanel);
         topPanel.add(jumpPanel);
         
         this.add(topPanel, BorderLayout.PAGE_START);
         
         //Left Panel
         leftPanel = new JPanel();
         leftPanel.setLayout(new GridLayout(5,1));
         
         
         chartButton = new JButton("Wykres");
         clearButton = new JButton("Czysc");
         if(language == 0) {
        	 ofonButton = new JButton("START");
         }
         if(language == 1) {
             ofonButton = new JButton("ON");
         }
         ofonButton.setBackground(Color.GREEN);
         ListenForOffOn lOffOn = new ListenForOffOn();
         ofonButton.addActionListener(lOffOn);
         stepButton = new JButton("Nastepny");
         ruleButton = new JButton("Zasady");
         ListenForRule lRule = new ListenForRule();
 		 ruleButton.addActionListener(lRule);
         ListenForChart lChart = new ListenForChart();
 		 chartButton.addActionListener(lChart);
         
         leftPanel.add(chartButton);
         leftPanel.add(ruleButton);
         leftPanel.add(stepButton);
         leftPanel.add(clearButton);
         leftPanel.add(ofonButton);
         leftPanel.setPreferredSize(new Dimension(90,520));
         this.add(leftPanel, BorderLayout.LINE_START);
         
         //Right Panel
         rightPanel = new JPanel();
         rightPanel.setLayout(new BorderLayout());
         	//Right top Panel
         righttopPanel = new JPanel();
         languageBox = new JComboBox<String>();
 		 languageBox.setPreferredSize(new Dimension(70,30));
 		 languageBox.addItem("PL");
 		 languageBox.addItem("ENG");
 		 ListenForLanguageBox lLanguage = new ListenForLanguageBox();
 		 languageBox.addActionListener(lLanguage);
 		 
 		 righttopPanel.add(languageBox);
 		rightPanel.add(righttopPanel, BorderLayout.PAGE_START);
         	//Right center Panel
         rightcenterPanel = new JPanel();
         rightcenterPanel.setPreferredSize(new Dimension(50,400));
         zoomSlider = new JSlider(JSlider.VERTICAL, ZOOM_SLIDER_MIN, ZOOM_SLIDER_MAX, ZOOM_SLIDER_INIT);
         zoomSlider.setPreferredSize(new Dimension(50,300));
         zoomSlider.setMajorTickSpacing(20);
         zoomSlider.setMinorTickSpacing(5);
         zoomSlider.setPaintTicks(true);
         zoomSlider.setPaintLabels(true);
         
         zoomLabel = new JLabel(); 
         zoomLabel.setText("Rozmiar");
         
         rightcenterPanel.add(zoomLabel);
         rightcenterPanel.add(zoomSlider);
         rightPanel.add(rightcenterPanel, BorderLayout.CENTER);
         
         this.add(rightPanel, BorderLayout.LINE_END);
         	

         //Bottom Panel
         bottomPanel = new JPanel();
         modelsBox = new JComboBox<String>();
 		 modelsBox.addItem("Brak");

 		 bottomPanel.add(modelsBox);
 		 this.add(bottomPanel, BorderLayout.PAGE_END);
 		 
 		 //Center Panel

 		 gameworld = new GameWorld();
 		 xP = gameworld.getWidth();
 		 yP = gameworld.getHeight();

         this.add(gameworld, BorderLayout.CENTER);
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
    private class ListenForLanguageBox implements ActionListener {				
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
				if(offon == false) {
					ofonButton.setText("START");
				}
				if(offon == true) {
					ofonButton.setText("STOP");
				}
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
				if(offon == false) {
					ofonButton.setText("ON");
				}
				if(offon == true) {
					ofonButton.setText("OFF");
				}
			}
		}
	}
    
    //Button Listener
    private class ListenForOffOn implements ActionListener {				
		public void actionPerformed(ActionEvent e) {
			if(offon == false) {
				offon = true;
				ofonButton.setBackground(Color.RED);
				if(language == 0) {
					ofonButton.setText("STOP");
				}
				if(language == 1) {
					ofonButton.setText("OFF");
				}
			}
			else{
				offon = false;
				ofonButton.setBackground(Color.GREEN);
				if(language == 0) {
					ofonButton.setText("START");
				}
				if(language == 1) {
					ofonButton.setText("ON");
				}
			}
		}
    }
    
    
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
                rule1Label.setText("1.W nastêpnej turze martwa komorka ozywa jesli ma");
                rule2Label.setText("2.Komorka umiera jezeli liczba jej sasiadow nie wynosi");
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
    		rule1Box.addItem("9");
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
			if (rule1Box.getSelectedIndex() == 8) {
				rule=9;
			}
		}
	}
    
    private class ListenForRule2Box implements ActionListener {				
		public void actionPerformed(ActionEvent e) {
			if (rule1Box.getSelectedIndex() == 0) {
				
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
    

    
    
    
    public static void main(String[] args){
        GameFrame frame = new GameFrame();
        frame.setVisible(true);
    }
}
