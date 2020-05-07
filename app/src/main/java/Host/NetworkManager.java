package Host;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class NetworkManager {
    private int _newPlayerId;
    private int _port;
    private ServerSocket _socket;
    private Map<InetAddress, ClientProxy> _mappingAddr2Proxy;
    private ClientProxy _hostClient;

    public NetworkManager(int port){
        _newPlayerId = 0;
        _port = port;
    }

    public void init(){
        try {
            _socket = new ServerSocket(_port);
            (new Thread(this::acceptor)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(long ms){
    }

    private void acceptor(){
        while (true) {
            try {
                Socket newSocket = _socket.accept();
                ClientProxy client = new ClientProxy(_newPlayerId++);
                _mappingAddr2Proxy.put(newSocket.getInetAddress(), client);
                (new Thread(()->reader(newSocket, client))).start();
            } catch (IOException e) {
                // socket closed; thread exit
                e.printStackTrace();
            }
        }
    }

    private void reader(Socket socket, ClientProxy client){
        int numBytes;
        try {
            InputStream stream = socket.getInputStream();
            while (true){
                numBytes = stream.read(client.getPacketBuffer());
                client.setPacketBufferLen(numBytes);
            }
        } catch (IOException e) {
            // socket closed; thread exit
            e.printStackTrace();
        }
    }

    public ClientProxy getHostClientProxy() {
        return _hostClient;
    }

    public void broadCastToClients(byte[] buffer) {
    }
}
