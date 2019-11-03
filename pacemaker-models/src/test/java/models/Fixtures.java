package models;

class Fixtures {
    public static User[] users = {
            new User("marge", "simpson", "marge@simpson.com", "secret"),
            new User("lisa", "simpson", "lisa@simpson.com", "secret"),
            new User("bart", "simpson", "bart@simpson.com", "secret"),
            new User("maggie", "simpson", "maggie@simpson.com", "secret")
    };
    public static Activity[] activities = {
            new Activity("walk", "fridge", 10, "2015-08-04T10:11:30", 10),
            new Activity("walk", "bar", 20, "2015-08-02T10:12:30", 20),
            new Activity("run", "work", 15, "2015-09-02T12:2:30", 25),
            new Activity("walk", "shop", 10, "2015-09-02T13:2:30", 15),
            new Activity("cycle", "school", 10, "2015-09-02T10:21:30", 10)
    };
    static Location[] locations = {
            new Location(23.3f, 33.3f),
            new Location(34.4f, 45.2f),
            new Location(25.3f, 34.3f),
            new Location(44.4f, 23.3f)
    };
}
