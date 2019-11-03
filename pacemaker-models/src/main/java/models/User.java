package models;

import java.util.*;
import com.google.common.base.Objects;
import static com.google.common.base.MoreObjects.toStringHelper;


public class User { // only for binary we have implemented Serializable Interface
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String id;
    public Map<String, Activity> activities = new HashMap<>();

    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstname() {
        return firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, Activity> getActivities() {
        return activities;
    }

    /**
     * using google API
     * <p>
     * toString() gets automatically called if we print user, then it will call user.toString() to print the user
     * this function will be used for all collection objects i.e. list, map etc.
     **/
    @Override
    public String toString() {
        return toStringHelper(this)
                .addValue(id)
                .addValue(firstName)
                .addValue(lastName)
                .addValue(email)
                .addValue(activities)
                .addValue(password).toString();
    }




   /* @Override
    public int hashCode() {
        return Objects.hashCode(this.lastName, this.firstName, this.email, this.password);
    }*/

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof User) {
            final User other = (User) obj;
            return Objects.equal(firstName, other.firstName)
                    && Objects.equal(lastName, other.lastName)
                    && Objects.equal(email, other.email)
                    && Objects.equal(password, other.password)
                    && Objects.equal(activities, other.activities);
        } else {
            return false;
        }
    }
}
