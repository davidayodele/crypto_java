package ElGamal;

import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalAlice
{
    private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d)
    {
        BigInteger y = g.modPow(d, p);
        return y;
    }

    private static BigInteger computeK(BigInteger p)
    {
        SecureRandom randNum = new SecureRandom();
        int numSize = 1024; // bits
        BigInteger i = new BigInteger(numSize, randNum);
        BigInteger p_1 = p.subtract(BigInteger.ONE); // calculates p - 1

        while(!i.gcd(p_1).equals(BigInteger.ONE)) //Checks if i is relatively prime to p - 1
        {
            i = new BigInteger(numSize, randNum);
        }
        return i;
    }

    private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k)
    {
        BigInteger i = g.modPow(k, p);
        return i;
    }

    private static BigInteger computeB(	String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p)
    {
        BigInteger i = new BigInteger(message.getBytes()); // converts input string to BigInteger
        BigInteger p_1 = p.subtract(BigInteger.ONE); // calculates p - 1
        BigInteger p_1a = p_1; // grab value of p - 1 for loop iteration
        BigInteger q0 = BigInteger.ZERO;
        BigInteger q1 = BigInteger.ONE;
        BigInteger j, j2, j3;

        while(!k.equals(BigInteger.ZERO))
        {
            j = p_1a.divide(k); // (p - 1) / q2
            j2 = p_1a.subtract(k.multiply(j)); // (p - 1) - q2*j
            p_1a = k;
            k = j2;
            j3 = q0.subtract(q1.multiply(j)); // q0 - q1*j
            q0 = q1;
            q1 = j3;
        }

        BigInteger b = q0.multiply(i.subtract(d.multiply(a))).mod(p_1); // b = ((i - d*a) / k) mod (p-1)

        return b;
    }

    public static void main(String[] args) throws Exception
    {
        String message = "The quick brown fox jumps over the lazy dog.";

        String host = "localhost";
        int port = 7999;
        Socket s = new Socket(host, port);
        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

        // You should consult BigInteger class in Java API documentation to find out what it is.
        BigInteger y, g, p; // public key
        BigInteger d; // private key

        int mStrength = 1024; // key bit length
        SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically strong pseudo-random number

        // Create a BigInterger with mStrength bit length that is highly likely to be prime.
        // (The '16' determines the probability that p is prime. Refer to BigInteger documentation.)
        p = new BigInteger(mStrength, 16, mSecureRandom);

        // Create a randomly generated BigInteger of length mStrength-1
        g = new BigInteger(mStrength-1, mSecureRandom);
        d = new BigInteger(mStrength-1, mSecureRandom);

        y = computeY(p, g, d);

        // At this point, you have both the public key and the private key. Now compute the signature.
        System.out.println("\nAlice's public key: ");
        System.out.println(y);
        System.out.println("\nAlice's private key: ");
        System.out.println(d);

        BigInteger k = computeK(p);
        BigInteger a = computeA(p, g, k);
        BigInteger b = computeB(message, d, a, k, p);

        // send public key
        os.writeObject(y);
        os.writeObject(g);
        os.writeObject(p);

        // send message
        os.writeObject(message);

        // send signature
        os.writeObject(a);
        os.writeObject(b);

        System.out.println("\nAlice's signature: ");
        System.out.println(b);

        s.close();
    }
}