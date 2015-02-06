package no.uis.msalte.thesis.crypto.model;

import java.math.BigInteger;
import java.util.ArrayList;

import no.uis.msalte.thesis.crypto.util.CryptoUtil;

public class CyclicGroup {
	private static final int EQUAL = 0;
	private static final BigInteger TWO = new BigInteger("2");
	
	private BigInteger p, g;

	public CyclicGroup(int bitLength) {
		init(bitLength);
	}

	private void init(int bitLength) {
		BigInteger q = BigInteger.ZERO;
		
		while (true) {
			q = CryptoUtil.getPrime(bitLength);
			
			// p = 2q+1
			p = (q.multiply(TWO)).add(BigInteger.ONE); 

			if (!p.isProbablePrime(40)) {
				// If p is not prime, pick new q and p
				continue;
			}

			while (true) {
				// h = random(1, p-1)
				BigInteger h = CryptoUtil.rand(BigInteger.ONE, p.subtract(BigInteger.ONE));
				
				// exponent = (p-1)/q
				int exponent = (p.subtract(BigInteger.ONE)).divide(q).intValue(); 
				
				// g = h^exponent
				g = h.pow(exponent);

				if (g.compareTo(BigInteger.ONE) != EQUAL) {
					// if g != 1, g is a generator
					break;
				}
			}

			break;
		}
	}

	public BigInteger getRandomElement() {		
		return g.modPow(CryptoUtil.rand(BigInteger.ONE, p), p);
	}
	
	public ArrayList<BigInteger> getElements() {
		// This method is horribly ineffective for large groups and should not be used
		
		ArrayList<BigInteger> elements = new ArrayList<BigInteger>();

		BigInteger index = BigInteger.ONE;
		BigInteger element = BigInteger.ZERO;

		while (element.compareTo(BigInteger.ONE) != EQUAL) {
			element = g.modPow(index, p);
			elements.add(element);
			
			index = index.add(BigInteger.ONE); // index++
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
