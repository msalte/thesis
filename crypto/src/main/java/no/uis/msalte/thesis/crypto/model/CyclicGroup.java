package no.uis.msalte.thesis.crypto.model;

import java.math.BigInteger;
import java.util.ArrayList;

import no.uis.msalte.thesis.crypto.util.CryptoUtil;

public class CyclicGroup {
	private static final int EQUAL = 0;

	private static final BigInteger TWO = new BigInteger("2");
	private BigInteger p, q, g;

	public CyclicGroup(int bitLength) {
		init(bitLength);
	}

	private void init(int bitLength) {
		while (true) {
			q = CryptoUtil.getPrime(bitLength);
			p = (q.multiply(TWO)).add(BigInteger.ONE); // p = 2q+1

			if (!p.isProbablePrime(40)) {
				continue;
			}

			while (true) {
				g = CryptoUtil.rand(TWO, p.subtract(BigInteger.ONE));

				BigInteger exp = (p.subtract(BigInteger.ONE)).divide(q);

				if (g.modPow(exp, p).compareTo(BigInteger.ONE) != EQUAL) {
					break;
				}

				break;
			}

			break;
		}
	}

	public BigInteger getRandomElement() {		
		return g.modPow(CryptoUtil.rand(BigInteger.ONE, p), p);
	}
	
	public ArrayList<BigInteger> getElements() {
		// This method is horribly ineffective and should not be used
		
		ArrayList<BigInteger> elements = new ArrayList<BigInteger>();

		BigInteger i = BigInteger.ZERO;
		BigInteger element = BigInteger.ZERO;

		while (element.compareTo(BigInteger.ONE) != EQUAL) {
			i = i.add(BigInteger.ONE);
			element = g.modPow(i, p);

			elements.add(element);
		}

		return elements;
	}

	public BigInteger getModulus() {
		return p;
	}

	public BigInteger getGenerator() {
		return g;
	}
}
