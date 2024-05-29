package model;


import java.io.Serial;
import java.io.Serializable;

public class Filters implements Serializable {

    private String user;
    private String area;
    private String dateRange;
    private int numOfPersons;
    private double price;
    private int stars;

    public Filters() {
        this.area = "";
        this.dateRange = "";
        this.numOfPersons = 0;
        this.price = 0.0;
        this.stars = 0;
        this.user = "";
    }
    public Filters(String area, String dateRange, int numOfPersons, double price, int stars, String user) {
        this.area = area;
        this.dateRange = dateRange;
        this.numOfPersons = numOfPersons;
        this.price = price;
        this.stars = stars;
        this.user = user;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public int getNumOfPersons() {
        return numOfPersons;
    }

    public void setNumOfPersons(int numOfPersons) {
        this.numOfPersons = numOfPersons;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Filters{" +
                "user='" + user + '\'' +
                ", area='" + area + '\'' +
                ", dateRange='" + dateRange + '\'' +
                ", numOfPersons=" + numOfPersons +
                ", price=" + price +
                ", stars=" + stars +
                '}';
    }
}
