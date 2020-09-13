package com.propscout.gui.controllers.schedule;

import com.propscout.data.adapters.CoursesAdapter;
import com.propscout.data.adapters.ScheduleAdapter;
import com.propscout.data.adapters.UnitsAdapter;
import com.propscout.data.models.Course;
import com.propscout.data.models.Unit;
import com.propscout.data.models.UnitSchedule;
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

public class AddController implements Initializable {

    @FXML
    public ComboBox<String> weekDaysComboBox;
    @FXML
    public ComboBox<String> courseComboBox;
    @FXML
    public ComboBox<String> unitsComboBox;
    @FXML
    public TextField startTimeField;
    @FXML
    public TextField durationField;
    @FXML
    public Button scheduleUnitButton;

    private CoursesAdapter coursesAdapter;

    private UnitsAdapter unitsAdapter;

    private ScheduleAdapter scheduleAdapter;

    /**
     * Use it to filter units if not null
     *
     * @var mSelectedCourse
     */
    private Course mSelectedCourse;

    private Unit mSelectedUnit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Initialize the adapters
        coursesAdapter = new CoursesAdapter();
        unitsAdapter = new UnitsAdapter();
        scheduleAdapter = new ScheduleAdapter();

        //Populate courses
        populateCourses();

        //Course selected action listener
        courseComboBox.setOnAction(event -> {
            String alias = courseComboBox.getSelectionModel().getSelectedItem().trim();
            mSelectedCourse = coursesAdapter.getCourseByAlias(alias);

            //Populate Units
            populateUnits();

        });

        //Unit selected action listener
        unitsComboBox.setOnAction(event -> {

            String code = unitsComboBox.getSelectionModel().getSelectedItem().trim();

            mSelectedUnit = unitsAdapter.getUnitByCode(code);

            System.out.println(mSelectedUnit);

        });

        scheduleUnitButton.setOnAction(event -> {
            //Validate necessary user data
            if (mSelectedUnit == null) {
                AlertHelper.showErrorAlert("Adding Lecture", "Please select a unit");
                return;
            }

            //Validate the weekday
            String weedDay = weekDaysComboBox.getSelectionModel().getSelectedItem().trim();
            if (weedDay.isEmpty()) {
                AlertHelper.showErrorAlert("Adding Lecture", "Please select the weekday");
                return;
            }

            String startTime = startTimeField.getText();
            if (startTime.isEmpty()) {
                AlertHelper.showErrorAlert("Adding Lecture", "Please set time");
                return;
            }

            int duration = 0;

            try {
                duration = Integer.parseInt(durationField.getText().trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                AlertHelper.showErrorAlert("Adding Lecture", "Duration should be an integer");
                return;
            }

            UnitSchedule unitSchedule = new UnitSchedule(0, mSelectedUnit.getId(), startTime, duration, weedDay);


            if (scheduleAdapter.add(unitSchedule)) {

                clearScheduleCache();

                AlertHelper.showSuccessAlert("Adding Lecture", "A lecture was added to the time table successfully");

            } else {
                AlertHelper.showErrorAlert("Adding Lecture", "Operation failed due a fatal error, check the logs");
            }

        });

    }

    private void populateUnits() {
        System.out.println(mSelectedCourse.getName() + " units populated");
        if (mSelectedCourse == null) {
            AlertHelper.showErrorAlert("Adding Lecture", "Select the course to filter the units");
            return;
        }

        ObservableList<String> units = FXCollections.observableArrayList();

        try {
            ResultSet rs = unitsAdapter.getUnitsByCourseId(mSelectedCourse.getId());

            while (rs.next()) {
                units.add(rs.getString("code"));
            }

            unitsComboBox.setItems(units);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateCourses() {
        ObservableList<String> courses = FXCollections.observableArrayList();

        try {
            ResultSet rs = coursesAdapter.getAll();

            while (rs.next()) {
                courses.add(rs.getString("alias"));
            }

            courseComboBox.setItems(courses);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Clear the previous entries of a schedule that has been successfully persisted
     */
    private void clearScheduleCache() {
        courseComboBox.getSelectionModel().clearSelection();
        unitsComboBox.getSelectionModel().clearSelection();
        startTimeField.clear();
        durationField.clear();
        weekDaysComboBox.getSelectionModel().clearSelection();
    }
}
