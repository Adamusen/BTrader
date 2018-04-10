package TrashCode;
import java.io.*;
import java.util.*;
import MainCode.*;
import MLSTMnetwork.*;


@SuppressWarnings("serial")
public class Neural {
	public static ArrayList<LSTMinput> trainData = new ArrayList<LSTMinput>();
	
	
	public static class LSTMinput implements Serializable {
		public long timestamp;
		public double lastdiff;
		public double avgdiff;
		public double lastprice;
		public double avgprice;
		
		public LSTMinput (long ts) {
			this.timestamp=ts;
		}
		
		public LSTMinput (long ts, double ld, double ad, double lp, double ap) {
			this.timestamp=ts;
			this.lastdiff=ld;
			this.avgdiff=ad;
			this.lastprice=lp;
			this.avgprice=ap;
		}
		
		@Override
		public boolean equals(Object other) {
			if (other instanceof LSTMinput ) {
				LSTMinput ot = (LSTMinput) other;
				if (ot.timestamp==this.timestamp ) return true;
				else return false;
			} else return false;
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static void charttesting(File trainDataFile, File networkWSFile) {
		try {
			FileInputStream fin = new FileInputStream(trainDataFile);
			ObjectInputStream ois = new ObjectInputStream(fin);
			trainData = (ArrayList<LSTMinput>) ois.readObject();
			ois.close();
			fin.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		MLSTM.MLSTMnWeights netws = MLSTM.loadMLSTMnWeightFromFile(networkWSFile);
		MLSTM.MLSTMnetwork net = new MLSTM.MLSTMnetwork(2, 1, netws.bc[0].length, netws.bc.length, false);
		net.ws = netws;
		LSTMinput previnp = trainData.get(0);
		net.input[0] = previnp.avgdiff;
		net.input[1] = previnp.lastdiff;
		net.runNeural();
		
		ArrayList<double[][]> chartList = new ArrayList<double[][]>();
		double error = 0;
		final long errorCst = 1436486400;
		int good = 0;
		int bad = 0;
		int c = 0;
		for (LSTMinput inp : trainData)
			if (inp.timestamp>=errorCst-3600 && inp.timestamp<=1447459200) {
				if (inp.timestamp>errorCst) {
					double[] expout = new double[1];
					expout[0] = inp.avgdiff;
					if (Math.signum(expout[0])==0 && Math.abs(net.output[0])<=0.01 )
						good++;
					else if (Math.signum(expout[0])==Math.signum(net.output[0]) )
						good++;
					else bad++;
					error+=Math.abs(expout[0]-net.output[0]);
					
					c++;
					//chartList.add(new double[][]{{c, previnp.avgprice * Math.exp(net.output[0])}, {c, inp.avgprice}});
					chartList.add(new double[][]{{c, net.output[0]}, {c, inp.avgdiff}});
				}
				
				net.input[0]=inp.avgdiff;
				net.input[1]=inp.lastdiff;
				net.runNeural();
				previnp = inp;
			}
		System.out.print("error: " + error + "   good: " + good + ", bad: " + bad + ";  goodpercent: " + (good/(double)(good+bad)) );
		
		double[][][] chartDataSet = new double[2][chartList.size()][2];
		for (int z=0;z<chartList.size();z++) {
			chartDataSet[0][z] = chartList.get(z)[0];
			chartDataSet[1][z] = chartList.get(z)[1];
		}
		Window.updateNeuralTrainerChartDataset(chartDataSet);
	}
	
	@SuppressWarnings("unchecked")
	public static void findBestTrainedMLSTMnetwork(File trainDataFile, File networkWSFile) {
		try {
			FileInputStream fin = new FileInputStream(trainDataFile);
			ObjectInputStream ois = new ObjectInputStream(fin);
			trainData = (ArrayList<LSTMinput>) ois.readObject();
			ois.close();
			fin.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		MLSTM.MLSTMnWeights netws = MLSTM.loadMLSTMnWeightFromFile(networkWSFile);
		MLSTM.MLSTMnetwork net = new MLSTM.MLSTMnetwork(2, 1, netws.bc[0].length, netws.bc.length, false);
		net.ws = netws;
		LSTMinput previnp = trainData.get(0);
		net.input[0] = previnp.avgdiff;
		net.input[1] = previnp.lastdiff;
		net.runNeural();
		
		double error = 0;
		final long errorCst = 1436486400;
		int good = 0;
		int bad = 0;
		for (LSTMinput inp : trainData)
			if (inp.timestamp>=errorCst-3600 && inp.timestamp<=1447459200) {
				if (inp.timestamp>errorCst) {
					double[] expout = new double[1];
					expout[0] = inp.avgdiff;
					if (Math.signum(expout[0])==0 && Math.abs(net.output[0])<=0.01 )
						good++;
					else if (Math.signum(expout[0])==Math.signum(net.output[0]) )
						good++;
					else bad++;
					error+=Math.abs(expout[0]-net.output[0]);
				}
				
				net.input[0]=inp.avgdiff;
				net.input[1]=inp.lastdiff;
				net.runNeural();
			}
		System.out.print("error: " + error + "   good: " + good + ", bad: " + bad + ";  goodpercent: " + (good/(double)(good+bad)) );		
	}
	
	@SuppressWarnings("unchecked")
	public static void trainMLSTMnetwork(File trainDataFile, int hNs, int hLs) {
		try {
			FileInputStream fin = new FileInputStream(trainDataFile);
			ObjectInputStream ois = new ObjectInputStream(fin);
			trainData = (ArrayList<LSTMinput>) ois.readObject();
			ois.close();
			fin.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		MLSTM.MLSTMnetwork net = new MLSTM.MLSTMnetwork(2, 1, hNs, hLs, false);
		net.ws.randomWeights();
		//net.ws = MLSTM.loadMLSTMnWeightFromFile(new File("data/Neural/min60_WS_ep1500.jdat") );
		LSTMinput previnp = trainData.get(0);
		net.input[0] = previnp.avgdiff;
		net.input[1] = previnp.lastdiff;
		net.runNeural();
		
		ArrayList<double[]> errorList = new ArrayList<double[]>();
		double error;
		double step = 0.001;
		int count=1500;
		final long trainst = trainData.get(0).timestamp+86400;
		while (true) {
			error=0;
			int good = 0;
			int bad = 0;
			net.resetStates();
			for (LSTMinput inp : trainData)
				if (inp.timestamp<=1436486400) {
					if (inp.timestamp>trainst) {
						double[] expout = new double[1];
						expout[0] = inp.avgdiff;
						if (Math.signum(expout[0])==0 && Math.abs(net.output[0])<=0.01 )
							good++;
						else if (Math.signum(expout[0])==Math.signum(net.output[0]) )
							good++;
						else bad++;
						if (inp.timestamp>=1415444400 && inp.timestamp<=1415451600) System.out.println("exp: " + expout[0] + "   out: " + net.output[0]);
						//expout[0] = Math.exp(inp.avgdiff)/(Math.exp(inp.avgdiff)+1);
						//expout[1] = 1/(Math.exp(inp.avgdiff)+1);
						net.trainNeural(expout, step);
						error+=net.outputError;
					}					
					
					net.input[0]=inp.avgdiff;
					net.input[1]=inp.lastdiff;
					net.runNeural();
				}
			
			System.out.println("good: " + good + "   bad: " + bad + "   percent: " + (good*100/(double)(good+bad)) );
			errorList.add(new double[]{count, error});
			double[][][] chartDataSet = new double[1][errorList.size()][2];
			for (int z=0;z<errorList.size();z++)
				chartDataSet[0][z] = errorList.get(z);
			Window.updateNeuralTrainerChartDataset(chartDataSet);
			
			count++;
			if (count%100 == 0)
				net.ws.saveToFile(new File("data/Neural/min60_WS_ep" + count + ".jdat"));
		}
	}
	
	//1394409600 - 1436486400 - 1445730000
	public static void createTrainData(long st, long ft, long twind) {
		FileReader fr = null;
		BufferedReader br = null;
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		
		class windowTrades {
			public long timestamp;
			public LinkedList<Double> tradePrices;
			public LinkedList<Double> tradeVolumes;
			
			public windowTrades (long ts, LinkedList<Double> tPs, LinkedList<Double> tVs) {
				this.timestamp=ts;
				this.tradePrices=tPs;
				this.tradeVolumes=tVs;
			}
		}
		
		try {
			File tradesFile = new File("data/SampleDatas/bitstampUSD.csv");
			fr = new FileReader(tradesFile);
			br = new BufferedReader(fr);
			
			String line;
			while (Integer.valueOf((line = br.readLine()).split(",")[0]) < st-5*twind);
			
			ArrayList<windowTrades> wTsList = new ArrayList<windowTrades>();
			LinkedList<Double> tPs = new LinkedList<Double>();
			LinkedList<Double> tVs = new LinkedList<Double>();
			
			String[] lsplit = line.split(",");
			long cts = Long.valueOf(lsplit[0]);
			double ctp = Double.valueOf(lsplit[1]);
			double ctv = Double.valueOf(lsplit[2]);
			long wet = st-4*twind;
			tPs.add(ctp);
			tVs.add(ctv);
			
			double prevavgp=ctp;
			double prevlastp=ctp;
			//belongs to next section
			
			while (cts < ft) {
				line = br.readLine();
				lsplit = line.split(",");
				cts = Long.valueOf(lsplit[0]);
				ctp = Double.valueOf(lsplit[1]);
				ctv = Double.valueOf(lsplit[2]);
				
				if (cts<wet) {
					tPs.add(ctp);
					tVs.add(ctv);
				} else {
					while (cts>=wet) {
						wTsList.add(new windowTrades(wet, tPs, tVs) );
						tPs = new LinkedList<Double>();
						tVs = new LinkedList<Double>();
						wet+=twind;
					}
					tPs.add(ctp);
					tVs.add(ctv);
				}
			}
			
			trainData = new ArrayList<LSTMinput>();
			for (int i=4;i<wTsList.size();i++) {
				double avgpsumm=0;
				double avgvsumm=0;
				double lastpsumm=0;
				double lastvsumm=0;
				
				for (int j=i-4;j<=i;j++)
					for (int k=0;k<wTsList.get(j).tradePrices.size();k++) {
						avgpsumm+=wTsList.get(j).tradePrices.get(k)*wTsList.get(j).tradeVolumes.get(k);
						avgvsumm+=wTsList.get(j).tradeVolumes.get(k);
						if (j==i) {
							lastpsumm+=wTsList.get(i).tradePrices.get(k)*wTsList.get(i).tradeVolumes.get(k);
							lastvsumm+=wTsList.get(i).tradeVolumes.get(k);
						}
					}
				
				double avgprice;
				double lastprice;
				if (lastvsumm!=0) lastprice = lastpsumm / lastvsumm;
					else lastprice = prevlastp;
				if (avgvsumm!=0) avgprice = avgpsumm / avgvsumm;
					else avgprice = prevavgp;
				
				long ts = wTsList.get(i).timestamp;
				double lastdiff = Math.log(lastprice / prevlastp);
				double avgdiff = Math.log(avgprice / prevavgp);
				trainData.add(new LSTMinput(ts, lastdiff, avgdiff, lastprice, avgprice) );
				
				System.out.println(ts + ", " + lastdiff + ", " + avgdiff + "; " + lastprice + ", " + avgprice);
				
				prevlastp = lastprice;
				prevavgp = avgprice;
			}
			
			File file = new File("data/Neural/min" + (int)(twind/60) + "_trainData.jdat");
			fout = new FileOutputStream(file, false);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(trainData);
			oos.close();
			fout.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
		
}
