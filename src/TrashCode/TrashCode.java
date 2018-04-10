package TrashCode;
import java.io.*;
import java.util.ArrayList;
//import java.util.Arrays;

import MainCode.*;
import MLSTMnetwork.*;



@SuppressWarnings("serial")
public class TrashCode {
	/*//init variables (MLSTM BPTT)
	double[][] octm1 = new double[hLs][];
	double[][] oct = new double[hLs][];
	double[][] omtm1 = new double[hLs][];
	double[][] omt = new double[hLs][];
	for (int i=0;i<hLs;i++) {
		octm1[i] = Arrays.copyOf(ctm1[i], hNs);
		oct[i] = Arrays.copyOf(ct[i], hNs);
		omtm1[i] = Arrays.copyOf(mtm1[i], hNs);
		omt[i] = Arrays.copyOf(mt[i], hNs);
	}
	
	final int BS = inputs.length;
	double[][] dWix = new double[hNs][0];
	double[][][] dWil = new double[hLs-1][hNs][0];
	double[][][] dWim = new double[hLs][hNs][0];
	double[][] dWic = new double[hLs][0];
	double[][] dbi = new double[hLs][0];
	double[][] dWfx = new double[hNs][0];
	double[][][] dWfl = new double[hLs-1][hNs][0];
	double[][][] dWfm = new double[hLs][hNs][0];
	double[][] dWfc = new double[hLs][0];
	double[][] dbf = new double[hLs][0];
	double[][] dWcx = new double[hNs][0];
	double[][][] dWcl = new double[hLs-1][hNs][0];
	double[][][] dWcm = new double[hLs][hNs][0];
	double[][] dbc = new double[hLs][0];
	double[][] dWox = new double[hNs][0];
	double[][][] dWol = new double[hLs-1][hNs][0];
	double[][][] dWom = new double[hLs][hNs][0];
	double[][] dWoc = new double[hLs][0];
	double[][] dbo = new double[hLs][0];
	double[][] dWym = new double[oNs][0];
	double[] dby = new double[oNs];
	
	double[][][] tmt = new double[BS][hLs][hNs];
	double[][][] tct = new double[BS][hLs][hNs];*/
	
	public static ArrayList<LSTMinput2> trainData2 = new ArrayList<LSTMinput2>();
	
	public static class LSTMinput2 implements Serializable {
		public long timestamp;
		public double avgprice;
		public int expchange;
		
		public LSTMinput2 (long ts) {
			this.timestamp=ts;
		}
		
		public LSTMinput2 (long ts, double avgp, int expch) {
			this.timestamp = ts;
			this.avgprice = avgp;
			this.expchange = expch;
		}
		
		@Override
		public boolean equals(Object other) {
			if (other instanceof LSTMinput2 ) {
				LSTMinput2 ot = (LSTMinput2) other;
				if (ot.timestamp==this.timestamp ) return true;
				else return false;
			} else return false;
		}
	}
	
	/*public static double[] cnet2inp(double x) {
		double[] ret = new double[15];
		if (x < -0.02) ret[0] = 1;
		else if (x<-0.011) ret[1] = 1;
		else if (x<-0.009) ret[2] = 1;
		else if (x<-0.007) ret[3] = 1;
		else if (x<-0.005) ret[4] = 1;
		else if (x<-0.003) ret[5] = 1;
		else if (x<-0.001) ret[6] = 1;
		else if (x<=0.001) ret[7] = 1;
		else if (x<=0.003) ret[8] = 1;
		else if (x<=0.005) ret[9] = 1;
		else if (x<=0.007) ret[10] = 1;
		else if (x<=0.009) ret[11] = 1;
		else if (x<=0.0011) ret[12] = 1;
		else if (x<=0.002) ret[13] = 1;
		else if (x>0.002) ret[14] = 1;
		return ret;
	}*/
	
	@SuppressWarnings("unchecked")
	public static void trainMLSTMnetwork2(File trainDataFile, int hNs, int hLs) {
		try {
			FileInputStream fin = new FileInputStream(trainDataFile);
			ObjectInputStream ois = new ObjectInputStream(fin);
			trainData2 = (ArrayList<LSTMinput2>) ois.readObject();
			ois.close();
			fin.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		MLSTM.MLSTMnetwork net = new MLSTM.MLSTMnetwork(8, 2, hNs, hLs, true);
		net.ws.randomWeights();
		//net.ws = MLSTM.loadMLSTMnWeightFromFile(new File("data/Neural/volume100_WS_ep12.jdat") );
		LSTMinput2 previnp = trainData2.get(0);
		double x = trainData2.get(1).avgprice / previnp.avgprice;
		net.input[0] = (Math.pow(x, 20)-1)/(Math.pow(x, 20)+1);
		net.runNeural();
		
		//ArrayList<double[]> errorList = new ArrayList<double[]>();
		ArrayList<double[][]> chartList;
		double error;
		double step = 0.003;
		int count=0;
		final long trainst = trainData2.get(0).timestamp+86400;
		while (true) {
			count++;
			error=0;
			int good = 0;
			int bad = 0;
			chartList = new ArrayList<double[][]>();
			net.input = new double[8];
			net.resetStates();
			int c=0;
			for (LSTMinput2 inp : trainData2) {
				double[] expout = new double[2];
				if (inp.expchange == 1)
					expout[0] = 1;
				else
					expout[1] = 1;
				
				if (inp.timestamp>trainst && inp.timestamp<=1436486400) {	
					for (int z=0;z<2;z++)
						if (expout[z]==1)
							if (net.output[z] == Math.max(net.output[0], net.output[1]) )
								good++;
							else
								bad++;
					
					chartList.add(new double[][]{{c, inp.expchange*0.01+0.5}, {c, net.output[0]}, {c, net.output[1]}});
					c++;

					net.trainNeural(expout, step);
					error+=net.outputError;
				}
				if (inp.timestamp>1436486400) {
					break;
				}
				
				net.input[7] = net.input[6];
				net.input[6] = net.input[5];
				net.input[5] = net.input[4];
				net.input[4] = net.input[3];
				net.input[3] = net.input[2];
				net.input[2] = net.input[1];
				net.input[1] = net.input[0];
				x = inp.avgprice / previnp.avgprice;
				net.input[0] = (Math.pow(x, 20)-1)/(Math.pow(x, 20)+1);
				previnp = inp;
				net.runNeural();
			}
						
			System.out.println("count: " + count + "   error: " + error + "   good: " + good + ", bad: " + bad + "   percent: " + (good*100/(double)(good+bad)) );
			/*errorList.add(new double[]{count, error});
			double[][][] chartDataSet = new double[1][errorList.size()][2];
			for (int z=0;z<errorList.size();z++)
				chartDataSet[0][z] = errorList.get(z);*/
			double[][][] chartDataSet = new double[3][chartList.size()][2];
			for (int z=0;z<chartList.size();z++) {
				chartDataSet[0][z] = chartList.get(z)[0];
				chartDataSet[1][z] = chartList.get(z)[1];
				chartDataSet[2][z] = chartList.get(z)[2];
			}
			Window.updateNeuralTrainerChartDataset(chartDataSet);
					
			if (count%10 == 0)
				net.ws.saveToFile(new File("data/Neural/volume100_WS_ep" + count + ".jdat"));
		}
	}
	
	public static void createTrainData2(long st, long ft, long vwind) {
		FileReader fr = null;
		BufferedReader br = null;
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		
		try {
			File tradesFile = new File("data/SampleDatas/bitstampUSD.csv");
			fr = new FileReader(tradesFile);
			br = new BufferedReader(fr);
			
			String line;
			while (Integer.valueOf((line = br.readLine()).split(",")[0]) <= st);
			
			String[] lsplit = line.split(",");
			long cts = Long.valueOf(lsplit[0]);
			double ctp = Double.valueOf(lsplit[1]);
			double ctv = Double.valueOf(lsplit[2]);
			
			double buffv = 0;
			double buffpv = 0;
			while (cts < ft) {
				while (buffv+ctv > vwind) {
					ctv -= (vwind-buffv);
					buffpv += (vwind-buffv)*ctp;
					buffv = vwind;					
					trainData2.add(new LSTMinput2(cts, buffpv/buffv, 0) );
					buffpv = 0;
					buffv = 0;
				}
				
				buffpv += ctv*ctp;
				buffv += ctv;
				
				line = br.readLine();
				lsplit = line.split(",");
				cts = Long.valueOf(lsplit[0]);
				ctp = Double.valueOf(lsplit[1]);
				ctv = Double.valueOf(lsplit[2]);
			}
			
			for (int i=0; i<trainData2.size(); i++) {
				double startp = trainData2.get(i).avgprice;
				for (int j=i+1; j<trainData2.size(); j++)
					if (trainData2.get(j).avgprice > startp*1.02) {
						trainData2.get(i).expchange = 1;
						break;
					} else if (trainData2.get(j).avgprice < startp/1.02) {
						trainData2.get(i).expchange = -1;
						break;
					}
				System.out.println("ts: " + trainData2.get(i).timestamp + ", avgp: " + trainData2.get(i).avgprice + ", expch: " + trainData2.get(i).expchange );
			}
			
			File file = new File("data/Neural/volume" + (int)(vwind) + "_trainData2.jdat");
			fout = new FileOutputStream(file, false);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(trainData2);
			oos.close();
			fout.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
