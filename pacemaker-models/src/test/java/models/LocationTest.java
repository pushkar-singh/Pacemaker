package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import static models.Fixtures.*;

class LocationTest {

    private Location location = new Location(23.3f, 33.3f);
    private Location location1 = new Location();

    @Test
    void testCreate() {
        assertEquals(23.3, location.getLatitude(), 0.01);
        assertEquals(33.3, location.getLongitude(), 0.01);
    }

    @Test
    void testToString() {
        assertEquals("Location{" + location.getId() + ", 23.3, 33.3}", location.toString());
    }

    public void testIds(){
        Set<Long> ids = new HashSet<>();
        for(Location location: locations){
            ids.add(location.id);
        }
        assertEquals(locations.length, ids.size());
        assertNotEquals(locations[0].id, locations[1].id);
    }
}
