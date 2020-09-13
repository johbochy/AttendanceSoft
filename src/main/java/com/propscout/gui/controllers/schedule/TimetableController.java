package com.propscout.gui.controllers.schedule;

import com.propscout.data.adapters.ScheduleAdapter;
import com.propscout.gui.helpers.AlertHelper;
import com.propscout.gui.helpers.NavigationHelper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TimetableController implements Initializable {

    @FXML
    private Button addUnitScheduleButton;

    @FXML
    private TableView<UnitSchedule> scheduleTable;
    @FXML
    private TableColumn<UnitSchedule, Integer> idCol;
    @FXML
    private TableColumn<UnitSchedule, String> codeCol;
    @FXML
    private TableColumn<UnitSchedule, String> lecturerCol;
    @FXML
    private TableColumn<UnitSchedule, String> startTimeCol;
    @FXML
    private TableColumn<UnitSchedule, String> durationCol;
    @FXML
    private TableColumn<UnitSchedule, String> weekDayCol;
    @FXML
    public MenuItem rescheduleUnitMenuItem;
    @FXML
    public MenuItem deleteScheduleItemMenuItem;

    private ScheduleAdapter scheduleAdapter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scheduleAdapter = new ScheduleAdapter();

        initCol();

        populateTable();

        addUnitScheduleButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/schedule/Add.fxml"));
            NavigationHelper.changeTitle("Schedule A Unit");
        });

        rescheduleUnitMenuItem.setOnAction(actionEvent -> {
            UnitSchedule unitSchedule = scheduleTable.getSelectionModel().getSelectedItem();

            if (unitSchedule == null) {
                AlertHelper.showErrorAlert("Rescheduling", "Select an Item to re-schedule please");
                return;
            }

            //Navigate to re schedule screen
            NavigationHelper.swapToEditSchedule(getClass().getResource("/layouts/schedule/Edit.fxml"), unitSchedule);
        });

        deleteScheduleItemMenuItem.setOnAction(actionEvent -> {

            UnitSchedule unitSchedule = scheduleTable.getSelectionModel().getSelectedItem();

            if (unitSchedule == null) {
                AlertHelper.showErrorAlert("Rescheduling", "Select an Item to re-schedule please");
                return;
            }

            if (scheduleAdapter.deleteItemById(unitSchedule.getId())) {
                scheduleTable.getItems().remove(unitSchedule);
                AlertHelper.showSuccessAlert("Deleting Item", "Item deleted successfully");
            } else {
                AlertHelper.showErrorAlert("Deleting Item", "Deletion failed for some reason, check the logs");
            }

        });
    }

    private void populateTable() {
        ObservableList<UnitSchedule> lecturesList = FXCollections.observableArrayList();

        ResultSet resultSet = scheduleAdapter.all();

        while (true) {

            try {
                if (!resultSet.next()) break;

                int id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String lecturer = resultSet.getString("lecturer");
                String startTime = resultSet.getString("start_time");
                String duration = resultSet.getString("duration_hrs");
                String weekday = resultSet.getString("week_day");

                lecturesList.add(new UnitSchedule(id, code, lecturer, startTime, duration, weekday));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        scheduleTable.setItems(lecturesList);

    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        lecturerCol.setCellValueFactory(new PropertyValueFactory<>("lecturer"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        weekDayCol.setCellValueFactory(new PropertyValueFactory<>("weekday"));
    }

    public static class UnitSchedule {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty code;
        private final SimpleStringProperty lecturer;
        private final SimpleStringProperty startTime;
        private final SimpleStringProperty duration;
        private final SimpleStringProperty weekday;

        public UnitSchedule(int id, String code, String lecturer, String startTime, String duration, String weekday) {
            this.id = new SimpleIntegerProperty(id);
            this.code = new SimpleStringProperty(code);
            this.lecturer = new SimpleStringProperty(lecturer);
            this.startTime = new SimpleStringProperty(startTime);
            this.duration = new SimpleStringProperty(duration);
            this.weekday = new SimpleStringProperty(weekday);
        }

        public int getId() {
            return id.get();
        }

        public String getCode() {
            return code.get();
        }

        public String getLecturer() {
            return lecturer.get();
        }

        public String getStartTime() {
            return startTime.get();
        }

        public String getDuration() {
            return duration.get();
        }

        public String getWeekday() {
            return weekday.get();
        }
    }
}
