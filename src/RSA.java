import java.math.BigInteger;
import java.security.Signature;

public class RSA {
	private PrivateKey _privateKey;	
	
	RSA(PrivateKey privateKey) {
		_privateKey = privateKey;
	}
	
	public byte[] sign(byte[] plainText) {
		byte[] signature;
		try {
			Signature instance = Signature.getInstance("SHA1withRSA");
			instance.initSign(_privateKey);
			instance.update(plainText);
			signature = instance.sign();
			System.out.println("Plain text: " + new String(plainText));
			System.out.println("Signature: " + new String(signature));
			return signature;
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
			return null;
		}		
	}
	
	public byte[] encode(byte[] plainText, PublicKey key) {
		try {
			BigInteger plain = new BigInteger(plainText);
			BigInteger cipher = plain.modPow(key.e, key.n);
			System.out.println("Plain text: " + new String(plainText));
			System.out.println("Cipher text: " + new String(cipher.toByteArray()));
			return cipher.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
}
