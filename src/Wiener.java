import java.math.*;
import java.util.LinkedList;


public class Wiener {
	
	class WienerResult {
		public BigInteger d;
		public BigInteger p;
		public BigInteger q;
		public boolean success;
		
		public WienerResult() {
			this.success = false;
		}
		
		WienerResult(BigInteger d, BigInteger p, BigInteger q, boolean success) {
			this.d = d;
			this.p = p;
			this.q = q;
			this.success = success;
		}
	}
	
	public final static BigInteger zero = BigInteger.ZERO;
	public final static BigInteger one = BigInteger.ONE;
	public final static BigInteger two = new BigInteger("2");
	
	private WienerResult _result;
	
	Wiener() {
		_result = new WienerResult();
	}	
	
	public void check(PublicKey key) {
		BigInteger e = key.e;
		BigInteger n = key.n;
		// System.out.println(e.subtract(n.pow(2).multiply(sqrt(n))).toString());
		if(e.compareTo(n.pow(2).multiply(sqrt(n))) > 0)
			System.out.println("e > n^(1.5) -> this is not guaranteed to work");
	}
	
	public boolean attack(PublicKey key) {		
		BigInteger e = key.e;
		BigInteger n = key.n;
		
		check(key);
//		BigInteger max_d = sqrt(sqrt(n)).divide(new BigInteger("3"));
		try {
			LinkedList<BigInteger> q = new LinkedList<BigInteger>();
			LinkedList<Double> r = new LinkedList<Double>();
			LinkedList<BigInteger> k = new LinkedList<BigInteger>();
			LinkedList<BigInteger> dg = new LinkedList<BigInteger>();
			int i = -1;
			while (true) {			
				i++;
				// System.out.println(i);
				BigInteger q_i;
				double r_i;
				
				// Find q[i] and r[i]
				if (i == 0) {
					// q[0] = floor(e/n)
					q_i = e.divide(n);
					// r[0] = (e/n) - q[0]
					r_i = (e.doubleValue() / n.doubleValue()) - q_i.doubleValue();
				} else {
					// q[i] = floor(1 / r[i-1])
					q_i = BigInteger.valueOf((long)Math.floor(1/(r.getLast())));
					// r[i] = (1 / r[i-1]) - q[i]
					r_i = (1 / r.getLast()) - q_i.doubleValue();
				}
				System.out.println("q[" + i + "] = " + q_i);
				q.add(q_i);
				r.add(r_i);
				
				// ed = 1 (mod lcm(p-1,q-1))
				// ed = K * lcm(p-1,q-1) + 1
				// Let G = gcd(p-1,q-1), k = K/gcd(K,G), g = G/gcd(K,G)
				// ed = K/G * (p-1)(q-1) + 1 = k/g * (p-1)(q-1) + 1
				// e/n = e/(pq) = k/(dg) * (1 - delta)
				// where delta = (p + q - 1 - g/k) / (pq)
				// edg = k * (p-1)(q-1) + 1
				// phi(n) = (p-1)*(q-1)
				// (pq + (p-1)(q-1) + 1)/2 = (p+q)/2
				// ((p+q)/2)^2 - pq = ((p-q)/2)^2
				//
				// We estimate e/n = k/(dg)
				// Guess of k and dg based on continued fraction q[i]
				if(i == 0) {
					// k[0] = q[0]
					k.add(q.get(0));
					// dg[0] = 1
					dg.add(one);
				} else if ( i == 1) {
					// k[1] = q[0]*q[1] + 1
					k.add((q.get(0)).multiply(q.get(1)).add(one));
					// dg[1] = q[1]
					dg.add(q.get(1));
				} else {
					// k[i] = q[i]*k[i-1] + k[i-2]
					k.add(q.get(i).multiply(k.get(i-1)).add(k.get(i-2)));
					// dg[i] = q[i]*dg[i-1] + dg[i-2]
					dg.add(q.get(i).multiply(dg.get(i-1)).add(dg.get(i-2)));
				}
				
//				BigInteger[] k_divide_dg = reconstructFraction(q);
//				k.add(k_divide_dg[0]);
//				dg.add(k_divide_dg[1]);
				
				BigInteger k_i, dg_i;
				dg_i = dg.get(i);
				k_i = k.get(i);
				if(i % 2 == 0) {
					k_i = k.get(i).add(one);
				} else {
					k_i = k.get(i);
				}
				System.out.println("k[" + i + "] = " + k_i);
				System.out.println("dg[" + i + "] = " + dg_i);
				// kdg < (pq)/((3/2)*(p+q))
//				if(k_i.multiply(dg_i).compareTo(n)> 0) {
//					System.out.println("kdg > n");
//					return false;
//				}
				if(k_i.compareTo(e) > 0) {
					System.out.println("k > e");
					return false;
				}
				BigInteger edg = e.multiply(dg_i);
				BigInteger phi_n = edg.divide(k_i);
				BigInteger g = edg.mod(k_i);
				// Check g, g should be greater than 0
				System.out.println("g = " + g.toString());
				if(g.compareTo(zero) == 0)
					continue;
				BigInteger guess_d = dg_i.divide(g);
				System.out.println("d = " + guess_d.toString());
				// System.out.println("(1/3) * n^(1/4) = " + max_d);
				// Check d
//				if(guess_d.compareTo(max_d) > 0) {
//					System.out.println("d > (1/3) * n^(1/4)");
//					return false;
//				}
				
				// (p+q)/2 = (pq - (p-1)*(q-1) + 1)/2
				if(n.subtract(phi_n).add(one).mod(two).compareTo(zero) == 0) {
					BigInteger p_plus_q_div_2 = n.subtract(phi_n).add(one).divide(two);
					if(!isSquare(p_plus_q_div_2.pow(2).subtract(n))) {
						// System.out.println("not square");
						continue;
					}
					// ((p-q)/2)^2 = ((p+q)/2)^2 - pq
					BigInteger p_minus_q_div_2 = sqrt(p_plus_q_div_2.pow(2).subtract(n));
					// p = (p+q)/2 + (p-q)/2
					BigInteger guess_p = p_plus_q_div_2.add(p_minus_q_div_2);
					// q = (p+q)/2 - (p-q)/2
					BigInteger guess_q = p_plus_q_div_2.subtract(p_minus_q_div_2);				
					this._result = new WienerResult(guess_d, guess_p, guess_q, true);
					System.out.println("Success");
					return true;
				}
			}
		} catch (Exception ex) {
			ex.getMessage();
			ex.printStackTrace();
			return false;
		}
		
	}
	
	/*
	 * Reconstruct the continued fraction n[i]/d[i] from <q[0],q[1],...,q[i]>
	 */
	public BigInteger[] reconstructFraction(LinkedList<BigInteger> q) {
		BigInteger[] result = new BigInteger[2];
		BigInteger n_i = zero;
		BigInteger n_i_1 = zero;
		BigInteger n_i_2 = zero;
		BigInteger d_i = zero;
		BigInteger d_i_1 = zero;
		BigInteger d_i_2 = zero;
		for(int i = 0; i < q.size(); i++) {
			if(i == 0) {
				n_i = q.get(0).add(one);
				d_i = one;
			} else if (i == 1) {
				n_i = q.get(1).multiply(q.get(0)).add(one);
				d_i = q.get(1);
			} else {
				if((i == q.size()-1) && (i % 2 == 0)) {
					n_i = q.get(i).multiply(n_i_1).add(n_i_2);
					d_i = q.get(i).multiply(d_i_1).add(d_i_2);
				} else {
					n_i = q.get(i).multiply(n_i_1).add(n_i_2);
					d_i = q.get(i).multiply(d_i_1).add(d_i_2);
				}
			}
			n_i_1 = n_i;
			n_i_2 = n_i_1;
			d_i_1 = d_i;
			d_i_2 = d_i_1;
		}
		result[0] = n_i;
		result[1] = d_i;
		return result;
	}
	
	public PrivateKey getResult() {
		return new PrivateKey(_result.p, _result.q, _result.d);
	}
	
	public void printResult() {
		if(_result.success) {
			System.out.println("p = " + _result.p.toString());
			System.out.println("q = " + _result.q.toString());
			System.out.println("d = " + _result.d.toString());
		} else {
			System.out.println("Unable to get the private key!");
		}
	}
	
	public static boolean isSquare(BigInteger n) {
		if(sqrt(n).pow(2).subtract(n).compareTo(zero) != 0)
			return false;
		else return true;
	}
	
	public static BigInteger sqrt(BigInteger n) {
		BigInteger a = one;
		BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
		while(b.compareTo(a) >= 0) {
			BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
			if(mid.multiply(mid).compareTo(n) > 0)
				b = mid.subtract(one);
			else a = mid.add(one);
		}
		return a.subtract(one);
	}

}
