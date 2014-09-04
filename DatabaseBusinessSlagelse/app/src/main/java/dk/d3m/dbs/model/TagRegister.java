package dk.d3m.dbs.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 02-09-2014.
 */
public class TagRegister extends Register<Tag> {
    @Override
    public void create(JSONObject obj) {
        try {
            int id = obj.getInt("id");
            String name = obj.getString("name");
            String description = obj.getString("description");

            Tag t = new Tag(id, name, description);
            insert(t);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void create(int id, String name, String description) {
        Tag t = new Tag(id, name, description);
        insert(t);
    }
}
