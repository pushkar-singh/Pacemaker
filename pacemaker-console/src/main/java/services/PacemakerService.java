package services;

import factories.SerializerFactory;
import models.Activity;
import models.FileFormatEnum;
import models.Location;
import models.User;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

import com.google.common.base.Optional;
import serializers.Serializer;

public class PacemakerService {
    private Map<String, User> userIndex = new HashMap<>();
    private Map<String, User> emailIndex = new HashMap<>();
    private Map<String, Activity> activitiesIndex = new HashMap<>();
    public String fileFormat;
    public File datastore;

    public Serializer serializer;

    public int totalTime;
    public int totalDistance;
    public int largestActivityNameLength;
    public Map<String, Integer> activitiesTime = new HashMap<>();
    public Map<String, Integer> activitiesDistance = new HashMap<>();

    public String ANSI_CYAN = "\u001B[36m";
    public String ANSI_BLUE = "\033[1;34m";
    public String ANSI_GREEN = "\033[1;92m";

    public PacemakerService() {
    }

    public Map<String, User> getUserIndex() {
        return userIndex;
    }

    public Map<String, User> getEmailIndex() {
        return emailIndex;
    }

    public Map<String, Activity> getActivitiesIndex() {
        return activitiesIndex;
    }

    public void setUserIndex(Map<String, User> userIndex) {
        this.userIndex = userIndex;
    }

    public void setEmailIndex(Map<String, User> emailIndex) {
        this.emailIndex = emailIndex;
    }

    public void setActivitiesIndex(Map<String, Activity> activitiesIndex) {
        this.activitiesIndex = activitiesIndex;
    }

    public void setDatastore(File datastore) {
        this.datastore = datastore;
    }

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    public String getCurrentFileFormat() {
        return fileFormat;
    }

    public void init(String filePath) {
        fileFormat = getFileFormat(filePath);
        serializer = SerializerFactory.getSerializer(filePath, fileFormat);
        datastore = new File(filePath + "." + fileFormat);
        if (datastore.isFile()) {
            try {
                this.load();
            } catch (Exception e) {
                System.out.println("Exception occurred while loading data during startup: " + e.getMessage());
            }
        }
    }

    public void changeFileFormat(String format) {
        /*if (datastore.isFile()) {
            try {
                store();
                this.load();
            } catch (Exception e) {
                System.out.println("Exception occurred while loading data during cff: " + e.getMessage());
            }
        }*/
        fileFormat = format;
        datastore = new File("datastore." + fileFormat);
        serializer = SerializerFactory.getSerializer("datastore", fileFormat);
    }

    /**
     * The load function is called when datasource file is already exist
     * It reads the data from that file and fill into maps like userIndex
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void load() throws Exception {
        serializer.read();

        activitiesIndex = (Map<String, Activity>) serializer.pop();
        emailIndex = (Map<String, User>) serializer.pop();
        userIndex = (Map<String, User>) serializer.pop();
    }

    public void store() throws Exception {
        serializer.push(userIndex);
        serializer.push(emailIndex);
        serializer.push(activitiesIndex);
        serializer.write();
    }

    public void deleteExtraFiles() {
        deleteExtraFiles("datastore", fileFormat);
    }

    void deleteExtraFiles(String filepath, String requiredFileFormat) {
        String[] fileFormats = {"json", "xml", "yaml"};
        for (int i = 0; i < fileFormats.length; i++) {
            if (!fileFormats[i].equals(requiredFileFormat)) {
                File file = new File(filepath + "." + fileFormats[i]);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    static String getFileFormat(String filePath) {
        FileFormatEnum[] formats = FileFormatEnum.values();
        String fileFormat = "";

        for (FileFormatEnum format : formats) {
            File file = new File(filePath + "." + format.name());
            if (file.exists()) {
                fileFormat = format.name();
                break;
            }
        }
        if (fileFormat.isEmpty()) {
            fileFormat = FileFormatEnum.yaml.name();
        }
        return fileFormat;
    }

    public void drawBar() {

        totalTime = 0;
        totalDistance = 0;
        largestActivityNameLength = 0;
        activitiesTime = new HashMap<>();
        activitiesDistance = new HashMap<>();
        Map<String, Activity> allActivities = getActivitiesIndex();
        for (String key : allActivities.keySet()) {
            String tempType = allActivities.get(key).getType();
            largestActivityNameLength = Math.max(tempType.length(), largestActivityNameLength);
            int tempDuration = allActivities.get(key).getDuration();
            int tempDistance = allActivities.get(key).getDistance();
            totalTime = totalTime + tempDuration;
            totalDistance = totalDistance + tempDistance;
            if (!activitiesTime.containsKey(tempType)) {
                activitiesTime.put(tempType, tempDuration);
                activitiesDistance.put(tempType, tempDistance);
            } else {
                int tempTimeVal = activitiesTime.get(tempType);
                int tempDurationVal = activitiesDistance.get(tempType);
                activitiesTime.put(tempType, tempDuration + tempTimeVal);
                activitiesDistance.put(tempType, tempDistance + tempDurationVal);
            }
        }
        /**
         *  activity distance chart
         */
        System.out.println("\n" + ANSI_GREEN + "All Users’ activities (distance travelled)" + "\u001B[0m");
        drawChartOnConsole(activitiesDistance, totalDistance, largestActivityNameLength, ANSI_CYAN);

        /**
         *  activity time chart
         */
        System.out.println("\n\n" + ANSI_GREEN + "All Users’ activities (time spent)" + "\u001B[0m");
        drawChartOnConsole(activitiesTime, totalTime, largestActivityNameLength, ANSI_BLUE);
        System.out.println("\n\n");
    }

    public void drawChartOnConsole(Map<String, Integer> activities, int total, int maxNameLength, String color) {
        for (String key : activities.keySet()) {
            int barLength = (activities.get(key) * 100) / total;
            StringBuilder activityName = new StringBuilder(key.toUpperCase() + ": ");
            if (activityName.length() < maxNameLength + 2) {
                for (int i = activityName.length(); i < maxNameLength + 2; i++) {

                    activityName.append(" ");
                }
            }
            System.out.print(activityName);
            for (int i = 0; i < barLength; i++) {
                System.out.print(color + "█");
            }
            System.out.print((int) Math.round(activities.get(key)) + "\u001B[0m" + "\n");
        }
    }

    public Collection<User> getUsers() {
        return userIndex.values();
    }

    public Collection<Activity> getActivitiesById(String id) {
        Optional<User> user = Optional.fromNullable(userIndex.get(id));
        return user.isPresent() ? user.get().getActivities().values() : new ArrayList<>();
    }

    public List<Activity> sortActivities(String userId, String sortBy) {
        List<Activity> activities = new ArrayList<>(getActivitiesById(userId));
        if (!activities.isEmpty()) {
            switch (sortBy) {
                case "type":
                    activities.sort((a, b) -> a.getType().compareToIgnoreCase(b.getType()));
                    break;
                case "location":
                    activities.sort((a, b) -> a.getLocation().compareToIgnoreCase(b.getLocation()));
                    break;
                case "distance":
                    activities.sort(Comparator.comparing(Activity::getDistance));
                    break;
                case "starttime":
                    activities.sort(Comparator.comparing(a -> LocalDateTime.parse(a.getStarttime())));
                    break;
                case "duration":
                    activities.sort(Comparator.comparing(Activity::getDuration));
                    break;
            }
        }
        return activities;
    }


    public User createUser(String firstName, String lastName, String email, String password) {
        User user = new User(firstName, lastName, email, password);
        userIndex.put(user.getId(), user);
        emailIndex.put(email, user);
        return user;
    }

    public User getUserByEmail(String email) {
        return emailIndex.get(email);
    }

    public User getUser(String id) {
        return userIndex.get(id);
    }

    public void deleteUser(String id) {
        userIndex.remove(id);
    }


    public Activity createActivity(String id, String type, String location, int distance, String startTime,
                                   int duration) {
        Activity activity = new Activity(type, location, distance, startTime, duration);
        Optional<User> user = Optional.fromNullable(userIndex.get(id));
        if (user.isPresent()) {
            user.get().getActivities().put(activity.getId(), activity);
            activitiesIndex.put(activity.getId(), activity);
        }
        return activity;
    }


    public Activity getActivity(String id) {
        return activitiesIndex.get(id);
    }


    public void addLocation(String id, float latitude, float longitude) {
        Optional<Activity> activity = Optional.fromNullable(activitiesIndex.get(id));
        if (activity.isPresent()) {
            activity.get().getroute().add(new Location(latitude, longitude));
        }
    }
}

