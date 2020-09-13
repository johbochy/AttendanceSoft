package com.propscout.data.adapters;

import com.propscout.data.models.Unit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitsAdapter extends AbstractDatabaseAdapter {

    public boolean add(Unit unit) {

        open();

        String sql = "INSERT INTO units(course_id, user_id, name, code, year, semester)\n" +
                "VALUES(?, ?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, unit.getCourseId());
            preparedStatement.setInt(2, unit.getOfficerId());
            preparedStatement.setString(3, unit.getName());
            preparedStatement.setString(4, unit.getCode());
            preparedStatement.setInt(5, unit.getYear());
            preparedStatement.setInt(6, unit.getSemester());

            int result = preparedStatement.executeUpdate();

            close();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ResultSet getUnitsByCourseId(int id) {
        open();
        String sql = "SELECT units.*, users.name as lecturer\n" +
                "FROM units\n" +
                "INNER JOIN users\n" +
                "ON units.user_id = users.id\n" +
                "WHERE course_id = ?;";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            return preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Getting units taught by a particular user
     *
     * @param id the primary key for the user
     * @return a ResultSet of the units from the database
     */
    public ResultSet getUnitsByUserId(int id) {
        open();

        String sql = "SELECT units.*, courses.name AS course\n" +
                "FROM units\n" +
                "INNER JOIN courses\n" +
                "ON units.course_id = courses.id\n" +
                "WHERE user_id = ?;";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            return preparedStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Unit getUnitByCode(String code) {

        open();

        String sql = "SELECT * FROM units\n" +
                "WHERE code = ?\n" +
                "LIMIT 1;";

        try (
                PreparedStatement preparedStatement = conn.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, code);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {

                int id = rs.getInt("id");
                int courseId = rs.getInt("course_id");
                int officerId = rs.getInt("user_id");
                String name = rs.getString("name");
                int year = rs.getInt("year");
                int semester = rs.getInt("semester");

                return new Unit(id, courseId, officerId, name, code, year, semester);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getLecturesCountForLecturer(int userId) {
        int unitsCount = 0;
        open();

        String sql = "SELECT COUNT(1) AS units_count \n" +
                "FROM units\n" +
                "WHERE user_id = ?;";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                unitsCount = rs.getInt("units_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return unitsCount;
    }

    public boolean deleteById(int id) {

        open();

        String sql = "DELETE FROM units WHERE id = ?";

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

    public boolean update(Unit unit) {

        open();

        String sql = "UPDATE units SET name = ?, year = ?, semester = ?, user_id = ?\n" +
                "WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, unit.getName());
            preparedStatement.setInt(2, unit.getYear());
            preparedStatement.setInt(3, unit.getSemester());
            preparedStatement.setInt(4, unit.getOfficerId());
            preparedStatement.setInt(5, unit.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error: " + e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
}
