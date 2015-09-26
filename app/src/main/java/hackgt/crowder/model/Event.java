package hackgt.crowder.model;

import android.location.Location;

public class Event {

    private int id;
    private Location location;

    private int score;

    private String title;

    private String startDate;

    private String endDate;

    public Event(double latitude, double longitude, String title, int score) {
        this.title = title;
        this.score = score;
        location = new Location(title);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
    }

    public Event(double latitude, double longitude, String title) {
        this(latitude, longitude, title, 0);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public void upvote() {
        score++;
    }

    public void downvote() {
        score--;
    }

    public Event(int id, double latitude, double longitude, String title) {
        this(latitude, longitude, title);
        this.id = id;

    }

    public Location getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
