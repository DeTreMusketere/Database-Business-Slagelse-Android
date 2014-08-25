package dk.d3m.dbs.model;

/**
 * Created by Patrick on 25-08-2014.
 */
public class PictureRegister extends Register<Picture> {

    public void create(int id, int updateNumber, String name, byte[] byteArray) {
        Picture p = new Picture(id, updateNumber, name, byteArray);
        insert(p);
    }
}
