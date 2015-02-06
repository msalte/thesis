package no.uis.msalte.thesis.crypto.bbs;

import java.math.BigInteger;

import no.uis.msalte.thesis.crypto.model.CyclicGroup;
import no.uis.msalte.thesis.crypto.model.IGlobalParams;

public class ParamsBBS implements IGlobalParams {

	private CyclicGroup group;

	public ParamsBBS(CyclicGroup group) {
		this.group = group;
	}

	public BigInteger newSecretKey() {
//		return group.getRandomElement();
		return new BigInteger("7");
	}
	
	public CyclicGroup getGroup() {
		return group;
	}

	public BigInteger newPublicKey(BigInteger secretKey) {
		return group.getGenerator().pow(secretKey.intValue());
	}

	public BigInteger newReEncryptionKey(BigInteger sourceSecretKey,
			BigInteger destinationSecretKey) {
		return destinationSecretKey.divide(sourceSecretKey);
	}

}
