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

public class EditStudentDetailsController implements Initializable {

    @FXML
    private TextField registrationNumberField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField mobileField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField semesterField;
    @FXML
    private ComboBox<String> coursesComboBox;
    @FXML
    private Button editStudentButton;

    private BrowseController.Student student;

    private CoursesAdapter coursesAdapter;
    private StudentsAdapter studentsAdapter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        coursesAdapter = new CoursesAdapter();
        studentsAdapter = new StudentsAdapter();

        initCoursesCombo();

        editStudentButton.setOnAction(actionEvent -> {
            //Update the student details
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

            Student uStudent = new Student(student.getId(), student.getRegNo(), name, mobile, course.getId(), year, semester, null, null);

            if (!valid(uStudent)) return;

            if (studentsAdapter.save(uStudent)) {
                AlertHelper.showSuccessAlert("Updating Student", "Student details were successfully updated");
            } else {

                AlertHelper.showErrorAlert("Updating Student", "A fatal error occurred, check logs");

            }
        });

    }

    public void setStudent(BrowseController.Student student) {
        this.student = student;

        populateFields();
    }

    private void populateFields() {

        registrationNumberField.setText(student.getRegNo());
        registrationNumberField.setEditable(false);
        nameField.setText(student.getName());
        mobileField.setText(student.getMobile());
        yearField.setText(String.valueOf(student.getYear()));
        semesterField.setText(String.valueOf(student.getSemester()));
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
     * Validate the student data
     *
     * @param student the student to be validated
     * @return boolean f whether the student data is valid or not
     */
    private boolean valid(Student student) {

        if (student.getName().isEmpty() || student.getRegNo().isEmpty() || student.getMobile().isEmpty()) {
            AlertHelper.showErrorAlert("Update Student", "All valid student details are required");
            return false;
        }
        return true;
    }
}
