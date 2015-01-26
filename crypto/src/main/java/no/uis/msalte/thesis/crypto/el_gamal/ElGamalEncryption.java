package no.uis.msalte.thesis.crypto.el_gamal;

import java.math.BigInteger;

import no.uis.msalte.thesis.crypto.model.CipherText;
import no.uis.msalte.thesis.crypto.util.CryptoUtil;

public class ElGamalEncryption {

	private ElGamalParams params;

	public ElGamalEncryption(ElGamalParams params) {
		this.params = params;
	}

	public CipherText encrypt(byte[] plaintext, BigInteger publicKey) {
		return encrypt(new BigInteger(plaintext), publicKey);
	}

	public byte[] decrypt(CipherText ciphertext, BigInteger secretKey) {
		return decrypt(ciphertext.getC1(), ciphertext.getC2(), secretKey)
				.toByteArray();
	}

	private CipherText encrypt(BigInteger plaintext, BigInteger publicKey) {
		if (plaintext.compareTo(params.q) > 0) {
			try {
				throw new Exception("Message is too large for q parameter");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		CipherText cipher = new CipherText();

		BigInteger k = CryptoUtil.rand(BigInteger.ONE, params.q);
		BigInteger bigK = publicKey.modPow(k, params.q);

		cipher.setC1(params.a.modPow(k, params.q));
		cipher.setC2(bigK.multiply(plaintext).mod(params.q));

		return cipher;
	}

	private BigInteger decrypt(BigInteger c1, BigInteger c2,
			BigInteger secretKey) {
		BigInteger bigK = c1.modPow(secretKey, params.q);
		BigInteger bigKInverse = bigK.modInverse(params.q);

		return c2.multiply(bigKInverse).mod(params.q);
	}
}
