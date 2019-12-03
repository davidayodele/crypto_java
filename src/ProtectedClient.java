import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;

public class ProtectedClient
{
    public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException
    {
        Date timestamp = new Date();
        long t1 = timestamp.getTime();
        DataOutputStream out = new DataOutputStream(outStream);
        double randNum1 = Math.random();
        double randNum2 = Math.random();
        byte [] digest1 = Protection.makeDigest(user, password, t1, randNum1);
        long t2 = timestamp.getTime();
        out.write(Protection.makeDigest(digest1, t2, randNum2));
        out.flush();
    }

    public static void main(String[] args) throws Exception
    {
        String host = "paradox.sis.pitt.edu"; // hostname or ip address
        int port = 7999;
        String user = "George";
        String password = "abc123";
        Socket s = new Socket(host, port);

        ProtectedClient client = new ProtectedClient();
        client.sendAuthentication(user, password, s.getOutputStream());

        s.close();
    }
}


