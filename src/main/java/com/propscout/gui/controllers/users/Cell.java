package com.propscout.gui.controllers.users;

import com.propscout.data.adapters.UsersAdapter;
import com.propscout.data.models.User;
import com.propscout.gui.helpers.AlertHelper;
import com.propscout.gui.helpers.NavigationHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class Cell extends ListCell<User> {

    @FXML
    public Label nameLabel, usernameLabel, mobileLabel, emailLabel, roleLabel;

    @FXML
    private ImageView userAvatarImageView;

    @FXML
    public Button editUserButton, deleteUserButton;

    private UsersAdapter usersAdapter;

    public Cell() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/users/Cell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
            usersAdapter = new UsersAdapter();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(User user, boolean empty) {
        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {

            nameLabel.setText(user.getName());

            usernameLabel.setText(user.getUsername());

            mobileLabel.setText(user.getMobile());

            emailLabel.setText(user.getEmail());

            roleLabel.setText(user.getRole());

            if (user.getAvatar() != null) {

                userAvatarImageView.setImage(new Image(user.getAvatar()));

            }

            editUserButton.setOnAction(actionEvent -> {
                //Navigate to a section to edit the selected user
                NavigationHelper.swapToEditUser(getClass().getResource("/layouts/users/EditUserDetails.fxml"), user);
            });

            deleteUserButton.setOnAction(actionEvent -> {
                //Try deleting the user from the db
                if (usersAdapter.deleteUserById(user.getId())) {
                    AlertHelper.showSuccessAlert("Deleting User", "User deleted from the database.\nRefresh for updated list");
                } else {
                    AlertHelper.showErrorAlert("Deleting User", "Operation failed for some reason.\nCheck the logs for more");
                }
            });

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        }
    }
}
