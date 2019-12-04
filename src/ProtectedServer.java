import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer
{
    public static long time;
    public static byte [] creds;
    public static boolean credCheck = false;
    public static String usr;
    public static String hashTxt1;
    public static String hashTxt2;
    public static byte [] digest1;
    public static byte [] digest2;

    public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException
    {
        DataInputStream in = new DataInputStream(inStream);

        String k = in.readUTF();
        time = in.readLong();
        creds = in.readAllBytes();

        digest1 = Protection.makeDigest(usr, lookupPassword(usr), ProtectedClient.t1, ProtectedClient.randNum1);
        digest2 = Protection.makeDigest(digest1, ProtectedClient.t2, ProtectedClient.randNum2);

        for (byte b : digest2) {
            for (byte c : creds) {
                if (Byte.compare(b, c) == 1) {
                    credCheck = true;
                }
            }
        }

        System.out.print(k);

        return credCheck;
    }

    protected String lookupPassword(String user) { return "abc123"; }

    public static void main(String[] args) throws Exception
    {
        int port = 7999;
        ServerSocket s = new ServerSocket(port);
        Socket client = s.accept();

        ProtectedServer server = new ProtectedServer();

        if (server.authenticate(client.getInputStream())) {
            System.out.println("Client logged in.");
        } else {
            System.out.println("Client failed to log in.");
        }
        s.close(); // close the server stream
    }
}