package com.propscout.gui.controllers;

import com.propscout.data.adapters.UsersAdapter;
import com.propscout.data.models.User;
import com.propscout.gui.helpers.AlertHelper;
import com.propscout.gui.helpers.NavigationHelper;
import com.propscout.utils.AuthHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the login UI
 */
public class LoginController implements Initializable {

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button loginButton;
    @FXML
    public Button cancelButton;

    private UsersAdapter usersAdapter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Check whether there is a logged in user and redirect
        if (AuthHelper.isLoggedIn()) {

            System.out.println("You are already logged in");

            User user = AuthHelper.getLoggedInUser();

            if (user != null) {

                usernameField.setText(user.getUsername());

                passwordField.setText(user.getPassword());

            } else {
                System.out.println("Something fishy is going on, may ClassCastException, just check the logs");
            }
        }


        //Initialize user adapter
        usersAdapter = new UsersAdapter();

        //Handle cancel button action
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            //Exit the program
            System.exit(0);
        });

        //Handle login button action
        loginButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            //Get and Validate User Credentials

            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                AlertHelper.showErrorAlert("Authentication", "All fields are required");
            } else {

                User user = usersAdapter.getByEmailOrUsername(username);

                if (user == null) {
                    AlertHelper.showErrorAlert("Authentication", "No user matches the given credentials");
                    return;
                }

                if (password.equals(user.getPassword())) {
                    //Login the user
                    AuthHelper.login(user);

                    redirectAuthenticatedUser(user);

                } else {
                    AlertHelper.showErrorAlert("Authentication", "Incorrect password!!! Try again.");
                }
            }
        });

    }

    private void redirectAuthenticatedUser(User user) {
        //Close the stage
        ((Stage) loginButton.getScene().getWindow()).close();

        //Check the role of the user and redirect accordingly
        if (user.getRole().equals("Admin")) {
            //Open new stage for the main window base on the group level of the user
            try {
                NavigationHelper.startWindow(getClass().getResource("/layouts/Main.fxml"), "Admin Dashboard");
            } catch (IOException e) {
                AlertHelper.showErrorAlert("Launching Window", "Design File Not Found");
                e.printStackTrace();
            }

        } else if (user.getRole().equals("Lecturer")) {
            //Open the Officers Dashboard
            try {
                NavigationHelper.startWindow(getClass().getResource("/layouts/officers/Main.fxml"), "Officers Dashboard");
            } catch (IOException e) {
                AlertHelper.showErrorAlert("Launching Window", "Design File Not Found");
                e.printStackTrace();
            }
        }
    }
}
