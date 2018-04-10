package MainCode;
import java.io.Serializable;
import java.util.LinkedList;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;


@SuppressWarnings("serial")
public class PubBapi {
	private static Pusher pusher = new Pusher("de504dc5763aeef9ff52");
	private static final String tradeEventName = "trade";
	private static final String dataEventName = "data";
	public static boolean pusherConnect = false;
	
	public static LinkedList<tradeTick> tradeList = new LinkedList<tradeTick>();
	public static LinkedList<orderBook> orderBookList = new LinkedList<orderBook>();
	
	
	
	
	
	public static class tradeTick implements Serializable {
		public int tradeid;
		public long pctimestamp;
		public double price;
		public double amount;
		
		public tradeTick(int tid) {
			this.tradeid=tid;
		}
		
		public tradeTick(int tid, double p, double a) {
			this.tradeid=tid;
			this.pctimestamp=System.currentTimeMillis() / 1000;
			this.price=p;
			this.amount=a;
		}
		
		@Override
		public boolean equals(Object other) {
			if (other instanceof tradeTick ) {
				tradeTick ot = (tradeTick) other;
				if (ot.tradeid==this.tradeid ) return true;
				else return false;
			} else return false;
		}
	}
	
	public static class orderBook implements Serializable {
		public LinkedList<orderData> bids;
		public LinkedList<orderData> asks;		
		public long pctimestamp;
		
		public orderBook(LinkedList<orderData> bids, LinkedList<orderData> asks) {
			this.bids = bids;
			this.asks = asks;		
			this.pctimestamp = System.currentTimeMillis() / 1000;
		}
	}
	
	public static class orderData implements Serializable {
		public double price;
		public double amount;
		
		public orderData(double p, double a) {
			this.price = p;
			this.amount = a;
		}
	}
	
	
	private static tradeTick createTradeTick(String msg) {
		int tid=-1;
		double p=0;
		double a=0;
		int s, e;
		
		try {
			s=msg.indexOf("price")+8;
			e=msg.indexOf(',', s);
			p=Double.parseDouble(msg.substring(s, e) );
			
			s=msg.indexOf("amount", e)+9;
			e=msg.indexOf(',', s);
			a=Double.parseDouble(msg.substring(s, e) );
			
			s=msg.indexOf("id", e)+5;
			e=msg.indexOf('}', s);
			tid=Integer.parseInt(msg.substring(s, e) );
			
			return new tradeTick(tid, p, a);
		} catch(Exception exc) {
			exc.printStackTrace();
			BTrader.addLogEntry("BPushapi's Trade tick data reading failed!");
		}
		
		return new tradeTick(-1, 0, 0);
	}
	
	private static orderBook createOrderBook(String msg) {
		LinkedList<orderData> bids = new LinkedList<orderData>();
		LinkedList<orderData> asks = new LinkedList<orderData>();
		int bsi, bei;
		int si, ei;
		int s, e;
		
		try {
			bsi = msg.indexOf("bids")+8;
			bei = msg.indexOf("]]", bsi)+1;
			String bstr = msg.substring(bsi, bei);
			
			si=0; ei=0;
			while (ei<bstr.length() ) {
				si = bstr.indexOf('[', ei);
				ei = bstr.indexOf(']', si)+1;
				
				String ostr = bstr.substring(si, ei);
				double p, a;
				
				s=ostr.indexOf('"')+1;
				e=ostr.indexOf('"', s);
				p=Double.parseDouble(ostr.substring(s, e) );
				
				s=e+4;
				e=ostr.indexOf('"', s);
				a=Double.parseDouble(ostr.substring(s, e) );
				
				bids.add(new orderData(p, a) );
			}
			
			
			bsi = msg.indexOf("asks", bei)+8;
			bei = msg.indexOf("]]", bsi)+1;
			String astr = msg.substring(bsi, bei);
			
			si=0; ei=0;
			while (ei<astr.length() ) {
				si = astr.indexOf('[', ei);
				ei = astr.indexOf(']', si)+1;
				
				String ostr = astr.substring(si, ei);
				double p, a;
				
				s=ostr.indexOf('"')+1;
				e=ostr.indexOf('"', s);
				p=Double.parseDouble(ostr.substring(s, e) );
				
				s=e+4;
				e=ostr.indexOf('"', s);
				a=Double.parseDouble(ostr.substring(s, e) );
				
				asks.add(new orderData(p, a) );
			}
			
			
			return new orderBook(bids, asks);
		} catch(Exception exc) {
			exc.printStackTrace();
			BTrader.addLogEntry("BPushapi's OrderBook data reading failed!");
		}
		
		return new orderBook(new LinkedList<orderData>(), new LinkedList<orderData>() );
	}
	
	public static double calcOrderBookImpulse(orderBook ob) {
		double price = (ob.bids.getFirst().price + ob.asks.getFirst().price) / 2.0;
		double drag = 0;		
		for (orderData od : ob.bids)
			drag += od.amount / ((od.price-price)*(od.price-price-1));
		for (orderData od : ob.asks)
			drag -= od.amount / ((od.price-price)*(od.price-price+1));		
		return drag / 10000.;
	}
	
	
	public static void connectPusherFirst() {
		pusherConnect = true;
		pusher.connect(new ConnectionEventListener() {
			int conntrys = 0;
			
		    @Override
		    public void onConnectionStateChange(ConnectionStateChange change) {
		        BTrader.addLogEntry("BPusher connection state changed to " + change.getCurrentState() + " from " + change.getPreviousState());
		        
		        if (change.getCurrentState().toString().equals("CONNECTED") ) {
		        	//Window.mntmConnectPusher.setEnabled(false);
		        	//Window.mntmDisconnectPusher.setEnabled(true);
		        	conntrys=0;
		        }
		        
		        if (change.getCurrentState().toString().equals("DISCONNECTED") ) {
		        	//Window.mntmConnectPusher.setEnabled(true);
		        	//Window.mntmDisconnectPusher.setEnabled(false);
		        	if (pusherConnect && conntrys<3) {
		        		if (conntrys>0) { try { Thread.sleep(5000); } catch (Exception e) { e.printStackTrace(); } }
		        		reconnectPusher();
		        		conntrys++;
		        	}
		        }
		        
		        if (change.getCurrentState().toString().equals("DISCONNECTING") ) {
		        	//Window.mntmConnectPusher.setEnabled(true);
		        	//Window.mntmDisconnectPusher.setEnabled(false);
		        }
		    }

		    @Override
		    public void onError(String message, String code, Exception e) {
		    	BTrader.addLogEntry("There was a problem connecting the BPusher! Code: " + code);
		        //e.printStackTrace();
		    }
		}, ConnectionState.ALL);
	}
	
	public static void reconnectPusher() {
		pusherConnect = true;
		pusher.connect();
	}
	
	public static void disconnectPusher() {
		pusherConnect = false;
		pusher.disconnect();
	}
	
	public static void subscribeLiveTradesChannel() {
		String chName = "live_trades";
		
		@SuppressWarnings("unused")
		Channel channel = pusher.subscribe(chName, new ChannelEventListener() {
		    @Override
		    public void onSubscriptionSucceeded(String channelName) {
		    	BTrader.addLogEntry("Subscribed to BPusher's live_trades channel");
		    }		    	
		    
		    @Override
		    public void onEvent(String channelName, String event, String data) {
		    	synchronized (PubBapi.tradeList ) {
		    		tradeList.add(createTradeTick(data) );
		    	}
		    	
		    	//BTrader.addLogEntry(tradeList.getLast().tradeid + ", " + tradeList.getLast().price + ", " + tradeList.getLast().amount + " on " + tradeList.getLast().pctimestamp);
		    }		    
		}, tradeEventName);
	}
	
	public static void subscribeOrderBookChannel() {
		String chName = "order_book";
		
		@SuppressWarnings("unused")
		Channel channel = pusher.subscribe(chName, new ChannelEventListener() {	
		    @Override
		    public void onSubscriptionSucceeded(String channelName) {
		    	BTrader.addLogEntry("Subscribed to BPusher's order_book channel");
		    }		    	
		    
		    @Override
		    public void onEvent(String channelName, String event, String data) {
		    	synchronized (PubBapi.orderBookList ) {
		    		orderBookList.add(createOrderBook(data) );
		    	}
		    	
		    	//BTrader.addLogEntry(orderBookList.getLast().bids.getFirst().price + ", " + orderBookList.getLast().bids.getFirst().amount + ";   " 
		    	//+ orderBookList.getLast().asks.getFirst().price + ", " + orderBookList.getLast().asks.getFirst().amount + " on " + orderBookList.getLast().pctimestamp);
		    }		    
		}, dataEventName);
	}
	
	public static void subscribeDiffOrderBookChannel() {
		String chName = "diff_order_book";
		
		@SuppressWarnings("unused")
		Channel channel = pusher.subscribe(chName, new ChannelEventListener() {
		    @Override
		    public void onSubscriptionSucceeded(String channelName) {
		    	BTrader.addLogEntry("Subscribed to BPusher's diff_order_book channel");
		    }		    	
		    
		    @Override
		    public void onEvent(String channelName, String event, String data) {
		    	BTrader.addLogEntry(data);
		    }		    
		}, dataEventName);
	}
}
