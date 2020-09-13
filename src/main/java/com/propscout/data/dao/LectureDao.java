package com.propscout.data.dao;

import com.propscout.data.models.UnitSchedule;
import com.propscout.gui.controllers.schedule.TimetableController;
import javafx.collections.ObservableList;

public interface LectureDao {

    boolean addLecture(UnitSchedule unitSchedule);

    boolean updateLecture(UnitSchedule unitSchedule);

    boolean deleteLecture(UnitSchedule unitSchedule);

    ObservableList<TimetableController.UnitSchedule> browse();

    UnitSchedule read(int id);

}
