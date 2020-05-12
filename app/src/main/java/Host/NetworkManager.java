package Host;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import Common.BitInputStream;
import Common.BitOutputStream;
import Common.InputBitStream;
import Common.OutputBitStream;
import Common.Settings;

public class NetworkManager {
    private int _newPlayerId;
    private ServerSocket _socket;
    private Map<InetAddress, ClientProxy> _mappingAddr2Proxy;
    private ArrayList<Socket> _clientSockets;
    private ClientProxy _hostClient;
    private OutputBitStream _sendThisFrame;

    public NetworkManager(){
        _newPlayerId = 0;
        _sendThisFrame = new BitOutputStream();
        _mappingAddr2Proxy = new HashMap<>();
        _clientSockets = new ArrayList<>();
    }

    public void open(){
        try {
            _socket = new ServerSocket(Settings.PORT);
            (new Thread(this::acceptor)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeAccept(){
        try {
            _socket.close();
        } catch (IOException e) {
            // I don't know how this could happen
            e.printStackTrace();
        }
    }

    public void send(){
        for (Socket socket : _clientSockets){
            try {
                OutputStream stream = socket.getOutputStream();
                stream.write(_sendThisFrame.getBuffer(), 0, _sendThisFrame.getBufferByteLength());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        _sendThisFrame.resetPos();
    }

    private void acceptor(){
        while (true) {
            try {
                Socket newSocket = _socket.accept();
                ClientProxy client = new ClientProxy(_newPlayerId++);
                _mappingAddr2Proxy.put(newSocket.getInetAddress(), client);
                _clientSockets.add(newSocket);
                (new Thread(() -> receive(newSocket, client))).start();
            } catch (IOException e) {
                // socket closed; thread exit
                e.printStackTrace();
            }
        }
    }

    private void receive(Socket socket, ClientProxy client){
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

    public OutputBitStream getPacketToSend(){
        return _sendThisFrame;
    }

    public int getNumConnections(){
        return _mappingAddr2Proxy.size();
    }

    public Collection<ClientProxy> getClientProxies(){
        return _mappingAddr2Proxy.values();
    }
}
