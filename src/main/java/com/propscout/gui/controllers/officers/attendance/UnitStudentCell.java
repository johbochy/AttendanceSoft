package com.propscout.gui.controllers.officers.attendance;

import com.propscout.data.adapters.AttendanceAdapter;
import com.propscout.data.models.Attendance;
import com.propscout.data.models.LectureSchedule;
import com.propscout.data.models.UnitStudent;
import com.propscout.gui.controllers.dialog.ConfirmRegNoDialogController;
import com.propscout.gui.helpers.AlertHelper;
import com.propscout.internals.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;

public class UnitStudentCell extends ListCell<UnitStudent> {

    @FXML
    public Label nameLabel, mobileLabel, courseLabel, yearLabel, semesterLabel;

    @FXML
    public Button markPresentButton, revokeAttendanceButton;

    private LectureSchedule lectureSchedule;

    public UnitStudentCell(LectureSchedule lectureSchedule) {
        this.lectureSchedule = lectureSchedule;
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/officers/attendance/UnitStudentCell.fxml"));

            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(UnitStudent unitStudent, boolean empty) {
        super.updateItem(unitStudent, empty);

        if (empty) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {

            nameLabel.setText(unitStudent.getName());
            mobileLabel.setText(unitStudent.getMobile());
            courseLabel.setText(unitStudent.getCourse());
            yearLabel.setText(String.valueOf(unitStudent.getYear()));
            semesterLabel.setText(String.valueOf(unitStudent.getSemester()));
            markPresentButton.setDisable(true);
            revokeAttendanceButton.setDisable(true);

            Attendance attendance = new Attendance(lectureSchedule.getLectureId(), unitStudent.getRegNo(), null);

            AttendanceAdapter attendanceAdapter = new AttendanceAdapter();

            try {

                /*
                 *Activate the mark present button decision
                 */

                //Current Localtime
                LocalTime localTime = LocalTime.now();

                //Lecture Localtime
                String[] dbTime = lectureSchedule.getStartTime().split(":");
                LocalTime lectureTime = LocalTime.of(
                        Integer.parseInt(dbTime[0]),
                        Integer.parseInt(dbTime[1]),
                        Integer.parseInt(dbTime[2])
                );

                if ((localTime.compareTo(lectureTime) != Constants.LESS_THAN) && (localTime.compareTo(lectureTime.plusMinutes(30)) != Constants.GREATER_THAN)) {

                    //Check whether attendance is already submitted

                    if (attendanceAdapter.getDatabaseInstanceOrNull(attendance) == null) {
                        markPresentButton.setDisable(false);
                    } else {
                        revokeAttendanceButton.setDisable(false);
                    }

                } else System.out.println("Time Conflict");


            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


            markPresentButton.setOnAction(event -> {

                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/dialog/ConfirmRegNoDialog.fxml"));

                    Parent root = loader.load();

                    ConfirmRegNoDialogController controller = loader.getController();

                    controller.setAttendance(attendance);

                    Stage stage = new Stage();
                    stage.setTitle("Confirm your Registration Number first");
                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            });

            revokeAttendanceButton.setOnAction(event -> {

                //Delete the attendance record if created
                if (attendanceAdapter.delete(attendance)) {
                    AlertHelper.showSuccessAlert("Manage Attendance", "Attendance Revoked successfully");
                }

            });

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
