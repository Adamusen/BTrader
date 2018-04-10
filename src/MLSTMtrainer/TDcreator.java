package MLSTMtrainer;

import java.io.*;
import java.util.ArrayList;

public class TDcreator {
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
			
			ArrayList<ArrayList<TrainData>> trainDataList = new ArrayList<ArrayList<TrainData>>();
			for (int i=0; i<twind/twindPD; i++) {
				final int ListN = trainDataList.size();
				trainDataList.add(new ArrayList<TrainData>() );
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
								trainDataList.get(ListN).add(new TrainData(wft, PVsumm/Vsumm, Vsumm) );
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
			
			File file = new File("data/MLSTMtrainer/TW" + twind + "TWP" + twindPD + "SN" + sampleN + "_trainData.jdat");
			fout = new FileOutputStream(file, false);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(trainDataList);
			oos.close();
			fout.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public static void createNetInp(long sampleN, long twind, long twindPD) {
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
			
			ArrayList<ArrayList<NetInp>> netInpList = new ArrayList<ArrayList<NetInp>>();
			for (int i=0; i<twind/twindPD; i++) {
				final int ListN = netInpList.size();
				netInpList.add(new ArrayList<NetInp>() );
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
								double avgprice = PVsumm/Vsumm;
								double pricediff = 0;
								if (!netInpList.get(ListN).isEmpty() )
									pricediff = avgprice - netInpList.get(ListN).get(netInpList.get(ListN).size()-1).avgprice;
								netInpList.get(ListN).add(new NetInp(wft, pricediff, avgprice, Vsumm) );
								System.out.println("wft: " + wft + "   PriceDiff: " + pricediff + "   Price: " + avgprice + "   Volume: " + Vsumm + "   fibN: " + netInpList.get(ListN).get(netInpList.get(ListN).size()-1).fibN );
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
			
			File file = new File("data/MLSTMtrainer/TW" + twind + "TWP" + twindPD + "SN" + sampleN + "_netInp.jdat");
			fout = new FileOutputStream(file, false);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(netInpList);
			oos.close();
			fout.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void createNetInp2(File file) {
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		
		try {
			ArrayList<ArrayList<TrainData>> trainDataList = new ArrayList<ArrayList<TrainData>>();
			
			fin = new FileInputStream(file);
			ois = new ObjectInputStream(fin);
			trainDataList = (ArrayList<ArrayList<TrainData>>) ois.readObject();
			ois.close();
			fin.close();
			
			ArrayList<TrainData> tdList = trainDataList.get(0);
			ArrayList<NetInp2> netInp2List = new ArrayList<NetInp2>();
			
			int pc=0, nc=0, c=0;
			for (int i=0;i<tdList.size()-1;i++) {
				long ts = tdList.get(i).timestamp;
				double apd = Math.log(tdList.get(i+1).avgprice/tdList.get(i).avgprice)*100;
				double ap = tdList.get(i).avgprice;
				double v = tdList.get(i).volume;
				int fpm=0;
				for (int j=i+1;j<Math.min(i+10, tdList.size());j++)
					if (tdList.get(j).avgprice/tdList.get(i).avgprice > 1.005) {
						fpm=1;
						pc++;
						break;
					} else if (tdList.get(i).avgprice/tdList.get(j).avgprice > 1.005) {
						fpm=-1;
						nc++;
						break;
					}
				c++;
				
				netInp2List.add(new NetInp2(ts, apd, ap, v, fpm));
			}
			
			File fileout = new File("data/MLSTMtrainer/NetInp2List.jdat");
			fout = new FileOutputStream(fileout, false);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(netInp2List);
			oos.close();
			fout.close();
			
			System.out.println("positives: " + pc + " neutrals: " + (c-pc-nc) + " negatives: " + nc);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public static void createNetInp2_fxdata(File file) {
		FileReader fr = null;
		BufferedReader br = null;
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			ArrayList<NetInp2> netInp2List = new ArrayList<NetInp2>();
			int pc=0, nc=0, c=0;
			
			String[] ld = br.readLine().split("	");
			while (ld!=null) {
				double cp = Double.parseDouble(ld[4]);
				double apd = 0;
				if (!netInp2List.isEmpty() ) apd = Math.log(cp/netInp2List.get(0).avgprice)*100;
				int fpm = 0;
				for (int j=0;j<Math.min(10, netInp2List.size());j++)
					if (netInp2List.get(j).avgprice/cp > 1.0015) {
						fpm=1;
						pc++;
						break;
					} else if (cp/netInp2List.get(j).avgprice > 1.0015) {
						fpm=-1;
						nc++;
						break;
					}
				c++;
				
				netInp2List.add(0, new NetInp2(0, apd, cp, 0, fpm) );
				
				String s = br.readLine();
				if (s==null) break;
				ld = s.split("	");
			}
			
			File fileout = new File("data/MLSTMtrainer/NetInp2List.jdat");
			fout = new FileOutputStream(fileout, false);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(netInp2List);
			oos.close();
			fout.close();
			
			System.out.println("positives: " + pc + " neutrals: " + (c-pc-nc) + " negatives: " + nc);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public static void createNetInp2_histdata(File file) {
		FileReader fr = null;
		BufferedReader br = null;
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			ArrayList<NetInp2> netInp2List = new ArrayList<NetInp2>();
			int pc=0, nc=0, c=0;
			
			String[] ld = br.readLine().split(";");
			while (ld!=null) {
				double cp = Double.parseDouble(ld[3]);				
				
				netInp2List.add(new NetInp2(0, 0, cp, 0, 0) );
				
				String s = null;
				for (int t=0;t<30;t++)
					s = br.readLine();
				if (s==null) break;
				ld = s.split(";");
			}
			
			for (int i=1;i<netInp2List.size();i++) {
				double apd = Math.log(netInp2List.get(i).avgprice/netInp2List.get(i-1).avgprice)*1000;
				netInp2List.get(i).avgpricediff = apd;
				
				int fpm = 0;
				for (int j=i+1;j<Math.min(i+12, netInp2List.size());j++)
					if (netInp2List.get(j).avgprice/netInp2List.get(i).avgprice > 1.0025) {
						fpm=1;
						pc++;
						break;
					} else if (netInp2List.get(i).avgprice/netInp2List.get(j).avgprice > 1.0025) {
						fpm=-1;
						nc++;
						break;
					}
				c++;
				
				netInp2List.get(i).futpmove = fpm;
			}
			
			/*while (netInp2List.size()>4320)
				netInp2List.remove(0);*/
			
			File fileout = new File("data/MLSTMtrainer/NetInp2List.jdat");
			fout = new FileOutputStream(fileout, false);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(netInp2List);
			oos.close();
			fout.close();
			
			System.out.println("positives: " + pc + " neutrals: " + (c-pc-nc) + " negatives: " + nc);
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
