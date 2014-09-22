package dk.d3m.dbs.util;

import java.util.ArrayList;

import dk.d3m.dbs.model.Sale;
import dk.d3m.dbs.model.Tag;

/**
 * Created by Patrick on 11-09-2014.
 */
public class SaleSortUtil {

    /**
     * Sorts sales by a tag. If tag is null, sales will be returned as is.
     * @param sales
     * @param tag
     * @return sorted sales
     */
    public static ArrayList<Sale> sortByTag(ArrayList<Sale> sales, Tag tag) {
        if(tag != null) {
            ArrayList<Sale> newSales = new ArrayList<Sale>();
            for (Sale s : sales) {
                System.out.println(s.getTags().size());
                if (s.getTags().contains(tag)) {
                    newSales.add(s);
                }
            }
            return newSales;
        } else {
            return sales;
        }
    }
}
