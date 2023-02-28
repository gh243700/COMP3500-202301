package academy.pocu.comp3500.lab5;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

public class Bank {
    private HashMap<byte[], Long> accounts;

    public Bank(byte[][] pubKeys, final long[] amounts) {
        assert (pubKeys.length == amounts.length);
        accounts = new HashMap<>(pubKeys.length * 2);

        for (int i = 0; i < pubKeys.length; ++i) {
            accounts.put(pubKeys[i], amounts[i]);
        }
    }

    public long getBalance(final byte[] pubKey) {
        if (accounts.containsKey(pubKey) == false) {
            return 0;
        }

        return accounts.get(pubKey);
    }

    public boolean transfer(final byte[] from, byte[] to, final long amount, final byte[] signature) {
        if (accounts.containsKey(from) == false || amount <= 0 || accounts.get(from) - amount < 0 || ((!accounts.containsKey(to)) ? 0 : accounts.get(to)) + amount < 0) {
            return false;
        }

        byte[] message = new byte[from.length + to.length + 8];

        int index = 0;
        for (int i = 0; i < from.length; ++i) {
            message[index++] = from[i];
        }

        for (int i = 0; i < to.length; ++i) {
            message[index++] = to[i];
        }

        long amountCopy = amount;

        for (int i = Long.BYTES - 1; i >= 0; --i) {
            message[index + i] = (byte) (0xFF & amountCopy);
            amountCopy >>= Byte.SIZE;
        }

        byte[] planeText = null;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(message);
            message = messageDigest.digest();

            Cipher cipher = Cipher.getInstance("RSA");
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(from));
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            planeText = cipher.doFinal(signature);

        } catch (Exception e) {
            return false;
        }

        for (int i = 0; i < planeText.length; i++) {
            if (message[i] != planeText[i]) {
                return false;
            }
        }

        if (accounts.containsKey(to) == false) {
            accounts.put(to, 0l);
        }

        accounts.replace(from, accounts.get(from) - amount);
        accounts.replace(to, accounts.get(to) + amount);

        return true;
    }
}