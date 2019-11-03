package models;

import com.google.common.base.Objects;

import java.io.Serializable;

import static com.google.common.base.MoreObjects.toStringHelper;

public class Location implements Serializable { // only for binary we have implemented Serializable Interface
    public long id;
    public float latitude;
    public float longitude;
    private static long counter = 0L;

    public Location() {
    }

    public Location(float latitute, float logitude) {
        this.id = counter++;
        this.latitude = latitute;
        this.longitude = logitude;
    }

    public long getId() {
        return id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return toStringHelper(this).addValue(id)
                .addValue(latitude)
                .addValue(longitude)
                .toString();
    }

   /* @Override
    public int hashCode()
    {
        return Objects.hashCode(this.id, this.latitude, this.longitude);
    }*/


    @Override
    public boolean equals(final Object obj)
    {
        if (obj instanceof Location)
        {
            final Location other = (Location) obj;
            return Objects.equal(latitude, other.latitude)
                    && Objects.equal(longitude, other.longitude);
        }
        else
        {
            return false;
        }
    }
}
