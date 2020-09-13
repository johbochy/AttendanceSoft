package com.propscout.gui.controllers;

import com.propscout.data.adapters.CoursesAdapter;
import com.propscout.data.adapters.ScheduleAdapter;
import com.propscout.data.adapters.StudentsAdapter;
import com.propscout.data.adapters.UsersAdapter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Text usersCountText;
    @FXML
    private Text studentsCountText;
    @FXML
    private Text coursesCountText;
    @FXML
    private Text scheduledUnitsCountText;

    private UsersAdapter usersAdapter;
    private StudentsAdapter studentsAdapter;
    private CoursesAdapter coursesAdapter;
    private ScheduleAdapter scheduleAdapter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        usersAdapter = new UsersAdapter();
        studentsAdapter = new StudentsAdapter();
        coursesAdapter = new CoursesAdapter();
        scheduleAdapter = new ScheduleAdapter();

        populateCountResults();
    }

    private void populateCountResults() {
        usersCountText.setText(String.format("%d", usersAdapter.getAllUsersCount()));
        studentsCountText.setText(String.format("%d", studentsAdapter.getAllStudentsCount()));
        coursesCountText.setText(String.format("%d", coursesAdapter.getAllCoursesCount()));
        scheduledUnitsCountText.setText(String.format("%d", scheduleAdapter.getTotalOfScheduledUnits()));
    }
}
