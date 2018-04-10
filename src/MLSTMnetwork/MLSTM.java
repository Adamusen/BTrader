package MLSTMnetwork;
import java.io.*;
import java.util.Arrays;

@SuppressWarnings("serial")
public class MLSTM {
	
	public static MLSTMnetwork loadMLSTMnetworkFromFile(File file) {
		MLSTMnWeights ws = loadMLSTMnWeightFromFile(file);
		MLSTMnetwork net = new MLSTMnetwork(ws.iNs, ws.oNs, ws.hNs, ws.hLs, ws.softmaxoutput);
		net.ws = ws;
		return net;
	}
	
	public static MLSTMnWeights loadMLSTMnWeightFromFile(File file) {
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		MLSTMnWeights ws = null;		
		try {
			fin = new FileInputStream(file);
			ois = new ObjectInputStream(fin);
			ws = (MLSTMnWeights) ois.readObject();
			ois.close();
			fin.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return ws;
	}
	
	public static class MLSTMnWeights implements Serializable {
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
		
		public MLSTMnWeights(int inputNvectSize, int outputNvectSize, int hiddenNs, int hiddenLayers, boolean softmaxoutput) {
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
		
		public MLSTMnWeights(MLSTMnWeights old) {
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
		
		public void mutateWeights(int absN, double maxmutatepercent) {
			for (int i=0;i<absN;i++) {
				int r;
				if (hLs>1)
					r = (int)(Math.random()*16);
				else
					r = (int)(Math.random()*12);
					
				int r1, r2, r3;
				double mx = (Math.random()*2-1)*(maxmutatepercent/100);
				switch (r) {
					case 0:
						r1 = (int)(Math.random()*Wix.length);
						r2 = (int)(Math.random()*Wix[r1].length);
						Wix[r1][r2] = Wix[r1][r2]+mx;
						break;
					case 1:
						r1 = (int)(Math.random()*Wim.length);
						r2 = (int)(Math.random()*Wim[r1].length);
						r3 = (int)(Math.random()*Wim[r1][r2].length);
						Wim[r1][r2][r3] = Wim[r1][r2][r3]+mx;
						break;
					case 2:
						r1 = (int)(Math.random()*Wic.length);
						r2 = (int)(Math.random()*Wic[r1].length);
						Wic[r1][r2] = Wic[r1][r2]+mx;
						break;
					case 3:
						r1 = (int)(Math.random()*Wfx.length);
						r2 = (int)(Math.random()*Wfx[r1].length);
						Wfx[r1][r2] = Wfx[r1][r2]+mx;
						break;
					case 4:
						r1 = (int)(Math.random()*Wfm.length);
						r2 = (int)(Math.random()*Wfm[r1].length);
						r3 = (int)(Math.random()*Wfm[r1][r2].length);
						Wfm[r1][r2][r3] = Wfm[r1][r2][r3]+mx;
						break;
					case 5:
						r1 = (int)(Math.random()*Wfc.length);
						r2 = (int)(Math.random()*Wfc[r1].length);
						Wfc[r1][r2] = Wfc[r1][r2]+mx;
						break;
					case 6:
						r1 = (int)(Math.random()*Wcx.length);
						r2 = (int)(Math.random()*Wcx[r1].length);
						Wcx[r1][r2] = Wcx[r1][r2]+mx;
						break;
					case 7:
						r1 = (int)(Math.random()*Wcm.length);
						r2 = (int)(Math.random()*Wcm[r1].length);
						r3 = (int)(Math.random()*Wcm[r1][r2].length);
						Wcm[r1][r2][r3] = Wcm[r1][r2][r3]+mx;
						break;
					case 8:
						r1 = (int)(Math.random()*Wox.length);
						r2 = (int)(Math.random()*Wox[r1].length);
						Wox[r1][r2] = Wox[r1][r2]+mx;
						break;
					case 9:
						r1 = (int)(Math.random()*Wom.length);
						r2 = (int)(Math.random()*Wom[r1].length);
						r3 = (int)(Math.random()*Wom[r1][r2].length);
						Wom[r1][r2][r3] = Wom[r1][r2][r3]+mx;
						break;
					case 10:
						r1 = (int)(Math.random()*Woc.length);
						r2 = (int)(Math.random()*Woc[r1].length);
						Woc[r1][r2] = Woc[r1][r2]+mx;
						break;
					case 11:
						r1 = (int)(Math.random()*Wym.length);
						r2 = (int)(Math.random()*Wym[r1].length);
						Wym[r1][r2] = Wym[r1][r2]+mx;
						break;
					case 12:
						r1 = (int)(Math.random()*Wil.length);
						r2 = (int)(Math.random()*Wil[r1].length);
						r3 = (int)(Math.random()*Wil[r1][r2].length);
						Wil[r1][r2][r3] = Wil[r1][r2][r3]+mx;
						break;
					case 13:
						r1 = (int)(Math.random()*Wfl.length);
						r2 = (int)(Math.random()*Wfl[r1].length);
						r3 = (int)(Math.random()*Wfl[r1][r2].length);
						Wfl[r1][r2][r3] = Wfl[r1][r2][r3]+mx;
						break;
					case 14:
						r1 = (int)(Math.random()*Wcl.length);
						r2 = (int)(Math.random()*Wcl[r1].length);
						r3 = (int)(Math.random()*Wcl[r1][r2].length);
						Wcl[r1][r2][r3] = Wcl[r1][r2][r3]+mx;
						break;
					case 15:
						r1 = (int)(Math.random()*Wol.length);
						r2 = (int)(Math.random()*Wol[r1].length);
						r3 = (int)(Math.random()*Wol[r1][r2].length);
						Wol[r1][r2][r3] = Wol[r1][r2][r3]+mx;
						break;
				}
			}
		}
		
	}
	
	public static class MLSTMnState {
		public double[][] ctm1;
		public double[][] ct;
		public double[][] mtm1;
		public double[][] mt;
		
		public MLSTMnState(double[][] ctm1, double[][] ct, double[][] mtm1, double[][] mt) {
			this.ctm1=ctm1;
			this.ct=ct;
			this.mtm1=mtm1;
			this.mt=mt;
		}
	}
	
	public static class MLSTMnetwork {
		public final int iNs, oNs, hNs, hLs;
		public final boolean softmaxoutput;
		public double outputError=0;
		
		public double[] input;
		public double[] output;
		public double[][] ctm1;
		public double[][] ctm1dx;
		public double[][] ct;
		public double[][] actdx;
		public double[][] mtm1;
		public double[][] mtm1dx;
		public double[][] mt;
		public double[][] amtdx;
		private double[][] cin;
		private double[][] it;
		private double[][] ot;
		private double[][] ft;
		public MLSTMnWeights ws;
		
		public MLSTMnetwork(int inputNvectSize, int outputNvectSize, int hiddenNs, int hiddenLayers, boolean softmaxoutput) {
			this.iNs = inputNvectSize;
			this.oNs = outputNvectSize;
			this.hNs = hiddenNs;
			this.hLs = hiddenLayers;
			this.softmaxoutput = softmaxoutput;
			
			input = new double[inputNvectSize];
			output = new double[outputNvectSize];			
			ctm1 = new double[hiddenLayers][hiddenNs];
			ctm1dx = new double[hiddenLayers][hiddenNs];
			ct = new double[hiddenLayers][hiddenNs];
			actdx = new double[hiddenLayers][hiddenNs];
			mtm1 = new double[hiddenLayers][hiddenNs];
			mtm1dx = new double[hiddenLayers][hiddenNs];
			mt = new double[hiddenLayers][hiddenNs];
			amtdx = new double[hiddenLayers][hiddenNs];
			cin = new double[hiddenLayers][hiddenNs];
			it = new double[hiddenLayers][hiddenNs];
			ot = new double[hiddenLayers][hiddenNs];
			ft = new double[hiddenLayers][hiddenNs];
			this.ws = new MLSTMnWeights(iNs, oNs, hNs, hLs, softmaxoutput);
		}
		
		public MLSTMnetwork(MLSTMnWeights weights) {
			this.iNs = new Integer(weights.iNs);
			this.oNs = new Integer(weights.oNs);
			this.hNs = new Integer(weights.hNs);
			this.hLs = new Integer(weights.hLs);
			this.softmaxoutput = new Boolean(weights.softmaxoutput);
			
			input = new double[iNs];
			output = new double[oNs];
			cin = new double[hLs][hNs];
			ctm1 = new double[hLs][hNs];
			ctm1dx = new double[hLs][hNs];
			ct = new double[hLs][hNs];
			actdx = new double[hLs][hNs];
			mtm1 = new double[hLs][hNs];
			mtm1dx = new double[hLs][hNs];
			mt = new double[hLs][hNs];
			amtdx = new double[hLs][hNs];
			it = new double[hLs][hNs];
			ot = new double[hLs][hNs];
			ft = new double[hLs][hNs];
			this.ws = new MLSTMnWeights(weights);
		}
		
		public MLSTMnState getStates() {
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
			return new MLSTMnState(tctm1, tct, tmtm1, tmt);
		}
		
		public void setStates(MLSTMnState state) {
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
		
		public void l2regu(double lv) {
			for (int i=0;i<hNs;i++)
				for (int j=0;j<iNs;j++) {
					ws.Wix[i][j] *= lv;
					ws.Wfx[i][j] *= lv;
					ws.Wcx[i][j] *= lv;
					ws.Wox[i][j] *= lv;
				}
			for (int i=0;i<hLs;i++)
				for (int j=0;j<hNs;j++)
					for (int k=0;k<hNs;k++) {
						ws.Wim[i][j][k] *= lv;
						ws.Wfm[i][j][k] *= lv;
						ws.Wcm[i][j][k] *= lv;
						ws.Wom[i][j][k] *= lv;
					}
			for (int i=0;i<hLs;i++)
				for (int j=0;j<hNs;j++) {
					ws.Wic[i][j] *= lv;
					ws.Wfc[i][j] *= lv;
					ws.Woc[i][j] *= lv;
				}
			for (int i=0;i<hLs-1;i++)
				for (int j=0;j<hNs;j++)
					for (int k=0;k<hNs;k++) {
						ws.Wil[i][j][k] *= lv;
						ws.Wfl[i][j][k] *= lv;
						ws.Wcl[i][j][k] *= lv;
						ws.Wol[i][j][k] *= lv;
					}
			for (int i=0;i<oNs;i++)
				for (int j=0;j<hNs;j++)
					ws.Wym[i][j] *= lv;
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
			final double maxgrad = 1;
			if (Math.abs(grad) > maxgrad) {
				//System.out.println("Grad Clipped!");
				return Math.signum(grad) * maxgrad;
			}
			return grad;
		}
		
		public static double vectorSize(double[] v) {
			double sum=0;
			for (int i=0;i<v.length;i++)
				sum+=v[i]*v[i];
			return Math.sqrt(sum);
		}
		
		public static double[] propVector(double[] vprop, double[] vsamp) {
			double prop = vectorSize(vsamp)/vectorSize(vprop);
			double[] ret = new double[vprop.length];
			for (int i=0;i<ret.length;i++)
				ret[i] = vprop[i]*prop;
			return ret;
		}
		
		public void trainNeural(double[] expout, double step) {
			calcOutputError(expout);
			
			mtm1dx = new double[hLs][hNs];
			ctm1dx = new double[hLs][hNs];
			double[][] mtdx = new double[hLs][hNs];
			for (int k=0;k<hLs;k++)
				mtdx[k] = Arrays.copyOf(amtdx[k], amtdx[k].length);
			
			for (int i=0;i<oNs;i++) {
				double opdx=(expout[i]-output[i]);
				//if (!softmaxoutput) opdx*=outputError;
				ws.by[i] += clipGrad(opdx) * step;
				for (int j=0;j<hNs;j++) {
					mtdx[hLs-1][j] += opdx * ws.Wym[i][j];
					ws.Wym[i][j] += clipGrad(opdx) * mt[hLs-1][j] * step;
				}
			}
			
			for (int k=hLs-1; k>=0; k--) {
				for (int i=0;i<hNs;i++) {
					double otdxsm1 = mtdx[k][i]*tanh(ct[k][i]) * ot[k][i]*(1-ot[k][i]);
					otdxsm1 = clipGrad(otdxsm1);
					double ctdx = mtdx[k][i]*ot[k][i] * (1 - Math.pow(tanh(ct[k][i]), 2) )  +  otdxsm1 * ws.Woc[k][i]     +     actdx[k][i];
					ctdx = clipGrad(ctdx);
					double cindxthm1 = ctdx*it[k][i] * (1 - Math.pow(cin[k][i], 2) );
					cindxthm1 = clipGrad(cindxthm1);
					double ftdxsm1 = ctdx*ctm1[k][i] * ft[k][i]*(1-ft[k][i]);
					ftdxsm1 = clipGrad(ftdxsm1);
					double itdxsm1 = ctdx*cin[k][i] * it[k][i]*(1-it[k][i]);
					itdxsm1 = clipGrad(itdxsm1);
					
					ctm1dx[k][i] += ctdx * ft[k][i];
					ctm1dx[k][i] += ftdxsm1 * ws.Wfc[k][i];
					ctm1dx[k][i] += itdxsm1 * ws.Wic[k][i];
					ws.bo[k][i] += otdxsm1 * step;
					ws.bc[k][i] += cindxthm1 * step;
					ws.bf[k][i] += ftdxsm1 * step;
					ws.bi[k][i] += itdxsm1 * step;
					ws.Woc[k][i] += otdxsm1 * ct[k][i] * step;
					ws.Wfc[k][i] += ftdxsm1 * ctm1[k][i] * step;
					ws.Wic[k][i] += itdxsm1 * ctm1[k][i] * step;
					for (int j=0;j<hNs;j++) {
						mtm1dx[k][j] += otdxsm1 * ws.Wom[k][i][j];
						mtm1dx[k][j] += cindxthm1 * ws.Wcm[k][i][j];
						mtm1dx[k][j] += ftdxsm1 * ws.Wfm[k][i][j];
						mtm1dx[k][j] += itdxsm1 * ws.Wim[k][i][j];
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
				if (k>0)
					//mtdx[k-1] = propVector(mtdx[k-1], mtdx[hLs-1]);
				;
				//System.out.println(vectorSize(mtdx[k]) );
			}
		}
		
		private static double[] addArrays(double[] a1, double[] a2) {
			for (int i=0;i<a1.length;i++)
				a1[i] = a1[i] + a2[i];
			return a1;
		}
		
		private static double[][] addArrays(double[][] a1, double[][] a2) {
			for (int i=0;i<a1.length;i++)
				for (int j=0;j<a1[i].length;j++)
					a1[i][j] = a1[i][j] + a2[i][j];
			return a1;
		}
		
		private static double[][][] addArrays(double[][][] a1, double[][][] a2) {
			for (int i=0;i<a1.length;i++)
				for (int j=0;j<a1[i].length;j++)
					for (int k=0;k<a1[i][j].length;k++)
						a1[i][j][k] = a1[i][j][k] + a2[i][j][k];
			return a1;
		}
		
		private static double[] multiplyArray(double[] a1, double s) {
			for (int i=0;i<a1.length;i++)
				a1[i] *= s;
			return a1;
		}
		
		private static double[][] multiplyArray(double[][] a1, double s) {
			for (int i=0;i<a1.length;i++)
				for (int j=0;j<a1[i].length;j++)
					a1[i][j] *= s;
			return a1;
		}
		
		private static double[][][] multiplyArray(double[][][] a1, double s) {
			for (int i=0;i<a1.length;i++)
				for (int j=0;j<a1[i].length;j++)
					for (int k=0;k<a1[i][j].length;k++)
						a1[i][j][k] *= s;
			return a1;
		}
		
		public void trainBPTT(double[][] inputs, double[][] expouts, double step) {
			double steps[] = new double[inputs.length];
			for (int i=0;i<steps.length;i++)
				steps[i] = step;
			trainBPTT(inputs, expouts, steps);
		}
		
		public void trainBPTT(double[][] inputs, double[][] expouts, double steps[]) {
			//initialize variables
			final int BS = inputs.length;
			MLSTMnetwork[] nets = new MLSTMnetwork[BS];
			for (int i=0;i<BS;i++)
				nets[i] = new MLSTMnetwork(this.ws);
			
			//forward pass
			for (int k=0;k<hLs;k++) {
				nets[0].ct[k] = Arrays.copyOf(this.ctm1[k], this.ctm1[k].length);
				nets[0].mt[k] = Arrays.copyOf(this.mtm1[k], this.mtm1[k].length);
			}
			nets[0].input = inputs[0];
			nets[0].runNeural();
			for (int i=1;i<BS;i++) {
				for (int k=0;k<hLs;k++) {
					nets[i].ct[k] = Arrays.copyOf(nets[i-1].ct[k], nets[i-1].ct[k].length);
					nets[i].mt[k] = Arrays.copyOf(nets[i-1].mt[k], nets[i-1].mt[k].length);
				}
				nets[i].input = inputs[i];
				nets[i].runNeural();
			}
			
			//back propagation
			nets[BS-1].trainNeural(expouts[BS-1], steps[BS-1]);
			for (int i=BS-2;i>=0;i--) {
				if (softmaxoutput) for (int k=0;k<hLs;k++) {
					nets[i].actdx[k] = Arrays.copyOf(nets[i+1].ctm1dx[k], nets[i+1].ctm1dx[k].length);
					nets[i].amtdx[k] = Arrays.copyOf(nets[i+1].mtm1dx[k], nets[i+1].mtm1dx[k].length);
				}				
				
				//extra, töröld ha már nem kell
				/*if ( (Math.max(nets[i].output[1], Math.max(nets[i].output[0], nets[i].output[2] ) ) == nets[i].output[0]) && (expouts[i][2] == 1) ) {
					steps[i]*=1.5;
				} else if ( (Math.max(nets[i].output[1], Math.max(nets[i].output[0], nets[i].output[2] ) ) == nets[i].output[2]) && (expouts[i][0] == 1) ) {
					steps[i]*=1.5;
				} else if ( (Math.max(nets[i].output[1], Math.max(nets[i].output[0], nets[i].output[2] ) ) == nets[i].output[0]) && (expouts[i][1] == 1) ) {
					steps[i]*=1.1;
				} else if ( (Math.max(nets[i].output[1], Math.max(nets[i].output[0], nets[i].output[2] ) ) == nets[i].output[2]) && (expouts[i][1] == 1) ) {
					steps[i]*=1.1;
				} else if ( (Math.max(nets[i].output[1], Math.max(nets[i].output[0], nets[i].output[2] ) ) == nets[i].output[1]) && (expouts[i][0] == 1) ) {
					steps[i]*=1.25;
				} else if ( (Math.max(nets[i].output[1], Math.max(nets[i].output[0], nets[i].output[2] ) ) == nets[i].output[1]) && (expouts[i][2] == 1) ) {
					steps[i]*=1.25;
				}*/
				
				nets[i].trainNeural(expouts[i], steps[i]);
			}
			
			//calculate average weights
			ws.Wix = new double[hNs][iNs];
			ws.Wil = new double[hLs-1][hNs][hNs];
			ws.Wim = new double[hLs][hNs][hNs];
			ws.Wic = new double[hLs][hNs];
			ws.bi = new double[hLs][hNs];
			ws.Wfx = new double[hNs][iNs];
			ws.Wfl = new double[hLs-1][hNs][hNs];
			ws.Wfm = new double[hLs][hNs][hNs];
			ws.Wfc = new double[hLs][hNs];
			ws.bf = new double[hLs][hNs];
			ws.Wcx = new double[hNs][iNs];
			ws.Wcl = new double[hLs-1][hNs][hNs];
			ws.Wcm = new double[hLs][hNs][hNs];
			ws.bc = new double[hLs][hNs];
			ws.Wox = new double[hNs][iNs];		
			ws.Wol = new double[hLs-1][hNs][hNs];
			ws.Wom = new double[hLs][hNs][hNs];
			ws.Woc = new double[hLs][hNs];
			ws.bo = new double[hLs][hNs];
			ws.Wym = new double[oNs][hNs];
			ws.by = new double[oNs];
			for (int i=0;i<BS;i++) {
				ws.Wix = addArrays(ws.Wix, nets[i].ws.Wix);
				ws.Wil = addArrays(ws.Wil, nets[i].ws.Wil);
				ws.Wim = addArrays(ws.Wim, nets[i].ws.Wim);
				ws.Wic = addArrays(ws.Wic, nets[i].ws.Wic);
				ws.bi = addArrays(ws.bi, nets[i].ws.bi);
				ws.Wfx = addArrays(ws.Wfx, nets[i].ws.Wfx);
				ws.Wfl = addArrays(ws.Wfl, nets[i].ws.Wfl);
				ws.Wfm = addArrays(ws.Wfm, nets[i].ws.Wfm);
				ws.Wfc = addArrays(ws.Wfc, nets[i].ws.Wfc);
				ws.bf = addArrays(ws.bf, nets[i].ws.bf);
				ws.Wcx = addArrays(ws.Wcx, nets[i].ws.Wcx);
				ws.Wcl = addArrays(ws.Wcl, nets[i].ws.Wcl);
				ws.Wcm = addArrays(ws.Wcm, nets[i].ws.Wcm);
				ws.bc = addArrays(ws.bc, nets[i].ws.bc);
				ws.Wox = addArrays(ws.Wox, nets[i].ws.Wox);
				ws.Wol = addArrays(ws.Wol, nets[i].ws.Wol);
				ws.Wom = addArrays(ws.Wom, nets[i].ws.Wom);
				ws.Woc = addArrays(ws.Woc, nets[i].ws.Woc);
				ws.bo = addArrays(ws.bo, nets[i].ws.bo);
				ws.Wym = addArrays(ws.Wym, nets[i].ws.Wym);
				ws.by = addArrays(ws.by, nets[i].ws.by);
			}
			final double avgdivisor = 1.0 / BS;
			ws.Wix = multiplyArray(ws.Wix, avgdivisor);
			ws.Wil = multiplyArray(ws.Wil, avgdivisor);
			ws.Wim = multiplyArray(ws.Wim, avgdivisor);
			ws.Wic = multiplyArray(ws.Wic, avgdivisor);
			ws.bi = multiplyArray(ws.bi, avgdivisor);
			ws.Wfx = multiplyArray(ws.Wfx, avgdivisor);
			ws.Wfl = multiplyArray(ws.Wfl, avgdivisor);
			ws.Wfm = multiplyArray(ws.Wfm, avgdivisor);
			ws.Wfc = multiplyArray(ws.Wfc, avgdivisor);
			ws.bf = multiplyArray(ws.bf, avgdivisor);
			ws.Wcx = multiplyArray(ws.Wcx, avgdivisor);
			ws.Wcl = multiplyArray(ws.Wcl, avgdivisor);
			ws.Wcm = multiplyArray(ws.Wcm, avgdivisor);
			ws.bc = multiplyArray(ws.bc, avgdivisor);
			ws.Wox = multiplyArray(ws.Wox, avgdivisor);
			ws.Wol = multiplyArray(ws.Wol, avgdivisor);
			ws.Wom = multiplyArray(ws.Wom, avgdivisor);
			ws.Woc = multiplyArray(ws.Woc, avgdivisor);
			ws.bo = multiplyArray(ws.bo, avgdivisor);
			ws.Wym = multiplyArray(ws.Wym, avgdivisor);
			ws.by = multiplyArray(ws.by, avgdivisor);
		}
		
	}
	
}
