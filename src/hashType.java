/*
 * Title: hashType.java
 * Date: 12/3/19
 * Desc: A simple demonstration of MD5 hashing.
 * By: David Ayodele
 */


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class hashType {
    private static String msgTxt;
    private static byte[] msgBytes;
    private static String digestTxt;
    private static byte[] digestBytes;

    public static String hashMD5 (String msg) throws NoSuchAlgorithmException { // Exception in case invalid hash type used
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        Scanner scan = new Scanner(System.in);
        StringBuffer strHolder = new StringBuffer();

        System.out.println("Enter a message for digestion: "); // prompts user for input String
        msgTxt = scan.next();
        msgBytes = msgTxt.getBytes(); // converts string array to byte array (required by "update" method)
        md5.update(msgBytes); // "seeds" the algorithm with a new input
        digestBytes = md5.digest(); // processes a new digest of the input

        System.out.print("MD5 digest: ");
        for (byte b : digestBytes) {
            strHolder.append(Integer.toHexString(b & 0xff).toString()); // converts output to hex with hex value starting from lowest 8 bits instead of upper(default)
            digestTxt = strHolder.toString();
        }
        System.out.println(digestTxt);
        return digestTxt;
    }

    public static String sha256 (String msg) throws NoSuchAlgorithmException { // Exception in case invalid hash type used
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        Scanner scan = new Scanner(System.in);
        StringBuffer strHolder = new StringBuffer();

        System.out.println("Enter a message for digestion: "); // prompts user for input String
        msgTxt = scan.next();
        msgBytes = msgTxt.getBytes(); // converts string array to byte array (required by "update" method)
        sha256.update(msgBytes); // "seeds" the algorithm with a new input
        digestBytes = sha256.digest(); // processes a new digest of the input

        System.out.print("SHA-256 digest: ");
        for (byte b : digestBytes) {
            strHolder.append(Integer.toHexString(b & 0xff).toString()); // converts output to hex with hex value starting from lowest 8 bits instead of upper(default)
            digestTxt = strHolder.toString();
        }
        System.out.println(digestTxt);
        return digestTxt;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner scan = new Scanner(System.in);
        String input;
        try {
            System.out.println("Select a hashing algorithm: \n 1 for 'MD5' \n 2 for 'SHA-256' \n Enter a value: ");
            input = scan.next().toString();
            System.out.println(input);
            if (Integer.parseInt(input) == 1) {
                hashMD5(input);
            }
            else if (Integer.parseInt(input) == 2) {
                sha256(input);
            }
        } catch(NoSuchAlgorithmException e){System.out.println("Please check the hashing algorithm");}
    }
}


