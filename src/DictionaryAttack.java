import java.security.MessageDigest;
import java.io.*;
import java.math.BigInteger;

public class DictionaryAttack {

	public static String getHash(String text) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		digest.digest(text.getBytes("UTF-8"));
		String hash = new BigInteger(1, digest.digest(text.getBytes("UTF-8"))).toString(16);
		return hash;
	}
	
	public static String attack(String dictionaryFileName, String hashString) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(dictionaryFileName));
			String line = "";
			while((line = br.readLine()) != null) {				
				if(getHash(line).equalsIgnoreCase(hashString)) {				
					break;
				}
			}
			br.close();
			return line;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String args[]) {
		String dict_file = "data/dictionary.txt";
		String hash = "cc79c205fa8418393f26e3e72867dc99b64b4f82";
		String text = attack(dict_file, hash);
		System.out.println("Hash = " + hash);
		System.out.println("Original text = " + text);
	}
}