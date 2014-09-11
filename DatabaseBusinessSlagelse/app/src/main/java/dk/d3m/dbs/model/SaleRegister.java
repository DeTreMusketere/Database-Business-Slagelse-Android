package dk.d3m.dbs.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Patrick on 25-08-2014.
 */
public class SaleRegister extends Register<Sale> {

    private PictureRegister pictureRegister;
    private TagRegister tagRegister;

    public SaleRegister(PictureRegister pictureRegister, TagRegister tagRegister) {
        this.pictureRegister = pictureRegister;
        this.tagRegister = tagRegister;
    }

    @Override
    public void create(JSONObject obj) {
        try {
            int id = obj.getInt("id");
            String name = obj.getString("name");
            String description = obj.getString("description");
            int pictureId = obj.getInt("picture");
            Picture picture = pictureRegister.get(pictureId);
            double price = obj.getDouble("price");
            Date start = new Date(obj.getLong("start"));
            Date end = new Date(obj.getLong("end"));
            ArrayList<Tag> tags = new ArrayList<Tag>();
            JSONArray tArray = obj.getJSONArray("tags");
            for(int i = 0; i < tArray.length(); i++) {
                Tag t = tagRegister.get(tArray.getInt(i));
                tags.add(t);
            }

            Sale s = new Sale(id, name, description, picture, price, start, end, tags);
            insert(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
