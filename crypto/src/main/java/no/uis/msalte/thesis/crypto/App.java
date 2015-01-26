package no.uis.msalte.thesis.crypto;

import java.math.BigInteger;

import no.uis.msalte.thesis.crypto.el_gamal.ElGamalEncryption;
import no.uis.msalte.thesis.crypto.el_gamal.ElGamalParams;
import no.uis.msalte.thesis.crypto.model.CipherText;

public class App {
	public static void main(String[] args) {
		ElGamalParams params = new ElGamalParams(1024);
		ElGamalEncryption scheme = new ElGamalEncryption(params);

		// Alice generates a secret and a public key
		BigInteger secretKey = params.newSecretKey();
		BigInteger publicKey = params.newPublicKey(secretKey);

		// Bob writes a message
		String message = "secret message";

		System.out.println(String.format("Plaintext: %s", message));

		// Bob encrypts the message using the global parameters and Alice's
		// public key
		CipherText ciphertext = scheme.encrypt(message.getBytes(), publicKey);

		// Alice decrypts the message using her secret key
		String decrypted = new String(scheme.decrypt(ciphertext, secretKey));

		System.out.println(String.format("Decrypted: %s", decrypted));
	}
}
