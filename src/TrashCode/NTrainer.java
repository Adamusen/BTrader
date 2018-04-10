package TrashCode;
import java.io.*;
import java.util.*;
import MainCode.*;
import MLSTMnetwork.*;


@SuppressWarnings("serial")
public class NTrainer {
	public static MLSTM.MLSTMnWeights bestTrainedWs = new MLSTM.MLSTMnWeights(1, 1, 1, 1, false);
	
	public static class WData implements Serializable {
		public long timestamp;
		public double avgprice;
		public double volume;
		
		public WData (long ts) {
			this.timestamp=ts;
		}
		
		public WData (long ts, double ap, double v) {
			this.timestamp=ts;
			this.avgprice=ap;
			this.volume=v;
		}
		
		@Override
		public boolean equals(Object other) {
			if (other instanceof WData ) {
				WData ot = (WData) other;
				if (ot.timestamp==this.timestamp ) return true;
				else return false;
			} else return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void lookatchart(File trainDataFile) {
		ArrayList<ArrayList<WData>> trainDataList = new ArrayList<ArrayList<WData>>();
		try {
			FileInputStream fin = new FileInputStream(trainDataFile);
			ObjectInputStream ois = new ObjectInputStream(fin);
			trainDataList = (ArrayList<ArrayList<WData>>) ois.readObject();
			ois.close();
			fin.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		double[][][] chartDataSet = new double[1][trainDataList.get(0).size()-5][2];
		Window.updateNeuralTrainerChartDataset(chartDataSet);
		int good=0;
		int bad=0;
		double prevx=0;
		for (int i=4; i<trainDataList.get(0).size()-1; i++) {
			double x=Math.log(calcPavg(trainDataList.get(0), i+1)/calcPavg(trainDataList.get(0), i) );
			//double x=Math.log(trainDataList.get(0).get(i).avgprice/trainDataList.get(0).get(i-1).avgprice);
			chartDataSet[0][i-4] = new double[]{i, x};
			if (Math.signum(prevx)==Math.signum(x) ) good++;
			else bad++;
			prevx = x;
		}
		Window.updateNeuralTrainerChartDataset(chartDataSet);
		System.out.println(good*100 / (double)(good+bad));
	}
	
	public static double sigma(double x) {
		return 1 / (1 + Math.exp(-x) );
	}
	
	public static double tanh(double x) {
		return (Math.exp(2*x)-1)/(Math.exp(2*x)+1);
	}
	
	public static double calcPavg(ArrayList<WData> list, int bisi) {
		double pv = 0;
		double v = 0;
		for (int i=bisi-5; i<=bisi; i++) {
			pv+=list.get(i).avgprice*list.get(i).volume;
			v+=list.get(i).volume;
		}
		return pv / v;
	}
	
	@SuppressWarnings("unchecked")
	public static void trainMLSTMnetwork(File trainDataFile) {
		ArrayList<ArrayList<WData>> trainDataList = new ArrayList<ArrayList<WData>>();
		try {
			FileInputStream fin = new FileInputStream(trainDataFile);
			ObjectInputStream ois = new ObjectInputStream(fin);
			trainDataList = (ArrayList<ArrayList<WData>>) ois.readObject();
			ois.close();
			fin.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		MLSTM.MLSTMnetwork net;
		//net = new MLSTM.MLSTMnetwork(6, 6, 100, 1, false);
		//net.ws.randomWeights();
		net = MLSTM.loadMLSTMnetworkFromFile(new File("data/NTrainer/hNs100hLs1_bestWS.jdat") );
		
		ArrayList<double[]> errorList = new ArrayList<double[]>();
		double lowesterror=Double.MAX_VALUE, error=0, trainerror=0;
		double step = 0.00001;
		int count=0;
		
		while (true) {
			error=0;
			trainerror=0;
			int good=0, traingood=0;
			int bad=0, trainbad=0;;
			net.resetStates();
			
			final int ListN = 0;//count % trainDataList.size();
			ArrayList<WData> cL = trainDataList.get(ListN);
			for (int i=12; i<cL.size()-12; i++) {
				net.input[0] = Math.log(calcPavg(cL, i)/calcPavg(cL, i-6) );
				net.input[1] = Math.log(calcPavg(cL, i)/calcPavg(cL, i-5) );
				net.input[2] = Math.log(calcPavg(cL, i)/calcPavg(cL, i-4) );
				net.input[3] = Math.log(calcPavg(cL, i)/calcPavg(cL, i-3) );
				net.input[4] = Math.log(calcPavg(cL, i)/calcPavg(cL, i-2) );
				net.input[5] = Math.log(calcPavg(cL, i)/calcPavg(cL, i-1) );
				net.runNeural();
				
				double[] expout = new double[6];
				expout[0] = Math.log(calcPavg(cL, i+1)/calcPavg(cL, i) );
				expout[1] = Math.log(calcPavg(cL, i+2)/calcPavg(cL, i) );
				expout[2] = Math.log(calcPavg(cL, i+3)/calcPavg(cL, i) );
				expout[3] = Math.log(calcPavg(cL, i+4)/calcPavg(cL, i) );
				expout[4] = Math.log(calcPavg(cL, i+5)/calcPavg(cL, i) );
				expout[5] = Math.log(calcPavg(cL, i+6)/calcPavg(cL, i) );
				
				if (i>=54 && i<cL.size()*0.7 ) {
					for (int z=0;z<6;z++)
						if (Math.signum(expout[z])==0 && Math.abs(net.output[z])<0.01 )
							traingood++;
						else if (Math.signum(expout[z])==Math.signum(net.output[z]) )
							traingood++;
						else trainbad++;
					
					net.trainNeural(expout, step);
					trainerror+=net.outputError;
				}

				if (i>=cL.size()*0.7) {
					for (int z=0;z<6;z++)
						if (Math.signum(expout[z])==0 && Math.abs(net.output[z])<0.01 )
							good++;
						else if (Math.signum(expout[z])==Math.signum(net.output[z]) )
							good++;
						else bad++;
					error+=net.calcOutputError(expout);
				}					
			}
			
			BTrader.addLogEntry(new String("Error: " + error + "   TrainError: " + trainerror + "   goodpercent: " + (good*100/(double)(good+bad)) )
				+ "   tgpercent: " + (traingood*100/(double)(traingood+trainbad)) );
			errorList.add(new double[]{count, error, trainerror});
			double[][][] chartDataSet = new double[2][errorList.size()][2];
			for (int z=0;z<errorList.size();z++) {
				chartDataSet[0][z] = new double[]{errorList.get(z)[0], errorList.get(z)[1]};
				chartDataSet[1][z] = new double[]{errorList.get(z)[0], errorList.get(z)[2]*3/7.};
			}
			Window.updateNeuralTrainerChartDataset(chartDataSet);
			
			count++;
			if (error<lowesterror) {
				bestTrainedWs = new MLSTM.MLSTMnWeights(net.ws);
				lowesterror = error;
			}
		}
	}
	
	public static void saveBestTrainedWs() {
		bestTrainedWs.saveToFile(new File("data/NTrainer/hNs" + bestTrainedWs.hNs + "hLs" + bestTrainedWs.hLs + "_bestWS.jdat"));
	}
	
	public static void createTrainData(long sampleN, long twind, long twindPD) {
		FileReader fr = null;
		BufferedReader br = null;
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		
		try {
			File tradesFile = new File("data/SampleDatas/bitstampUSD.csv");
			fr = new FileReader(tradesFile);
			br = new BufferedReader(fr);
			
			String[] ld = br.readLine().split(",");
			while (br.ready() ) ld=br.readLine().split(",");
			br.close();
			final long lastitemts = Long.parseLong(ld[0]);
			System.out.println("LastItemTS: " + lastitemts);
			
			ArrayList<ArrayList<WData>> trainDataList = new ArrayList<ArrayList<WData>>();
			for (int i=0; i<twind/twindPD; i++) {
				final int ListN = trainDataList.size();
				trainDataList.add(new ArrayList<WData>() );
				final long finishts = lastitemts/twind*twind-twind+i*twindPD;
				final long startts = finishts - sampleN*twind;
				System.out.println("Starting No.: " + i + "   st: " + startts + " ft: " + finishts);
				
				double PVsumm = 0;
				double Vsumm = 0;
				long wft = startts + twind;
				
				FileReader fr2 = new FileReader(tradesFile);
				BufferedReader br2 = new BufferedReader(fr2);
				ld = br2.readLine().split(",");
				while (ld != null) {
					long ts = Long.parseLong(ld[0]);
					double p = Double.parseDouble(ld[1]);
					double v = Double.parseDouble(ld[2]);
					if (ts > finishts) break;
					if (ts >= startts) {
						if (ts<wft) {
							PVsumm += p*v;
							Vsumm += v;
						} else {
							while (ts>=wft) {
								trainDataList.get(ListN).add(new WData(wft, PVsumm/Vsumm, Vsumm) );
								System.out.println("wft: " + wft + "   Price: " + PVsumm/Vsumm + "   Volume: " + Vsumm);
								wft += twind;
							}
							PVsumm = p*v;
							Vsumm = v;
						}
					}
					ld = br2.readLine().split(",");
				}
				br2.close();
			}
			
			File file = new File("data/NTrainer/TW" + twind + "TWP" + twindPD + "SN" + sampleN + "_trainData.jdat");
			fout = new FileOutputStream(file, false);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(trainDataList);
			oos.close();
			fout.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
}
