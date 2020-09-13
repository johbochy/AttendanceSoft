package com.propscout.gui.controllers.settings;

import com.propscout.data.adapters.SettingsAdapter;
import com.propscout.data.models.Settings;
import com.propscout.gui.helpers.AlertHelper;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SettingsController implements Initializable {

    @FXML
    private DatePicker semesterCommenceDate;
    @FXML
    private DatePicker semesterEndDate;
    @FXML
    private ComboBox<Integer> semesterOption;
    @FXML
    private Button saveSettingsButton;

    private SettingsAdapter settingsAdapter;

    private Executor executor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        executor = Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);

            return thread;
        });

        settingsAdapter = new SettingsAdapter();

        saveSettingsButton.setOnAction(actionEvent -> {
            try {
                LocalDate commenceDate = semesterCommenceDate.getValue();
                LocalDate endDate = semesterEndDate.getValue();
                Integer semester = semesterOption.getSelectionModel().getSelectedItem();

                //Commence date validation
                if (commenceDate.isBefore(LocalDate.now())) {
                    AlertHelper.showErrorAlert("Settings", "You must choose a later date");
                    return;
                }

                //End date validation
                if (endDate.isBefore(LocalDate.now())) {
                    AlertHelper.showErrorAlert("Settings", "You must choose a later date");
                    return;
                }


                Settings settings = new Settings(commenceDate, endDate, semester);

                Task<Optional<Settings>> changeSettingsTask = new Task<>() {
                    @Override
                    protected Optional<Settings> call() throws Exception {

                        if (settingsAdapter.count() == 0) {
                            return settingsAdapter.save(settings);
                        } else {
                            settingsAdapter.update(settings);

                            return settingsAdapter.findById(1);
                        }

                    }
                };

                changeSettingsTask.setOnSucceeded(maybeSettings -> {

                    try {

                        changeSettingsTask.get().ifPresent(updatedSettings -> {
                            System.out.println(settings);
                            AlertHelper.showSuccessAlert("Updating Settings", "Settings Updated Successfully");
                        });

                    } catch (InterruptedException | ExecutionException e) {

                        e.printStackTrace();
                        AlertHelper.showErrorAlert("Updating Settings", e.getLocalizedMessage());

                    }

                });

                changeSettingsTask.setOnFailed(workerStateEvent -> {
                    changeSettingsTask.getException().printStackTrace();

                    AlertHelper.showErrorAlert("Updating Settings", changeSettingsTask.getException().getLocalizedMessage());
                });


                executor.execute(changeSettingsTask);

            } catch (Exception e) {
                e.printStackTrace();
                AlertHelper.showErrorAlert("Update Settings", e.getLocalizedMessage());
            }


        });

    }
}
