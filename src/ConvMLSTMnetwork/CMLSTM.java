package ConvMLSTMnetwork;
import java.io.*;
import java.util.Arrays;

@SuppressWarnings("serial")
public class CMLSTM {
	
	public static CMLSTMnetwork loadMLSTMnetworkFromFile(File file) {
		CMLSTMnWeights ws = loadMLSTMnWeightFromFile(file);
		CMLSTMnetwork net = new CMLSTMnetwork(ws.iNs, ws.oNs, ws.hNs, ws.hLs, ws.softmaxoutput);
		net.ws = ws;
		return net;
	}
	
	public static CMLSTMnWeights loadMLSTMnWeightFromFile(File file) {
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		CMLSTMnWeights ws = null;		
		try {
			fin = new FileInputStream(file);
			ois = new ObjectInputStream(fin);
			ws = (CMLSTMnWeights) ois.readObject();			
			ois.close();
			fin.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return ws;
	}
	
	public static class CMLSTMnWeights implements Serializable {
		public final int iNs, oNs, hNs, hLs;
		public final boolean softmaxoutput;
		
		public double[][] Wix;
		public double[][][] Wil;
		public double[][][] Wim;
		public double[][] Wic;
		public double[][] bi;
		public double[][] Wfx;
		public double[][][] Wfl;
		public double[][][] Wfm;
		public double[][] Wfc;
		public double[][] bf;
		public double[][] Wcx;
		public double[][][] Wcl;
		public double[][][] Wcm;
		public double[][] bc;
		public double[][] Wox;
		public double[][][] Wol;
		public double[][][] Wom;
		public double[][] Woc;
		public double[][] bo;
		public double[][] Wym;
		public double[] by;
		
		public CMLSTMnWeights(int inputNvectSize, int outputNvectSize, int hiddenNs, int hiddenLayers, boolean softmaxoutput) {
			this.iNs = inputNvectSize;
			this.oNs = outputNvectSize;
			this.hNs = hiddenNs;
			this.hLs = hiddenLayers;
			this.softmaxoutput = softmaxoutput;
			
			Wix = new double[hiddenNs][inputNvectSize];
			Wil = new double[hiddenLayers-1][hiddenNs][hiddenNs];
			Wim = new double[hiddenLayers][hiddenNs][hiddenNs];
			Wic = new double[hiddenLayers][hiddenNs];
			bi = new double[hiddenLayers][hiddenNs];
			Wfx = new double[hiddenNs][inputNvectSize];
			Wfl = new double[hiddenLayers-1][hiddenNs][hiddenNs];
			Wfm = new double[hiddenLayers][hiddenNs][hiddenNs];
			Wfc = new double[hiddenLayers][hiddenNs];
			bf = new double[hiddenLayers][hiddenNs];
			Wcx = new double[hiddenNs][inputNvectSize];
			Wcl = new double[hiddenLayers-1][hiddenNs][hiddenNs];
			Wcm = new double[hiddenLayers][hiddenNs][hiddenNs];
			bc = new double[hiddenLayers][hiddenNs];
			Wox = new double[hiddenNs][inputNvectSize];
			Wol = new double[hiddenLayers-1][hiddenNs][hiddenNs];
			Wom = new double[hiddenLayers][hiddenNs][hiddenNs];
			Woc = new double[hiddenLayers][hiddenNs];
			bo = new double[hiddenLayers][hiddenNs];
			Wym = new double[outputNvectSize][hiddenNs];
			by = new double[outputNvectSize];
		}
		
		public CMLSTMnWeights(CMLSTMnWeights old) {
			this.iNs = new Integer(old.iNs);
			this.oNs = new Integer(old.oNs);
			this.hNs = new Integer(old.hNs);
			this.hLs = new Integer(old.hLs);
			this.softmaxoutput = new Boolean(old.softmaxoutput);
			
			Wix = new double[hNs][0];
			Wil = new double[hLs-1][hNs][0];
			Wim = new double[hLs][hNs][0];
			Wic = new double[hLs][0];
			bi = new double[hLs][0];
			Wfx = new double[hNs][0];
			Wfl = new double[hLs-1][hNs][0];
			Wfm = new double[hLs][hNs][0];
			Wfc = new double[hLs][0];
			bf = new double[hLs][0];
			Wcx = new double[hNs][0];
			Wcl = new double[hLs-1][hNs][0];
			Wcm = new double[hLs][hNs][0];
			bc = new double[hLs][0];
			Wox = new double[hNs][0];
			Wol = new double[hLs-1][hNs][0];
			Wom = new double[hLs][hNs][0];
			Woc = new double[hLs][0];
			bo = new double[hLs][0];
			Wym = new double[oNs][0];
			
			for (int i=0;i<hNs;i++) {
				this.Wix[i] = Arrays.copyOf(old.Wix[i], old.Wix[i].length);
				this.Wfx[i] = Arrays.copyOf(old.Wfx[i], old.Wfx[i].length);
				this.Wcx[i] = Arrays.copyOf(old.Wcx[i], old.Wcx[i].length);
				this.Wox[i] = Arrays.copyOf(old.Wox[i], old.Wox[i].length);
			}
			for (int i=0;i<hLs;i++)
				for (int j=0;j<hNs;j++) {
					this.Wim[i][j] = Arrays.copyOf(old.Wim[i][j], old.Wim[i][j].length);
					this.Wfm[i][j] = Arrays.copyOf(old.Wfm[i][j], old.Wfm[i][j].length);
					this.Wcm[i][j] = Arrays.copyOf(old.Wcm[i][j], old.Wcm[i][j].length);
					this.Wom[i][j] = Arrays.copyOf(old.Wom[i][j], old.Wom[i][j].length);
				}
			for (int i=0;i<hLs;i++) {
				this.Wic[i] = Arrays.copyOf(old.Wic[i], old.Wic[i].length);
				this.bi[i] = Arrays.copyOf(old.bi[i], old.bi[i].length);
				this.Wfc[i] = Arrays.copyOf(old.Wfc[i], old.Wfc[i].length);
				this.bf[i] = Arrays.copyOf(old.bf[i], old.bf[i].length);
				this.bc[i] = Arrays.copyOf(old.bc[i], old.bc[i].length);
				this.Woc[i] = Arrays.copyOf(old.Woc[i], old.Woc[i].length);
				this.bo[i] = Arrays.copyOf(old.bo[i], old.bo[i].length);
			}
			for (int i=0;i<hLs-1;i++)
				for (int j=0;j<hNs;j++) {
					this.Wil[i][j] = Arrays.copyOf(old.Wil[i][j], old.Wil[i][j].length);
					this.Wfl[i][j] = Arrays.copyOf(old.Wfl[i][j], old.Wfl[i][j].length);
					this.Wcl[i][j] = Arrays.copyOf(old.Wcl[i][j], old.Wcl[i][j].length);
					this.Wol[i][j] = Arrays.copyOf(old.Wol[i][j], old.Wol[i][j].length);
				}
			for (int i=0;i<oNs;i++) {
				this.Wym[i] = Arrays.copyOf(old.Wym[i], old.Wym[i].length);
			}
			this.by = Arrays.copyOf(old.by, old.by.length);
		}
		
		public void saveToFile(File file) {
			FileOutputStream fout = null;
			ObjectOutputStream oos = null;
			
			try {
				fout = new FileOutputStream(file, false);
				oos = new ObjectOutputStream(fout);
				oos.writeObject(this);
				oos.close();
				fout.close();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
		
		private static double[] vectornorm(double[] x) {
			/*double vsumm=0;
			for (int i=0;i<x.length;i++)
				vsumm += Math.abs(x[i]);*/
			double[] ret = new double[x.length];
			double denom = Math.sqrt(x.length);
			for (int i=0;i<x.length;i++)
				ret[i] = x[i] / denom;//vsumm;
			return ret;
		}
		
		public void randomWeights() {
			java.util.Random r = new java.util.Random();
			for (int i=0;i<hNs;i++) {
				for (int j=0;j<iNs;j++) {
					Wix[i][j] = r.nextGaussian();
					Wfx[i][j] = r.nextGaussian();
					Wcx[i][j] = r.nextGaussian();
					Wox[i][j] = r.nextGaussian();
				}
				Wix[i] = vectornorm(Wix[i]);
				Wfx[i] = vectornorm(Wfx[i]);
				Wcx[i] = vectornorm(Wcx[i]);
				Wox[i] = vectornorm(Wox[i]);
			}
			for (int i=0;i<hLs;i++)
				for (int j=0;j<hNs;j++) {
					for (int k=0;k<hNs;k++) {
						Wim[i][j][k] = r.nextGaussian();
						Wfm[i][j][k] = r.nextGaussian();
						Wcm[i][j][k] = r.nextGaussian();
						Wom[i][j][k] = r.nextGaussian();
					}
					Wim[i][j] = vectornorm(Wim[i][j]);
					Wfm[i][j] = vectornorm(Wfm[i][j]);
					Wcm[i][j] = vectornorm(Wcm[i][j]);
					Wom[i][j] = vectornorm(Wom[i][j]);
				}
			for (int i=0;i<hLs;i++)
				for (int j=0;j<hNs;j++) {
					Wic[i][j] = r.nextGaussian()/2.0;
					bi[i][j] = r.nextGaussian()/2.0;
					Wfc[i][j] = r.nextGaussian()/2.0;
					bf[i][j] = (r.nextGaussian()+1)/4.0;
					bc[i][j] = 0;
					Woc[i][j] = r.nextGaussian()/2.0;
					bo[i][j] = r.nextGaussian()/2.0;
				}
			for (int i=0;i<hLs-1;i++)
				for (int j=0;j<hNs;j++) {
					for (int k=0;k<hNs;k++) {
						Wil[i][j][k] = r.nextGaussian();
						Wfl[i][j][k] = r.nextGaussian();
						Wcl[i][j][k] = r.nextGaussian();
						Wol[i][j][k] = r.nextGaussian();
					}
					Wil[i][j] = vectornorm(Wil[i][j]);
					Wfl[i][j] = vectornorm(Wfl[i][j]);
					Wcl[i][j] = vectornorm(Wcl[i][j]);
					Wol[i][j] = vectornorm(Wol[i][j]);
				}
			for (int i=0;i<oNs;i++) {
				for (int j=0;j<hNs;j++)
					Wym[i][j] = r.nextGaussian();
				Wym[i] = vectornorm(Wym[i]);
				by[i] = 0;
			}
		}
		
	}
	
	public static class CMLSTMnState {
		public double[][] ctm1;
		public double[][] ct;
		public double[][] mtm1;
		public double[][] mt;
		
		public CMLSTMnState(double[][] ctm1, double[][] ct, double[][] mtm1, double[][] mt) {
			this.ctm1=ctm1;
			this.ct=ct;
			this.mtm1=mtm1;
			this.mt=mt;
		}
	}
	
	public static class CMLSTMnetwork {
		public final int iNs, oNs, hNs, hLs;
		public final boolean softmaxoutput;
		public double outputError=0;
		
		public double[] input;
		public double[] output;
		public double[][] ctm1;
		public double[][] ct;
		public double[][] mtm1;
		public double[][] mt;
		private double[][] cin;
		private double[][] it;
		private double[][] ot;
		private double[][] ft;
		public CMLSTMnWeights ws;
		
		public CMLSTMnetwork(int inputNvectSize, int outputNvectSize, int hiddenNs, int hiddenLayers, boolean softmaxoutput) {
			this.iNs = inputNvectSize;
			this.oNs = outputNvectSize;
			this.hNs = hiddenNs;
			this.hLs = hiddenLayers;
			this.softmaxoutput = softmaxoutput;
			
			input = new double[inputNvectSize];
			output = new double[outputNvectSize];			
			ctm1 = new double[hiddenLayers][hiddenNs];
			ct = new double[hiddenLayers][hiddenNs];
			mtm1 = new double[hiddenLayers][hiddenNs];
			mt = new double[hiddenLayers][hiddenNs];
			cin = new double[hiddenLayers][hiddenNs];
			it = new double[hiddenLayers][hiddenNs];
			ot = new double[hiddenLayers][hiddenNs];
			ft = new double[hiddenLayers][hiddenNs];
			this.ws = new CMLSTMnWeights(iNs, oNs, hNs, hLs, softmaxoutput);
		}
		
		public CMLSTMnetwork(CMLSTMnWeights weights) {
			this.iNs = new Integer(weights.iNs);
			this.oNs = new Integer(weights.oNs);
			this.hNs = new Integer(weights.hNs);
			this.hLs = new Integer(weights.hLs);
			this.softmaxoutput = new Boolean(weights.softmaxoutput);
			
			input = new double[iNs];
			output = new double[oNs];
			cin = new double[hLs][hNs];
			ctm1 = new double[hLs][hNs];
			ct = new double[hLs][hNs];
			mtm1 = new double[hLs][hNs];
			mt = new double[hLs][hNs];
			it = new double[hLs][hNs];
			ot = new double[hLs][hNs];
			ft = new double[hLs][hNs];
			this.ws = new CMLSTMnWeights(weights);
		}
		
		public CMLSTMnState getStates() {
			double[][] tctm1 = new double[hLs][0];
			double[][] tct = new double[hLs][0];
			double[][] tmtm1 = new double[hLs][0];
			double[][] tmt = new double[hLs][0];
			for (int i=0;i<hLs;i++) {
				tctm1[i] = Arrays.copyOf(ctm1[i], hNs);
				tct[i] = Arrays.copyOf(ct[i], hNs);
				tmtm1[i] = Arrays.copyOf(mtm1[i], hNs);
				tmt[i] = Arrays.copyOf(mt[i], hNs);
			}
			return new CMLSTMnState(tctm1, tct, tmtm1, tmt);
		}
		
		public void setStates(CMLSTMnState state) {
			for (int i=0;i<hLs;i++) {
				ctm1[i] = Arrays.copyOf(state.ctm1[i], hNs);
				ct[i] = Arrays.copyOf(state.ct[i], hNs);
				mtm1[i] = Arrays.copyOf(state.mtm1[i], hNs);
				mt[i] = Arrays.copyOf(state.mt[i], hNs);
			}
		}
		
		public void resetStates() {
			ctm1 = new double[hLs][hNs];
			ct = new double[hLs][hNs];
			mtm1 = new double[hLs][hNs];
			mt = new double[hLs][hNs];
		}
		
		private static double sigma(double x) {
			return 1 / (1 + Math.exp(-x) );
		}
		
		private static double tanh(double x) {
			return Math.tanh(x);
		}
		
		private static double scalarM(double[] a, double[] b) {
			double summ = 0;
			for (int i=0;i<a.length;i++) {
				summ += a[i]*b[i];
			}
			return summ;
		}
		
		private static double[] softmax(double[] z) {
			double[] ret = new double[z.length];
			double summ=0;
			for (int i=0;i<z.length;i++) {
				ret[i] = Math.exp(z[i]);
				summ += ret[i];
			}
			for (int i=0;i<z.length;i++) {
				ret[i] = ret[i] / summ;
			}
			return ret;
		}
		
		public double[] runNeural() {
			ctm1 = ct;
			mtm1 = mt;
			ct = new double[hLs][hNs];
			mt = new double[hLs][hNs];
			
			for (int i=0;i<hNs;i++) {
				it[0][i] = sigma( scalarM(input, ws.Wix[i]) + scalarM(mtm1[0], ws.Wim[0][i]) + ctm1[0][i]*ws.Wic[0][i] + ws.bi[0][i] );
				ft[0][i] = sigma( scalarM(input, ws.Wfx[i]) + scalarM(mtm1[0], ws.Wfm[0][i]) + ctm1[0][i]*ws.Wfc[0][i] + ws.bf[0][i] );
				cin[0][i] = tanh( scalarM(input, ws.Wcx[i]) + scalarM(mtm1[0], ws.Wcm[0][i]) + ws.bc[0][i] );
				ct[0][i] = ft[0][i]*ctm1[0][i] + it[0][i]*cin[0][i];
				ot[0][i] = sigma( scalarM(input, ws.Wox[i]) + scalarM(mtm1[0], ws.Wom[0][i]) + ct[0][i]*ws.Woc[0][i] + ws.bo[0][i] );
				mt[0][i] = ot[0][i] * tanh(ct[0][i]);
			}
			
			for (int k=1;k<hLs;k++)
				for (int i=0;i<hNs;i++) {
					it[k][i] = sigma( scalarM(mt[k-1], ws.Wil[k-1][i]) + scalarM(mtm1[k], ws.Wim[k][i]) + ctm1[k][i]*ws.Wic[k][i] + ws.bi[k][i] );
					ft[k][i] = sigma( scalarM(mt[k-1], ws.Wfl[k-1][i]) + scalarM(mtm1[k], ws.Wfm[k][i]) + ctm1[k][i]*ws.Wfc[k][i] + ws.bf[k][i] );
					cin[k][i] = tanh( scalarM(mt[k-1], ws.Wcl[k-1][i]) + scalarM(mtm1[k], ws.Wcm[k][i]) + ws.bc[k][i] );
					ct[k][i] = ft[k][i]*ctm1[k][i] + it[k][i]*cin[k][i];
					ot[k][i] = sigma( scalarM(mt[k-1], ws.Wol[k-1][i]) + scalarM(mtm1[k], ws.Wom[k][i]) + ct[k][i]*ws.Woc[k][i] + ws.bo[k][i] );
					mt[k][i] = ot[k][i] * tanh(ct[k][i]);
				}
			
			for (int i=0;i<oNs;i++) {
				output[i] = scalarM(mt[hLs-1], ws.Wym[i]) + ws.by[i];
				if (Double.isNaN(output[i]) ) {
					System.out.println("FUUUUCK");
					System.exit(0);
				}
			}
			
			if (softmaxoutput) output = softmax(output);
			
			return output;
		}
		
		public double calcOutputError(double[] expout) {
			outputError=0;
			if (softmaxoutput) {
				for (int i=0;i<oNs;i++)
					outputError-= expout[i]*Math.log(output[i]);// + (1-expout[i])*(Math.log(1-output[i]) );
				outputError = outputError / oNs;
			} else {
				for (int i=0;i<oNs;i++)
					outputError+= Math.pow(expout[i]-output[i], 2);
				outputError = Math.sqrt(outputError / oNs);
			}
			return outputError;
		}
		
		private static double clipGrad(double grad) {
			final double maxgrad = 5;
			if (Math.abs(grad) > maxgrad) {
				//System.out.println("Grad Clipped!");
				return Math.signum(grad) * maxgrad;
			}
			return grad;
		}
		
		public void trainNeural(double[] expout, double step) {
			calcOutputError(expout);
			
			double[][] mtdx = new double[hLs][hNs];
			for (int i=0;i<oNs;i++) {
				double opdx=(expout[i]-output[i]);
				//if (softmaxoutput) opdx*=outputError;
				opdx = clipGrad(opdx);
				ws.by[i] += opdx * step;
				for (int j=0;j<hNs;j++) {
					mtdx[hLs-1][j] += opdx * ws.Wym[i][j];
					mtdx[hLs-1][j] = clipGrad(mtdx[hLs-1][j]);
					ws.Wym[i][j] += opdx * mt[hLs-1][j] * step;
				}
			}
			
			for (int k=hLs-1; k>=0; k--) {
				for (int i=0;i<hNs;i++) {
					double otdxsm1 = mtdx[k][i]*tanh(ct[k][i]) * ot[k][i]*(1-ot[k][i]);
					otdxsm1 = clipGrad(otdxsm1);
					double ctdx = mtdx[k][i]*ot[k][i] * (1 - Math.pow(tanh(ct[k][i]), 2) )  +  otdxsm1 * ws.Woc[k][i];
					ctdx = clipGrad(ctdx);
					double cindxthm1 = ctdx*it[k][i] * (1 - Math.pow(cin[k][i], 2) );
					cindxthm1 = clipGrad(cindxthm1);
					double ftdxsm1 = ctdx*ctm1[k][i] * ft[k][i]*(1-ft[k][i]);
					ftdxsm1 = clipGrad(ftdxsm1);
					double itdxsm1 = ctdx*cin[k][i] * it[k][i]*(1-it[k][i]);
					itdxsm1 = clipGrad(itdxsm1);
					
					ws.bo[k][i] += otdxsm1 * step;
					ws.bc[k][i] += cindxthm1 * step;
					ws.bf[k][i] += ftdxsm1 * step;
					ws.bi[k][i] += itdxsm1 * step;
					ws.Woc[k][i] += otdxsm1 * ct[k][i] * step;
					ws.Wfc[k][i] += ftdxsm1 * ctm1[k][i] * step;
					ws.Wic[k][i] += itdxsm1 * ctm1[k][i] * step;
					for (int j=0;j<hNs;j++) {
						ws.Wom[k][i][j] += otdxsm1 * mtm1[k][j] * step;
						ws.Wcm[k][i][j] += cindxthm1 * mtm1[k][j] * step;
						ws.Wfm[k][i][j] += ftdxsm1 * mtm1[k][j] * step;
						ws.Wim[k][i][j] += itdxsm1 * mtm1[k][j] * step;
					}
					if (k>0) for (int j=0;j<hNs;j++) {
						mtdx[k-1][j] += otdxsm1 * ws.Wol[k-1][i][j];
						mtdx[k-1][j] += cindxthm1 * ws.Wcl[k-1][i][j];
						mtdx[k-1][j] += ftdxsm1 * ws.Wfl[k-1][i][j];
						mtdx[k-1][j] += itdxsm1 * ws.Wil[k-1][i][j];
						mtdx[k-1][j] = clipGrad(mtdx[k-1][j]);
						ws.Wol[k-1][i][j] += otdxsm1 * mt[k-1][j] * step;
						ws.Wcl[k-1][i][j] += cindxthm1 * mt[k-1][j] * step;
						ws.Wfl[k-1][i][j] += ftdxsm1 * mt[k-1][j] * step;
						ws.Wil[k-1][i][j] += itdxsm1 * mt[k-1][j] * step;
					} else for (int j=0;j<iNs;j++) {
						ws.Wox[i][j] += otdxsm1 * input[j] * step;
						ws.Wcx[i][j] += cindxthm1 * input[j] * step;
						ws.Wfx[i][j] += ftdxsm1 * input[j] * step;
						ws.Wix[i][j] += itdxsm1 * input[j] * step;
					}
					
				}
			}
		}
		
	}
	
}
