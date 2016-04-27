package ip.theia2;

import java.io.InputStream;

import ip.theia2.exceptions.TheiaLoginException;
import ip.theia2.interfaces.LoginHandler;
import ip.theia2.interfaces.NetworkMessageHandler;

/**
 * Created by Zach on 27/04/2016.
 */
public class ServerMessageHandler implements NetworkMessageHandler{

    private LoginHandler lh;
    private ServerConnection conn;

    private String lastMessage;

    /**
     * Adds a login handler.
     * @param lhSet Login handler to add.
     */
    public void addLoginHandler(LoginHandler lhSet){
        lh = lhSet;
    }

    public ServerMessageHandler(final String host, final int port, final InputStream trustStore){

        final NetworkMessageHandler nmh = this;

            (new Thread() {
                public void run() {
                    conn = new ServerConnection(host, port, trustStore, nmh); //We are always using 5575.
                }
            }).start();
    }

    /**
     * Attempt a login
     * @param username Username to use
     * @param password Password to us
     */
    public void login(final String username, final String password) throws TheiaLoginException {
        if(lh != null){
            (new Thread() {
                public void run() {
                    //Send the login message
                    sendMessage("login " + username + " " + password);
                }
            }).start();
        }
        else{
            TheiaLoginException le = new TheiaLoginException();
            throw le;
        }
    }

    /**
     * Send a message to the server.
     * @param message Message to send to the server.
     */
    public void sendMessage(String message){
        lastMessage = splitCmdStr(message)[0]; //Record the last message.

        conn.sendMessage(message);
    }

    /**
     * Implements the handle message interface.
     * @param msg message to handle.
     */
    public void handleMessage(final String msg){
        //Split into commands.
        String[] cmds = splitCmdStr(msg);

        switch(cmds[0]){
            case "acceptlog":

                if(lh != null){
                    lh.loginSuccess();
                }
                break;
            case "rejectlog":

                if(lh != null){
                    lh.loginReject();
                }

                break;
            case "fail":
                failMessage();
        }
    }

    /**
     * Act based on a fail message from the server.
     */
    private void failMessage(){
        switch(lastMessage){
            case "login":

                if(lh != null){
                    lh.loginFail();
                }

                break;
        }
    }

    /**
     * Takes a string command, and gives a more manageable array,
     * where the first element of the array is the command, and the rest are
     * the args.
     *
     * @param cmdStr Command string to split.
     * @return String array in split form.
     */
    private String[] splitCmdStr(String cmdStr){
        String[] cmdArr = cmdStr.split("\\s");

        //Treat underscores as spaces after splitting the string.
        for(int i = 0; i<cmdArr.length; i++){
            cmdArr[i] = cmdArr[i].replace('_', ' ');
        }

        return cmdArr;
    }
}
