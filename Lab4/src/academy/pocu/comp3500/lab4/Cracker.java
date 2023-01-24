package academy.pocu.comp3500.lab4;

import academy.pocu.comp3500.lab4.pocuhacker.RainbowTable;
import academy.pocu.comp3500.lab4.pocuhacker.User;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.zip.CRC32;

public final class Cracker {
    private User[] userTable;
    private String email;
    private int hashAlgorithm = -1;

    public Cracker(User[] userTable, String email, String password) {
        this.email = email;
        this.userTable = userTable;
        byte[] pwBytes = password.getBytes();
        {
            CRC32 crc32 = new CRC32();
            crc32.update(pwBytes);
            String hash = String.format("%d", crc32.getValue());
            for (int i = 0; i < userTable.length; ++i) {
                if (hash.equals(userTable[i].getPasswordHash())) {
                    this.hashAlgorithm = 0;
                    return;
                }
            }
        }
        {
            String[] algorithms = {"MD2", "MD5", "SHA-1", "SHA-256"};
            for (int k = 0; k < algorithms.length; ++k) {
                String hash = null;
                try {
                    MessageDigest md = MessageDigest.getInstance(algorithms[k]);
                    byte[] m = md.digest(pwBytes);
                    hash = new String(Base64.getEncoder().encodeToString(m));
                } catch (Exception e) {
                    System.out.println(algorithms[k] + " FAIL ABORT");
                    System.out.println(e.getMessage());
                    System.exit(-1);
                }

                for (int i = 0; i < userTable.length; ++i) {
                    if (hash.equals(userTable[i].getPasswordHash())) {
                        this.hashAlgorithm = k + 1;
                        return;
                    }
                }
            }
        }
    }

    public String[] run(final RainbowTable[] rainbowTables) {

        String[] result = new String[userTable.length];

        if (rainbowTables.length == 0 || this.hashAlgorithm == -1) {
            return result;
        }

        for (int i = 0; i < this.userTable.length; ++i) {
            result[i] = rainbowTables[hashAlgorithm].get(this.userTable[i].getPasswordHash());
        }

        return result;
    }
}