package hackgt.crowder.model;

import android.location.Location;

import java.util.ArrayList;

public class Event {

    private int id;
    private int score;
    private String title;
    private String startDate;
    private String endDate;
    private String description;
    private double price;
    private double latitude;
    private double longitude;
    private String address;
    private ArrayList<String> tags;
    private ArrayList<String> comments;

    public Event() {
        id = -1;
        score = -1;
        title = "";
        startDate = "";
        endDate = "";
        description = "";
        price = 0.00;
        latitude = 0;
        longitude = 0;
        tags = new ArrayList<>();
    }

    public void upvote() {
        score++;
    }

    public void downvote() {
        score--;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
