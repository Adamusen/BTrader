package MLSTMtrainer;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NetInp implements Serializable {
	final static double FIB = (1+Math.sqrt(5) )/2;
	final static double MINDIV = 0.0002;
	final static int INPNN = 25;
	
	public long timestamp;
	public double avgpricediff;
	public double avgprice;
	public double volume;
	public double fibN;
	
	public NetInp (long ts) {
		this.timestamp=ts;
	}
	
	public NetInp (long ts, double apd, double ap, double v) {
		this.timestamp=ts;
		this.avgpricediff=apd;
		this.avgprice=ap;
		this.volume=v;
		this.fibN=calcfibN(avgprice, avgprice-avgpricediff);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof NetInp ) {
			NetInp ot = (NetInp) other;
			if (ot.timestamp==this.timestamp ) return true;
			else return false;
		} else return false;
	}
	
	public static double calcfibN(double p1, double p0) {
		double sign = Math.signum(p1/p0-1);
		double n1 = (Math.max(p1, p0)/Math.min(p1, p0)-1)/MINDIV;
		double n2 = 0, n3 = 0;
		if (n1 > 0) n2 = Math.log(n1)/Math.log(FIB);
		if (n2 > 0) n3 = sign*n2;
		return n3;
	}
	
	public double[] makeInput() {
		double[] input = new double[INPNN];		
		if (fibN == 0)
			input[(INPNN-1)/2] = 1;
		else if (fibN > (INPNN-1)/2)
			input[INPNN-1] = 1;
		else if (fibN < -(INPNN-1)/2)
			input[0] = 1;
		else if (fibN > 0)
			input[(int)fibN+(INPNN+1)/2] = 1;
		else
			input[(INPNN-3)/2-(int)Math.abs(fibN)] = 1;
		return input;
	}
}
