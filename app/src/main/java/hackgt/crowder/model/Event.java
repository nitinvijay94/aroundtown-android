package hackgt.crowder.model;

import android.location.Location;

public class Event {

    private int id;
    private Location location;
    private String title;

    public Event(double latitude, double longitude, String title) {
        this.title = title;
        location = new Location(title);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
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
