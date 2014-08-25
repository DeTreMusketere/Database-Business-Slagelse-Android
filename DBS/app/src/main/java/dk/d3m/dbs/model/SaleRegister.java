package dk.d3m.dbs.model;

import java.util.Date;

/**
 * Created by Patrick on 25-08-2014.
 */
public class SaleRegister extends Register<Sale> {

    public void create(int id, int updateNumber, String name, String description, Picture picture, double price, Date start, Date end, Date publish) {
        Sale s = new Sale(id, updateNumber, name, description, picture, price, start, end, publish);
        insert(s);
    }
}
