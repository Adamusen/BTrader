package TrashCode;
import java.util.Arrays;

public class LSTM {
	
	public static class LSTMnWeights {
		private final int iNs, oNs, hNs, wsN;
		
		double[][] Wix;
		double[][] Wim;
		double[] Wic;
		double[] bi;
		double[][] Wfx;
		double[][] Wfm;
		double[] Wfc;
		double[] bf;
		double[][] Wcx;
		double[][] Wcm;
		double[] bc;
		double[][] Wox;
		double[][] Wom;
		double[] Woc;
		double[] bo;
		double[][] Wym;
		double[] by;
		
		public LSTMnWeights(int inputNvectSize, int outputNvectSize, int hiddenNs) {
			this.iNs = inputNvectSize;
			this.oNs = outputNvectSize;
			this.hNs = hiddenNs;
			this.wsN = 4*hNs*iNs + 4*hNs*hNs + 3*hNs + oNs*hNs + 4*hNs + oNs;
			
			Wix = new double[hiddenNs][inputNvectSize];
			Wim = new double[hiddenNs][hiddenNs];
			Wic = new double[hiddenNs];
			bi = new double[hiddenNs];
			Wfx = new double[hiddenNs][inputNvectSize];
			Wfm = new double[hiddenNs][hiddenNs];
			Wfc = new double[hiddenNs];
			bf = new double[hiddenNs];
			Wcx = new double[hiddenNs][inputNvectSize];
			Wcm = new double[hiddenNs][hiddenNs];
			bc = new double[hiddenNs];
			Wox = new double[hiddenNs][inputNvectSize];
			Wom = new double[hiddenNs][hiddenNs];
			Woc = new double[hiddenNs];
			bo = new double[hiddenNs];
			Wym = new double[outputNvectSize][hiddenNs];
			by = new double[outputNvectSize];
		}
		
		public LSTMnWeights(LSTMnWeights old) {
			this.iNs = new Integer(old.iNs);
			this.oNs = new Integer(old.oNs);
			this.hNs = new Integer(old.hNs);
			this.wsN = new Integer(old.wsN);
			
			Wix = new double[hNs][0];
			Wim = new double[hNs][0];
			Wfx = new double[hNs][0];
			Wfm = new double[hNs][0];
			Wcx = new double[hNs][0];
			Wcm = new double[hNs][0];
			Wox = new double[hNs][0];
			Wom = new double[hNs][0];
			Wym = new double[oNs][0];
			
			for (int i=0;i<hNs;i++) {
				this.Wix[i] = Arrays.copyOf(old.Wix[i], old.Wix[i].length);
				this.Wfx[i] = Arrays.copyOf(old.Wfx[i], old.Wfx[i].length);
				this.Wcx[i] = Arrays.copyOf(old.Wcx[i], old.Wcx[i].length);
				this.Wox[i] = Arrays.copyOf(old.Wox[i], old.Wox[i].length);
				this.Wim[i] = Arrays.copyOf(old.Wim[i], old.Wim[i].length);
				this.Wfm[i] = Arrays.copyOf(old.Wfm[i], old.Wfm[i].length);
				this.Wcm[i] = Arrays.copyOf(old.Wcm[i], old.Wcm[i].length);
				this.Wom[i] = Arrays.copyOf(old.Wom[i], old.Wom[i].length);
			}
			this.Wic = Arrays.copyOf(old.Wic, old.Wic.length);
			this.bi = Arrays.copyOf(old.bi, old.bi.length);
			this.Wfc = Arrays.copyOf(old.Wfc, old.Wfc.length);
			this.bf = Arrays.copyOf(old.bf, old.bf.length);
			this.bc = Arrays.copyOf(old.bf, old.bf.length);
			this.Woc = Arrays.copyOf(old.Woc, old.Woc.length);
			this.bo = Arrays.copyOf(old.bo, old.bo.length);
			for (int i=0;i<oNs;i++) {
				this.Wym[i] = Arrays.copyOf(old.Wym[i], old.Wym[i].length);
			}
			this.by = Arrays.copyOf(old.by, old.by.length);
		}
		
		private double[] vectornorm(double[] x) {
			/*double vsumm=0;
			for (int i=0;i<x.length;i++)
				vsumm += Math.abs(x[i]);*/
			double[] ret = new double[x.length];
			for (int i=0;i<x.length;i++)
				ret[i] = x[i] / x.length;//vsumm;
			
			return ret;
		}
		
		public void randomWeights() {
			for (int i=0;i<hNs;i++) {
				for (int j=0;j<iNs;j++) {
					Wix[i][j] = Math.random()*2-1;
					Wfx[i][j] = Math.random()*2-1;
					Wcx[i][j] = Math.random()*2-1;
					Wox[i][j] = Math.random()*2-1;
				}
				Wix[i] = vectornorm(Wix[i]);
				Wfx[i] = vectornorm(Wfx[i]);
				Wcx[i] = vectornorm(Wcx[i]);
				Wox[i] = vectornorm(Wox[i]);
			}
				
			for (int i=0;i<hNs;i++) {
				for (int j=0;j<hNs;j++) {
					Wim[i][j] = Math.random()*2-1;
					Wfm[i][j] = Math.random()*2-1;
					Wcm[i][j] = Math.random()*2-1;
					Wom[i][j] = Math.random()*2-1;
				}
				Wim[i] = vectornorm(Wim[i]);
				Wfm[i] = vectornorm(Wfm[i]);
				Wcm[i] = vectornorm(Wcm[i]);
				Wom[i] = vectornorm(Wom[i]);
			}
				
			for (int i=0;i<hNs;i++) {
				Wic[i] = Math.random()-0.5;
				bi[i] = Math.random()-0.5;
				Wfc[i] = Math.random()-0.5;
				bf[i] = Math.random()/2.0;
				bc[i] = 0;
				Woc[i] = Math.random()-0.5;
				bo[i] = Math.random()-0.5;
			}
			for (int i=0;i<oNs;i++) {
				for (int j=0;j<hNs;j++) {
					Wym[i][j] = Math.random()*2-1;
				}
				by[i] = 0;
			}				
		}
		
		public void pulldownWeights(double factor) {
			double f = 1-factor;
			
			for (int i=0;i<Wix.length;i++)
				for (int j=0;j<Wix[i].length;j++) {
					Wix[i][j] = Wix[i][j]*f;
					Wfx[i][j] = Wfx[i][j]*f;
					Wcx[i][j] = Wcx[i][j]*f;
					Wox[i][j] = Wox[i][j]*f;
				}											
			for (int i=0;i<Wim.length;i++)
				for (int j=0;j<Wim[i].length;j++) {
					Wim[i][j] = Wim[i][j]*f;
					Wfm[i][j] = Wfm[i][j]*f;
					Wcm[i][j] = Wcm[i][j]*f;
					Wom[i][j] = Wom[i][j]*f;
				}				
			for (int i=0;i<Wic.length;i++) {
				Wic[i] = Wic[i]*f;
				Wfc[i] = Wfc[i]*f;
				Woc[i] = Woc[i]*f;
			}
			for (int i=0;i<Wym.length;i++)
				for (int j=0;j<Wym[i].length;j++) {
					Wym[i][j] = Wym[i][j]*f;
				}
		}
		
		public void mutateWeights(int absN, double maxmutatepercent) {
			for (int i=0;i<absN;i++) {
				int r = (int)(Math.random()*wsN);
				double mx = (Math.random()*2-1)*(maxmutatepercent/100);
				
				if (r<4*hNs*iNs) {
					int c = r / hNs*iNs;
					switch (c) {
						case 0:
							Wix[r / iNs][r % iNs] += mx;
							break;
						case 1:
							r -= hNs*iNs;
							Wfx[r / iNs][r % iNs] += mx;
							break;
						case 2:
							r -= 2*hNs*iNs;
							Wcx[r / iNs][r % iNs] += mx;
							break;
						case 3:
							r -= 3*hNs*iNs;
							Wox[r / iNs][r % iNs] += mx;
							break;
					}
				} else {
					r -= 4*hNs*iNs;
					if (r<4*hNs*hNs) {
						int c = r / hNs*hNs;
						switch (c) {
							case 0:
								Wim[r / hNs][r % hNs] += mx;
								break;
							case 1:
								r -= hNs*hNs;
								Wfm[r / hNs][r % hNs] += mx;
								break;
							case 2:
								r -= 2*hNs*hNs;
								Wcm[r / hNs][r % hNs] += mx;
								break;
							case 3:
								r -= 3*hNs*hNs;
								Wom[r / hNs][r % hNs] += mx;
								break;
						}
					} else {
						r -= 4*hNs*hNs;
						if (r<3*hNs) {
							int c = r / hNs;
							switch (c) {
								case 0:
									Wic[r] += mx;
									break;
								case 1:
									r -= hNs;
									Wfc[r] += mx;
									break;
								case 2:
									r -= 2*hNs;
									Woc[r] += mx;
									break;
							}
						} else {
							r -= 3*hNs;
							if (r<oNs*hNs) {
								Wym[r / hNs][r % hNs] += mx;
							} else {
								r -= oNs*hNs;
								if (r<4*hNs) {
									int c = r / hNs;
									switch (c) {
										case 0:
											bi[r] += mx;
											break;
										case 1:
											r -= hNs;
											bf[r] += mx;
											break;
										case 2:
											r -= 2*hNs;
											bc[r] += mx;
											break;
										case 3:
											r -= 3*hNs;
											bo[r] += mx;
											break;
									}
								} else {
									r -= 4*hNs;
									by[r] += mx;
								}
							}							
						}
					}
				}
			}
		}
		
	}
	
	public static class LSTMnetwork {
		private final int iNs, oNs, hNs;
		
		public double[] input;
		private double[] output;
		private double[] cin;
		private double[] ctm1;
		private double[] ct;
		private double[] mtm1;
		private double[] mt;
		private double[] it;
		private double[] ot;
		private double[] ft;
		LSTMnWeights ws;
		
		public LSTMnetwork(int inputNvectSize, int outputNvectSize, int hiddenNs) {
			this.iNs = inputNvectSize;
			this.oNs = outputNvectSize;
			this.hNs = hiddenNs;
			
			input = new double[inputNvectSize];
			output = new double[outputNvectSize];
			cin = new double[hiddenNs];
			ctm1 = new double[hiddenNs];
			ct = new double[hiddenNs];
			mtm1 = new double[hiddenNs];
			mt = new double[hiddenNs];
			it = new double[hiddenNs];
			ot = new double[hiddenNs];
			ft = new double[hiddenNs];
			this.ws = new LSTMnWeights(iNs, oNs, hNs);
		}
		
		public void resetStates() {
			ct = new double[hNs];
			mt = new double[hNs];
		}
		
		private double sigma(double x) {
			return 1 / (1 + Math.exp(-x) );
		}
		
		private double tanh(double x) {
			return (Math.exp(2*x)-1)/(Math.exp(2*x)+1);
		}
		
		private double scalarM(double[] a, double[] b) {
			double summ = 0;
			for (int i=0;i<a.length;i++) {
				summ += a[i]*b[i];
			}
			return summ;
		}
		
		private double[] softmax(double[] z) {
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
			ct = new double[hNs];
			mt = new double[hNs];
			
			for (int i=0;i<hNs;i++) {
				it[i] = sigma( scalarM(input, ws.Wix[i]) + scalarM(mtm1, ws.Wim[i]) + ctm1[i]*ws.Wic[i] + ws.bi[i] );
				ft[i] = sigma( scalarM(input, ws.Wfx[i]) + scalarM(mtm1, ws.Wfm[i]) + ctm1[i]*ws.Wfc[i] + ws.bf[i] );
				cin[i] = tanh( scalarM(input, ws.Wcx[i]) + scalarM(mtm1, ws.Wcm[i]) + ws.bc[i] );
				ct[i] = ft[i]*ctm1[i] + it[i]*cin[i];
				ot[i] = sigma( scalarM(input, ws.Wox[i]) + scalarM(mtm1, ws.Wom[i]) + ct[i]*ws.Woc[i] + ws.bo[i] );
				mt[i] = ot[i] * tanh(ct[i]);
			}
			
			for (int i=0;i<oNs;i++)
				output[i] = scalarM(mt, ws.Wym[i]) + ws.by[i];
			output = softmax(output);
			
			return output;
		}
		
		public void trainNeural(double[] expout, double step) {
			double crossLoss=0;
			for (int i=0;i<oNs;i++)
				crossLoss-= expout[i]*Math.log(output[i]) + (1-expout[i])*(Math.log(1-output[i]) );
			crossLoss = crossLoss / oNs;
			
			double[] mtdx = new double[hNs];
			for (int i=0;i<oNs;i++) {
				double opdx=(expout[i]-output[i])*crossLoss;
				ws.by[i] += opdx * step;
				for (int j=0;j<hNs;j++) {
					mtdx[j] += opdx * ws.Wym[i][j];
					ws.Wym[i][j] += opdx * mt[j] * step;
				}
			}
			
			for (int i=0;i<hNs;i++) {
				double otdxsm1 = mtdx[i]*tanh(ct[i]) * ot[i]*(1-ot[i]);
				double ctdx = mtdx[i]*ot[i] * (1 - Math.pow(tanh(ct[i]), 2) )  +  otdxsm1 * ws.Woc[i];
				double cindxthm1 = ctdx*it[i] * (1 - Math.pow(cin[i], 2) );
				double ftdxsm1 = ctdx*ctm1[i] * ft[i]*(1-ft[i]);
				double itdxsm1 = ctdx*cin[i] * it[i]*(1-it[i]);
				
				ws.bo[i] += otdxsm1 * step;
				ws.bc[i] += cindxthm1 * step;
				ws.bf[i] += ftdxsm1 * step;
				ws.bi[i] += itdxsm1 * step;
				ws.Woc[i] += otdxsm1 * ct[i] * step;
				ws.Wfc[i] += ftdxsm1 * ctm1[i] * step;
				ws.Wic[i] += itdxsm1 * ctm1[i] * step;
				for (int j=0;j<hNs;j++) {
					ws.Wom[i][j] += otdxsm1 * mtm1[j] * step;
					ws.Wcm[i][j] += cindxthm1 * mtm1[j] * step;
					ws.Wfm[i][j] += ftdxsm1 * mtm1[j] * step;
					ws.Wim[i][j] += itdxsm1 * mtm1[j] * step;
				}
				for (int j=0;j<iNs;j++) {
					ws.Wox[i][j] += otdxsm1 * input[j] * step;
					ws.Wcx[i][j] += cindxthm1 * input[j] * step;
					ws.Wfx[i][j] += ftdxsm1 * input[j] * step;
					ws.Wix[i][j] += itdxsm1 * input[j] * step;
				}
			}
			
		}
	}
	
	
	public static void test() {
		int[] testseq = {3, 5, 9, 4, 4, 0, 1, 0, 2, 5, 7, 9, 8, 3, 7, 1, 6, 2, 6, 8, 7, 4, 3, 5, 9, 3, 1, 1, 6, 0, 7, 0, 4, 7, 5, 6, 0, 4, 4, 1, 3};
		//int[] testseq = {1, 3, 2, 4, 4, 0, 1, 2, 3, 0};
		int vc = 10;
		
		LSTMnetwork ann = new LSTMnetwork(vc, vc, 20);
		double[] out = new double[vc];
		double[] expout = new double[vc];
		double preverr=1000;
		double err=0;
		
		ann.ws.randomWeights();
		
		int count = 0;
		double step = 0.1;
		while (true) {
			err=0;
			//ann.resetStates();
			
			for (int i=0;i<testseq.length;i++) {
				for (int j=0;j<vc;j++) {
					if (j==testseq[i % testseq.length])
						ann.input[j] = 1;
					else
						ann.input[j] = 0;
					
					if (j==testseq[(i+1) % testseq.length])
						expout[j] = 1;
					else
						expout[j] = 0;
				}
				
				out=ann.runNeural();
				ann.trainNeural(expout, step);
				//ann.ws.pulldownWeights(0.00000);
				
				double max=0;
				for (double o : out) {
					max=Math.max(max, o);
				}
				int winner=-1;
				for (int k=0;k<vc;k++)
					if (max==out[k]) {
						winner=k;
						break;
					}
				System.out.print(winner + ", ");
				
				for (int k=0;k<vc;k++) {
					if (testseq[((i+1) % testseq.length)]==k) {
						err += Math.abs(1-out[k]);
					} else {
						err += Math.abs(out[k]);
					}						
					//System.out.print(out[k] + ", ");
				}
				//System.out.println(ann.ct[0]);
			}
			System.out.println(" ");
			
			if (err<preverr) {
				preverr=err;
				System.out.println("Good " + err + " < " + preverr + " c:" + count);
			} else {
				System.out.println("Bad " + err + " > " + preverr + " c:" + count);
			}
			
			count++;
			//try { Thread.sleep(100); } catch (Exception e) { e.printStackTrace(); }
			//if (count>10) System.exit(0);
		}
	}
	
	public static void test2() {
		//int[] testseq = {3, 5, 9, 4, 4, 0, 1, 0, 2, 5, 7, 9, 8, 3, 7, 1, 6, 2, 6, 8};
		int[] testseq = {1, 3, 2, 4, 4, 0, 1, 2, 3, 0};
		int vc = 5;
		
		LSTMnetwork ann = new LSTMnetwork(vc, vc, 8);		
		
		double[] out;
		double preverr=0;
		double err=0;
		
		ann.ws.randomWeights();
		LSTMnWeights oldws = new LSTMnWeights(ann.ws);
		for (int i=0;i<testseq.length*4;i++) {
			for (int j=0;j<vc;j++) {
				if (j==testseq[i % testseq.length])
					ann.input[j] = 1;
				else
					ann.input[j] = 0;
			}
			out=ann.runNeural();
			for (int k=0;k<vc;k++) {
				if (testseq[((i+1) % testseq.length)]==k)
					preverr += Math.abs(1-out[k]);
				else
					preverr += Math.abs(out[k]);
			}
		}
		
		int count = 0;
		while (true) {
			err=0;
			ann.resetStates();
			if (preverr>60) {
				ann.ws.mutateWeights(20, 10d);
			} else if (preverr>30) {
				ann.ws.mutateWeights(10, 10d);
			} else if (preverr>20) {
				ann.ws.mutateWeights(10, 5d);
			} else if (preverr>10) {
				ann.ws.mutateWeights(5, 5d);
			} else if (preverr>5) {
				ann.ws.mutateWeights(5, 2d);
			} else {
				ann.ws.mutateWeights(2, 2d);
			}
			//ann.mutateWeights(20, 5d);
			
			for (int i=0;i<testseq.length*4;i++) {
				for (int j=0;j<vc;j++) {
					if (j==testseq[i % testseq.length])
						ann.input[j] = 1;
					else
						ann.input[j] = 0;
				}
				out=ann.runNeural();
				
				double max=-1.0E5f;
				for (double o : out) {
					max=Math.max(max, o);
				}
				int winner=-1;
				for (int k=0;k<vc;k++)
					if (max==out[k]) {
						winner=k;
						break;
					}
				System.out.print(winner + ", ");
				
				for (int k=0;k<vc;k++) {
					if (testseq[((i+1) % testseq.length)]==k) {
						err += Math.abs(1-out[k]);
					} else {
						err += Math.abs(out[k]);
					}						
					//System.out.print(out[k] + ", ");
				}
				//System.out.println(" ");
			}
			System.out.println(" ");
			
			if (err<preverr) {
				oldws = new LSTMnWeights(ann.ws);
				preverr=err;
				System.out.println("Good " + err + " < " + preverr + " c:" + count);
			} else {
				ann.ws = new LSTMnWeights(oldws);
				System.out.println("Bad " + err + " > " + preverr + " c:" + count);
			}
			
			count++;
		}
	}
	
}
