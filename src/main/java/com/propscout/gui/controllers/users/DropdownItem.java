package com.propscout.gui.controllers.users;

import com.propscout.data.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

import java.io.IOException;

public class DropdownItem extends ListCell<User> {

    @FXML
    public Text nameText, usernameText, mobileText, emailText;

    public DropdownItem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/users/UserDropdownItem.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

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

            nameText.setText(user.getName());

            usernameText.setText(user.getUsername());

            mobileText.setText(user.getMobile());

            emailText.setText(user.getEmail());

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        }
    }
}
