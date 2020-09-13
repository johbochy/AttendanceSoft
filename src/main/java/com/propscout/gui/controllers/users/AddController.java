package com.propscout.gui.controllers.users;

import com.propscout.data.adapters.UsersAdapter;
import com.propscout.data.models.User;
import com.propscout.gui.helpers.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Handles the login behind adding a new user of the application or system
 */
public class AddController implements Initializable {

    @FXML
    private TextField nameField, usernameField, mobileField, emailField;

    @FXML
    private Button addUserButton;
    @FXML
    public ComboBox<String> roleComboBox;

    private UsersAdapter usersAdapter;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Initialize the usersAdapter field
        usersAdapter = new UsersAdapter();

        addUserButton.setOnAction(event -> {
            //Get the entered details
            String name = nameField.getText();
            String username = usernameField.getText();
            String mobile = mobileField.getText();
            String email = emailField.getText();
            String role = roleComboBox.getSelectionModel().getSelectedItem();

            //Validate the details
            if (!validInputData()) return;

            //Persist the user to db
            // Have you seen where the password is hard coded
            User user = new User(0, name.trim(), username.trim(), email.trim(), mobile.trim(), role.trim(), null, null, "password", 0, null, null);


            if (usersAdapter.add(user)) {

                clearUserCache();

                AlertHelper.showSuccessAlert("Adding User", "Operation was successfully");

            } else {
                AlertHelper.showErrorAlert("Adding User", "An error occurred, check the logs");
            }
        });

    }

    private boolean validInputData() {

        //Check name
        if (nameField.getText().trim().isEmpty()) {
            AlertHelper.showErrorAlert("Adding User", "Name is required");
            nameField.requestFocus();
            return false;
        }

        //Check Username
        if (usernameField.getText().trim().isEmpty()) {
            AlertHelper.showErrorAlert("Adding User", "Username is required");
            usernameField.requestFocus();
            return false;
        }

        //Check Mobile
        if (mobileField.getText().trim().isEmpty()) {
            AlertHelper.showErrorAlert("Adding User", "Mobile is required");
            mobileField.requestFocus();
            return false;
        }

        //Check Email
        if (emailField.getText().trim().isEmpty()) {
            AlertHelper.showErrorAlert("Adding User", "Email address is required");
            emailField.requestFocus();
            return false;
        }

        //Check role
        if (roleComboBox.getSelectionModel().getSelectedItem().trim().isEmpty()) {
            AlertHelper.showErrorAlert("Adding User", "User Role is required");
            roleComboBox.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Clear the previous entries after a user has been successfully persisted to the database
     */
    private void clearUserCache() {
        nameField.clear();
        emailField.clear();
        mobileField.clear();
        usernameField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }
}
