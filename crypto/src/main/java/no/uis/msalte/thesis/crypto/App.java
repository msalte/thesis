package no.uis.msalte.thesis.crypto;

import java.io.IOException;

import no.uis.msalte.thesis.crypto.pre.ProxyReEncryption;
import no.uis.msalte.thesis.crypto.pre.ProxyReEncryptionParams;

public class App {
	public static void main(String[] args) throws IOException {

		ProxyReEncryptionParams params = new ProxyReEncryptionParams();
		
		params.initialize();

		ProxyReEncryption scheme = new ProxyReEncryption(params);
		
		scheme.test();
		
//		Element secretKey = scheme.newSecretKey();
//		Element publicKey = scheme.newPublicKey(secretKey);
//		
//
//		Element message = scheme.stringToElement("a");
//		
//		Tuple ciphertext = scheme.encryptFirstLevel(message, secretKey);
//
//		Element recovered = scheme.decryptFirstLevel(ciphertext, secretKey);
//		
//		System.out.println(recovered);
		
		// ElGamalParams params = new ElGamalParams(1024);
		// ElGamalEncryption scheme = new ElGamalEncryption(params);
		//
		// // Alice generates a secret and a public key
		// BigInteger secretKey = params.newSecretKey();
		// BigInteger publicKey = params.newPublicKey(secretKey);
		//
		// Path inputPath = Paths.get("C:\\Users\\Morten\\Desktop\\test.txt");
		// Path outputPath = Paths
		// .get("C:\\Users\\Morten\\Desktop\\decrypted.txt");
		//
		// byte[] plaintext = Files.readAllBytes(inputPath);
		//
		// CipherText ciphertext = scheme.encrypt(plaintext, publicKey);
		//
		// // Alice decrypts the message using her secret key
		// byte[] decrypted = scheme.decrypt(ciphertext, secretKey);
		//
		// Files.write(outputPath, decrypted, StandardOpenOption.CREATE);
		//
		// System.out.println("Success: " + Arrays.equals(plaintext,
		// decrypted));
	}
}
