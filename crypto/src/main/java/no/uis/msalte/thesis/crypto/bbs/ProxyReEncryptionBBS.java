package no.uis.msalte.thesis.crypto.bbs;

import java.math.BigInteger;

import no.uis.msalte.thesis.crypto.model.CipherText;

public class ProxyReEncryptionBBS {

	private ParamsBBS params;

	public ProxyReEncryptionBBS(ParamsBBS params) {
		this.params = params;
	}

	public CipherText encrypt(BigInteger plaintext, BigInteger secretKey) {
		BigInteger r = params.getGroup().getRandomElement();
		BigInteger g = params.getGroup().getGenerator();

		CipherText cipher = new CipherText();

		cipher.setC1(g.pow(r.intValue()).multiply(plaintext));
		cipher.setC2(g.pow(r.intValue()*secretKey.intValue()));

		return cipher;
	}

	public BigInteger decrypt(CipherText ciphertext, BigInteger secretKey) {		
		BigInteger numerator = ciphertext.getC1();
		double exp = (double) 1/secretKey.intValue();
		
		BigInteger denominator = ciphertext.getC2().pow((int)exp);

		return numerator.divide(denominator);
	}
}
