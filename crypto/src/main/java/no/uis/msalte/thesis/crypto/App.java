package no.uis.msalte.thesis.crypto;

import java.io.IOException;

import no.uis.msalte.thesis.crypto.model.CyclicGroup;

public class App {
	public static void main(String[] args) throws IOException {

		CyclicGroup cg = new CyclicGroup(16);
		
		System.out.println("Modulus (p): " + cg.getModulus());
		System.out.println("Generator (g): " + cg.getGenerator());
//		System.out.println(cg.getElements());
		System.out.println("Random element in group: " + cg.getRandomElement());

	
		
//		ElGamalParams params = new ElGamalParams(1024);
//		ElGamalEncryption scheme = new ElGamalEncryption(params);
//
//		// Alice generates a secret and a public key
//		BigInteger secretKey = params.newSecretKey();
//		BigInteger publicKey = params.newPublicKey(secretKey);
//
//		Path inputPath = Paths.get("C:\\Users\\Morten\\Desktop\\test.txt");
//		Path outputPath = Paths
//				.get("C:\\Users\\Morten\\Desktop\\decrypted.txt");
//
//		byte[] plaintext = Files.readAllBytes(inputPath);
//
//		CipherText ciphertext = scheme.encrypt(plaintext, publicKey);
//
//		// Alice decrypts the message using her secret key
//		byte[] decrypted = scheme.decrypt(ciphertext, secretKey);
//
//		Files.write(outputPath, decrypted, StandardOpenOption.CREATE);
//
//		System.out.println("Success: " + Arrays.equals(plaintext, decrypted));
	}
}
