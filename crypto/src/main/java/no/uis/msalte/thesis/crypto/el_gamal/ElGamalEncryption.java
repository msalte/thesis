package no.uis.msalte.thesis.crypto.el_gamal;

import java.math.BigInteger;

import no.uis.msalte.thesis.crypto.model.CipherText;
import no.uis.msalte.thesis.crypto.util.CryptoUtil;

public class ElGamalEncryption {

	private BigInteger q, alpha;

	public ElGamalEncryption(BigInteger q, BigInteger alpha) {
		this.q = q;
		this.alpha = alpha;
	}

	public CipherText encrypt(BigInteger plaintext, BigInteger publicKey) {
		CipherText cipher = new CipherText();

		BigInteger k = CryptoUtil.rand(BigInteger.ONE, q);
		BigInteger bigK = publicKey.modPow(k, q);

		cipher.setC1(alpha.modPow(k, q));
		cipher.setC2(bigK.multiply(plaintext).mod(q));

		return cipher;
	}

	public BigInteger decrypt(CipherText ciphertext, BigInteger secretKey) {
		BigInteger bigK = ciphertext.getC1().modPow(secretKey, q);
		BigInteger bigKInverse = bigK.modInverse(q);

		BigInteger plainText = ciphertext.getC2().multiply(bigKInverse).mod(q);

		return plainText;
	}
}
