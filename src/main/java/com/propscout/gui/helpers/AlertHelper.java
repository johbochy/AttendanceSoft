package com.propscout.gui.helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertHelper {

    /**
     * Show an error alert to user as feedback
     *
     * @param title   the title for the alert
     * @param content a little explanation of what has occurred
     */
    public static void showErrorAlert(String title, String content) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Show a success message to the user
     *
     * @param title   heading for the alert
     * @param content message by the alert
     */
    public static void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();

    }

    /**
     * Gives a user an option to cancel a critical operation or go ahead with it
     *
     * @param title   heading for the alert
     * @param content message by the alert
     */
    public static boolean showConfirmationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Optional<ButtonType> buttonType = alert.showAndWait();

        assert buttonType.isPresent();

        return buttonType.get() == ButtonType.OK;
    }
}
