package com.propscout.gui.controllers;

import com.propscout.data.models.User;
import com.propscout.gui.helpers.AlertHelper;
import com.propscout.gui.helpers.NavigationHelper;
import com.propscout.utils.AuthHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the main scene UI
 */
public class MainController implements Initializable {

    @FXML
    public Button settingButton;

    @FXML
    private Button brandButton, dashboardButton, studentsButton, coursesButton, timetableButton, usersButton;
    @FXML
    private StackPane contentPane;

    @FXML
    private Text titleText;

    @FXML
    private Button signOutButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Initialize the navigator helper
        NavigationHelper.initNavigator(contentPane, titleText);

        //Initial content pane content
        initContent();

        dashboardButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/Dashboard.fxml"));
            NavigationHelper.changeTitle("Dashboard");
        });

        studentsButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/students/Browse.fxml"));
            NavigationHelper.changeTitle("Students");
        });

        coursesButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/courses/Browse.fxml"));
            NavigationHelper.changeTitle("Courses");
        });

        usersButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/users/Browse.fxml"));
            NavigationHelper.changeTitle("Application Users");
        });

        timetableButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/schedule/Timetable.fxml"));
            NavigationHelper.changeTitle("Time Table");
        });

        settingButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/settings/Settings.fxml"));
            NavigationHelper.changeTitle("Settings");
        });

        signOutButton.setOnAction(actionEvent -> {

            try {

                boolean result = AlertHelper.showConfirmationAlert("Authentication", "Are you sure you want to logout");

                if (result) {
                    //Delete the authentication serialized files
                    AuthHelper.logout();

                    //Close Current Window
                    ((Stage) contentPane.getScene().getWindow()).close();

                    //Opening the login window
                    NavigationHelper.startWindow(getClass().getResource("/layouts/auth/Login.fxml"), "Login Attendance Software Jfx");
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        });
    }

    private void initContent() {
        NavigationHelper.swapContent(getClass().getResource("/layouts/Dashboard.fxml"));
        NavigationHelper.changeTitle("Dashboard");
    }

}
