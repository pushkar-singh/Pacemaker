package models;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static models.Fixtures.locations;
import static org.junit.jupiter.api.Assertions.*;

import static models.Fixtures.*;

class ActivityTest {
    private Activity activity;
    private Activity newActivity = new Activity();

    @BeforeEach
    void setup() {
        activity = new Activity("walk", "fridge", 20, "2015-08-04T10:10:30", 12);
        for (Location loc : locations) {
            activity.addRoute(loc);
        }
    }

    @Test
    void testCreate() {
        assertEquals("walk", activity.getType());
        assertEquals("fridge", activity.getLocation());
        assertEquals(20, activity.getDistance());
        assertEquals(12, activity.getDuration());
        assertEquals("2015-08-04T10:10:30", activity.getStarttime());
    }

    @Test
    void testGetRoute() {
        List<Float> locationList = new ArrayList<>();
        for (Location loc : locations) {
            locationList.add(loc.getLatitude());
            locationList.add(loc.getLongitude());
        }
        assertIterableEquals(locationList, activity.getRoute());
    }

    @Test
    void testToString()
    {
        assertEquals ("Activity{" + activity.getId() + ", walk, fridge, 2015-08-04T10:10:30, 12, " + activity.getroute() + "}", activity.toString());
    }

   @Test
    public void testIds() {
        Set<String> ids = new HashSet<>();
        for (Activity activity : activities) {
            ids.add(activity.id);
        }
        assertEquals(activities.length, ids.size());
        assertNotEquals(activities[0].id, activities[1].id);
    }
}
