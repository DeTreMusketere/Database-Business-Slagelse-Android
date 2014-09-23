package dk.d3m.dbs.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import dk.d3m.dbs.util.SaleSortUtil;

/**
 * Created by Patrick on 25-08-2014.
 */
public class SaleRegister extends Register<Sale> {

    private PictureRegister pictureRegister;
    private TagRegister tagRegister;
    private ArrayList<Sale> sorted;

    public SaleRegister(PictureRegister pictureRegister, TagRegister tagRegister) {
        this.pictureRegister = pictureRegister;
        this.tagRegister = tagRegister;
        sorted = new ArrayList<Sale>();
    }

    public ArrayList<Sale> getSorted() {
        return sorted;
    }

    public void sort(Tag tag) {
        sorted.clear();
        if(tag != null) {
            for(Sale s : getObjects()) {
                for(Tag t : s.getTags()) {
                    if(t.getId() == tag.getId()) {
                        sorted.add(s);
                    }
                }
            }
        } else {
            sorted.addAll(getObjects());
        }
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
