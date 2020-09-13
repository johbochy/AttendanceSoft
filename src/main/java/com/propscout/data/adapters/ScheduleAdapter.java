package com.propscout.data.adapters;

import com.propscout.data.models.UnitSchedule;
import com.propscout.gui.controllers.schedule.TimetableController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ScheduleAdapter extends AbstractDatabaseAdapter {

    public boolean add(UnitSchedule unitSchedule) {
        open();

        String sql = "INSERT INTO schedule(unit_id, start_time, duration_hrs, week_day)\n" +
                "VALUES(?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, unitSchedule.getUnitId());
            preparedStatement.setString(2, unitSchedule.getStartTime());
            preparedStatement.setInt(3, unitSchedule.getDuration());
            preparedStatement.setString(4, unitSchedule.getWeekDay());

            int result = preparedStatement.executeUpdate();

            close();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ResultSet all() {
        open();
        String sql = "SELECT schedule.*, units.code AS code, users.name AS lecturer\n" +
                "FROM schedule\n" +
                "INNER JOIN units\n" +
                "ON schedule.unit_id = units.id\n" +
                "INNER JOIN users\n" +
                "ON units.user_id = users.id;";

        try {

            Statement statement = conn.createStatement();

            return statement.executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ObservableList<TimetableController.UnitSchedule> getByLecturer(int userId) {
        open();
        String sql = "SELECT schedule.*, units.code AS code\n" +
                "FROM schedule\n" +
                "INNER JOIN units\n" +
                "ON schedule.unit_id = units.id\n" +
                "WHERE units.user_id = ?;";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();

            ObservableList<TimetableController.UnitSchedule> returnList = FXCollections.observableArrayList();

            while (true) {

                try {
                    if (!rs.next()) break;

                    int id = rs.getInt("id");
                    String code = rs.getString("code");
                    String startTime = rs.getString("start_time");
                    String duration = rs.getString("duration_hrs");
                    String weekday = rs.getString("week_day");

                    returnList.add(new TimetableController.UnitSchedule(id, code, null, startTime, duration, weekday));

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            close();

            return returnList;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getTotalOfScheduledUnits() {

        int scheduledUnitsCount = 0;
        open();

        String sql = "SELECT DISTINCT COUNT(unit_id) AS scheduled_units_count FROM schedule;";

        try (Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                scheduledUnitsCount = rs.getInt("scheduled_units_count");
            }

            close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scheduledUnitsCount;
    }


    public int getTotalOfScheduledUnitsForLecturer(int userId) {
        int scheduledUnitsCount = 0;

        open();

        String sql = "SELECT COUNT(*) AS schedule_count\n" +
                "FROM schedule\n" +
                "INNER JOIN units\n" +
                "ON schedule.unit_id = units.id\n" +
                "WHERE units.user_id = ?;";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                scheduledUnitsCount = rs.getInt("schedule_count");
            }

            close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scheduledUnitsCount;

    }

    public boolean deleteItemById(int id) {

        open();
        String sql = "DELETE FROM schedule WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            final int result = preparedStatement.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            System.err.println("Error: " + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            close();
        }

        return false;
    }

    public boolean update(UnitSchedule unitSchedule) {

        open();

        String sql = "UPDATE schedule SET start_time = ?, duration_hrs = ?, week_day = ?\n" +
                "WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, unitSchedule.getStartTime());
            preparedStatement.setInt(2, unitSchedule.getDuration());
            preparedStatement.setString(3, unitSchedule.getWeekDay());
            preparedStatement.setInt(4, unitSchedule.getId());

            int result = preparedStatement.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return false;
    }
}
