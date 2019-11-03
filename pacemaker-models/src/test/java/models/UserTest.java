package models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/*import java.util.HashSet;
import java.util.Set;

 */

import static models.Fixtures.*;

class UserTest {
    private User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
    private User user1 = new User();

    @Test
    void testCreate() {
        assertEquals("homer", homer.getFirstname());
        assertEquals("simpson", homer.getLastname());
        assertEquals("homer@simpson.com", homer.getEmail());
        assertEquals("secret", homer.getPassword());
    }

   @Test
    public void testIds() {
        Set<String> ids = new HashSet<>();
        for (User user : users) {
            ids.add(user.id);
        }
        assertEquals(users.length, ids.size());
    }

    @Test
    void testToString() {
        assertEquals("User{" + homer.getId() + ", homer, simpson, homer@simpson.com, {}, secret}", homer.toString());
    }

    @Test
    void testEquals() {
        User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
        User bart = new User("bart", "simpson", "bartr@simpson.com", "secret");

        assertEquals(homer, homer);
        assertNotEquals(homer, bart);
    }
}
