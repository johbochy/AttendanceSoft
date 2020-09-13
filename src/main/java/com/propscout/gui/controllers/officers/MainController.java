package com.propscout.gui.controllers.officers;

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

public class MainController implements Initializable {

    @FXML
    private Button brandButton, dashboardButton, viewAttendanceButton, viewUnitsButton;

    @FXML
    private Button takeAttendanceButton;

    @FXML
    private Button updateAccountButton;

    @FXML
    private Text titleText;

    @FXML
    private StackPane contentPane;

    @FXML
    private Button signOutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        NavigationHelper.initNavigator(contentPane, titleText);

        //Initial content pane content
        initContent();

        dashboardButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/officers/Dashboard.fxml"));
            NavigationHelper.changeTitle("Dashboard");
        });

        viewUnitsButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/officers/units/Browse.fxml"));
            NavigationHelper.changeTitle("Your Units");
        });

        viewAttendanceButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/officers/attendance/ViewLectures.fxml"));
            NavigationHelper.changeTitle("View Attendance");

        });

        takeAttendanceButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/officers/attendance/Take.fxml"));
            NavigationHelper.changeTitle("Take Attendance");

        });

        updateAccountButton.setOnAction(actionEvent -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/account/UpdateAccount.fxml"));
            NavigationHelper.changeTitle("Update Account");
        });

        signOutButton.setOnAction(actionEvent -> {

            try {

                boolean result = AlertHelper.showConfirmationAlert("Authentication", "Are sure you want to logout");

                if (result) {
                    //Logout
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
        NavigationHelper.swapContent(getClass().getResource("/layouts/officers/Dashboard.fxml"));
        NavigationHelper.changeTitle("Dashboard");
    }
}
