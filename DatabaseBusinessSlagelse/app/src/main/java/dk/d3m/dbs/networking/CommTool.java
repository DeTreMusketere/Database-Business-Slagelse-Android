package dk.d3m.dbs.networking;

/**
 * Created by Patrick on 23-08-2014.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import dk.d3m.dbs.exceptions.SocketNotConnectedException;

public class CommTool {

    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;

    public CommTool(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * Sends a message
     * @param message
     */
    public void sendMessage(String message) throws SocketNotConnectedException {
        if(socket.isConnected()) {
            writer.println(message);
        } else {
            throw new SocketNotConnectedException("Socket was not connect when trying to send a message");
        }
    }

    /**
     * Receives a message as a string
     * @return message (Will be null if nothing received)
     */
    public String receiveStringMessage() throws SocketNotConnectedException {
        if(socket.isConnected()) {
            String answer = null;
            try {
                answer = reader.readLine();
            } catch (IOException ex) {
                throw new SocketNotConnectedException("Socket was not connect when trying to receive a string message");
            }
            return answer;
        } else {
            throw new SocketNotConnectedException("Socket was not connect when trying to receive a string message");
        }
    }

}
