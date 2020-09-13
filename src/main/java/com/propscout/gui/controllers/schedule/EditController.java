package com.propscout.gui.controllers.schedule;

import com.propscout.data.adapters.ScheduleAdapter;
import com.propscout.data.models.UnitSchedule;
import com.propscout.gui.helpers.AlertHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class EditController implements Initializable {

    @FXML
    private Text unitCodeText;
    @FXML
    private ComboBox<String> weekDaysComboBox;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField durationField;
    @FXML
    private Button updateUnitSchedule;

    private TimetableController.UnitSchedule unitSchedule;
    private ScheduleAdapter scheduleAdapter;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        scheduleAdapter = new ScheduleAdapter();

        updateUnitSchedule.setOnAction(actionEvent -> {

            //Validate the weekday
            String weedDay = weekDaysComboBox.getSelectionModel().getSelectedItem().trim();
            if (weedDay.isEmpty()) {
                AlertHelper.showErrorAlert("Rescheduling Lecture", "Please select the weekday");
                return;
            }

            String startTime = startTimeField.getText();
            if (startTime.isEmpty()) {
                AlertHelper.showErrorAlert("Rescheduling Lecture", "Please set time");
                return;
            }

            int duration = 0;

            try {
                duration = Integer.parseInt(durationField.getText().trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                AlertHelper.showErrorAlert("Rescheduling Lecture", "Duration should be an integer");
                return;
            }


            if (scheduleAdapter.update(new UnitSchedule(
                    unitSchedule.getId(),
                    0,
                    startTime,
                    duration,
                    weedDay
            ))) {
                AlertHelper.showSuccessAlert("Reschedule Lecture", "A lecture was successfully rescheduled");
            } else {
                AlertHelper.showErrorAlert("Reschedule Lecture", "Operation failed, check the logs for more info");
            }
        });

    }

    public void setUnitSchedule(TimetableController.UnitSchedule unitSchedule) {
        this.unitSchedule = unitSchedule;

        populateFields();
    }

    private void populateFields() {
        startTimeField.setText(unitSchedule.getStartTime());
        durationField.setText(unitSchedule.getDuration());
        unitCodeText.setText(unitSchedule.getCode());
        weekDaysComboBox.getSelectionModel().select(unitSchedule.getWeekday());
    }
}
