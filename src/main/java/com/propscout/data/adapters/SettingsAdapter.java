package com.propscout.data.adapters;

import com.propscout.data.models.Settings;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class SettingsAdapter extends AbstractDatabaseAdapter {

    public Optional<Settings> save(Settings settings) {
        System.out.println("Save settings called");

        String sql = "INSERT INTO settings(commence_date, end_date, semester) VALUES(?, ?, ?);";

        open();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setDate(1, Date.valueOf(settings.getCommenceDate()));
            preparedStatement.setDate(2, Date.valueOf(settings.getEndDate()));
            preparedStatement.setInt(3, settings.getSemester());

            final int result = preparedStatement.executeUpdate();

            return result > 0 ? Optional.of(settings) : Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean update(Settings settings) {
        System.out.println("Update settings called");
        String sql = "UPDATE settings SET commence_date = ?, end_date = ?, semester = ? WHERE id = 1";

        open();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setDate(1, Date.valueOf(settings.getCommenceDate()));
            preparedStatement.setDate(2, Date.valueOf(settings.getEndDate()));
            preparedStatement.setInt(3, settings.getSemester());

            final int result = preparedStatement.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public int count() {
        System.out.println("Count called");
        String sql = "SELECT COUNT(id) AS count FROM settings";

        open();

        try (Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                System.out.println("Greater than 0 returned");
                return rs.getInt("count");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public Optional<Settings> findById(int id) {
        open();

        String sql = "SELECT * FROM settings WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {

                LocalDate commenceDate = rs.getDate("commence_date").toLocalDate();
                LocalDate endDate = rs.getDate("end_date").toLocalDate();

                int semester = rs.getInt("semester");

                return Optional.of(new Settings(commenceDate, endDate, semester));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
