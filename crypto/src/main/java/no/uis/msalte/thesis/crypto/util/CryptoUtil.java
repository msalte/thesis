package no.uis.msalte.thesis.crypto.util;

import java.math.BigInteger;
import java.util.Random;

public class CryptoUtil {
	private static final int LESS_THAN = -1;
	private static final int GREATER_THAN = 1;

	public static BigInteger getPrime(int bitLength) {
		return BigInteger.probablePrime(bitLength, new Random());
	}

	public static BigInteger getCoPrime(BigInteger prime) {
		BigInteger candidate = BigInteger.ZERO;

		while (!isCoPrime(prime, candidate)) {
			int min = 2;
			int max = prime.bitLength()-1;
			
			if(min > max) {
				max = min;
			}
			
			int rnd = randInt(min, max);

			candidate = getPrime(rnd);
		}

		return candidate;
	}

	public static BigInteger rand(BigInteger min, BigInteger max) {
		Random rnd = new Random();

		BigInteger result = BigInteger.ZERO;

		while (result.compareTo(min) == LESS_THAN
				|| result.compareTo(max) == GREATER_THAN) {
			result = new BigInteger(max.bitLength(), rnd);
		}

		return result;
	}

	private static int randInt(int min, int max) {
		return new Random().nextInt((max - min) + 1) + min;
	}

	private static boolean isCoPrime(BigInteger a, BigInteger b) {
		return a.gcd(b).equals(BigInteger.ONE);
	}
}
