package models;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.MoreObjects.toStringHelper;


public class Activity {
    public String id;
    public String type;
    public String location;
    public int distance;
    public String startTime;
    public int duration;
    public List<Location> route = new ArrayList<>();

    public Activity(String type, String location, int distance, String startTime, int duration) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.location = location;
        this.distance = distance;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Activity() {
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public int getDistance() {
        return distance;
    }

    public String getStarttime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public List<Location> getroute() {
        return route;
    }

    public List<Float> getRoute() {
        List<Float> locationsList = new ArrayList<>();
        this.route.forEach((temp) -> {
            locationsList.add(temp.getLatitude());
            locationsList.add(temp.getLongitude());
        });
        return locationsList;
    }

    public void addRoute(Location location) {
        route.add(location);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .addValue(id)
                .addValue(type)
                .addValue(location)
                .addValue(startTime)
                .addValue(duration)
                .addValue(route)
                .toString();
    }
    /*@Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.type, this.location, this.distance, this.startTime, this.duration);
    }*/

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Activity) {
            final Activity other = (Activity) obj;
            return Objects.equal(type, other.type)
                    && Objects.equal(location, other.location)
                    && Objects.equal(distance, other.distance)
                    && Objects.equal(startTime, other.startTime)
                    && Objects.equal(duration, other.duration)
                    && Objects.equal(route, other.route);
        } else {
            return false;
        }
    }
}
