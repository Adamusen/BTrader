package MainCode;
import org.jfree.chart.*;
//import org.jfree.chart.event.*;
import org.jfree.chart.axis.*;
//import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.*;


public class Window {
	public static final JFrame frame = new JFrame("BTrader");
	
	public static XYSeriesCollection NeuralTrainerChartDataset = new XYSeriesCollection( );
	public static JFreeChart NeuralTrainerChart = ChartFactory.createXYLineChart("Trade history", "Time", "Price", NeuralTrainerChartDataset);
	public static JTextArea LogTextArea = new JTextArea();
	
	
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void createWindow() {
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				BTrader.onExit();
			}
		});
		
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("data/Icons/trayicon.jpg"));
		frame.setSize(874, 480);
		
		JTabbedPane Main_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(Main_tabbedPane, BorderLayout.CENTER);
		
		ChartPanel NeuralTrainer_ChartPanel = new ChartPanel(NeuralTrainerChart);
		Main_tabbedPane.addTab("Neural Trainer", NeuralTrainer_ChartPanel);
		
		JScrollPane Log_scrollPane = new JScrollPane();
		Log_scrollPane.setViewportView(LogTextArea);
		Main_tabbedPane.addTab("Log", null, Log_scrollPane, null);
		
		initCharts();
		frame.setVisible(true);
	}
	
	private static void initCharts() {
		NumberAxis yAxis = (NumberAxis) NeuralTrainerChart.getXYPlot().getRangeAxis();
	    yAxis.setAutoRangeIncludesZero(false);
	    yAxis.setNumberFormatOverride(new DecimalFormat("#0.00000000"));
	}
	
	
	
	public static void updateNeuralTrainerChartDataset(double[][][] data) {
		NeuralTrainerChartDataset.removeAllSeries();
		int scount=0;
		for (double[][] sdata : data) {
			final XYSeries series = new XYSeries("Series " + scount);
			
			for (double[] sde : sdata) {
				series.add(sde[0], sde[1]);
			}
			
			NeuralTrainerChartDataset.addSeries(series);
			scount++;
		}
	}
	
	/*public static void replaceNeuralTrainerChartDataLastSeries(double[][] data) {
		NeuralTrainerChartDataset.removeSeries(NeuralTrainerChartDataset.getSeriesCount()-1);
		final XYSeries series = new XYSeries("New Series");
		
		for (double[] sde : data) {			
			series.add(sde[0], sde[1]);
		}
		
		NeuralTrainerChartDataset.addSeries(series);
	}*/
	
	public static void createTray() {
		final PopupMenu popup = new PopupMenu();	
		
		TrayIcon trayIcon = null;
		try {
			trayIcon = new TrayIcon(ImageIO.read(new File("data/Icons/trayicon.jpg")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
		trayIcon.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		frame.setVisible(true);
        	}
		});
		
        final SystemTray tray = SystemTray.getSystemTray();
        
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		BTrader.onExit();
        	}
        });
        
        popup.add(exitItem);
        
        trayIcon.setPopupMenu(popup);
        
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
        	e.printStackTrace();
            BTrader.addLogEntry("TrayIcon could not be added.");
        }
	}
}
