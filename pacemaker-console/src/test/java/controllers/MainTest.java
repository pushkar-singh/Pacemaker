package controllers;

import models.Activity;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import services.PacemakerService;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MainTest {
    private Main main;
    @Mock
    PacemakerService pacemakerService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        main = new Main();
        main.pacemakerService = pacemakerService;
    }

    @Test
    public void testCreateUser() {
        String firstName = "yashi";
        String lastName = "gupta";
        String email = "yashi@gmail.com";
        String password = "password";
        User user = new User(firstName, lastName, email, password);
        when(pacemakerService.createUser(any(String.class), any(String.class), any(String.class), any(String.class))).thenReturn(user);
        main.createUser(firstName, lastName, email, password);
        verify(pacemakerService).createUser(firstName, lastName, email, password);
    }

    @Test
    public void testListUser() {
        String firstName = "yashi";
        String lastName = "gupta";
        String email = "yashi@gmail.com";
        String password = "password";
        User user = new User(firstName, lastName, email, password);
        when(pacemakerService.getUser(user.getId())).thenReturn(user);
        main.listUserbyid(user.getId());
        verify(pacemakerService).getUser(user.getId());
    }

    @Test
    public void listActivities() {
        String firstName = "yashi";
        String lastName = "gupta";
        String email = "yashi@gmail.com";
        String password = "password";
        User user = new User(firstName, lastName, email, password);
        pacemakerService.createActivity(user.getId(), "walk", "wit", 100, "2015-08-04T10:11:10", 200);
        List<Activity> activities = new ArrayList<>(pacemakerService.getActivitiesById(user.getId()));
        doAnswer((i) -> {
            return null;
        }).when(pacemakerService).drawBar();
        main.listActivities(user.getId());
    }
}
