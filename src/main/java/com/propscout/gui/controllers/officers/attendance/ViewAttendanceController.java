package com.propscout.gui.controllers.officers.attendance;

import com.propscout.data.adapters.AttendanceAdapter;
import com.propscout.data.models.StudentAttendance;
import com.propscout.data.models.UnitLecture;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewAttendanceController implements Initializable {

    @FXML
    private TableView<StudentAttendance> attendanceTable;
    @FXML
    private TableColumn<StudentAttendance, String> regNoCol;
    @FXML
    private TableColumn<StudentAttendance, String> nameCol;
    @FXML
    private TableColumn<StudentAttendance, String> mobileCol;
    @FXML
    private TableColumn<StudentAttendance, String> arrivedAtCol;
    @FXML
    private TableColumn<StudentAttendance, String> delayedForCol;

    private UnitLecture unitLecture;
    private AttendanceAdapter mAttendanceAdapter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mAttendanceAdapter = new AttendanceAdapter();

        initCols();
    }

    private void initCols() {
        regNoCol.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        arrivedAtCol.setCellValueFactory(new PropertyValueFactory<>("arrivedAt"));
        delayedForCol.setCellValueFactory(new PropertyValueFactory<>("delayedFor"));
    }

    public void setUnitLecture(UnitLecture unitLecture) {

        this.unitLecture = unitLecture;

        populateTable();

    }

    private void populateTable() {
        attendanceTable.setItems(mAttendanceAdapter.getAllStudentsThatAttendedALecture(unitLecture.getLectureId()));
    }
}
