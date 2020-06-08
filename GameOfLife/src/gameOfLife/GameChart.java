package gameOfLife;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;


public class GameChart extends JFrame{
	
	public JFreeChart chartxy;
	public XYSeriesCollection dataset;
	public ChartFrame chartFrame;
	//
	public ChartPanel chartPanel;
	ResourceBundle labels;
	
	public GameChart(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(750,500);
        this.setLayout(new BorderLayout());
        this.setVisible(true);
        //this.setTitle(labels.getString("chart"));

        //series = new XYSeries("Population");
        dataset = new XYSeriesCollection(GameWorld.series);
        chartxy = ChartFactory.createXYLineChart (
        			"Y = F(X)", // chart title
        			"X", // x axis label
        			"Y", // y axis label
        			dataset, // data
        			PlotOrientation.VERTICAL,
        			false, // include legend
        			false, // tooltips
        			false // urls
        			);
        chartFrame=new ChartFrame("Game Of Life",chartxy);
        chartPanel = new ChartPanel(chartxy);
        
        this.add(chartPanel, BorderLayout.CENTER);
          
	}
	
}
