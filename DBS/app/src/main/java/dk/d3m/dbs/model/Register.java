package dk.d3m.dbs.model;

import java.util.ArrayList;

/**
 * Created by Patrick on 23-08-2014.
 */
public abstract class Register<DATATYPE extends Data> {

    private ArrayList<DATATYPE> objects;

    public Register() {
        objects = new ArrayList<DATATYPE>();
    }

    /**
     * Inserts an object
     * @param source
     */
    public void insert(DATATYPE source) {
        objects.add(source);
    }

    /**
     * Returns an object based on id
     * @param id
     * @return object
     */
    public DATATYPE get(int id) {
        for(DATATYPE d : objects) {
            if(d.getId() == id) {
                return d;
            }
        }
        return null;
    }

    /**
     * Updates an object
     * @param target
     * @param source
     */
    public void update(DATATYPE target, DATATYPE source) {
        objects.set(objects.indexOf(target), source);
    }

    /**
     * Deletes an object
     * @param target
     */
    public void delete(DATATYPE target) {
        objects.remove(target);
    }

    /**
     * Returns all objects
     * @return all objects
     */
    public ArrayList<DATATYPE> getObjects() {
        return objects;
    }


}
