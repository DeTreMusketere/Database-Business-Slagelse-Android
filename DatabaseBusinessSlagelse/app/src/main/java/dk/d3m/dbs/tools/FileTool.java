package dk.d3m.dbs.tools;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import dk.d3m.dbs.model.Picture;
import dk.d3m.dbs.model.PictureRegister;
import dk.d3m.dbs.model.Sale;
import dk.d3m.dbs.model.SaleRegister;
import dk.d3m.dbs.model.Tag;
import dk.d3m.dbs.model.TagRegister;

/**
 * Created by Patrick on 28-08-2014.
 */
public class FileTool {

    private static final String picturePath = "pictureObjects.data";
    private static final String salePath = "saleObjects.data";
    private static final String tagPath = "tagObjects.data";

    public static void saveRegisters(Context context, PictureRegister pictureRegister, SaleRegister saleRegister, TagRegister tagRegister) {
        try {
            FileOutputStream fout = context.openFileOutput(picturePath, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(pictureRegister.getObjects());

            oos.close();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fout = context.openFileOutput(salePath, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(saleRegister.getObjects());

            oos.close();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fout = context.openFileOutput(tagPath, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(tagRegister.getObjects());

            oos.close();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadRegisters(Context context, PictureRegister pictureRegister, SaleRegister saleRegister, TagRegister tagRegister) {

        try {
            FileInputStream fin = context.openFileInput(picturePath);
            ObjectInputStream ois = new ObjectInputStream(fin);
            pictureRegister.setObjects((ArrayList<Picture>) ois.readObject());
            ois.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fin = context.openFileInput(salePath);
            ObjectInputStream ois = new ObjectInputStream(fin);
            saleRegister.setObjects((ArrayList<Sale>) ois.readObject());
            ois.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fin = context.openFileInput(tagPath);
            ObjectInputStream ois = new ObjectInputStream(fin);
            tagRegister.setObjects((ArrayList<Tag>) ois.readObject());
            ois.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Tag tag = new Tag(0, "Alle", "Alle tilbud");
        if(!tagRegister.contains(tag.getId())) {
            tagRegister.getObjects().add(0, tag);
        }

    }
}
