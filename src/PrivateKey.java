import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

public class PrivateKey implements RSAPrivateKey {
	public BigInteger p;
	public BigInteger q;
	public BigInteger d;
	
	public PrivateKey(BigInteger p, BigInteger q, BigInteger d) {
		this.p = p;
		this.q = q;
		this.d = d;
	}

	@Override
	public BigInteger getPrivateExponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAlgorithm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getEncoded() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger getModulus() {
		return p.multiply(q);
	}
	
	public String toString() {
		String str = "";
		str += "p = " + p.toString() + "\n";
		str += "q = " + q.toString() + "\n";
		str += "d = " + d.toString() + "\n";
		return str;
	}

}
