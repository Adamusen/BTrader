package MainCode;
import java.util.*;
import java.io.*;


@SuppressWarnings("unchecked")
public class DataC {
	public static void writeEverythingToFiles () {
		writeTradeListFile();
		writeOrderBookListFile();
	}
	
	private static void writeTradeListFile() {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		
		if (!PubBapi.tradeList.isEmpty() ) {
			File market = new File("data/Market");
			if (!market.exists() ) {
				market.mkdirs();
			}
			
			try {
				File tradeList = new File("data/Market/TradeList.jdat");
				if (!tradeList.exists() ) {
					tradeList.createNewFile();
					
					fout = new FileOutputStream(tradeList, false);
					oos = new ObjectOutputStream(fout);
					
					oos.writeObject(PubBapi.tradeList);
					
					oos.close();
					fout.close();
				} else {
					fin = new FileInputStream(tradeList);
					ois = new ObjectInputStream(fin);
					
					LinkedList<PubBapi.tradeTick> tempTL;
					tempTL = (LinkedList<PubBapi.tradeTick>) ois.readObject();
					
					ois.close();
					fin.close();
					
					if (!tempTL.isEmpty() ) {
						for (int i=0;i<PubBapi.tradeList.size();i++)
							if (PubBapi.tradeList.get(i).tradeid>tempTL.getLast().tradeid)
								tempTL.add(PubBapi.tradeList.get(i) );
					} else
						tempTL = PubBapi.tradeList;
					
					fout = new FileOutputStream(tradeList, false);
					oos = new ObjectOutputStream(fout);
					
					oos.writeObject(tempTL);
					
					oos.close();
					fout.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
	private static void writeOrderBookListFile() {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		
		if (!PubBapi.tradeList.isEmpty() ) {
			File market = new File("data/Market");
			if (!market.exists() ) {
				market.mkdirs();
			}
			
			try {
				File orderBookList = new File("data/Market/OrderBookList.jdat");
				if (!orderBookList.exists() ) {
					orderBookList.createNewFile();
					
					fout = new FileOutputStream(orderBookList, false);
					oos = new ObjectOutputStream(fout);
					
					oos.writeObject(PubBapi.orderBookList);
					
					oos.close();
					fout.close();
				} else {
					fin = new FileInputStream(orderBookList);
					ois = new ObjectInputStream(fin);
					
					LinkedList<PubBapi.orderBook> tempOBL;
					tempOBL = (LinkedList<PubBapi.orderBook>) ois.readObject();
					
					ois.close();
					fin.close();
					
					if (!tempOBL.isEmpty() ) {
						for (int i=0;i<PubBapi.orderBookList.size();i++)
							if (PubBapi.orderBookList.get(i).pctimestamp>tempOBL.getLast().pctimestamp)
								tempOBL.add(PubBapi.orderBookList.get(i) );
					} else
						tempOBL = PubBapi.orderBookList;
					
					fout = new FileOutputStream(orderBookList, false);
					oos = new ObjectOutputStream(fout);
					
					oos.writeObject(tempOBL);
					
					oos.close();
					fout.close();
				}
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}	
}
