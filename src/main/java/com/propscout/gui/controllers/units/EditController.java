package com.propscout.gui.controllers.units;

import com.propscout.data.adapters.UnitsAdapter;
import com.propscout.data.adapters.UsersAdapter;
import com.propscout.data.models.Unit;
import com.propscout.data.models.User;
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

public class EditController implements Initializable {

    @FXML
    private TextField yearField;
    @FXML
    private TextField codeField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField semesterField;
    @FXML
    private ComboBox<String> lecturerComboBox;
    @FXML
    private Button updateUnitButton;

    private Unit unit;

    private UnitsAdapter unitsAdapter;
    private UsersAdapter usersAdapter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        unitsAdapter = new UnitsAdapter();
        usersAdapter = new UsersAdapter();

        populateLecturersComboBox();

        updateUnitButton.setOnAction(actionEvent -> {

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
                AlertHelper.showErrorAlert("Adding Course", "An up to 10 characters unit code is required");
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
            String username = lecturerComboBox.getSelectionModel().getSelectedItem();

            //Get the selected user from the db
            User user = usersAdapter.getByEmailOrUsername(username);

            if (user == null) {
                AlertHelper.showErrorAlert("Adding Course", "Lecturer is required");
                lecturerComboBox.requestFocus();
                return;
            }

            unit.setName(name);
            unit.setYear(year);
            unit.setSemester(semester);
            unit.setOfficerId(user.getId());

            if (unitsAdapter.update(unit)) {
                AlertHelper.showSuccessAlert("Updating Unit", "Unit updated successfully");
            } else {
                AlertHelper.showErrorAlert("Updating Unit", "Some fatal error occurred, check the log to assert");
            }
        });
    }

    public void setUnit(Unit unit) {
        this.unit = unit;

        populateFields();
    }

    private void populateLecturersComboBox() {

        ObservableList<String> officersList = FXCollections.observableArrayList();

        try {
            ResultSet rs = usersAdapter.getUserByRole("Lecturer");

            while (rs.next()) {
                String username = rs.getString("username");
                officersList.add(username);
            }

            lecturerComboBox.setItems(officersList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateFields() {

        codeField.setText(unit.getCode());
        codeField.setEditable(false);

        yearField.setText(String.valueOf(unit.getYear()));
        semesterField.setText(String.valueOf(unit.getSemester()));
        nameField.setText(unit.getName());
    }
}
