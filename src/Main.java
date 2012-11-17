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
	
	public static void getSignature(String inputFile, String outputFile, PrivateKey pk) {		
		byte[] plainText = readFromFile(inputFile);
		RSA rsa = new RSA(pk);
		byte[] signature = rsa.sign(plainText);
		writeToFile(outputFile, signature);
	}
	
	public static void main(String[] args) {
		try {
			BigInteger e = new BigInteger(readFromFile("data/e1.bin"));
			BigInteger n = new BigInteger(readFromFile("data/n1.bin"));
			System.out.println("e = " + e.toString());
			System.out.println("n = " + n.toString());
			PublicKey key = new PublicKey(n, e);
			
			Wiener Mallory = new Wiener();
			Mallory.attack(key);
			Mallory.printResult();					
			
			getSignature("data/data.any", "output/data.sign", Mallory.getResult());
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}

}
