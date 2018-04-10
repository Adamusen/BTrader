package MLSTMtrainer;

import java.io.*;
//import java.text.DecimalFormat;
import java.util.ArrayList;

import MLSTMnetwork.MLSTM;
import MLSTMnetwork.MLSTM.*;
import MainCode.Window;

@SuppressWarnings("unchecked")
public class Trainer {	
	public static boolean going = true;
	
	public static void trainMLSTMnetwork(File trainDataFile) {
		ArrayList<ArrayList<NetInp>> netInpList = new ArrayList<ArrayList<NetInp>>();
		try {
			FileInputStream fin = new FileInputStream(trainDataFile);
			ObjectInputStream ois = new ObjectInputStream(fin);
			netInpList = (ArrayList<ArrayList<NetInp>>) ois.readObject();
			ois.close();
			fin.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		/*double[] cs = new double[25];
		for (NetInp inp : netInpList.get(0) ) {
			double[] inpd = inp.makeInput();
			for (int i=0;i<25;i++)
				if (inpd[i]==1)
					cs[i]++;
		}
		for (double d : cs)
			System.out.println(d);*/
		
		/*double[][][] chartDataSet = new double[1][netInpList.get(0).size()][2];
		for (int z=0;z<netInpList.get(0).size();z++) {
			chartDataSet[0][z] = new double[]{z, netInpList.get(0).get(z).fibN};
		}
		Window.updateNeuralTrainerChartDataset(chartDataSet);*/
		
		MLSTMnetwork net;
		//net = MLSTM.loadMLSTMnetworkFromFile(new java.io.File("data/MLSTMtrainer/TestANNws.jdat") );
		net = new MLSTMnetwork(NetInp.INPNN, 1, 100, 2, false);
		net.ws.randomWeights();
		
		//ArrayList<double[]> errorList = new ArrayList<double[]>();
		double lowesterror=Double.MAX_VALUE, validerror, trainerror;
		double step = 0.003;
		int count=0;
		
		while (going) {
			net.resetStates();
			trainerror = 0;
			validerror = 0;
			int tec=0, vtec=0;
			int good=0, bad=0;		
			double[][][] chartDataSet = new double[2][netInpList.get(0).size()-1][2];
			for (int i=1; i<netInpList.get(0).size()-48; i++) {			
				//NetInp inp0 = netInpList.get(0).get(i-1);
				NetInp inp1 = netInpList.get(0).get(i);
				
				double[][] inputs = new double[48][];
				double[][] expouts = new double[48][];
				for (int l=0;l<inputs.length;l++) {
					inputs[l] = netInpList.get(0).get(i-1+l).makeInput();
					expouts[l] = new double[]{Math.log((netInpList.get(0).get(i+l).avgprice+netInpList.get(0).get(i+l).avgpricediff)/netInpList.get(0).get(i+l).avgprice)};
				}
				
				net.input = inputs[0];
				net.runNeural();
				
				if (i>240)
					if (i<netInpList.get(0).size()*0.8) {
						//net.trainBPTT(inputs, expouts, step);
						net.trainNeural(expouts[0], step);
						trainerror += net.outputError;
						tec++;
					} else {
						validerror += net.calcOutputError(new double[]{Math.log((inp1.avgprice+inp1.avgpricediff)/inp1.avgprice)});
						vtec++;
						if ((net.output[0]*Math.log((inp1.avgprice+inp1.avgpricediff)/inp1.avgprice)) > 0)
							good++;
						else bad++;
					}
				
				chartDataSet[0][i-1] = new double[]{i-1, net.output[0]};
				chartDataSet[1][i-1] = new double[]{i-1, Math.log((inp1.avgprice+inp1.avgpricediff)/inp1.avgprice) };
			}
			Window.updateNeuralTrainerChartDataset(chartDataSet);
			
			trainerror = trainerror / tec;
			validerror = validerror / vtec;			
			lowesterror = Math.min(lowesterror, validerror);
			System.out.println("c: " + count + "  validerrmin: " + lowesterror + " validerr: " + validerror + " trainerr: " + trainerror + " good: " + good + " bad: " + bad);
			/*for (double d : netInpList.get(0).get(netInpList.get(0).size()-1).makeInput() ) 
				System.out.print(d + ", ");
			System.out.println("");
			DecimalFormat df = new DecimalFormat("#0.000");
			for (double d : net.output)
				System.out.print(df.format(d) + " ");
			System.out.println("");*/
			count++;
			//100-2 c: 170  validerrmin: 0.0035495654658184933 validerr: 0.0035495654658184933 trainerr: 0.0020299345280086825
		}
		net.ws.saveToFile(new java.io.File("data/MLSTMtrainer/TestANNws.jdat") );
		System.exit(0);
	}
	
	public static void inspectTrainData(File trainDataFile) {
		ArrayList<NetInp2> netInp2List = new ArrayList<NetInp2>();
		try {
			FileInputStream fin = new FileInputStream(trainDataFile);
			ObjectInputStream ois = new ObjectInputStream(fin);
			netInp2List = (ArrayList<NetInp2>) ois.readObject();
			ois.close();
			fin.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		double[][][] chartDataSet = new double[2][netInp2List.size()][2];
		for (int i=0; i<netInp2List.size(); i++) {			
			chartDataSet[0][i] = new double[]{i, netInp2List.get(i).avgprice};
			chartDataSet[1][i] = new double[]{i, netInp2List.get(i).avgprice + netInp2List.get(i).futpmove*0.01 };
		}
		
		Window.updateNeuralTrainerChartDataset(chartDataSet);
	}
	
	public static void trainMLSTMnetwork2(File trainDataFile) {
		ArrayList<ArrayList<NetInp>> netInpList = new ArrayList<ArrayList<NetInp>>();
		try {
			FileInputStream fin = new FileInputStream(trainDataFile);
			ObjectInputStream ois = new ObjectInputStream(fin);
			netInpList = (ArrayList<ArrayList<NetInp>>) ois.readObject();
			ois.close();
			fin.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		MLSTMnetwork net;
		//net = MLSTM.loadMLSTMnetworkFromFile(new java.io.File("data/MLSTMtrainer/TestANNws.jdat") );
		net = new MLSTMnetwork(1, 1, 40, 1, false);
		net.ws.randomWeights();
		
		//ArrayList<double[]> errorList = new ArrayList<double[]>();
		double lowesterror=Double.MAX_VALUE, validerror, trainerror;
		double step = 0.0003;
		int count=0;
		
		while (going) {
			net.resetStates();
			trainerror = 0;
			validerror = 0;
			int tec=0, vtec=0;
			
			double[][][] chartDataSet = new double[2][netInpList.get(0).size()-1][2];
			for (int i=1; i<netInpList.get(0).size()-48; i++) {
				//NetInp inp0 = netInpList.get(0).get(i-1);
				NetInp inp1 = netInpList.get(0).get(i);
				
				double[][] inputs = new double[48][];
				double[][] expouts = new double[48][];
				for (int l=0;l<inputs.length;l++) {
					inputs[l] = new double[]{Math.log((netInpList.get(0).get(i-1+l).avgprice+netInpList.get(0).get(i-1+l).avgpricediff)/netInpList.get(0).get(i-1+l).avgprice)};
					expouts[l] = new double[]{Math.log((netInpList.get(0).get(i+l).avgprice+netInpList.get(0).get(i+l).avgpricediff)/netInpList.get(0).get(i+l).avgprice)};
				}
				
				net.input = inputs[0];
				net.runNeural();
				
				if (i>240)
					if (i<netInpList.get(0).size()*0.8) {
						net.trainBPTT(inputs, expouts, step);
						trainerror += net.outputError;
						tec++;
					} else {
						validerror += net.calcOutputError(new double[]{Math.log((inp1.avgprice+inp1.avgpricediff)/inp1.avgprice)});
						vtec++;
					}
				
				chartDataSet[0][i-1] = new double[]{i-1, net.output[0]};
				chartDataSet[1][i-1] = new double[]{i-1, Math.log((inp1.avgprice+inp1.avgpricediff)/inp1.avgprice) };
			}
			Window.updateNeuralTrainerChartDataset(chartDataSet);
			
			trainerror = trainerror / tec;
			validerror = validerror / vtec;			
			lowesterror = Math.min(lowesterror, validerror);
			System.out.println("c: " + count + "  validerrmin: " + lowesterror + " validerr: " + validerror + " trainerr: " + trainerror);
			/*for (double d : netInpList.get(0).get(netInpList.get(0).size()-1).makeInput() ) 
				System.out.print(d + ", ");
			System.out.println("");
			DecimalFormat df = new DecimalFormat("#0.000");
			for (double d : net.output)
				System.out.print(df.format(d) + " ");
			System.out.println("");*/
			count++;
			//100-2 c: 170  validerrmin: 0.0035495654658184933 validerr: 0.0035495654658184933 trainerr: 0.0020299345280086825
		}
		net.ws.saveToFile(new java.io.File("data/MLSTMtrainer/TestANNws.jdat") );
		System.exit(0);
	}
	
	public static void trainMLSTMnetwork3(File trainDataFile) {
		ArrayList<NetInp2> netInp2List = new ArrayList<NetInp2>();
		try {
			FileInputStream fin = new FileInputStream(trainDataFile);
			ObjectInputStream ois = new ObjectInputStream(fin);
			netInp2List = (ArrayList<NetInp2>) ois.readObject();
			ois.close();
			fin.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		final int IP = 48;
		final int BS = 48;
		
		MLSTMnetwork net;
		//net = MLSTM.loadMLSTMnetworkFromFile(new java.io.File("data/MLSTMtrainer/TestANNws.jdat") );
		net = new MLSTMnetwork(IP, 3, 100, 1, true);
		net.ws.randomWeights();
		
		//ArrayList<double[]> errorList = new ArrayList<double[]>();
		double lowesterror=Double.MAX_VALUE, validerror, trainerror;
		double step = 0.001;
		int count=0;
		
		while (going) {
			net.resetStates();
			trainerror = 0;
			validerror = 0;
			int tec=0, vtec=0;
			double money = 10000, realmoney = 10000;
			boolean inbtc = false;
			
			double[][][] chartDataSet = new double[3][netInp2List.size()-IP-BS-1][2];
			for (int i=0; i<netInp2List.size()-IP-BS-1; i++) {		
				double[][] inputs = new double[BS][IP];
				double[][] expouts = new double[BS][3];
				double[] steps = new double[BS];
				for (int l=0;l<BS;l++) {
					for (int z=0;z<IP;z++)
						inputs[l][z] = netInp2List.get(i+l+z).avgpricediff;
					
					if (netInp2List.get(i+l+IP-1).futpmove==1) {
						expouts[l][0] = 1;
						steps[l] = 0.0003;
					} else if (netInp2List.get(i+l+IP-1).futpmove==0) {
						expouts[l][1] = 1;
						steps[l] = 0.000015;
					} else if (netInp2List.get(i+l+IP-1).futpmove==-1) {
						expouts[l][2] = 1;
						steps[l] = 0.0003;
					}
				}
				
				net.input = inputs[0];
				net.runNeural();
				//System.out.println(inputs[0][8] + ", " + inputs[0][9] + ";   " + expouts[0][0] + "_" + expouts[0][1] + "_" +expouts[0][2]);
				
				if (i>48)
					if (i<netInp2List.size()*0.8) {
						if (i % BS == 0)
							net.trainBPTT(inputs, expouts, step);
						net.calcOutputError(expouts[0]);
						trainerror += net.outputError;
						tec++;
					} else {
						/*if ( (Math.max(net.output[0], net.output[2]) == net.output[0]) && !inbtc ) {
							money = money / netInp2List.get(i).avgprice;
							realmoney = realmoney * 0.9998 / netInp2List.get(i).avgprice;
							inbtc = true;
						} else if ( (Math.max(net.output[0], net.output[2]) == net.output[2]) && inbtc ) {
							money = money * netInp2List.get(i).avgprice;
							realmoney = realmoney * 0.9998 * netInp2List.get(i).avgprice;
							inbtc = false;
						}*/
						
						if ( (net.output[0]-net.output[2] > 0.05) && !inbtc ) {
							money = money / netInp2List.get(i).avgprice;
							realmoney = realmoney * 0.9998 / netInp2List.get(i).avgprice;
							inbtc = true;
						} else if ( (net.output[2]-net.output[0] > 0.05) && inbtc ) {
							money = money * netInp2List.get(i).avgprice;
							realmoney = realmoney * 0.9998 * netInp2List.get(i).avgprice;
							inbtc = false;
						}
						
						validerror += net.calcOutputError(expouts[0]);
						vtec++;
					}
				
				/*chartDataSet[0][i] = new double[]{i, net.output[0]};
				chartDataSet[1][i] = new double[]{i, net.output[1]};
				chartDataSet[2][i] = new double[]{i, net.output[2]};
				chartDataSet[3][i] = new double[]{i, expouts[0][0]-expouts[0][2]};*/
				chartDataSet[0][i] = new double[]{i, (net.output[0]-net.output[2])};
				chartDataSet[1][i] = new double[]{i, expouts[0][0]-expouts[0][2]};
				chartDataSet[2][i] = new double[]{i, net.output[1]};
			}
			Window.updateNeuralTrainerChartDataset(chartDataSet);
			
			trainerror = trainerror / tec;
			validerror = validerror / vtec;			
			lowesterror = Math.min(lowesterror, validerror);
			if (inbtc) {
				money = money * netInp2List.get(netInp2List.size()-IP-BS-1).avgprice;
				realmoney = realmoney * 0.9998 * netInp2List.get(netInp2List.size()-IP-BS-1).avgprice;
			}
			double buynholdM = 10000 * netInp2List.get(netInp2List.size()-IP-BS-1).avgprice / netInp2List.get((int)(netInp2List.size()*0.8)).avgprice;
			System.out.println("c: " + count + "  validerrmin: " + lowesterror + " validerr: " + validerror + " trainerr: " + trainerror + " expmoney: " + money + " realexpmoney: " + realmoney + " BnH M: " + buynholdM);
			/*for (double d : netInpList.get(0).get(netInpList.get(0).size()-1).makeInput() ) 
				System.out.print(d + ", ");
			System.out.println("");
			DecimalFormat df = new DecimalFormat("#0.000");
			for (double d : net.output)
				System.out.print(df.format(d) + " ");
			System.out.println("");*/
			count++;
			//100-2 c: 170  validerrmin: 0.0035495654658184933 validerr: 0.0035495654658184933 trainerr: 0.0020299345280086825
		}
		//net.ws.saveToFile(new java.io.File("data/MLSTMtrainer/TestANNws.jdat") );
		//System.exit(0);
	}
	
}
