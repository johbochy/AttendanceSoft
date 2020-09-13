package com.propscout.gui.controllers.students;

import com.propscout.data.adapters.CoursesAdapter;
import com.propscout.data.adapters.StudentsAdapter;
import com.propscout.data.models.Course;
import com.propscout.data.models.Student;
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

public class RegisterController implements Initializable {

    @FXML
    public Button addStudentButton;

    @FXML
    public TextField registrationNumberField, nameField, mobileField, yearField, semesterField;

    @FXML
    public ComboBox<String> coursesComboBox;

    private CoursesAdapter coursesAdapter;
    private StudentsAdapter studentsAdapter;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Initialize the courses adapter
        coursesAdapter = new CoursesAdapter();
        studentsAdapter = new StudentsAdapter();

        //Initialize added courses in the combo box

        initCoursesCombo();

        //Set action listener to the button in question
        addStudentButton.setOnAction(event -> {

            String regNo = registrationNumberField.getText();
            String name = nameField.getText();
            String mobile = mobileField.getText();
            int year, semester;

            try {

                year = Integer.parseInt(yearField.getText());
                semester = Integer.parseInt(semesterField.getText());

            } catch (NumberFormatException e) {
                e.printStackTrace();
                AlertHelper.showErrorAlert("Adding Student", "Check all numeric fields");
                return;
            }

            String courseName = coursesComboBox.getSelectionModel().getSelectedItem();
            Course course = coursesAdapter.getCourseByAlias(courseName);

            if (course == null) {
                AlertHelper.showErrorAlert("Registration", "Please select course");
                return;
            }

            Student student = new Student(0, regNo, name, mobile, course.getId(), year, semester, null, null);

            if (!valid(student)) return;

            if (studentsAdapter.add(student)) {

                clearStudentCache();
                AlertHelper.showSuccessAlert("Registration", "Student successfully registered");

            } else {

                AlertHelper.showErrorAlert("Registration", "A fatal error occurred, check logs");

            }
        });

    }

    /**
     * Validate the student data
     *
     * @param student the student to be validated
     * @return boolean f whether the student data is valid or not
     */
    private boolean valid(Student student) {

        if (student.getName().isEmpty() || student.getRegNo().isEmpty() || student.getMobile().isEmpty()) {
            AlertHelper.showErrorAlert("Registration", "All details are required");
            return false;
        }
        return true;
    }

    /**
     * Get databases courses and populate the courses combo box
     */
    private void initCoursesCombo() {

        ObservableList<String> coursesList = FXCollections.observableArrayList();

        try {
            ResultSet rs = coursesAdapter.getAll();

            while (rs.next()) {
                //String name = rs.getString("name");
                String alias = rs.getString("alias");

                //coursesList.add(String.format("%s(%s)", name, alias));
                coursesList.add(alias);
            }

            coursesComboBox.getItems().setAll(coursesList);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Clear the previous students entries after successfully persisting the student to the database
     */
    private void clearStudentCache() {
        registrationNumberField.setText("");
        nameField.setText("");
        mobileField.setText("");
        yearField.setText("");
        semesterField.setText("");
        coursesComboBox.getSelectionModel().clearSelection();
    }

}
