package dk.d3m.dbs.model;

import java.io.Serializable;

/**
 * Created by Patrick on 23-08-2014.
 */
public abstract class Data implements Serializable {

    protected int id;

    public Data(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
