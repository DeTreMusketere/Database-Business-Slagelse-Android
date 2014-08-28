package dk.d3m.dbs.networking;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.RegisterHandler;
import dk.d3m.dbs.model.SaleRegister;

/**
 * Created by Patrick on 23-08-2014.
 */
public class Connector {

    private CommTool commTool;
    private Socket socket;
    private String address;
    private int port;
    private boolean auto;
    private Activity context;
    protected SharedPreferences prefs;

    private PictureRegister pictureRegister;
    private SaleRegister saleRegister;

    public Connector(Activity context, boolean auto) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.address = prefs.getString("host", "");
        this.port = Integer.valueOf(prefs.getString("port", ""));
        this.auto = auto;
        this.pictureRegister = RegisterHandler.getPictureRegisterInstance();
        this.saleRegister = RegisterHandler.getSaleRegisterInstance();
        this.context = context;

    }

    public boolean connect() {
        try {
            this.socket = new Socket();
            this.socket.connect(new InetSocketAddress(address, port), 2000);
            if(socket != null) {
                this.commTool = new CommTool(socket);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean disconnect() {
        try {
            if(socket != null) {
                this.socket.close();
            }
            if(commTool != null) {
                this.commTool = null;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getCurrrentUpdateNumber() {
        System.out.println("Connector: Getting UN");
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
        System.out.println("Connector: Finished Getting UN");
        return updateNumber;
    }

    public boolean updateRegisters(int updateNumber) {
        System.out.println("Connector: Updating registers");
        if(auto) {
            connect();
        }

        if(socket != null && commTool != null) {
            commTool.sendMessage("getobjs");
            String answer = commTool.receiveStringMessage();
            System.out.println("ServerAnswer: " + answer);
            if(answer.equalsIgnoreCase("ok")) {
                commTool.sendMessage(String.valueOf(updateNumber));
                String jsonArrayString = commTool.receiveStringMessage();

                System.out.println("JSON: " + jsonArrayString);

                JSONArray array;
                JSONArray JSONPictures = null;
                JSONArray JSONSales = null;
                try {
                    array = new JSONArray(jsonArrayString);
                    JSONPictures = array.getJSONArray(1);
                    JSONSales = array.getJSONArray(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }

                for(int i = 0; i < JSONPictures.length(); i++) {
                    System.out.println("Creating picture");
                    try {
                        JSONObject obj = JSONPictures.getJSONObject(i);
                        pictureRegister.create(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                for(int i = 0; i < JSONSales.length(); i++) {
                    System.out.println("Creating sale");
                    try {
                        JSONObject obj = JSONSales.getJSONObject(i);
                        saleRegister.create(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    }
                }

            } else {
                return false;
            }
        } else {
            return false;
        }

        if(auto) {
            disconnect();
        }

        System.out.println("Connector: Finished Updating registers");
        return true;
    }
}
