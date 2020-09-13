package com.propscout.gui.controllers.officers;

import com.propscout.data.adapters.LecturesAdapter;
import com.propscout.data.adapters.ScheduleAdapter;
import com.propscout.data.adapters.UnitsAdapter;
import com.propscout.data.models.User;
import com.propscout.utils.AuthHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Text unitsCountText;

    @FXML
    private Text lecturesCountText;

    @FXML
    private Text scheduledUnitsCountText;

    private UnitsAdapter unitsAdapter;
    private LecturesAdapter lecturesAdapter;
    private ScheduleAdapter scheduleAdapter;
    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        unitsAdapter = new UnitsAdapter();
        lecturesAdapter = new LecturesAdapter();
        scheduleAdapter = new ScheduleAdapter();

        //Set authenticated user avatar
        if (AuthHelper.isLoggedIn()) {

            user = AuthHelper.getLoggedInUser();

            populateResourceCounts();
        }

    }

    private void populateResourceCounts() {
        unitsCountText.setText(String.format("%s", unitsAdapter.getLecturesCountForLecturer(user.getId())));
        scheduledUnitsCountText.setText(String.format("%s", scheduleAdapter.getTotalOfScheduledUnitsForLecturer(user.getId())));
        lecturesCountText.setText(String.format("%s", lecturesAdapter.getLecturesCountForLecturer(user.getId())));
    }
}
