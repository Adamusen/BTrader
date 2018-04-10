package MLSTMtrainer;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TrainData implements Serializable {
	public long timestamp;
	public double avgprice;
	public double volume;
	
	public TrainData (long ts) {
		this.timestamp=ts;
	}
	
	public TrainData (long ts, double ap, double v) {
		this.timestamp=ts;
		this.avgprice=ap;
		this.volume=v;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof TrainData ) {
			TrainData ot = (TrainData) other;
			if (ot.timestamp==this.timestamp ) return true;
			else return false;
		} else return false;
	}
}
