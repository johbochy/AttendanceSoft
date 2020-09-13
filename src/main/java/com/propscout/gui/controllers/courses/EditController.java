package com.propscout.gui.controllers.courses;

import com.propscout.data.adapters.CoursesAdapter;
import com.propscout.gui.helpers.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditController implements Initializable {


    @FXML
    private TextField aliasField;
    @FXML
    private TextField nameField;
    @FXML
    private Button updateCourseButton;

    private BrowseController.Course mCourse;
    private CoursesAdapter coursesAdapter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        coursesAdapter = new CoursesAdapter();

        //Set a click listener on the update button
        updateCourseButton.setOnAction(actionEvent -> {
            //Persist the course to the database

            //Get the alias
            String alias = aliasField.getText();
            String name = nameField.getText();

            //Validate the alias

            if (alias.isEmpty() || alias.trim().length() < 3) {
                aliasField.requestFocus();
                AlertHelper.showErrorAlert("Updating a Course", "At least a 3 char value needed");
            }

            if (name.isEmpty()) {
                aliasField.requestFocus();
                AlertHelper.showErrorAlert("Updating a Course", "Name of the course is required");
            }

            mCourse.setAlias(alias);

            mCourse.setName(name);

            if (coursesAdapter.updateCourse(mCourse)) {
                AlertHelper.showSuccessAlert("Updating a Course", "Course successfully updated");
            } else {
                AlertHelper.showErrorAlert("Updating a Course", "A fatal error occurred, check the console to debug");
            }
        });
    }

    public void setCourse(BrowseController.Course course) {
        this.mCourse = course;

        assert mCourse != null;
        updateUI();
    }

    private void updateUI() {
        nameField.setText(mCourse.getName());
        aliasField.setText(mCourse.getAlias());
    }
}
