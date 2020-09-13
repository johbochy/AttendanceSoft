package com.propscout.data.adapters;

import com.propscout.data.models.Lecture;
import com.propscout.data.models.UnitLecture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LecturesAdapter extends AbstractDatabaseAdapter {

    /**
     * Creates a new lecture in the database
     *
     * @param scheduleId the schedule id for the lecture being created
     * @return whether the lecture was creates
     */
    public boolean insert(int scheduleId) {
        open();
        String sql = "INSERT INTO lectures(schedule_id) VALUES(?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, scheduleId);

            int result = preparedStatement.executeUpdate();

            close();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;

    }

    public Lecture getTodayLectureOrNull(int scheduleId) {
        open();

        String sql = "SELECT * FROM lectures\n" +
                "WHERE schedule_id = ? AND DATE(commenced_at) = DATE(CURRENT_TIMESTAMP)\n" +
                "LIMIT 1;";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, scheduleId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int lectureNumber = resultSet.getInt("lecture_number");
                String commencedAt = resultSet.getString("commenced_at");

                Lecture lecture = new Lecture(id, scheduleId, lectureNumber, commencedAt);

                close();

                return lecture;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    public ObservableList<UnitLecture> getAllLecturesByLecturerId(int userId) {
        open();


        ObservableList<UnitLecture> returnList = FXCollections.observableArrayList();

        String sql = "SELECT schedule.week_day, schedule.duration_hrs, lectures.id, lectures.commenced_at, units.code\n" +
                "FROM schedule\n" +
                "INNER JOIN units\n" +
                "ON schedule.unit_id = units.id\n" +
                "CROSS JOIN lectures\n" +
                "ON schedule.id = lectures.schedule_id\n" +
                "WHERE units.user_id = ?;";

        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {

            //Bind the Lecturer Id
            pStmt.setInt(1, userId);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {

                int lectureId = rs.getInt("id");
                String weekday = rs.getString("week_day");
                int duration = rs.getInt("duration_hrs");
                String commencedAt = rs.getString("commenced_at");
                String unitCode = rs.getString("code");

                returnList.add(new UnitLecture(lectureId, weekday, duration, commencedAt, unitCode));
            }

            close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnList;
    }

    public int getLecturesCountForLecturer(int userId) {

        int lecturesCount = 0;

        open();

        String sql = "SELECT COUNT(*) lectures_count\n" +
                "FROM schedule\n" +
                "INNER JOIN units\n" +
                "ON schedule.unit_id = units.id\n" +
                "CROSS JOIN lectures\n" +
                "ON schedule.id = lectures.schedule_id\n" +
                "WHERE units.user_id = ?;";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {

                lecturesCount = rs.getInt("lectures_count");

            }

            close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lecturesCount;
    }
}
