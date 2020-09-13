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

public class EditUserDetailsController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField mobileField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Button updateUserButton;

    private User user;
    private UsersAdapter usersAdapter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usersAdapter = new UsersAdapter();

        updateUserButton.setOnAction(actionEvent -> {

            //Get the entered details
            String name = nameField.getText();
            String username = usernameField.getText();
            String mobile = mobileField.getText();
            String email = emailField.getText();
            String role = roleComboBox.getSelectionModel().getSelectedItem();

            //Validate the details
            if (!validInputData()) return;

            //Persist the user to db
            user.setName(name);
            user.setUsername(username);
            user.setMobile(mobile);
            user.setEmail(email);
            user.setRole(role);


            if (usersAdapter.updateUser(user)) {
                AlertHelper.showSuccessAlert("Updating User", "Operation was successfully");
            } else {
                AlertHelper.showErrorAlert("Updating User", "An error occurred, check the logs");
            }

        });
    }

    public void setUser(User user) {
        this.user = user;

        updateUI();
    }


    /**
     * @return boolean value to whether the updating  the user is complete is better
     */
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

    private void updateUI() {

        if (user != null) {
            nameField.setText(user.getName());
            usernameField.setText(user.getUsername());
            mobileField.setText(user.getMobile());
            emailField.setText(user.getEmail());
            roleComboBox.getSelectionModel().select(user.getRole());
        }

    }
}
