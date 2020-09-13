package com.propscout.gui.controllers.account;

import com.propscout.data.adapters.UsersAdapter;
import com.propscout.data.models.User;
import com.propscout.gui.helpers.AlertHelper;
import com.propscout.utils.AuthHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.UUID;

public class UpdateAccountController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField mobileField;
    @FXML
    private TextField emailField;
    @FXML
    private ImageView avatarImageView;
    @FXML
    private Button chooseImageButton;
    @FXML
    private Button updateAccountButton;
    @FXML
    private PasswordField passwordField;

    private UsersAdapter usersAdapter;

    private File mSelectedFile = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        usersAdapter = new UsersAdapter();

        //Set action listener on the choose image button
        chooseImageButton.setOnAction(event -> {
            FileChooser chooserAvatarFileChooser = new FileChooser();
            chooserAvatarFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("image/*", "jpg", "png", "jpeg", "bmp", "gif");

            chooserAvatarFileChooser.setSelectedExtensionFilter(extensionFilter);
            chooserAvatarFileChooser.setTitle("Choose User Avatar");

            File selectedFile = chooserAvatarFileChooser.showOpenDialog(chooseImageButton.getScene().getWindow());

            if (selectedFile != null) {
                mSelectedFile = selectedFile;
                Image image = new Image(selectedFile.toURI().toString(), 128.0, 128.0, true, true, true);
                avatarImageView.setImage(image);
            }
        });

        //Check whether the user is logged in

        if (AuthHelper.isLoggedIn()) updateUI(AuthHelper.getLoggedInUser());

        updateAccountButton.setOnAction(event -> {
            //Get the entered details
            String name = nameField.getText();
            String username = usernameField.getText();
            String mobile = mobileField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String avatar = null;

            //Check and create a folder to upload images if does not exists
            if (mSelectedFile != null) {
                avatar = saveImageToFile();
            }

            //Validate the details
            if (!validInputData()) return;

            //Update the current user details
            User user = AuthHelper.getLoggedInUser();

            user.setName(name.trim());
            user.setUsername(username.trim());
            user.setEmail(email.trim());
            user.setMobile(mobile.trim());
            user.setPassword(password.trim());

            if (mSelectedFile != null) user.setAvatar(avatar);

            if (usersAdapter.updateUser(user)) {

                AlertHelper.showSuccessAlert("Adding User", "Operation was successfully,  re-authenticate for your details to update");

            } else {
                AlertHelper.showErrorAlert("Adding User", "An error occurred, check the logs");
            }
        });
    }

    /**
     * Update the UI based on the current logged in user
     *
     * @param user the logged in user
     */
    private void updateUI(User user) {

        nameField.setText(user.getName());

        usernameField.setText(user.getUsername());

        mobileField.setText(user.getMobile());

        emailField.setText(user.getEmail());

    }


    private String saveImageToFile() {

        File uploadsDirectory = getUploadsDirectory();

        try {
            File createdFile = Files.createFile(Paths.get(uploadsDirectory.getAbsolutePath() + "/" + UUID.randomUUID() + ".jpg")).toFile();
            System.out.println("Saving the image to " + createdFile.getAbsolutePath() + "...");
            ImageIO.write(ImageIO.read(mSelectedFile), "jpg", createdFile);

            return createdFile.toURI().toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private File getUploadsDirectory() {
        Path rootPath = Paths.get(this.getClass().getResource("/").getPath());

        Path uploadsPath = Paths.get(rootPath.toAbsolutePath() + "/uploads");

        if (!uploadsPath.toFile().exists()) {

            System.out.println("Creating an uploads folder in resources...");

            try {
                Files.createDirectories(uploadsPath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return uploadsPath.toFile();
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

        //Check Password
        if (passwordField.getText().trim().isEmpty()) {
            AlertHelper.showErrorAlert("Adding User", "Password is required");
            passwordField.requestFocus();
            return false;
        }

        return true;
    }

}
