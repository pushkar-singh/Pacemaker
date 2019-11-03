package controllers;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import com.bethecoder.ascii_table.ASCIITable;
import com.bethecoder.ascii_table.impl.CollectionASCIITableAware;
import com.bethecoder.ascii_table.spec.IASCIITableAware;
import com.google.common.base.Optional;
import models.Activity;
import models.User;
import services.PacemakerService;

import java.util.*;


public class Main {
    public PacemakerService pacemakerService;
    private static final String RESET = "\u001B[0m";
    private static final String ANSI_PURPLE_BOLD_BRIGHT = "\033[1;35m";

    public void init() {
        pacemakerService = new PacemakerService();
        pacemakerService.init("datastore");
    }

    @Command(description = "Create a new user")
    public void createUser(@Param(name = "First Name") String firstName, @Param(name = "Last Name") String lastName,
                           @Param(name = "Email") String email, @Param(name = "Password") String password) {
        pacemakerService.createUser(firstName, lastName, email, password);
    }

    @Command(description = "List all users' details")
    public void listUsers() {
        System.out.println(ANSI_PURPLE_BOLD_BRIGHT);
        List<User> users = new ArrayList<>(pacemakerService.getUsers());
        if (!users.isEmpty()) {
            IASCIITableAware iasciiTableAware = new CollectionASCIITableAware<User>(users, "id", "firstname", "lastname", "email", "password");
            ASCIITable.getInstance().printTable(iasciiTableAware);
        } else {
            System.out.println("No user records found");
        }
        System.out.println(RESET);
    }
    @Command(description = "List a user's detail by email")
    public void listUser(@Param(name = "email") String email) {
        System.out.println(ANSI_PURPLE_BOLD_BRIGHT);
        User u = pacemakerService.getUserByEmail(email);
        List<User> user = new ArrayList<>();
        if (u != null) {
            user = new ArrayList<>(List.of(u));
        }
        if (!user.isEmpty()) {
            IASCIITableAware iasciiTableAware = new CollectionASCIITableAware<User>(user, "id", "firstname", "lastname", "email", "password");
            ASCIITable.getInstance().printTable(iasciiTableAware);
        } else {
            System.out.println("No user records found");
        }
        System.out.println(RESET);
    }

    @Command(description = "List a user's detail by id")
    public void listUserbyid(@Param(name = "user-id") String id) {
        System.out.println(ANSI_PURPLE_BOLD_BRIGHT);
        User u = pacemakerService.getUser(id);
        List<User> user = new ArrayList<>();
        if (u != null) {
            user = new ArrayList<>(List.of(u));
        }
        if (!user.isEmpty()) {
            IASCIITableAware iasciiTableAware = new CollectionASCIITableAware<User>(user, "id", "firstname", "lastname", "email", "password");
            ASCIITable.getInstance().printTable(iasciiTableAware);
        } else {
            System.out.println("No user records found");
        }
        System.out.println(RESET);
    }


    @Command(description = "Delete a User")
    public void deleteUser(@Param(name = "user-id") String id) {
        Optional<User> user = Optional.fromNullable(pacemakerService.getUser(id));
        if (user.isPresent()) {
            pacemakerService.deleteUser(id);
        }
    }

    @Command(description = "Add an activity")
    public void addActivity(@Param(name = "user-id") String id, @Param(name = "type") String type,
                            @Param(name = "location") String location, @Param(name = "distance") int distance,
                            @Param(name = "start-time") String startTime, @Param(name = "duration") int duration) {
        Optional<User> user = Optional.fromNullable(pacemakerService.getUser(id));
        if (user.isPresent()) {
            pacemakerService.createActivity(id, type, location, distance, startTime, duration);
        }
    }

    @Command(description = "Add Location to an activity")
    public void addLocation(@Param(name = "activity-id") String id, @Param(name = "latitude") float latitude,
                            @Param(name = "longitude") float longitude) {
        Optional<Activity> activity = Optional.fromNullable(pacemakerService.getActivity(id));
        if (activity.isPresent()) {
            pacemakerService.addLocation(activity.get().getId(), latitude, longitude);
        }
    }

    @Command(description = "List Activities by user ID")
    public void listActivities(@Param(name = "user-id") String id) {
        System.out.println(ANSI_PURPLE_BOLD_BRIGHT);
        List<Activity> activities = new ArrayList<>(pacemakerService.getActivitiesById(id));
        if (!activities.isEmpty()) {
            IASCIITableAware iasciiTableAware = new CollectionASCIITableAware<Activity>(activities, "id", "type", "location", "distance", "starttime", "duration", "route");
            ASCIITable.getInstance().printTable(iasciiTableAware);
            pacemakerService.drawBar();
        } else {
            System.out.println("No activities found");
        }

        System.out.println(RESET);

    }

    @Command(description = "List Activities by user ID")
    public void listActivities(@Param(name = "user-id") String id, @Param(name = "sortBy") String sortBy) {
        System.out.println(ANSI_PURPLE_BOLD_BRIGHT);
        List<Activity> activities = pacemakerService.sortActivities(id, sortBy);
        if (!activities.isEmpty()) {
            IASCIITableAware iasciiTableAware = new CollectionASCIITableAware<>(activities, "id", "type", "location", "distance", "starttime", "duration", "route");
            ASCIITable.getInstance().printTable(iasciiTableAware);
            pacemakerService.drawBar();
        } else {
            System.out.println("No activities found");
        }
        System.out.println(RESET);
    }

    @Command(description = "Change file format")
    public void changeFileFormat(@Param(name = "format") String format) throws Exception {
        pacemakerService.changeFileFormat(format);
    }

    private void store() throws Exception {
        pacemakerService.store();
    }

    private void deleteExtraFiles() {
        pacemakerService.deleteExtraFiles();
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.init();
        Shell shell = ShellFactory.createConsoleShell("pm", "Welcome to pacemaker-console - ?help for instructions", main);
        shell.commandLoop();
        main.store();
        main.deleteExtraFiles();

    }
}
