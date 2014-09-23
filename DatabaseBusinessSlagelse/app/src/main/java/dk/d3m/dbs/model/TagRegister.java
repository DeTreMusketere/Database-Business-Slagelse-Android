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

    public String[] getObjectsAsStringArray() {
        String[] array = new String[objects.size()];

        for(int i=0; i < objects.size(); i++) {
            array[i] = objects.get(i).getName();
        }

        return array;
    }
}
