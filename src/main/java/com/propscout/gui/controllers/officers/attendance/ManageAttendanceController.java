package com.propscout.gui.controllers.officers.attendance;

import com.propscout.data.adapters.AttendanceAdapter;
import com.propscout.data.models.LectureSchedule;
import com.propscout.data.models.UnitStudent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageAttendanceController implements Initializable {

    @FXML
    private ListView<UnitStudent> manageAttendanceListView;

    @FXML
    private Button refreshButton;

    private AttendanceAdapter attendanceAdapter;

    private String mUnitCode;
    private LectureSchedule mLectureSchedule;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        attendanceAdapter = new AttendanceAdapter();

        refreshButton.setOnAction(actionEvent -> {
            manageAttendanceListView.getItems().clear();

            populateList();
        });

    }

    public void setUnitCode(String mUnitCode) {
        this.mUnitCode = mUnitCode;

        populateList();
    }

    public void setLectureSchedule(LectureSchedule lectureSchedule) {
        this.mLectureSchedule = lectureSchedule;

        manageAttendanceListView.setCellFactory(unitStudent -> new UnitStudentCell(mLectureSchedule));

        populateList();
    }

    private void populateList() {

        if (mUnitCode != null) {
            manageAttendanceListView.setItems(attendanceAdapter.getStudentsByUnitCode(mUnitCode));
        }

        if (mLectureSchedule != null) {
            manageAttendanceListView.setItems(attendanceAdapter.getStudentsByUnitId(mLectureSchedule.getUnitId()));
        }

    }
}
