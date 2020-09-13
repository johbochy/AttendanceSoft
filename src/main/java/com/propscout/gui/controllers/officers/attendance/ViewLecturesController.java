package com.propscout.gui.controllers.officers.attendance;

import com.propscout.data.adapters.LecturesAdapter;
import com.propscout.data.models.UnitLecture;
import com.propscout.data.models.User;
import com.propscout.gui.helpers.NavigationHelper;
import com.propscout.utils.AuthHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewLecturesController implements Initializable {

    @FXML
    private TableView<UnitLecture> lecturesTable;
    @FXML
    public TableColumn<UnitLecture, Integer> lectureIdCol;
    @FXML
    private TableColumn<UnitLecture, String> unitCodeCol;
    @FXML
    private TableColumn<UnitLecture, String> weekdayCol;
    @FXML
    private TableColumn<UnitLecture, String> commencedCol;
    @FXML
    private TableColumn<UnitLecture, Integer> durationCol;
    @FXML
    private MenuItem viewAttendanceContextMenuItem;

    //LecturesAdapter Instance
    private LecturesAdapter mLecturesAdapter;

    private User mUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mLecturesAdapter = new LecturesAdapter();

        iniCols();

        //Get logged In Lecturer / Officer
        if (AuthHelper.isLoggedIn()) mUser = AuthHelper.getLoggedInUser();

        populateUnitLectureTable();

        viewAttendanceContextMenuItem.setOnAction(actionEvent -> {

            //Get the clicked Lecture
            UnitLecture unitLecture = lecturesTable.getSelectionModel().getSelectedItem();

            assert unitLecture != null;

            //Navigate to show the attendance for that lecture
            NavigationHelper.swapToViewAttendance(getClass().getResource("/layouts/officers/attendance/ViewAttendance.fxml"), unitLecture);

        });
    }

    /**
     * Try populating the table with all the lectures for the units
     */
    private void populateUnitLectureTable() {
        if (mUser != null) lecturesTable.setItems(mLecturesAdapter.getAllLecturesByLecturerId(mUser.getId()));
    }

    /**
     * Initialize the column and their properties
     */
    private void iniCols() {
        lectureIdCol.setCellValueFactory(new PropertyValueFactory<>("lectureId"));
        unitCodeCol.setCellValueFactory(new PropertyValueFactory<>("unitCode"));
        weekdayCol.setCellValueFactory(new PropertyValueFactory<>("weekday"));
        commencedCol.setCellValueFactory(new PropertyValueFactory<>("commencedAt"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
    }
}
