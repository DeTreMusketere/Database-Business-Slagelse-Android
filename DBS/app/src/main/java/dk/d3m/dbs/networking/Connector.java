package dk.d3m.dbs.networking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import dk.d3m.dbs.model.Data;
import dk.d3m.dbs.model.Picture;
import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.SaleRegister;

/**
 * Created by Patrick on 23-08-2014.
 */
public abstract class Connector {

    private CommTool commTool;
    private Socket socket;
    private String address = "10.64.9.166";
    private int port = 6666;
    private boolean auto;

    private PictureRegister pictureRegister;
    private SaleRegister saleRegister;

    /**
     * Constructs a connector
     * @param address - specifies the address
     * @param port - specifies the port
     * @param auto - specifies if connect/disconnect is done automatically
     */
    public Connector(String address, int port, boolean auto, PictureRegister pictureRegister, SaleRegister saleRegister) {
        this.address = address;
        this.port = port;
        this.auto = auto;
        this.pictureRegister = pictureRegister;
        this.saleRegister = saleRegister;
    }

    public Connector(boolean auto, PictureRegister pictureRegister, SaleRegister saleRegister) {
        this.auto = auto;
        this.pictureRegister = pictureRegister;
        this.saleRegister = saleRegister;
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

    public void updateRegisters(int updateNumber) {
        if(auto) {
            connect();
        }

        if(socket != null && commTool != null) {
            commTool.sendMessage("getobjs");
            String answer = commTool.receiveStringMessage();
            if(answer.equalsIgnoreCase("ok")) {
                commTool.sendMessage(String.valueOf(updateNumber));
                String jsonArrayString = commTool.receiveStringMessage();

                JSONArray array;
                JSONArray JSONPictures = null;
                JSONArray JSONSales = null;
                try {
                    array = new JSONArray(jsonArrayString);
                    JSONPictures = array.getJSONArray(0);
                    JSONSales = array.getJSONArray(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for(int i = 0; i < JSONPictures.length(); i++) {
                    try {
                        JSONObject obj = JSONPictures.getJSONObject(i);
                        pictureRegister.create(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                for(int i = 0; i < JSONSales.length(); i++) {
                    try {
                        JSONObject obj = JSONSales.getJSONObject(i);
                        saleRegister.create(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



            }
        }

        if(auto) {
            disconnect();
        }
    }
}
