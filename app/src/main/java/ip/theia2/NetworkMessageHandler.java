package ip.theia2;

public interface NetworkMessageHandler {
    /**
     * Handle a message from a network connection.
     * @return True if succeeded, false if failed.
     * @
     */
    public Boolean handleMessage(String msg);
}
