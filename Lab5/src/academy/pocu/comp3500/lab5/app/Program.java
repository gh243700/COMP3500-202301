package academy.pocu.comp3500.lab5.app;

import academy.pocu.comp3500.lab5.Bank;
import academy.pocu.comp3500.lab5.KeyGenerator;

import java.math.BigInteger;
import java.util.ArrayList;

public class Program {
    private static byte[] decodeFromHexString(String hexString) {
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            int firstDigit = Character.digit(hexString.charAt(i), 16);
            int secondDigit = Character.digit(hexString.charAt(i + 1), 16);
            bytes[i / 2] = (byte) ((firstDigit << 4) + secondDigit);
        }
        return bytes;
    }

    private static String encodeToHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte oneByte : bytes) {
            result.append(String.format("%02x", oneByte));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        // write your code here


        {
            // KeyGenerator
            assert (!KeyGenerator.isPrime(BigInteger.ZERO));
            assert (!KeyGenerator.isPrime(BigInteger.ONE));
            assert (KeyGenerator.isPrime(BigInteger.valueOf(2)));
            assert (KeyGenerator.isPrime(BigInteger.valueOf(3)));
            assert (!KeyGenerator.isPrime(BigInteger.valueOf(4)));
            assert (KeyGenerator.isPrime(BigInteger.valueOf(5)));
            assert (!KeyGenerator.isPrime(BigInteger.valueOf(6)));
            assert (KeyGenerator.isPrime(BigInteger.valueOf(7)));

            assert (!KeyGenerator.isPrime(BigInteger.valueOf(2475)));
            assert (!KeyGenerator.isPrime(BigInteger.valueOf(2476)));
            assert (KeyGenerator.isPrime(BigInteger.valueOf(2477)));

            assert (KeyGenerator.isPrime(BigInteger.valueOf(886913)));
            assert (KeyGenerator.isPrime(BigInteger.valueOf(8900000189L)));

            // Bank
            final String TEST_PUBLIC_KEY_1 = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100b2bb213e18fe414ff32bf17f6630d542a667275813627445a92043791cd924dc4dec2007a10aa6a268bfef2b56677e2cecd0092a2e348aec34316edc20648820fee83125daba065826d2cbcc684fcbafc8fb22930eb6bd827713d7c7e598b9efd83689745288e9a1630175bf2759e5749cdfbad304921d15bb901d1ba0ca31b367733161d60839c7378be720863cb5e20d845edff236f442bc0bb6ac726970038b4490d2d4f25b3b0721510cea4aa45a50fe59fa09cdadcd4c0d1ab7f268e02b3cced773985e10a18f72cb808d104874e43a0c2eb0e44345751fefd6153211a9b3dc53592e2c203694bd501d6fabc3ae53b7ec8207de79bc8188a74e0d359f290203010001";
            final String TEST_PRIVATE_KEY_1 = "308204be020100300d06092a864886f70d0101010500048204a8308204a40201000282010100b2bb213e18fe414ff32bf17f6630d542a667275813627445a92043791cd924dc4dec2007a10aa6a268bfef2b56677e2cecd0092a2e348aec34316edc20648820fee83125daba065826d2cbcc684fcbafc8fb22930eb6bd827713d7c7e598b9efd83689745288e9a1630175bf2759e5749cdfbad304921d15bb901d1ba0ca31b367733161d60839c7378be720863cb5e20d845edff236f442bc0bb6ac726970038b4490d2d4f25b3b0721510cea4aa45a50fe59fa09cdadcd4c0d1ab7f268e02b3cced773985e10a18f72cb808d104874e43a0c2eb0e44345751fefd6153211a9b3dc53592e2c203694bd501d6fabc3ae53b7ec8207de79bc8188a74e0d359f290203010001028201006d1649f3dccd528fb99bd88a29952494c97bac47d58d01ea828f6e9f3d3c4b832b0c3380748a4baa54c4d0f56287483ba34e5649a869e960c17a6a6f7055ebc037d5002a5a95afc99de88afaf7afbc624e08f9d147e9c1411fecbc87055b7221319617cf2790e907d7a5cc781f4c7effedbfa007ab8282be00fcb4c7681c00b8b625a772ad0a50047128d9a0c87286bb94b7c3791f1b1d5465e57726440f93da0cb4a125e25c607b85b52647b5a6297e4ff327e94eaa8a0bf42999e9704a43adb524bfa018"
                    + "92a33dc6bac2d3ef18f5fb726f2b4ba05a5d9e0255ba1c10261fbba41eb67c6d0c9f6451134462959098318922671efd329848803d06e27573a00102818100fe41190885fd8081fa6b1f7835212f38cc7a1ac347af29daecede85086c3884e888855a82f5d5b911677135d4bda8d4a0dfbff8ec36c2bc4de8d2fd59569aceb1d6c88a28dfd8adf40f1b5875fa45cab4237f5fa32821db5c627ec7ce1d22970bc2a263d45e6396ec0755ec7cd999ba04196cb6c242caaa227a90179779dd0b102818100b3f548ef813691dabc39497064a50df193dc3c6d22d2c52fac56f6d59a33e21da8f05dd01a4db5c86a6a0fd4b6b863d5bb1d705af383c194ee972cb53df22b84fb3899be0b47f92941daba20ffab2bf6d6fbc0a1c8472049071c7d800c09fe88d09d86a341eae402805dec7bf82b4241779525950ae754411b2b60d0ebe093f90281804ffa1c2cc1311fa64326452ce3d17f702769d5574e0a3c69401409faa299891be2f8b7b102cd240b7c7c925bcb8b80dc258ffa30672a748e25cf5597a2bb2e087b25f3c8e64f35caca3bfdf50a51c250a0b0c7b01f092cdd4beaad0a0f62b36461dc04514bd682846307c87ba321c9815c805996b7be512256c563d0589f0cd1028181009d6e46d927ec0a5b15f0f3db3c60dcbaeb446f2476a376d7c6e7e95662696335d956366bd8a70511886e"
                    + "b2dcbee0be8dfafcbd0f041bce9d30c0d5ded48b682114f9a61e08967555ef839ef3415788c58fbb7c00cc49d4e7ae2df168b49f85aa40fe9e0b729e3fcc246a8678c95966c4090e546b77b77a10dee8eaf0ba79a45902818100f3fea8d7c7614302034dd0729a0fc01e7680e24183f89c88264978ba134b86e0bc5f7edaecd2796dc751dcd463c58d432e8463105714480705bf91936b87a97e12ec2fa0c7e49786fd517dfaef4aabcaeab68f6b966a3dae8e705c9d166f1d8210a39e9b0a7da48d907c7c280f38bdfe676e65db7bbf60a99a4151e7b6ab8356";

            final String TEST_PUBLIC_KEY_2 = "30820122300d06092a864886f70d01010105000382010f003082010a02820101008f5a32c9c3e467541fb63aa0ac9223f599f36ad95618428b43fadbd9aa89bfe17f1bfce5af0a5f03773d6b90e8b62f5b44d995a618b98663e449fa6870d18c93fb2dd4c30db100b20103c7ae3b8fbb5fd76112bc198cc195d43da9d776e90e446b2cb2261bbed1804ff3b2f49b6b827ed61f740910dbf10c248482c9a950f3e54751964ef7c78738c9d54e675a764f94d4f734db665e10ca64a2a4d5597610bd83302b825c3963d72beaa870fd0fe5f2672533f343c17416d2a58190bfca3aa8749a7fcc85f38d0ff92bc32570f456010a9df9306aa00634edfb23126e1973096a88c6daaeed5766ba03c2bff8dae7d241d718131cbdefce0a88806816fa034b0203010001";
            final String TEST_PRIVATE_KEY_2 = "308204bd020100300d06092a864886f70d0101010500048204a7308204a302010002820101008f5a32c9c3e467541fb63aa0ac9223f599f36ad95618428b43fadbd9aa89bfe17f1bfce5af0a5f03773d6b90e8b62f5b44d995a618b98663e449fa6870d18c93fb2dd4c30db100b20103c7ae3b8fbb5fd76112bc198cc195d43da9d776e90e446b2cb2261bbed1804ff3b2f49b6b827ed61f740910dbf10c248482c9a950f3e54751964ef7c78738c9d54e675a764f94d4f734db665e10ca64a2a4d5597610bd83302b825c3963d72beaa870fd0fe5f2672533f343c17416d2a58190bfca3aa8749a7fcc85f38d0ff92bc32570f456010a9df9306aa00634edfb23126e1973096a88c6daaeed5766ba03c2bff8dae7d241d718131cbdefce0a88806816fa034b02030100010282010038805dba46dab49b64b067a542bf8b2ad79628cc26f202f06f47e5f582112f2f0af196195587a9f16cc329ad80fba5e8bc57627388153fd2097ddb13e53bc2c3e4e1a89584c4fa382ecc359d07dedc04a7989c50c565443bf0dea91dc2c1931d067d246e7ecb143a15c94104c6697692d584aa3094938c0bb42d027bc79f9fb16c42be8e77ff1c858fe97055f256e58b53233400dac9b1aacb2b89cecdf7aced09a5bfe72559968e529f39a595c9288793cb62bde101da704ab0923c80cd165a7f35fa5c92"
                    + "3c3f5996657fb4a79aa1b4366b81fb31457e518c6cea6d8b99bd688e3a6694df9f75395107ea0803bc0ee4519f8e7c7a237948af503dbd367b854102818100d40f0a390062d4bfe39d16b58c2bd188d5c9ea78728b5748d189e48c30934dea7ddbdf391c36b84a36741e0e69bb6f546012ef49a417a85f5afe9de528be8d816fafabc00edd408286455e687ffc4f19b26386d4dce0392079f355d6256fda92e6da3d03b6e556b4cf3e57d94cda6dd5d2fa72b7520c190689ccf49d29d9a7ab02818100ad0e8746edd5745b24b9bf0ca128e81679b4bcaa2e89b069cf872f52082a8de50ff21429e16d3325e090ee0496d9c9830ec83b1db7f775e3c1a7cfcf0a2e485616bde0d166f4597a412af941a180c7a7ecf80d434a45707715f6f1fd3e0e2aff6c31b4ed9ccd1885b18e1db04157f36fb7b73b80def658b9a4578eca848af2e102818100bfa85834ec84668487d39292c2fd5783b4b5a994f2f1bd11b3504d9fe6c73bc493bc052438bbbc1456e2dbc76b085cb6859e976697484a84e458fb9c0217370da543c096f4d84355b4c53d6e753e34bfa4db0f2193ff93a22696271e44f7b334d230acb48eb8f884b24a465bbf7a2b8af604256a4a5d64c0e40ab98ad22845c102818013476c109dcc4ad3208b44da5e55d54d33afcbd8d5a8672bd27fc7e8c69a1f06a6b91c3ff22a1d299a13"
                    + "eb403da1f38e1326815775382f3cf944fda49e08bbec434a51f2621ad4a107038793252fd88e3f9afeb7415ded028742e48086da43360352a24a5a4e8aedd1f2f821bb95d5bbdb66a14d610966a6ffc4f556605fae610281803768bf3a3eb157277e931bb3bf1bde0f79d023098678bcfc0c1247a3bb14a1b6d5857cfeb709515c5c492cb8852d58f2465de38c5906d3ae33977c1bba11c20d84fe73432eedd72f603f76de4036df5950c8a88d125706611f5bd22030ec7854c0101ff3041c53b8431712512c7da456a29d2e9bfce77afa87f6f92794fca9ff";

            final String TEST_PUBLIC_KEY_3 = "30820122300d06092a864886f70d01010105000382010f003082010a02820101008fb34c30552be6bab475a0415a16acc071e23741aefa927e6f034cf787e52b8bf25a9c3ddd41c452079656ce6069eee2ace65eab6fb8c19c78873bfd4c1b3b6c355ac34a81ede266462f6a19a1af3324f552dd42e3a54d0ed66aef9d0267f72efea2399b8d58ea5681e1b42c49b114ec83c607c60779c75cae5b73c38de51ddfd2eeecce8edf9d01e8bfa0161cb988bc2e5962b3ae7fa99e0fa0f55b3709ab5e66bb4592fd37ca4f13cb17a5d776617be7ba48fd6ffa45e28e068f26058c3298286be28e788782adec9391e62070f56cb9a6398ed148066c89cae17d282debef7a7acaea00726c4e3f8f1ee994230435c7b84878f94ee87b8ae4b4353fb942110203010001";
            final String TEST_PRIVATE_KEY_3 = "308204bd020100300d06092a864886f70d0101010500048204a7308204a302010002820101008fb34c30552be6bab475a0415a16acc071e23741aefa927e6f034cf787e52b8bf25a9c3ddd41c452079656ce6069eee2ace65eab6fb8c19c78873bfd4c1b3b6c355ac34a81ede266462f6a19a1af3324f552dd42e3a54d0ed66aef9d0267f72efea2399b8d58ea5681e1b42c49b114ec83c607c60779c75cae5b73c38de51ddfd2eeecce8edf9d01e8bfa0161cb988bc2e5962b3ae7fa99e0fa0f55b3709ab5e66bb4592fd37ca4f13cb17a5d776617be7ba48fd6ffa45e28e068f26058c3298286be28e788782adec9391e62070f56cb9a6398ed148066c89cae17d282debef7a7acaea00726c4e3f8f1ee994230435c7b84878f94ee87b8ae4b4353fb94211020301000102820100709ce345314a013c29b5d08bc66ce49ebfe0b6caebb4845f3ee2484be79c89bed78378d3e673e6c51e1b0c16196a8b01eadb722f4993716f0ce975f2afd4db819662758f0b39806603f49e624dd9fdeba5b175a238c24f1c249e92fee966ce486ed674ecd91d682fb90346ee3a25e32ee2a9ab67de6de9f550d26c6b40e4dfbe5e63ed94f5d6abd58d7213d8dd299428813059555a3fc447855d62af7d5a57ff7a83c0eb8ba37ab3b5fb73b7183c1416a6411270426364c104b2243e1ffdd1bbad88507ab7"
                    + "cf79146032070c2d2fbaab7c592190cc1f00b0ddfa193b6256d5cf2baa3502fe1f46d3c92ddcafb725ca6d095977cb7092f40492bbcfc0f0f8c65d02818100c380755ea0fad835b66bd6e9609335788d53392bf899b38af876dac4c5851d4eedbd4c8adea380ad58877be2e4e3663e755411926896f270a7300a38ed3bade8bfdb2bdafdca6816b6d7b78ef1d24afa39f114c2e7fcbdde3320a470b1fc99f7cf619f34118052c0e1918c757f3a49d094517c7023feeb428510e675e59b05bf02818100bc2b292413e5bf518bcbd9fce22deccfda100b3ee386a79122c61d37130c084e83a86577edef686f663e084307b1549e6b4490019df8db1b48904d7790550405b2fbf5e79756c3220d66eba17ec2e9f951f10edffd92755525d6388908302c4a18527e8d6b24c71686b36b049d193fe2f9effec702c4f2aecee7ef0a5211cc2f02818050d082340ad1869cb57ae08cfa69ffd9847a19910ea3e2af18c470ec9445d176b619e1512a756614c21ac760648387d95bc0d3beb4d369069e65e263e3d51eafaed757ca9fcc92b888fea51746e94a2b23d67f618e6a50c6505637038adfc379c7d52fed632aab8aa5f3f79efe67c6e99cd60e5d80d3b8c677a123d32a85a99b02818050946714d51480de89f02d1497433573391dbfc319aa535ddab75e51746da5ad01c27e59626230a5ebf1ca9"
                    + "5f1d865b5459861d6df706f5bfab2190a879b7092843853f0ee253081079e386fbb19fb1a5295d959e07bf4951713eab7fbf2ad34b9bfb2041b8aef5add2a3e3d068d86874ba313c6e38fb5a15867883d3231feef02818100b8744f350ef776bb595efe618614a6b1daa2db1906832f80b7840e6568aa7b88c1b02a8b24e3d88fa01fba1b43e47a747e6c800f5594e2e3254fd67d8c565e34b21cfed77a715916aefc64b392885cfc5ceacfb2cc69c6c22c8a7fb5aa1d4888fa731368a86b0b7d2532bc04f437ef37faa71c1f1b511019e555b0dbf557a4d2";

            byte[] senderPublicKey = decodeFromHexString(TEST_PUBLIC_KEY_1);
            byte[] receiverPublicKey = decodeFromHexString(TEST_PUBLIC_KEY_2);
            long senderInitialBalance = 20000;
            long receiverInitialBalance = 3000;

            Bank bank = new Bank(new byte[][]{senderPublicKey, receiverPublicKey}, new long[]{senderInitialBalance, receiverInitialBalance});

            long senderBalance = bank.getBalance(senderPublicKey);
            long receiverBalance = bank.getBalance(receiverPublicKey);

            assert (senderBalance == senderInitialBalance);
            assert (receiverBalance == receiverInitialBalance);

            final long AMOUNT = 5000;
            final long WRONG_AMOUNT = 4000;
            final String TEST_1_TEST_2_5000_SIGNATURE = "355913bad7f97ecf38c27a234d1957de9fe366eedc66a365114692833fb6045c5b907c1bea26bd30d23b92d98f2fc3d5e5dbd9d377c8ce499ffd7120fffb3b3bbdd9cecfcef883a672e86510b3b6a8364b2ef146a51cbb0cd3f71ca412bfebfa050547616f2bb8839caf282f4a5abbd56860649b7f292ec670995b0d48d7d085637d647ea9aaf99ffdce84c87a8360718cc38f4657af81ebd1beebe0e71ed132ce5c59718a2d097ff13128c02d34fd681c0cf409381e715aa0593c28e1606e193476f153d4c1676194460e8f17916c1f30151f0cc503aa965553589006e3be38c2ed3a91601340261778f05d9439b2232a3ea0574dd74af01c53dee79651846e";
            byte[] signature = decodeFromHexString(TEST_1_TEST_2_5000_SIGNATURE);

            boolean wrongAccountTransferResult = bank.transfer(receiverPublicKey, senderPublicKey, AMOUNT, signature);
            assert (!wrongAccountTransferResult);

            boolean wrongAmountTransferResult = bank.transfer(senderPublicKey, receiverPublicKey, WRONG_AMOUNT, signature);
            assert (!wrongAmountTransferResult);

            boolean transferResult = bank.transfer(senderPublicKey, receiverPublicKey, AMOUNT, signature);
            assert (transferResult);

            senderBalance = bank.getBalance(senderPublicKey);
            receiverBalance = bank.getBalance(receiverPublicKey);

            assert (senderBalance == senderInitialBalance - AMOUNT);
            assert (receiverBalance == receiverInitialBalance + AMOUNT);


        }


        {/*

            for (int i = -9999999; i < 9999999; ++i) {
                assert (KeyGenerator.isPrime(BigInteger.valueOf(i)) == BigInteger.valueOf(i).isProbablePrime(100));
            }
            */
        }
        smallPrimeNumberTest();

    }

    private static void smallPrimeNumberTest() {
        final int LIMIT = 1000000;
        boolean[] era = new boolean[LIMIT];

        // true => not prime number. Because the default value is false.
        era[0] = true;
        era[1] = true;
        for (int i = 2; i < LIMIT; ++i) {
            if (era[i] == false) {
                for (int notPrime = i + i; notPrime < LIMIT; notPrime += i) {
                    era[notPrime] = true;
                }
            }
        }

        for (int i = 0; i < LIMIT; ++i) {
            boolean bPrime = !era[i];
            boolean bIsPrimeResult = KeyGenerator.isPrime(BigInteger.valueOf(i));

            if (bPrime != bIsPrimeResult) {
                System.out.println(String.format("%d is %s\n", i, (bPrime ? "prime number" : "not a prime number")));
            }
            assert(bPrime == bIsPrimeResult);
        }
    }

}
