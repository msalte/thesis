package no.uis.msalte.thesis.crypto.util;

import java.math.BigInteger;
import java.util.Random;

public class CryptoUtil {
	public static BigInteger getPrime(int bitLength) {
		return BigInteger.probablePrime(bitLength, new Random());
	}

	public static BigInteger getCoPrime(BigInteger prime) {
		BigInteger candidate = BigInteger.ZERO;

		while (!isCoPrime(prime, candidate)) {
			candidate = getPrime(randInt(2, prime.bitLength()) - 1);
		}

		return candidate;
	}

	public static BigInteger rand(BigInteger min, BigInteger max) {
		Random rnd = new Random();

		BigInteger result = BigInteger.ZERO;

		while (result.compareTo(min) == -1 || result.compareTo(max) == 1) {
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
