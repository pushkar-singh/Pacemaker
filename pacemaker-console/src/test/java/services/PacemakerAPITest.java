package services;

import factories.SerializerFactory;
import models.Activity;
import models.FileFormatEnum;
import models.Location;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serializers.Serializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static services.Fixtures.users;
import static services.Fixtures.activities;
import static services.Fixtures.locations;
import static org.junit.jupiter.api.Assertions.*;

public class PacemakerAPITest {
    private PacemakerService pacemaker;

    @BeforeEach
    void setup() {
        pacemaker = new PacemakerService();
        for (User user : users) {
            pacemaker.createUser(user.getFirstname(), user.getLastname(), user.getEmail(), user.getPassword());
        }
    }

    @AfterEach
    void tearDown() {
        pacemaker = null;
    }

    @Test
    void testCreateUser() {
        assertEquals(users.length, pacemaker.getUsers().size());
        pacemaker.createUser("homer", "marge", "homer@simpson.com", "secret");
        assertEquals(users.length + 1, pacemaker.getUsers().size());
        User expectedUser = users[0];
        User actualUser = pacemaker.getUserByEmail(users[0].getEmail());
        assertEquals(expectedUser.getId().length(), 36); // checks if the UUID length is 36
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void testGetUserById() {
        for (User user : pacemaker.getUsers()) {
            User eachUser = pacemaker.getUser(user.getId());
            assertEquals(user, eachUser);
        }
    }

    @Test
    void testGetUserByEmail() {
        assertEquals(users.length, pacemaker.getUsers().size());
        for (User user : users) {
            User eachUser = pacemaker.getUserByEmail(user.getEmail());
            assertEquals(user, eachUser);
        }
    }

    @Test
    void testDeleteUsers() {
        assertEquals(users.length, pacemaker.getUsers().size());
        User marge = pacemaker.getUserByEmail("marge@simpson.com");
        pacemaker.deleteUser(marge.getId());
        assertEquals(users.length - 1, pacemaker.getUsers().size());
    }

    @Test
    void testAddActivity() {
        User marge = pacemaker.getUserByEmail("marge@simpson.com");
        Activity activity = pacemaker.createActivity(marge.getId(), activities[0].getType(), activities[0].getLocation(),
                activities[0].getDistance(), activities[0].getStarttime(), activities[0].getDuration());
        Activity returnedActivity = pacemaker.getActivity(activity.getId());
        assertEquals(activities[0], returnedActivity);
        assertNotSame(activities[0], returnedActivity);
    }

    @Test
    void testAddActivityWithSingleLocation() {
        User marge = pacemaker.getUserByEmail("marge@simpson.com");
        String activityId = pacemaker.createActivity(marge.getId(), activities[0].getType(), activities[0].getLocation(),
                activities[0].getDistance(), activities[0].getStarttime(), activities[0].getDuration()).getId();

        pacemaker.addLocation(activityId, locations[0].getLatitude(), locations[0].getLongitude());

        Activity activity = pacemaker.getActivity(activityId);
        assertEquals(1, activity.getroute().size());
        assertEquals(0.0001, locations[0].getLatitude(), activity.getroute().get(0).getLatitude());
        assertEquals(0.0001, locations[0].getLongitude(), activity.getroute().get(0).getLongitude());
        assertEquals(pacemaker.getActivitiesById(marge.getId()).size(), 1);
    }

    @Test
    void testAddActivityWithMultipleLocation() {
        User marge = pacemaker.getUserByEmail("marge@simpson.com");
        String activityId = pacemaker.createActivity(marge.getId(), activities[0].getType(), activities[0].getLocation(), activities[0].getDistance(), activities[0].getStarttime(), activities[0].getDuration()).getId();

        for (Location location : locations) {
            pacemaker.addLocation(activityId, location.getLatitude(), location.getLongitude());
        }

        Activity activity = pacemaker.getActivity(activityId);
        assertEquals(locations.length, activity.getroute().size());
        int i = 0;
        for (Location location : activity.getroute()) {
            assertEquals(location, locations[i]);
            i++;
        }
    }

    @Test
    void testDeleteOldFile() throws IOException {
        String filePath = Paths.get("").toAbsolutePath() + "/src/test/java/services/test";
        File file = new File(filePath + ".xml");
        FileWriter writer = new FileWriter(file);
        writer.close();
        //delete extra file except the required file
        pacemaker.deleteExtraFiles(filePath, "yaml");
        assertFalse(file.exists());
    }

    @Test
    void testGetFileFormat() {
        String filePath = Paths.get("").toAbsolutePath() + "/src/test/java/services/test";
        if (new File(filePath + ".xml").exists()) {
            assertEquals("xml", PacemakerService.getFileFormat(filePath));
        } else if (new File(filePath + ".json").exists()) {
            assertEquals("json", PacemakerService.getFileFormat(filePath));
        } else if (new File(filePath + ".yaml").exists()) {
            assertEquals("yaml", PacemakerService.getFileFormat(filePath));
        } else {
            assertEquals("yaml", PacemakerService.getFileFormat(filePath));
        }
    }

    @Test
    void testGetFileFormatWhenNoFileAlreadyExist() {
        String filePath = Paths.get("").toAbsolutePath() + "/src/test/java/services/test1";
        if (new File(filePath + ".xml").exists()) {
            assertEquals("xml", PacemakerService.getFileFormat(filePath));
        } else if (new File(filePath + ".json").exists()) {
            assertEquals("json", PacemakerService.getFileFormat(filePath));
        } else if (new File(filePath + ".yaml").exists()) {
            assertEquals("yaml", PacemakerService.getFileFormat(filePath));
        } else {
            assertEquals("yaml", PacemakerService.getFileFormat(filePath));
        }
    }

    @Test
    void testChangeFileFormat() {
        String fileFormat = FileFormatEnum.yaml.name();
        String filePath = Paths.get("").toAbsolutePath() + "/src/test/java/services/test";
        File file = new File(filePath + "." + fileFormat);
        pacemaker.setDatastore(file);
        pacemaker.setSerializer(SerializerFactory.getSerializer(filePath, fileFormat));
        pacemaker.changeFileFormat(FileFormatEnum.xml.name());
        assertEquals(pacemaker.getCurrentFileFormat(), FileFormatEnum.xml.name());
    }

    @Test
    void testDrawBar() {
        Map<String, Integer> activitiesDistanceTest = new HashMap<>();
        int totalDistanceTest = 65;
        int largestActivityLength = 5;
        String color = "\u001B[36m";

        activitiesDistanceTest.put("run", 15);
        activitiesDistanceTest.put("cycle", 10);
        activitiesDistanceTest.put("walk", 40);

        User marge = pacemaker.getUserByEmail("marge@simpson.com");
        for (Activity activity : activities) {
            pacemaker.createActivity(marge.getId(), activity.getType(), activity.getLocation(), activity.getDistance(), activity.getStarttime(), activity.getDuration());
        }
        pacemaker.drawBar();
        assertEquals(totalDistanceTest, pacemaker.totalDistance);
        assertEquals(largestActivityLength, pacemaker.largestActivityNameLength);
        assertEquals(activitiesDistanceTest.size(), pacemaker.activitiesDistance.size());
        assertEquals(color, pacemaker.ANSI_CYAN);
    }

    @Test
    void testSortActivities() {
        List<Activity> sortedActivites;
        User marge = pacemaker.getUserByEmail("marge@simpson.com");
        sortedActivites = pacemaker.sortActivities(marge.getId(), "type");
        assertTrue(sortedActivites.isEmpty());
        for (Activity activity : activities) {
            pacemaker.createActivity(marge.getId(), activity.getType(), activity.getLocation(), activity.getDistance(), activity.getStarttime(), activity.getDuration());
        }
        sortedActivites = pacemaker.sortActivities(marge.getId(), "type");
        assertEquals("cycle", sortedActivites.get(0).type);

        sortedActivites = pacemaker.sortActivities(marge.getId(), "location");
        assertEquals("bar", sortedActivites.get(0).location);

        sortedActivites = pacemaker.sortActivities(marge.getId(), "distance");
        assertEquals(10, sortedActivites.get(0).distance);

        sortedActivites = pacemaker.sortActivities(marge.getId(), "duration");
        assertEquals(10, sortedActivites.get(0).duration);

        sortedActivites = pacemaker.sortActivities(marge.getId(), "starttime");
        assertEquals("2015-08-04T10:08:50", sortedActivites.get(0).startTime);

        sortedActivites = pacemaker.sortActivities(marge.getId(), "random");
        assertEquals(new ArrayList<>(pacemaker.getActivitiesById(marge.getId())), sortedActivites);
    }

    @Test
    void testInit() {
        String fileFormat;
        String filePath = Paths.get("").toAbsolutePath() + "/src/test/java/services/test";
        pacemaker.init(filePath);
        if (new File(filePath + ".xml").exists()) {
            fileFormat = "xml";
            assertTrue(pacemaker.serializer instanceof serializers.XMLSerializer);
        } else if (new File(filePath + ".json").exists()) {
            fileFormat = "json";
            assertTrue(pacemaker.serializer instanceof serializers.JSONSerializer);
        } else if (new File(filePath + ".yaml").exists()) {
            fileFormat = "yaml";
            assertTrue(pacemaker.serializer instanceof serializers.YamlSerializer);
        } else {
            fileFormat = "yaml";
            assertTrue(pacemaker.serializer instanceof serializers.YamlSerializer);
        }
        assertEquals(fileFormat, pacemaker.fileFormat);
        assertEquals("test." + fileFormat, pacemaker.datastore.getName());
    }

    @Test
    void testGetActivitiesByUserIdWhenUserNotPresent() {
        assertTrue(pacemaker.getActivitiesById("random").isEmpty());
    }

    @Test
    void testStore() {
        String fileFormat = FileFormatEnum.json.name();
        String filePath = Paths.get("").toAbsolutePath() + "/src/test/java/services/test";
        File file = new File(filePath + "." + fileFormat);
        try {
            Map<String, User> userMap = new HashMap<>();
            Map<String, User> emailMap = new HashMap<>();
            Map<String, Activity> activityMap = new HashMap<>();
            pacemaker.setDatastore(file);
            pacemaker.setSerializer(SerializerFactory.getSerializer(filePath, fileFormat));
            pacemaker.setUserIndex(userMap);
            pacemaker.setEmailIndex(emailMap);
            pacemaker.setActivitiesIndex(activityMap);
            pacemaker.store();
            assertTrue(file.exists());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
