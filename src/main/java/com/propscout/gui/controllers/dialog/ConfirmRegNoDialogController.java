package com.propscout.gui.controllers.dialog;

import com.propscout.data.adapters.AttendanceAdapter;
import com.propscout.data.models.Attendance;
import com.propscout.gui.helpers.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmRegNoDialogController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField regNoField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button submitButton;

    private Attendance attendance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setOnAction(event -> closeCurrentStage());
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;

        createActionForSubmitButton();
    }

    private void createActionForSubmitButton() {

        if (attendance != null)
            submitButton.setOnAction(event -> {
                //Check the registration number

                if (regNoField.getText().trim().equals(attendance.getRegNo())) {

                    //Submit and persist the attendance

                    AttendanceAdapter attendanceAdapter = new AttendanceAdapter();
                    if (attendanceAdapter.insert(attendance)) {
                        AlertHelper.showSuccessAlert("Submitting Attendance", "Operation successfully");
                        closeCurrentStage();
                    } else {
                        AlertHelper.showErrorAlert("Submitting Attendance", "Operation failed likely due SQLException, check your CLI log for verbose information");
                    }
                }
            });
    }

    private void closeCurrentStage() {
        ((Stage) rootPane.getScene().getWindow()).close();
    }
}
