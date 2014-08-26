package dk.d3m.dbs.model;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 25-08-2014.
 */
public class PictureRegister extends Register<Picture> {

    public void create(int id, String name, byte[] byteArray) {
        Picture p = new Picture(id, name, byteArray);
        insert(p);
    }

    public void create(JSONObject obj) {
        try {
            int id = obj.getInt("id");
            String name = obj.getString("name");
            String array = obj.getString("array");
            byte[] byteArray = Base64.decode(array, Base64.DEFAULT);

            Picture p = new Picture(id, name, byteArray);
            insert(p);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
