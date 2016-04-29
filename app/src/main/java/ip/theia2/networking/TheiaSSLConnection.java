package ip.theia2.networking;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * This class consists of methods which handles client-server connection.
 *
 * @author Zachary Shannon
 */

public class TheiaSSLConnection {

    private Socket sock;
    private InputStream is;
    private OutputStream os;

    /**
     * Creates a new server connection.
     *
     * TODO: Try to limit this to only the SECURE PROTOCOLS.
     *
     * @param host Host to connect to
     * @param port Port to connect to
     * @param trustStoreFile InputStream for the custom trust store to use.
     * @see <a href="http://blog.trifork.com/2009/11/10/securing-connections-with-tls/">SSL Socket
     *          Setup by Erik van Oosten (2009)</a>
     */
    public TheiaSSLConnection(String host, int port, InputStream trustStoreFile){
        try {
            sock = new Socket(host, port);
            System.err.println("I HAVE CREATED A SOCKET");
        } catch (UnknownHostException e) {
            System.err.println("Cannot connect to host: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("error: "+ e.getMessage());
        }

        //Wrap existing socket with SSLSocketFactory
        SSLSocketFactory sssf = getSssf(trustStoreFile);

        SSLSocket sslSock = null;

        try {
            sslSock = (SSLSocket) sssf.createSocket(sock, host, port, true);
        } catch (IOException e) {
            System.err.println("Error making connection secure: " + e.getMessage());
        }

        //TODO Try to limit this to only the SECURE PROTOCOLS.
        sslSock.setEnabledProtocols(sslSock.getEnabledProtocols());
        sslSock.setEnabledCipherSuites(sslSock.getEnabledCipherSuites());

        //Execute handshake
        try {
            sslSock.startHandshake();
        } catch (IOException e) {
            System.err.println("Error with SSL handshake: " + e.getMessage());
        }

        sock = sslSock; //Can use the socket now.

        try {
            is = sock.getInputStream();
            os = sock.getOutputStream();
        } catch (IOException e) {
            System.err.println("Input output error! Couldn't get IO streams: " + e.getMessage());
        }

    }

    /**
     * I don't know what to write here - Kai
     *
     * @param trustStoreFile
     * @return SSLSocketFactoru
     */
    private SSLSocketFactory getSssf(InputStream trustStoreFile){
        //Need a trust store - borrowed from the server code.
        KeyStore ksTrusts = null;
        InputStream trusts = null;
        TrustManagerFactory tmf = null;

        //To generate a factory
        SSLContext sslContext = null;

        //Password for truststore.
        char[] trustsPass = "hfjcv956".toCharArray();

        //Create a new keystore for the trusts.
        try {
            ksTrusts = KeyStore.getInstance("BKS");
            ksTrusts.load(trustStoreFile, trustsPass);

            //Create a trust manager factory
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ksTrusts);

            //Generate an ssl context
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

        } catch (KeyStoreException e) {
            System.err.println("Issue with keystore: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("No algorithm!: " + e.getMessage());
        } catch (java.security.cert.CertificateException e) {
            System.err.println("Certificate error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
        } catch (KeyManagementException e) {
            System.err.println("Key management error!: " + e.getMessage());
        }

        //Finally, generate the sslSocketFactory used to wrap the client sockets.
        return sslContext.getSocketFactory();
    }

    /**
     * @return Returns an output stream to write to.
     */
    public OutputStream getOutputStream(){
        return os;
    }

    /**
     * @return Returns an input stream to read from.
     */
    public InputStream getInputStream(){
        return is;
    }

    /**
     * @return A list of cipher suites that this will consider 'secure'.
     * @see <a href="www.ssllabs.com/ssltest/viewClient.html?name=Java&version=7u25">Secure Suites</a>
     */
    public static String[] getSecureSuites(){
        //Taken from server
        return new String[]{
                "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
                "TLS_RSA_WITH_AES_128_CBC_SHA",
                "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA",
                "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA",
                "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
                "TLS_DHE_DSS_WITH_AES_128_CBC_SHA"
        };
    }

    /**
     * @return A list of protocols this will consider 'secure'.
     * @see <a href="https://cipherli.st/">Strong ciphers</a>
     */
    public static String[] getSecureProtocols(){
        //Taken from server
        return new String[]{
                "TLSv1.2",
                "TLSv1.1",
                "TLSv1",
                "SSLv3"
        };
    }
}
