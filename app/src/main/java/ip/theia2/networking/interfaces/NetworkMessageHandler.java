package ip.theia2.networking.interfaces;

public interface NetworkMessageHandler {
    /**
     * Handle a message from a network connection.
     * @return True if succeeded, false if failed.
     * @
     */
    public void handleMessage(String msg);
}
