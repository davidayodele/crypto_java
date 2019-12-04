import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;
import java.util.Scanner;

public class ProtectedClient
{
    public static Date timestamp = new Date();
    public static long t1 = timestamp.getTime();
    public static long t2;
    public static double randNum1;
    public static double randNum2;

    public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException
    {
        DataOutputStream out = new DataOutputStream(outStream);
        randNum1 = Math.random();
        randNum2 = Math.random();
        byte [] digest1 = Protection.makeDigest(user, password, t1, randNum1);
        t2 = timestamp.getTime();
        out.write(Protection.makeDigest(digest1, t2, randNum2));
        out.flush();
    }

    public static void main(String[] args) throws Exception
    {
        String host = "localhost"; // hostname or ip address (paradox.sis.pitt.edu)
        int port = 7999;
        String user; // = "George";
        String password; // = "abc123";
        Socket s = new Socket(host, port);
        String s2 = "test text to be written in output stream";

        ProtectedClient client = new ProtectedClient();

        Scanner scan = new Scanner(System.in);
        StringBuffer strHolder = new StringBuffer();
        System.out.println("Enter your username (correct user is George): "); // prompts user for input String
        user = scan.next();
        System.out.println("Enter your password (correct pass is abc123): "); // prompts user for input String
        password = scan.next();

        client.sendAuthentication(user, password, s.getOutputStream());

        File file = new File("logs.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(s2.getBytes()); // Writes bytes from the specified byte array to the file output stream
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe) {
            System.out.println("Exception while writing file " + ioe);
        }
        finally {
            try {
                if (fos != null) { // close the file stream
                    fos.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing file stream: " + ioe);
            }
        }
        //s.close(); // close the client stream
    }
}


