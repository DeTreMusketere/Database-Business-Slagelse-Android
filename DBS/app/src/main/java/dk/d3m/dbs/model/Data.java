package dk.d3m.dbs.model;

/**
 * Created by Patrick on 23-08-2014.
 */
public abstract class Data {

    protected int id;
    protected int updateNumber;

    public Data(int id, int updateNumber) {
        this.id = id;
        this.updateNumber = updateNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUpdateNumber() {
        return updateNumber;
    }

    public void setUpdateNumber(int updateNumber) {
        this.updateNumber = updateNumber;
    }
}
