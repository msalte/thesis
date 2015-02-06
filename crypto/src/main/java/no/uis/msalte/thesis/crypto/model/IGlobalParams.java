package no.uis.msalte.thesis.crypto.model;

import java.math.BigInteger;

public interface IGlobalParams {
	public BigInteger newSecretKey();
	public BigInteger newPublicKey(BigInteger secretKey);
}
