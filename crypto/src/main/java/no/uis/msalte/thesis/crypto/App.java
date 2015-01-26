package no.uis.msalte.thesis.crypto;

import java.math.BigInteger;

import no.uis.msalte.thesis.crypto.el_gamal.ElGamalEncryption;
import no.uis.msalte.thesis.crypto.model.CipherText;
import no.uis.msalte.thesis.crypto.util.CryptoUtil;

public class App {
	public static void main(String[] args) {
		BigInteger q = CryptoUtil.getPrime(32);
		BigInteger alpha = CryptoUtil.getCoPrime(q);

		ElGamalEncryption scheme = new ElGamalEncryption(q, alpha);

		// Alice generates a secret and a public key
		BigInteger secretKey = CryptoUtil.rand(BigInteger.ONE,
				q.subtract(BigInteger.ONE));
		BigInteger publicKey = alpha.modPow(secretKey, q);

		// Bob writes a message
		BigInteger message = new BigInteger("40");

		System.out.println("Plaintext: " + message);

		// Bob encrypts the message using the global parameters and Alice's
		// public key
		CipherText c = scheme.encrypt(message, publicKey);

		System.out.println("C1: " + c.getC1());
		System.out.println("C2: " + c.getC2());

		// Alice decrypts the message using her secret key
		System.out.println("Decrypted: " + scheme.decrypt(c, secretKey));
	}
}
