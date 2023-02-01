package academy.pocu.comp3500.lab5;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;

public class KeyGenerator {
    private final static BigInteger NEGATIVE_ONE = BigInteger.valueOf(-1);
    private final static BigInteger NEGATIVE_TWO = BigInteger.valueOf(-2);
    private final static BigInteger ZERO = BigInteger.valueOf(0);
    private final static BigInteger ONE = BigInteger.valueOf(1);
    private final static BigInteger TWO = BigInteger.valueOf(2);
    public static boolean isPrime(final BigInteger number) {

        if (number.equals(TWO) || number.equals(NEGATIVE_TWO)) {
            return true;
        }

        if (number.remainder(TWO).equals(ZERO) || number.equals(ONE) || number.equals(NEGATIVE_ONE)) {
            return false;
        }

        BigInteger numberCopy = number;

        if (numberCopy.compareTo(ZERO) == -1) {
            numberCopy = numberCopy.multiply(NEGATIVE_ONE);
        }

        BigInteger nMinusOne = numberCopy.subtract(ONE);
        int s = nMinusOne.getLowestSetBit();
        BigInteger d = numberCopy.divide(TWO.pow(s));

        BigInteger minLimit = ONE;
        BigInteger bigInteger = nMinusOne.subtract(minLimit);

        for (int k = 0; k < 20; ++k) {
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
                if (randomA.modPow(d, numberCopy).equals(ONE) || randomA.modPow(TWO.pow(i).multiply(d), numberCopy).equals(nMinusOne)) {
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