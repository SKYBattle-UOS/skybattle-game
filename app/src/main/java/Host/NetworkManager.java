package Host;

import android.util.Log;

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
import Common.Util;

public class NetworkManager {
    private int _newPlayerId;
    private ServerSocket _socket;
    private Map<InetAddress, ClientProxy> _mappingAddr2Proxy;
    private Map<Integer, ClientProxy> _mappingPlayer2Proxy;
    private ArrayList<Socket> _clientSockets;
    private ClientProxy _hostClient;
    private OutputBitStream _sendThisFrame;
    private boolean _shoudSendThisFrame;


    public NetworkManager(){
        _newPlayerId = 0;
        _sendThisFrame = new BitOutputStream();
        _mappingAddr2Proxy = new HashMap<>();
        _mappingPlayer2Proxy = new HashMap<>();
        _clientSockets = new ArrayList<>();
        _shoudSendThisFrame = false;
    }

    public void update(){
        send();
        _sendThisFrame.resetPos();
    }

    public void open(){
        try {
            _socket = new ServerSocket(Util.PORT);
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
        if (!_shoudSendThisFrame) return;

        for (Socket socket : _clientSockets){
            try {
                OutputStream stream = socket.getOutputStream();
                int packetByteLen = _sendThisFrame.getBufferByteLength();
                byte[] sendThis = new byte[packetByteLen + 2];
                System.arraycopy(_sendThisFrame.getBuffer(), 0, sendThis, 2, packetByteLen);

                sendThis[0] = (byte) (packetByteLen & 0xFF);
                sendThis[1] = (byte) ((packetByteLen >>> 8) & 0xFF);

                stream.write(sendThis, 0, sendThis.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        _shoudSendThisFrame = false;
    }

    private void acceptor(){
        while (true) {
            try {
                Socket newSocket = _socket.accept();
                ClientProxy client = new ClientProxy(_newPlayerId);
                _mappingAddr2Proxy.put(newSocket.getInetAddress(), client);
                _mappingPlayer2Proxy.put(_newPlayerId++, client);
                _clientSockets.add(newSocket);
                (new Thread(() -> receive(newSocket, client))).start();
            } catch (IOException e) {
                // socket closed; thread exit
                e.printStackTrace();
                break;
            }
        }
    }

    private void receive(Socket socket, ClientProxy client){
        try {
            InputStream stream = socket.getInputStream();
            while (true){
                InputBitStream newPacket = new BitInputStream();

                int lbyte = stream.read();
                int hbyte = stream.read();
                int packetByteLen = (hbyte << 8) | lbyte;

                int i = 0;
                while (packetByteLen > 0){
                    int b = stream.read();
                    newPacket.getBuffer()[i++] = (byte) b;
                    packetByteLen--;
                }

                newPacket.setBufferLength(i);
                client.getRawPacketQueue().offer(newPacket);
            }
        } catch (IOException e) {
            // TODO
            // socket closed; thread exit
            ClientProxy disconnected = _mappingAddr2Proxy.get(socket.getInetAddress());
            disconnected.setDisconnected(true);
        }
    }

    public void shouldSendThisFrame(){
        _shoudSendThisFrame = true;
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

    public ClientProxy getClientById(int id){
        return _mappingPlayer2Proxy.get(id);
    }
}
