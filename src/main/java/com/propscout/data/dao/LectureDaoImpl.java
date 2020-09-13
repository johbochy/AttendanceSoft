package com.propscout.data.dao;

import com.propscout.data.adapters.ScheduleAdapter;
import com.propscout.data.models.UnitSchedule;
import com.propscout.gui.controllers.schedule.TimetableController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LectureDaoImpl implements LectureDao {

    private ScheduleAdapter scheduleAdapter;

    public LectureDaoImpl() {
        scheduleAdapter = new ScheduleAdapter();
    }

    @Override
    public boolean addLecture(UnitSchedule unitSchedule) {
        return false;
    }

    @Override
    public boolean updateLecture(UnitSchedule unitSchedule) {
        return false;
    }

    @Override
    public boolean deleteLecture(UnitSchedule unitSchedule) {
        return false;
    }

    @Override
    public ObservableList<TimetableController.UnitSchedule> browse() {

        ObservableList<TimetableController.UnitSchedule> returnList = FXCollections.observableArrayList();

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

                returnList.add(new TimetableController.UnitSchedule(id, code, lecturer, startTime, duration, weekday));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return returnList;
    }

    @Override
    public UnitSchedule read(int id) {
        return null;
    }

}
