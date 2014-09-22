package dk.d3m.dbs.networking;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import dk.d3m.dbs.exceptions.SocketNotConnectedException;
import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.SaleRegister;
import dk.d3m.dbs.model.TagRegister;

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
    private SharedPreferences prefs;

    private PictureRegister pictureRegister;
    private SaleRegister saleRegister;
    private TagRegister tagRegister;

    public Connector(Activity context, SharedPreferences prefs, boolean auto, PictureRegister pictureRegister, SaleRegister saleRegister, TagRegister tagRegister) {
        this.address = prefs.getString("host", "");
        this.port = prefs.getInt("port", 0);
        this.auto = auto;
        this.pictureRegister = pictureRegister;
        this.saleRegister = saleRegister;
        this.tagRegister = tagRegister;
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
            try {
                commTool.sendMessage("getun");
            } catch (SocketNotConnectedException e) {
                e.printStackTrace();
            }
            try {
                updateNumber = Integer.valueOf(commTool.receiveStringMessage());
            } catch (SocketNotConnectedException e) {
                e.printStackTrace();
            }
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
            try {
                commTool.sendMessage("getobjs");
            } catch (SocketNotConnectedException e) {
                e.printStackTrace();
            }
            String answer = null;
            try {
                answer = commTool.receiveStringMessage();
            } catch (SocketNotConnectedException e) {
                e.printStackTrace();
            }
            System.out.println("ServerAnswer: " + answer);
            if(answer.equalsIgnoreCase("ok")) {
                try {
                    commTool.sendMessage(String.valueOf(updateNumber));
                } catch (SocketNotConnectedException e) {
                    e.printStackTrace();
                }
                String jsonArrayString = null;
                try {
                    jsonArrayString = commTool.receiveStringMessage();
                } catch (SocketNotConnectedException e) {
                    e.printStackTrace();
                }

                System.out.println("JSON: " + jsonArrayString);

                JSONArray array;
                JSONArray JSONPictures = null;
                JSONArray JSONSales = null;
                JSONArray JSONTags = null;
                try {
                    array = new JSONArray(jsonArrayString);
                    JSONSales = array.getJSONArray(0);
                    JSONPictures = array.getJSONArray(1);
                    JSONTags = array.getJSONArray(2);
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

                for(int i = 0; i < JSONTags.length(); i++) {
                    System.out.println("Creating tag");
                    try {
                        JSONObject obj = JSONTags.getJSONObject(i);
                        tagRegister.create(obj);
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
