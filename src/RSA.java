import java.math.BigInteger;
import java.security.MessageDigest;

public class RSA {
	private PrivateKey _privateKey;	
	
	RSA(PrivateKey privateKey) {
		_privateKey = privateKey;
	}
	
	public byte[] sign(byte[] plainText) {
		byte[] signature;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.reset();
			byte[] hash = md.digest(plainText);
			BigInteger h = new BigInteger(hash);
			BigInteger n = _privateKey.p.add(BigInteger.ONE).multiply(_privateKey.q.add(BigInteger.ONE));
			signature = h.modPow(_privateKey.d, n).toByteArray();
			return signature;
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
			return null;
		}		
	}
	
}
