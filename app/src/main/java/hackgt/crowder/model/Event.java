package hackgt.crowder.model;

import android.location.Location;

public class Event {

    private Location location;

    private String title;

    public Event(double latitude, double longitude, String title) {
        this.title = title;
        location = new Location(title);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
    }

    public Location getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }
}
