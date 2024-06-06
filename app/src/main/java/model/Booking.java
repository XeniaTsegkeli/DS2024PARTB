package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Booking implements Serializable {
    private String owner;
    private String roomName;
    private String dates;
    private List<Date> dateList = new ArrayList<>();
    private String renterName;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }

    public List<Date> getDateList() {
        return dateList;
    }

    public void setDateList(List<Date> dateList) {
        this.dateList = dateList;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String user) {
        this.owner = user;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "user='" + owner + '\'' +
                ", roomName='" + roomName + '\'' +
                ", dates='" + dates + '\'' +
                ", dateList=" + dateList +
                ", renterName='" + renterName + '\'' +
                '}';
    }
}