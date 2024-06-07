package model;

import java.io.Serializable;

public class RoomRating implements Serializable {

    private String roomOwner;
    private String roomName;
    private int rating;

    public String getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(String roomOwner) {
        this.roomOwner = roomOwner;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "RoomRating{" +
                "roomOwner='" + roomOwner + '\'' +
                ", roomName='" + roomName + '\'' +
                ", rating=" + rating +
                '}';
    }
}