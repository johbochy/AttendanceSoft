package com.propscout.gui.controllers.officers.attendance;

import com.propscout.data.adapters.AttendanceAdapter;
import com.propscout.data.adapters.LecturesAdapter;
import com.propscout.data.models.LectureSchedule;
import com.propscout.gui.controllers.schedule.TimetableController;
import com.propscout.gui.helpers.NavigationHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class LectureCell extends ListCell<TimetableController.UnitSchedule> {

    @FXML
    private Label codeLabel, weekdayLabel, startTimeLabel, durationLabel;

    @FXML
    private Button takeAttendanceButton;

    public LectureCell() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/officers/attendance/LectureCell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void updateItem(TimetableController.UnitSchedule item, boolean empty) {

        if (empty) {

            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);

        } else {
            codeLabel.setText(item.getCode());
            weekdayLabel.setText(item.getWeekday());
            startTimeLabel.setText(item.getStartTime());
            durationLabel.setText(String.format("%s hours", item.getDuration()));
            takeAttendanceButton.setDisable(true);

            try {
                //Just gate the day of week
                var now = LocalDateTime.now();

                //Current Localtime
                LocalTime localTime = LocalTime.now();

                //Lecture Localtime
                LocalTime lectureTime = LocalTime.parse(item.getStartTime());

                if (item.getWeekday().equalsIgnoreCase(now.getDayOfWeek().toString()))
                    if (localTime.isAfter(lectureTime) && localTime.isBefore(lectureTime.plusMinutes(30)))
                        takeAttendanceButton.setDisable(false);
                    else System.out.println("Not within the set time bounds");
                else System.out.println("Lecture not today");


                takeAttendanceButton.setOnAction(event -> {
                    //Record the lecture in the database if not yet
                    LecturesAdapter lecturesAdapter = new LecturesAdapter();

                    if (lecturesAdapter.getTodayLectureOrNull(item.getId()) == null) {

                        if (lecturesAdapter.insert(item.getId())) {
                            manageAttendance(item.getId());
                        } else {
                            System.out.println("Lecture not created");
                        }

                    } else {

                        manageAttendance(item.getId());
                    }

                });


            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    private void manageAttendance(int scheduleId) {

        AttendanceAdapter attendanceAdapter = new AttendanceAdapter();

        //Navigate to manage the attendance
        LectureSchedule lectureSchedule = attendanceAdapter.getCurrentLectureScheduleOrNullScheduleId(scheduleId);

        assert lectureSchedule != null;

        NavigationHelper.swapToManageAttendance(getClass().getResource("/layouts/officers/attendance/Manage.fxml"), lectureSchedule);

    }
}
