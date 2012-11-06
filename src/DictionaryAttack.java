import java.security.MessageDigest;
import java.io.*;

public class DictionaryAttack {

	public static String getHash(String text) throws NoAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		return digest.digest(text.getBytes("UTF-8")).toString();
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
		}
	}
	
	public static void main(String args[]) {
		String dict_file = "data/dictionary.txt";
		String hash = "";
		String text = attack(dict, hash);
		System.out.println("Hash = " + hash);
		System.out.println("Original text = " + text);
	}
}