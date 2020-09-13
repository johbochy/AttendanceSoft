package com.propscout.gui.controllers.units;

import com.propscout.data.adapters.UnitsAdapter;
import com.propscout.data.adapters.UsersAdapter;
import com.propscout.data.models.Unit;
import com.propscout.data.models.User;
import com.propscout.gui.controllers.courses.BrowseController;
import com.propscout.gui.helpers.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Handles addition of new course units to the database
 */
public class AddController implements Initializable {

    @FXML
    public TextField courseNameField, codeField, nameField, yearField, semesterField;

    @FXML
    public Button addUnitButton;

    @FXML
    public ComboBox<String> officerComboBox;

    private BrowseController.Course course;

    private UnitsAdapter unitsAdapter;
    private UsersAdapter usersAdapter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        unitsAdapter = new UnitsAdapter();

        usersAdapter = new UsersAdapter();

        populateOfficersComboBox();

        addUnitButton.setOnAction(event -> {
            //Get all the users values
            int courseId = course.getId();
            String name = nameField.getText();
            String code = codeField.getText();
            int year = 0, semester = 0;

            try {
                year = Integer.parseInt(yearField.getText().trim());
                semester = Integer.parseInt(semesterField.getText().trim());
            } catch (NumberFormatException e) {
                AlertHelper.showErrorAlert("Adding Unit", "Fill in valid values please!!!");
            }


            //Validate the values

            if (code.isEmpty() || code.trim().length() > 10) {
                AlertHelper.showErrorAlert("Adding Course", "An upto 10 characters unit code is required");
                codeField.requestFocus();
                return;
            }

            if (name.isEmpty()) {

                AlertHelper.showErrorAlert("Adding Course", "Name field is required");
                nameField.requestFocus();
                return;
            }

            if (year < 1 || year > 4) {

                AlertHelper.showErrorAlert("Adding Course", "0 < year < 4");
                yearField.requestFocus();
                return;
            }

            if (semester < 1 || semester > 2) {

                AlertHelper.showErrorAlert("Adding Course", "0 < semester < 2");
                semesterField.requestFocus();
                return;
            }

            //Get the selected username
            String username = officerComboBox.getSelectionModel().getSelectedItem();

            //Get the selected user from the db
            User user = usersAdapter.getByEmailOrUsername(username);

            if (user == null) {
                AlertHelper.showErrorAlert("Adding Course", "Officer is required");
                officerComboBox.requestFocus();
                return;
            }

            //Persist the unit to the database
            Unit unit = new Unit(0, courseId, user.getId(), name, code, year, semester);

            if (unitsAdapter.add(unit)) {
                clearUnitCache();
                AlertHelper.showSuccessAlert("Adding Unit", "Unit added successfully");
            } else {
                AlertHelper.showErrorAlert("Adding Unit", "Some fatal error occurred, check the log to assert");
            }
        });

        courseNameField.setEditable(false);
    }

    private void populateOfficersComboBox() {

        ObservableList<String> officersList = FXCollections.observableArrayList();

        try {
            ResultSet rs = usersAdapter.getUserByRole("Lecturer");

            while (rs.next()) {
                String username = rs.getString("username");
                officersList.add(username);
            }

            officerComboBox.setItems(officersList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCourse(BrowseController.Course course) {
        this.course = course;

        courseNameField.setText(course.getName());
    }

    /**
     * Clear previous entries of a unit after successful database persistence
     */
    private void clearUnitCache() {
        nameField.clear();
        //courseNameField.clear();
        codeField.clear();
        yearField.clear();
        semesterField.clear();

        officerComboBox.getSelectionModel().clearSelection();
    }
}
