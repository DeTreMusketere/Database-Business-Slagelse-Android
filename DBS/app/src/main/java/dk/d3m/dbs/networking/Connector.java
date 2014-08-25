package dk.d3m.dbs.networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Patrick on 23-08-2014.
 */
public class Connector {

    private CommTool commTool;
    private Socket socket;
    private String address = "192.168.0.31";
    private int port = 6666;
    private boolean auto;

    /**
     * Constructs a connector
     * @param address - specifies the address
     * @param port - specifies the port
     * @param auto - specifies if connect/disconnect is done automatically
     */
    public Connector(String address, int port, boolean auto) {
        this.address = address;
        this.port = port;
        this.auto = auto;
    }

    public Connector(boolean auto) {
        this.auto = auto;
    }

    public void connect() {
        try {
            this.socket = new Socket();
            this.socket.connect(new InetSocketAddress(address, port), 2000);
            if(socket != null) {
                this.commTool = new CommTool(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if(socket != null) {
                this.socket.close();
            }
            if(commTool != null) {
                this.commTool = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCurrrentUpdateNumber() {
        int updateNumber = -1;
        if(auto) {
            connect();
        }
        if(socket != null && commTool != null) {
            commTool.sendMessage("getun");
            updateNumber = Integer.valueOf(commTool.receiveStringMessage());
        }
        if(auto) {
            disconnect();
        }
        return updateNumber;
    }
}
