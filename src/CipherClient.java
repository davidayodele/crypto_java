import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Scanner;
import javax.crypto.*;

public class CipherClient
{
    public static void main(String[] args) throws Exception
    {
        String message = "The quick brown fox jumps over the lazy dog.";
        String host = "localhost";
        int port = 7999;
        Socket s = new Socket(host, port);

        // YOU NEED TO DO THESE STEPS:
        // -Generate a DES key.
        // -Store it in a file.
        // -Use the key to encrypt the message above and send it over socket s to the server.

        // -Generate a DES key.
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(new SecureRandom());
        Key key = keyGen.generateKey();

        // -Store it in a file.
        FileOutputStream fos = new FileOutputStream("Key.dat");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(key);
        out.close();

        System.out.print("The DES key: ");
        System.out.print(key);

        /*System.out.print("\nPress Enter to encrypt the message.");
        Scanner scanIn = new Scanner(System.in);
        scanIn.nextLine();
        scanIn.close();*/

        // -Use the key to encrypt the message above and send it over socket s to the server.
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        System.out.print("\nThe ciphertext: ");
        System.out.print(cipher);

        CipherOutputStream cipher_out = new CipherOutputStream(s.getOutputStream(), cipher);
        cipher_out.write(message.getBytes(), 0, message.getBytes().length);
        cipher_out.flush();
        cipher_out.close();
        s.close();
    }
}