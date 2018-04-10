package MLSTMtrainer;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NetInp2 implements Serializable {	
	public long timestamp;
	public double avgpricediff;
	public double avgprice;
	public double volume;
	public int futpmove;
	
	public NetInp2 (long ts) {
		this.timestamp=ts;
	}
	
	public NetInp2 (long ts, double apd, double ap, double v, int futpmove) {
		this.timestamp=ts;
		this.avgpricediff=apd;
		this.avgprice=ap;
		this.volume=v;
		this.futpmove=futpmove;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof NetInp ) {
			NetInp ot = (NetInp) other;
			if (ot.timestamp==this.timestamp ) return true;
			else return false;
		} else return false;
	}
}
