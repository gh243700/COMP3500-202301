package academy.pocu.comp3500.lab5;

import java.math.BigInteger;
import java.util.Random;

public class KeyGenerator {
    public static boolean isPrime(final BigInteger number) {

        if (number.equals(BigInteger.TWO)) {
            return true;
        }

        if (number.compareTo(BigInteger.ZERO) == -1 || number.remainder(BigInteger.TWO).equals(BigInteger.ZERO) || number.equals(BigInteger.ONE)) {
            return false;
        }

        BigInteger nMinusOne = number.subtract(BigInteger.ONE);
        int s = nMinusOne.getLowestSetBit();
        BigInteger d = number.divide(BigInteger.TWO.pow(s));

        BigInteger minLimit = BigInteger.ONE;
        BigInteger bigInteger = nMinusOne.subtract(minLimit);

        for (int k = 0; k < 10; ++k) {
            Random random = new Random();
            int len = nMinusOne.bitLength();
            BigInteger randomA = new BigInteger(len, random);

            if (randomA.compareTo(bigInteger) < 0) {
                randomA = randomA.add(minLimit);
            }

            if (randomA.compareTo(bigInteger) >= 0) {
                randomA = randomA.mod(bigInteger).add(minLimit);
            }

            boolean possiblePrime = false;

            for (int i = 0; i < s; ++i) {
                if (randomA.modPow(d, number).equals(BigInteger.ONE) || randomA.modPow(BigInteger.TWO.pow(i).multiply(d), number).equals(nMinusOne)) {
                    possiblePrime = true;

                    break;
                }
            }

            if (possiblePrime == false) {
                return false;
            }
        }

        return true;
    }
}