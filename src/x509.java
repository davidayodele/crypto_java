import javax.net.ssl.X509ExtendedTrustManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


public class x509 {
    private X509Certificate loadCertificate(CertificateFactory cf, File f) throws CertificateException, IOException, IOException {
        FileInputStream in=new FileInputStream(f);
        try {
            X509Certificate c=(X509Certificate)cf.generateCertificate(in);
            c.checkValidity();
            return c;
        }
        finally {
            in.close();
        }
    }

    public static X509Certificate createCert(byte[] certData){
        try {
            CertificateFactory cf=CertificateFactory.getInstance("X509");
            X509Certificate cert=(X509Certificate)cf.generateCertificate(new ByteArrayInputStream(certData));
            return cert;
        }
        catch (  Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void checkCert(X509Certificate[] certificates,String authType) throws CertificateException {
        if (certificates != null) {
            for (int c=0; c < certificates.length; c++) {
                X509Certificate cert=certificates[c];
                System.out.println(" Client certificate " + (c + 1) + ":");
                System.out.println("  Subject DN: " + cert.getSubjectDN());
                System.out.println("  Signature Algorithm: " + cert.getSigAlgName());
                System.out.println("  Valid from: " + cert.getNotBefore());
                System.out.println("  Valid until: " + cert.getNotAfter());
                System.out.println("  Issuer: " + cert.getIssuerDN());
            }
        }
        X509ExtendedTrustManager defaultTrustManager = null;
        defaultTrustManager.checkClientTrusted(certificates,authType);
    }
}
