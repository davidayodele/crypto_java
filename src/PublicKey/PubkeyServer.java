package PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class PubkeyServer {

    private static boolean verifySignature(String msg, PublicKey p)
    {
        byte[] decipherTxt = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, p);
            decipherTxt = cipher.doFinal(msg.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return decipherTxt.equals(msg);
    }

    public static void main(String[] args) throws Exception
    {
        int port = 7999;
        ServerSocket s = new ServerSocket(port);
        Socket client = s.accept();
        ObjectInputStream is = new ObjectInputStream(client.getInputStream());

        // read public key
        PublicKey pubkey = (PublicKey) is.readObject();

        // read message
        String message = (String)is.readObject();

        // read signature
        BigInteger sig = (BigInteger)is.readObject();

        boolean result = verifySignature(message, pubkey);

        System.out.println(message);

        if (result == true)
            System.out.println("Signature verified.");
        else
            System.out.println("Signature verification failed.");

        s.close();
    }
}
