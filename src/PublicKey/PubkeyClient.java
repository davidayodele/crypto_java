package PublicKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Scanner;

public class PubkeyClient {

    private void saveKeys (String filename, BigInteger mod, BigInteger exp) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            System.out.println("\nSaving " + filename + " ...");
            fos = new FileOutputStream(filename);
            oos = new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(mod);
            oos.writeObject(exp);
            System.out.println("Done.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                oos.close();
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }

    private byte[] encryptMsg(String msg, PublicKey p) throws Exception {
        byte[] msgBytes = msg.getBytes();
        byte[] cipherTxt = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, p);
            cipherTxt = cipher.doFinal(msgBytes);
            System.out.println("Ciphertext: " + cipherTxt);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return cipherTxt;
    }

    private void decryptMsg(byte[] msg, PrivateKey p) throws Exception {
        byte[] decipherTxt = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, p);
            decipherTxt = cipher.doFinal(msg);
            System.out.println("Deciphered text: " + decipherTxt);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception
    {
        String msgTxt; // message = "The quick brown fox jumps over the lazy dog.";
        byte[] msgBytes;
        byte[] cipherTxt;

        String host = "localhost";
        int port = 7999;
        Socket s = new Socket(host, port);
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

        Scanner scan = new Scanner(System.in);
        StringBuffer strHolder = new StringBuffer();

        System.out.println("Enter a message for encryption: "); // prompts user for input String
        msgTxt = scan.next();
        msgBytes = msgTxt.getBytes();

        System.out.println("\nGenerating public key...");
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair keys = keyGen.generateKeyPair();

        PublicKey pubKey = keys.getPublic();
        System.out.println("Done.");

        System.out.println("\nGenerating private key...");
        PrivateKey privKey = keys.getPrivate();
        System.out.println("Done.");

        System.out.println("\nSaving keys...");
        PubkeyClient keyObj = new PubkeyClient();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(pubKey, RSAPublicKeySpec.class);
        RSAPrivateKeySpec rsaPrivKeySpec = keyFactory.getKeySpec(privKey, RSAPrivateKeySpec.class);
        keyObj.saveKeys("RSA_Pubkey.dat", rsaPubKeySpec.getModulus(), rsaPubKeySpec.getPublicExponent());
        keyObj.saveKeys("RSA_Privkey.dat", rsaPrivKeySpec.getModulus(), rsaPrivKeySpec.getPrivateExponent());
        System.out.println("Done.");


        // Encrypting message
        System.out.println("\nEncrypting message with public key...");
        cipherTxt = keyObj.encryptMsg(msgTxt, pubKey);
        System.out.println("Done.");

        // send public key
        os.writeObject(pubKey);

        // send message
        os.writeObject(msgBytes);

        // send signature
        os.writeObject(cipherTxt);

        System.out.println("\nRSA signature: ");
        System.out.println(cipherTxt);

        s.close();
    }
}
