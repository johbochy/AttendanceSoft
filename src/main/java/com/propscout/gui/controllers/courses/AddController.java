package com.propscout.gui.controllers.courses;

import com.propscout.data.adapters.CoursesAdapter;
import com.propscout.data.models.Course;
import com.propscout.gui.helpers.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Adding a new course to the database brains, and ui handler
 */
public class AddController implements Initializable {

    @FXML
    public TextField aliasField, nameField;

    @FXML
    public Button addCourseButton;

    private CoursesAdapter adapter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        adapter = new CoursesAdapter();

        addCourseButton.setOnAction(event -> {

            //Get user input
            String alias = aliasField.getText();
            String name = nameField.getText();

            //Validate User Input

            if (alias.isEmpty() || name.isEmpty()) {
                AlertHelper.showErrorAlert("Adding a new Course", "All fields are required");
            } else {
                Course course = new Course();
                course.setAlias(alias);
                course.setName(name);

                if (adapter.add(course)) {

                    clearCourseCache();

                    AlertHelper.showSuccessAlert("Adding a new Course", "A course has been added successfully");

                } else {

                    AlertHelper.showErrorAlert("Adding a new Course", "All fatal error occurred");
                }

            }

        });
    }

    /**
     * Clearing the previous entries of the persisted course
     */
    private void clearCourseCache() {
        aliasField.clear();
        nameField.clear();
    }
}
