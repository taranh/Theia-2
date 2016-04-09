package ip.theia2;

/**
 * Created by Zach on 03/03/2016.
 */
public interface NetworkMessageHandler {
    /**
     * Handle a message from a network connection.
     * @return True if succeeded, false if failed.
     * @
     */
    public Boolean handleMessage(String msg);
}
