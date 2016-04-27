package ip.theia2.interfaces;

public interface NetworkMessageHandler {
    /**
     * Handle a message from a network connection.
     * @return True if succeeded, false if failed.
     * @
     */
    public void handleMessage(String msg);
}
