package MainCode;
import java.io.File;
import java.util.LinkedList;

import MLSTMnetwork.MLSTM;
import MLSTMtrainer.Trainer;


public class BTrader {
	public static LinkedList<String> Log = new LinkedList<String>();
	
	public static void addLogEntry(String entry) {
		String currtime = new java.sql.Timestamp(System.currentTimeMillis()).toString();
		Log.add(currtime + " \"" + entry + "\"");
		Window.LogTextArea.append(currtime + " \"" + entry + "\"\n");
		System.out.println(currtime + " \"" + entry + "\"");
	}
	
	public static void init() {
		Window.createWindow();
		//Window.createTray();
		
		//PubBapi.connectPusherFirst();
		//PubBapi.subscribeLiveTradesChannel();
		//PubBapi.subscribeOrderBookChannel();
	}
	
	public static void onExit() {
		Window.frame.setVisible(false);
		//PubBapi.disconnectPusher();
		//try { Thread.sleep(500); } catch (Exception e) { e.printStackTrace(); }
		//DataC.writeEverythingToFiles();
		
		Trainer.going = false;
		//System.exit(0);
	}
	
	
	public static void main (String args[]) {	
		init();
		
		//MLSTMtrainer.TDcreator.createNetInp2_histdata(new File("data/SampleDatas/DAT_ASCII_EURUSD_M1_2015.csv"));
		//MLSTMtrainer.Trainer.inspectTrainData(new java.io.File("data/MLSTMtrainer/NetInp2List.jdat") );
		//MLSTMtrainer.Trainer.trainMLSTMnetwork3(new java.io.File("data/MLSTMtrainer/NetInp2List.jdat") );
		
		//MLSTMtrainer.TDcreator.createTrainData(17520, 1800, 1800);
		//MLSTMtrainer.TDcreator.createNetInp(11680, 1800, 1800);
		//MLSTMtrainer.TDcreator.createNetInp2(new File("data/MLSTMtrainer/TW1800TWP1800SN17520_trainData.jdat"));
		//MLSTMtrainer.TDcreator.createNetInp2_histdata(new File("data/SampleDatas/DAT_ASCII_EURUSD_M1_2015.csv"));
		
		//NTrainer.lookatchart(new java.io.File("data/NTrainer/TW3600TWP120SN8760_trainData.jdat") );
		//NTrainer.trainMLSTMnetwork(new java.io.File("data/NTrainer/TW1800TWP60SN17520_trainData.jdat") );
		//NTrainer.trainMLSTMnetwork(new java.io.File("data/NTrainer/TW3600TWP120SN8760_trainData.jdat") );
		//NTrainer.createTrainData(8760, 3600, 120);
		
		//Neural.charttesting(new java.io.File("data/Neural/min60_trainData.jdat"), new java.io.File("data/Neural/min60_WS_BEST.jdat"));
		//Neural.trainMLSTMnetwork(new java.io.File("data/Neural/min30_trainData.jdat"), 60, 2);
		//Neural.findBestTrainedMLSTMnetwork(new java.io.File("data/Neural/min15_trainData.jdat"), new java.io.File("data/Neural/min15_WS_BEST.jdat"));
				
		//Neural.createTrainData(1394409600, 1447544279, 300);
		//MLSTMnetwork.Test.test5();
		//System.out.println(1 / (1 + Math.exp(+5000) ) );
	}
}
