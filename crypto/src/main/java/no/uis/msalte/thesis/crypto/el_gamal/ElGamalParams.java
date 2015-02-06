package no.uis.msalte.thesis.crypto.el_gamal;

import java.math.BigInteger;

import no.uis.msalte.thesis.crypto.model.IGlobalParams;
import no.uis.msalte.thesis.crypto.util.CryptoUtil;

public class ElGamalParams implements IGlobalParams {
	public BigInteger q, a;

	public ElGamalParams(int bitLength) {
		q = CryptoUtil.getPrime(bitLength);
		a = CryptoUtil.getCoPrime(q);
	}

	public BigInteger newSecretKey() {
		return CryptoUtil.rand(BigInteger.ONE, q.subtract(BigInteger.ONE));
	}

	public BigInteger newPublicKey(BigInteger secretKey) {
		return a.modPow(secretKey, q);
	}

}
