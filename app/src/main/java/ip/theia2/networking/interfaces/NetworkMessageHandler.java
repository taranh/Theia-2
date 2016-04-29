package ip.theia2.networking.interfaces;

/**
 * Interface for handling messages from a network connection.
 *
 * @author Zachary Shannon
 */
public interface NetworkMessageHandler {

    void handleMessage(String msg);

}
