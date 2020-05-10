package Host;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;

import Common.BitInputStream;
import Common.InputBitStream;
import Common.OutputBitStream;

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

    public void open(){
        try {
            _socket = new ServerSocket(_port);
            (new Thread(this::acceptor)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeAccept(){
        try {
            _socket.close();
        } catch (IOException e) {
            // I don't know why this could happen
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
                InputBitStream packetStream = new BitInputStream();
                numBytes = stream.read(packetStream.getBuffer());
                packetStream.setBufferLength(numBytes);
                client.getRawPacketQueue().offer(packetStream);
            }
        } catch (IOException e) {
            // socket closed; thread exit
            ClientProxy disconnected = _mappingAddr2Proxy.get(socket.getInetAddress());
            disconnected.setDisconnected(true);
        }
    }

    public ClientProxy getHostClientProxy() {
        return _hostClient;
    }

    public void broadCastToClients(byte[] buffer) {
    }

    public OutputBitStream getPacketToSend(){
        return null;
    }

    public int getNumConnections(){
        return _mappingAddr2Proxy.size();
    }

    public Collection<ClientProxy> getClientProxies(){
        return _mappingAddr2Proxy.values();
    }
}
