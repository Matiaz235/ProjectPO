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
	
	private JPanel topPanel, bottomPanel, leftPanel, rightPanel, righttopPanel, rightcenterPanel, centerPanel;
    private JSlider jumpSlider, zoomSlider, speedSlider;
    private JComboBox<String> modelsBox, languageBox;
    private JButton chartButton, clearButton, ofonButton, stepButton, ruleButton;
    static JLabel speedLabel, jumpLabel, zoomLabel; 
    
    static final int JUMP_SLIDER_MIN = 0;
    static final int JUMP_SLIDER_MAX = 100;
    static final int JUMP_SLIDER_INIT = 0;
    static final int ZOOM_SLIDER_MIN = 0;
    static final int ZOOM_SLIDER_MAX = 100;
    static final int ZOOM_SLIDER_INIT = 0;
    static final int SPEED_SLIDER_MIN = 0;
    static final int SPEED_SLIDER_MAX = 100;
    static final int SPEED_SLIDER_INIT = 0;
    
    boolean[][] life;
    public static int size=10;
    
    public static int language, jump, speed;
    
    public GameFrame() {
    	 this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
         this.setSize(900,670);
         this.setTitle("Our Game Of Life");
         this.setLayout(new BorderLayout());
         this.setFocusable(true);
         this.addKeyListener((KeyListener) this);
         
         
         //Top Panel
         topPanel = new JPanel();
         topPanel.setLayout(new FlowLayout());
         
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
         
         topPanel.add(speedSlider);
         topPanel.add(jumpSlider);
         this.add(topPanel, BorderLayout.PAGE_START);
         
         //Left Panel
         leftPanel = new JPanel();
         leftPanel.setLayout(new GridLayout(5,1));
         
         
         chartButton = new JButton("Wykres");
         clearButton = new JButton("Czysc");
         ofonButton = new JButton("ON/OFF");
         stepButton = new JButton("Nastepny");
         ruleButton = new JButton("Zasady");
         
         leftPanel.add(chartButton);
         leftPanel.add(ruleButton);
         leftPanel.add(stepButton);
         leftPanel.add(clearButton);
         leftPanel.add(ofonButton);
         leftPanel.setPreferredSize(new Dimension(90,500));
         this.add(leftPanel, BorderLayout.LINE_START);
         
         //Right Panel
         rightPanel = new JPanel();
         rightPanel.setLayout(new BorderLayout());
         	//Right top Panel
         righttopPanel = new JPanel();
         languageBox = new JComboBox<String>();
 		 languageBox.setEditable(true);
 		 languageBox.setPreferredSize(new Dimension(70,30));
 		 languageBox.addItem("PL");
 		 languageBox.addItem("ENG");
 		 ListenForLanguageBox lLanguage = new ListenForLanguageBox();
 		 languageBox.addActionListener(lLanguage);
 		 
 		 righttopPanel.add(languageBox);
 		rightPanel.add(righttopPanel, BorderLayout.PAGE_START);
         	//Right center Panel
         rightcenterPanel = new JPanel();
         rightcenterPanel.setPreferredSize(new Dimension(50,500));
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
 		 modelsBox.setEditable(true);
 		 modelsBox.addItem("Brak");

 		 bottomPanel.add(modelsBox);
 		 this.add(bottomPanel, BorderLayout.PAGE_END);
 		 
 		 //Center Panel
 		 centerPanel= new JPanel();
         centerPanel.setBackground(Color.white);

         this.add(centerPanel, BorderLayout.CENTER);
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
				ofonButton.setText("ON/OFF");
				stepButton.setText("Nastepny");
				ruleButton.setText("Zasady");
				modelsBox.removeAllItems();
				modelsBox.addItem("Brak");
				zoomLabel.setText("Rozmiar");
				
			}
			if (languageBox.getSelectedIndex() == 1) {
				language=1;
				chartButton.setText("Chart");
				clearButton.setText("Clear"); 
				ofonButton.setText("ON/OFF");
				stepButton.setText("Next");
				ruleButton.setText("Rules");
				modelsBox.removeAllItems();
				modelsBox.addItem("None");
				zoomLabel.setText("Size");
				
			}
		}
	}

    
    
    
    public static void main(String[] args){
        GameFrame frame = new GameFrame();
        frame.setVisible(true);
    }
}
