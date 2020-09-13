package com.propscout.gui.controllers.officers.attendance;

import com.propscout.data.adapters.ScheduleAdapter;
import com.propscout.data.models.User;
import com.propscout.gui.controllers.schedule.TimetableController;
import com.propscout.utils.AuthHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class TakeController implements Initializable {

    @FXML
    private ListView<TimetableController.UnitSchedule> lecturesListView;
    @FXML
    private Button refreshButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ScheduleAdapter mScheduleAdapter = new ScheduleAdapter();

        lecturesListView.setCellFactory(lecture -> new LectureCell());

        if (AuthHelper.isLoggedIn()) {

            User mUser = AuthHelper.getLoggedInUser();

            refreshButton.setOnAction(actionEvent -> {
                //Refresh the of lectures
                lecturesListView.setItems(mScheduleAdapter.getByLecturer(mUser.getId()));
            });

            lecturesListView.setItems(mScheduleAdapter.getByLecturer(mUser.getId()));

        }
    }

}
