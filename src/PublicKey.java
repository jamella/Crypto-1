import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

public class PublicKey implements RSAPublicKey {
	BigInteger n;
	BigInteger e;
	
	public PublicKey(BigInteger n, BigInteger e) {
		this.e = e;
		this.n = n;
	}

	@Override
	public BigInteger getPublicExponent() {
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
		return n;
	}
}