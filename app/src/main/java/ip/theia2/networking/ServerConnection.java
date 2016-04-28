package ip.theia2.networking;

import java.io.*;

import ip.theia2.networking.interfaces.NetworkMessageHandler;

/**
 * Created by Zachary Shannon on 03/03/2016.
 */
public class ServerConnection {

    private BufferedReader br;
    private BufferedWriter wr;
    private NetworkMessageHandler nmh;

    /**
     * Creates a new network connection.
     * @param trustStore TrustStore file to use for the connection.
     */
    public ServerConnection(final String host, final int port, final InputStream trustStore, NetworkMessageHandler newNmh){
        nmh = newNmh;

        TheiaSSLConnection sslCon = new TheiaSSLConnection(host, port, trustStore);

        br = new BufferedReader(new InputStreamReader(sslCon.getInputStream()));
        wr = new BufferedWriter(new OutputStreamWriter(sslCon.getOutputStream()));

        (new Thread(){
            public void run(){
                while(true){
                    try{
                        String cmd = br.readLine();
                        nmh.handleMessage(cmd);
                    }catch(IOException e){
                        System.err.println("Error reading server message " + e.getMessage());
                    }
                }
            }
        }).start();

    }

    /**
     * Sends the message to the server.
     * @param msg Message to send to the server.
     */
    public synchronized void sendMessage(String msg){
        try {
            wr.write(msg);
            wr.newLine();
            wr.flush();
        }catch(IOException e){
            System.err.println("Error sending message to server! " + e.getMessage());
        }
    }
}
