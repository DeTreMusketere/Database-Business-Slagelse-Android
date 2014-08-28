package dk.d3m.dbs.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Patrick on 25-08-2014.
 */
public class Picture extends Data {

    private String name;
    private byte[] byteArray;

    public Picture(int id, String name, byte[] byteArray) {
        super(id);
        this.name = name;
        this.byteArray = byteArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bitmap;
    }

    @Override
    public String toString() {
        return "name: " + name;
    }
}
