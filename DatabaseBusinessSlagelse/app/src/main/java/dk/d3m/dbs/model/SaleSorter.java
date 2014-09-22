package dk.d3m.dbs.model;

import java.util.ArrayList;

import dk.d3m.dbs.util.SaleSortUtil;

/**
 * Created by Patrick on 18-09-2014.
 */
public class SaleSorter {

    private SaleRegister saleRegister;
    private ArrayList<Sale> sortedObjects;

    public SaleSorter(SaleRegister saleRegister) {
        this.saleRegister = saleRegister;
        sortedObjects = saleRegister.getObjects();
    }

    public ArrayList<Sale> getSortedObjects() {
        return sortedObjects;
    }

    public void sortObjects(Tag tag) {
        sortedObjects.clear();
        sortedObjects.addAll(SaleSortUtil.sortByTag(saleRegister.getObjects(), tag));
    }
}
