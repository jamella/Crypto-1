/**
 * @author LE TAN KHOA
 *
 */

import java.io.*;
import java.math.BigInteger;

public class Main {
			
	public static byte[] readFromFile(String filename) {				
		try {
			File file = new File(filename);
			byte[] data = new byte[(int)file.length()];
			InputStream input = new BufferedInputStream(new FileInputStream(file));
			DataInputStream dis = new DataInputStream(input);
			dis.readFully(data);
			dis.close();
			return data;
		} catch (FileNotFoundException e) {			
			System.out.println("File not found!");
		} catch (IOException e) {
			e.getMessage();
		}
		return null;
	}
	
	public static void writeToFile(String filename, byte[] data) {
		try {
			File file = new File(filename);			
			OutputStream output = new BufferedOutputStream(new FileOutputStream(file));
			DataOutputStream dos = new DataOutputStream(output);
			dos.write(data);
			dos.close();
		} catch (FileNotFoundException e) {
			e.getMessage();
			System.out.println("File not found!");
		} catch (IOException e) {
			e.getMessage();
		}
	}
	
	public static void main(String[] args) {
		try {
//			BigInteger e = new BigInteger("2621");
////			BigInteger e = new BigInteger("17993");
//			BigInteger n = new BigInteger("8927");
////			BigInteger n = new BigInteger("90581");
//			System.out.println("e = " + e.toString());
//			System.out.println("n = " + n.toString());
//			PublicKey key = new PublicKey(n, e);
//			
//			Wiener Mallory = new Wiener();
//			Mallory.attack(key);
//			Mallory.printResult();

//		Map<String, String> keyPair = new HashMap<String, String>();
//		keyPair.put("esmart.bin", "Nsmart.bin");
//		for(int i = 1; i < 14; i++) {
//			keyPair.put("e" + i + ".bin", "n" + i + ".bin");
//		}
//		
//		for(Map.Entry<String, String> entry : keyPair.entrySet()) {
//			try {
//				BigInteger e = new BigInteger(readFromFile(entry.getKey()));
//				BigInteger n = new BigInteger(readFromFile(entry.getValue()));
//				System.out.println("e = " + e.toString());
//				System.out.println("n = " + n.toString());
//				PublicKey key = new PublicKey(n, e);
//				
//				Wiener Mallory = new Wiener();
//				Mallory.attack(key);
//				Mallory.printResult();
//			} catch (Exception e) {}
//		}
			
			BigInteger e = new BigInteger(readFromFile("data/e1.bin"));
			BigInteger n = new BigInteger(readFromFile("data/n1.bin"));
			System.out.println("e = " + e.toString());
			System.out.println("n = " + n.toString());
			PublicKey key = new PublicKey(n, e);
			
			Wiener Mallory = new Wiener();
			Mallory.attack(key);
			Mallory.printResult();					
			
			PrivateKey pk = Mallory.getResult();			
			byte[] plainText = readFromFile("data/data.any");
			RSA Alice = new RSA(pk);
			byte[] signature = Alice.sign(plainText);
			writeToFile("output/data.sign", signature);
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

}
