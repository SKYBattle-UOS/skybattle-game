package Host;

public interface ConnectionListener {
    void onConnectionLost(ClientProxy client);
    void onNewConnection(ClientProxy client);
}
