package MLSTMnetwork;
import MLSTMnetwork.MLSTM.MLSTMnWeights;
import MLSTMnetwork.MLSTM.MLSTMnetwork;

public class Test {
	public static void test() {
		int[] testseq = {3, 5, 9, 4, 4, 0, 1, 0, 2, 5, 7, 9, 8, 3, 7, 1, 6, 2, 6, 8, 7, 4, 3, 5, 9, 3, 1, 1, 6, 0, 7, 0, 4, 7, 5, 6, 0, 4, 4, 1, 3, 7, 5, 4, 0, 8, 2, 1, 6, 3, 9};
		//int[] testseq = {1, 3, 2, 4, 4, 0, 1, 2, 3, 0};
		int vc = 10;
		
		MLSTMnetwork ann = new MLSTMnetwork(vc, vc, 20, 2, true);
		double[] out = new double[vc];
		double[] expout = new double[vc];
		double preverr=1000;
		double err=0;
		
		ann.ws.randomWeights();
		//ann.ws = loadMLSTMnWeightFromFile(new File("data/Neural/Test.jdat"));
		
		int count = 0;
		int badcount = 0;
		double step = 0.03;
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
				badcount=0;
			} else {
				System.out.println("Bad " + err + " > " + preverr + " c:" + count);
				badcount++;
				if (badcount>100) {
					ann.ws.mutateWeights(ann.hNs*ann.hLs/5, 10);
					badcount=0;
				}
			}
			
			count++;
			//try { Thread.sleep(100); } catch (Exception e) { e.printStackTrace(); }
			/*if (count>3000) {
				ann.ws.saveToFile(new File("data/Neural/Test.jdat"));
				System.exit(0);
			}*/
			
		}
	}
	
	public static void test2() {
		int[] testseq = {3, 5, 9, 4, 4, 0, 1, 0, 2, 5, 7, 9, 8, 3, 7, 1, 6, 2, 6, 8, 7, 4, 3, 5, 9, 3, 1, 1, 6, 0, 7, 0, 4, 7, 5, 6, 0, 4, 4, 1, 3, 7, 5, 4, 0, 8, 2, 1, 6, 3, 9};
		//int[] testseq = {1, 3, 2, 4, 4, 0, 1, 2, 3, 0};
		//int vc = 10;
		
		MLSTMnetwork ann = new MLSTMnetwork(1, 1, 20, 2, false);
		double[] out = new double[1];
		double[] expout = new double[1];
		double preverr=1000;
		double err=0;
		
		ann.ws.randomWeights();
		//ann.ws = loadMLSTMnWeightFromFile(new File("data/Neural/Test.jdat"));
		
		int count = 0;
		int badcount = 0;
		double step = 0.03;
		while (true) {
			err=0;
			//ann.resetStates();
			
			for (int i=0;i<testseq.length;i++) {
				ann.input[0] = testseq[i % testseq.length] / 10.;
				expout[0] = testseq[(i+1) % testseq.length] / 10.;
				/*for (int j=0;j<vc;j++) {
					if (j==testseq[i % testseq.length])
						ann.input[j] = 1;
					else
						ann.input[j] = 0;
					
					if (j==testseq[(i+1) % testseq.length])
						expout[j] = 1;
					else
						expout[j] = 0;
				}*/
				
				out=ann.runNeural();
				
				/*double bmerr = ann.calcOutputError(expout);
				MLSTMnWeights bmws = new MLSTMnWeights(ann.ws);
				MLSTM.MLSTMnState bmsts = ann.getStates();
				ann.ws.mutateWeights(5, 2);*/
				
				ann.trainNeural(expout, step);
				//ann.ws.pulldownWeights(0.00000);
				
				/*double max=0;
				for (double o : out) {
					max=Math.max(max, o);
				}*/
				int winner=(int)Math.round(out[0]*10);
				/*for (int k=0;k<vc;k++)
					if (max==out[k]) {
						winner=k;
						break;
					}*/
				System.out.print(winner + ", ");
				
				/*for (int k=0;k<vc;k++) {
					if (testseq[((i+1) % testseq.length)]==k) {
						err += Math.abs(1-out[k]);
					} else {
						err += Math.abs(out[k]);
					}						
					//System.out.print(out[k] + ", ");
				}
				//System.out.println(ann.ct[0]);*/
				err += ann.outputError;
			}
			System.out.println(" ");
			
			if (err<preverr) {
				preverr=err;
				System.out.println("Good " + err + " < " + preverr + " c:" + count);
				badcount=0;
			} else {
				System.out.println("Bad " + err + " > " + preverr + " c:" + count);
				badcount++;
				/*if (badcount>100) {
					ann.ws.mutateWeights(ann.hNs*ann.hLs/5, 10);
					badcount=0;
				}*/
			}
			
			count++;
			//try { Thread.sleep(100); } catch (Exception e) { e.printStackTrace(); }
			/*if (count>3000) {
				ann.ws.saveToFile(new File("data/Neural/Test.jdat"));
				System.exit(0);
			}*/
			
		}
	}
	
	public static void test3() {
		int[] testseq = {3, 5, 9, 4, 4, 0, 1, 0, 2, 5, 7, 9, 8, 3, 7, 1, 6, 2, 6, 8, 7, 4, 3, 5, 9, 3, 1, 1, 6, 0, 7, 0, 4, 7, 5, 6, 0, 4, 4, 1, 3, 7, 5, 4, 0, 8, 2, 1, 6, 3, 9};
		//int[] testseq = {1, 3, 2, 4, 4, 0, 1, 2, 3, 0};
		//int vc = 10;
		
		MLSTMnetwork ann = new MLSTMnetwork(1, 1, 20, 2, false);
		double[] out = new double[1];
		double[] expout = new double[1];
		double preverr=1000;
		double err=0;
		
		ann.ws.randomWeights();
		//ann.ws = loadMLSTMnWeightFromFile(new File("data/Neural/Test.jdat"));
		
		int count = 0;
		int badcount = 0;
		double step = 0.03;
		while (true) {
			err=0;
			//ann.resetStates();
			
			for (int i=0;i<testseq.length;i++) {
				final int bsize = 20;
				double[][] inputs = new double[bsize][1];
				double[][] expouts = new double[bsize][1];
				
				for (int k=0;k<bsize;k++) {
					inputs[k][0] = testseq[(i+k) % testseq.length] / 10.;
					expouts[k][0] = testseq[(i+k+1) % testseq.length] / 10.;
				}
				
				ann.input[0] = testseq[i % testseq.length] / 10.;
				expout[0] = testseq[(i+1) % testseq.length] / 10.;
				/*for (int j=0;j<vc;j++) {
					if (j==testseq[i % testseq.length])
						ann.input[j] = 1;
					else
						ann.input[j] = 0;
					
					if (j==testseq[(i+1) % testseq.length])
						expout[j] = 1;
					else
						expout[j] = 0;
				}*/
				
				out=ann.runNeural();
				
				/*double bmerr = ann.calcOutputError(expout);
				MLSTMnWeights bmws = new MLSTMnWeights(ann.ws);
				MLSTM.MLSTMnState bmsts = ann.getStates();
				ann.ws.mutateWeights(5, 2);*/
				
				//ann.trainNeural(expout, step);				
				ann.trainBPTT(inputs, expouts, step);
				
				//ann.ws.pulldownWeights(0.00000);
				
				/*double max=0;
				for (double o : out) {
					max=Math.max(max, o);
				}*/
				int winner=(int)Math.round(out[0]*10);
				/*for (int k=0;k<vc;k++)
					if (max==out[k]) {
						winner=k;
						break;
					}*/
				System.out.print(winner + ", ");
				
				/*for (int k=0;k<vc;k++) {
					if (testseq[((i+1) % testseq.length)]==k) {
						err += Math.abs(1-out[k]);
					} else {
						err += Math.abs(out[k]);
					}						
					//System.out.print(out[k] + ", ");
				}
				//System.out.println(ann.ct[0]);*/
				ann.calcOutputError(expout);
				err += ann.outputError;
			}
			System.out.println(" ");
			
			if (err<preverr) {
				preverr=err;
				System.out.println("Good " + err + " < " + preverr + " c:" + count);
				badcount=0;
			} else {
				System.out.println("Bad " + err + " > " + preverr + " c:" + count);
				badcount++;
				/*if (badcount>100) {
					ann.ws.mutateWeights(ann.hNs*ann.hLs/5, 10);
					badcount=0;
				}*/
			}
			
			count++;
			//try { Thread.sleep(100); } catch (Exception e) { e.printStackTrace(); }
			/*if (count>3000) {
				ann.ws.saveToFile(new File("data/Neural/Test.jdat"));
				System.exit(0);
			}*/
			
		}
	}
	
	public static void test4() {
		//int[] testseq = {3, 5, 9, 4, 4, 0, 1, 0, 2, 5, 7, 9, 8, 3, 7, 1, 6, 2, 6, 8, 7, 4, 3, 5, 9, 3, 1, 1, 6, 0, 7, 0, 4, 7, 5, 6, 0, 4, 4, 1, 3, 7, 5, 4, 0, 8, 2, 1, 6, 3, 9};
		int[] testseq = {3, 5, 9, 4, 4, 0, 1, 0, 2, 5, 7, 9, 8, 3, 7, 1, 6, 2, 6, 8, 7, 4, 3, 5, 9, 3, 1, 1, 6, 0, 7, 0, 4, 7, 5, 6, 0, 4, 4, 1, 3, 7, 5, 4, 0, 8, 2, 1, 6, 3, 9, 1, 7, 3, 0, 1, 0, 5, 6, 4, 9, 4, 2, 1, 5, 8, 8, 2, 7, 4, 2, 0, 6, 9, 9, 2, 6, 5, 4, 3, 1, 8, 2, 2, 2, 2, 0, 3};
		//int[] testseq = {1, 3, 2, 4, 4, 0, 1, 2, 3, 0};
		int vc = 10;
		
		MLSTMnetwork ann = new MLSTMnetwork(vc, vc, 20, 2, true);
		double[] out = new double[vc];
		//double[] expout = new double[vc];
		double preverr=1000;
		double err=0;
		
		ann.ws.randomWeights();
		//ann.ws = loadMLSTMnWeightFromFile(new File("data/Neural/Test.jdat"));
		
		int count = 0;
		int badcount = 0;
		double step = 0.1;
		while (true) {
			err=0;
			//ann.resetStates();
			
			for (int i=0;i<testseq.length;i++) {
				final int bsize = 5;
				double[][] inputs = new double[bsize][vc];
				double[][] expouts = new double[bsize][vc];
				
				for (int k=0;k<bsize;k++) {			
					for (int j=0;j<vc;j++) {
						if (j==testseq[(i+k) % testseq.length])
							inputs[k][j] = 1;
						else
							inputs[k][j] = 0;
						
						if (j==testseq[(i+k+1) % testseq.length])
							expouts[k][j] = 1;
						else
							expouts[k][j] = 0;
					}
				}
				
				ann.input = inputs[0];
				out=ann.runNeural();
				ann.trainBPTT(inputs, expouts, step);
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
				badcount=0;
			} else {
				System.out.println("Bad " + err + " > " + preverr + " c:" + count);
				badcount++;
				if (badcount>100) {
					ann.ws.mutateWeights(ann.hNs*ann.hLs/5, 10);
					badcount=0;
				}
			}
			
			count++;
			//try { Thread.sleep(100); } catch (Exception e) { e.printStackTrace(); }
			/*if (count>100) {
				//ann.ws.saveToFile(new File("data/Neural/Test.jdat"));
				System.exit(0);
			}*/
			
		}
	}
	
	public static void test5() {
		//int[] testseq = {3, 5, 9, 4, 4, 0, 1, 0, 2, 5, 7, 9, 8, 3, 7, 1, 6, 2, 6, 8, 7, 4, 3, 5, 9, 3, 1, 1, 6, 0, 7, 0, 4, 7, 5, 6, 0, 4, 4, 1, 3, 7, 5, 4, 0, 8, 2, 1, 6, 3, 9};
		int[] testseq = {3, 5, 9, 4, 4, 0, 1, 0, 2, 5, 7, 9, 8, 3, 7, 1, 6, 2, 6, 8, 7, 4, 3, 5, 9, 3, 1, 1, 6, 0, 7, 0, 4, 7, 5, 6, 0, 4, 4, 1, 3, 7, 5, 4, 0, 8, 2, 1, 6, 3, 9, 1, 7, 3, 0, 1, 0, 5, 6, 4, 9, 4, 2, 1, 5, 8, 8, 2, 7, 4, 2, 0, 6, 9, 9, 2, 6, 5, 4, 3, 1, 8, 2, 2, 2, 2, 0, 3};
		//int[] testseq = {1, 3, 2, 4, 4, 0, 1, 2, 3, 0};
		int vc = 10, ip = 5;
		
		MLSTMnetwork ann = new MLSTMnetwork(ip, vc, 30, 2, true);
		double[] out = new double[vc];
		//double[] expout = new double[vc];
		double preverr=1000;
		double err=0;
		
		ann.ws.randomWeights();
		//ann.ws = loadMLSTMnWeightFromFile(new File("data/Neural/Test.jdat"));
		
		int count = 0;
		int badcount = 0;
		double step = 0.1;
		while (true) {
			err=0;
			//ann.resetStates();
			
			for (int i=0;i<testseq.length;i++) {
				final int bsize = 5;
				double[][] inputs = new double[bsize][ip];
				double[][] expouts = new double[bsize][vc];
				
				for (int k=0;k<bsize;k++) {
					for (int z=0;z<ip;z++)
						inputs[k][z] = testseq[(testseq.length+i+k-z) % testseq.length] / 10.;
					for (int j=0;j<vc;j++) {						
						if (j==testseq[(i+k+1) % testseq.length])
							expouts[k][j] = 1;
						else
							expouts[k][j] = 0;
					}
				}
				
				ann.input = inputs[0];
				out=ann.runNeural();
				ann.trainBPTT(inputs, expouts, step);
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
				badcount=0;
			} else {
				System.out.println("Bad " + err + " > " + preverr + " c:" + count);
				badcount++;
				if (badcount>100) {
					ann.ws.mutateWeights(ann.hNs*ann.hLs/5, 10);
					badcount=0;
				}
			}
			
			count++;
			//try { Thread.sleep(100); } catch (Exception e) { e.printStackTrace(); }
			/*if (count>100) {
				//ann.ws.saveToFile(new File("data/Neural/Test.jdat"));
				System.exit(0);
			}*/
			
		}
	}
	
}
