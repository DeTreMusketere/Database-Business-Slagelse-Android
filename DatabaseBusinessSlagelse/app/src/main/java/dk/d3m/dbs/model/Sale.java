package dk.d3m.dbs.model;

import java.util.Date;

/**
 * Created by Patrick on 25-08-2014.
 */
public class Sale extends Data {

    private String name;
    private String description;
    private Picture picture;
    private double price;
    private Date start;
    private Date end;

    public Sale(int id, String name, String description, Picture picture, double price, Date start, Date end) {
        super(id);
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.price = price;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        String s = "name: " + name + " description: " + description + " picture: " + picture + " price: " + price + " start: " + start.toString() + " end: " + end.toString();
        return s;
    }
}