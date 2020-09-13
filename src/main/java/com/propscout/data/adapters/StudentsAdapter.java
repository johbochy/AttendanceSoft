package com.propscout.data.adapters;

import com.propscout.data.models.Student;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentsAdapter extends AbstractDatabaseAdapter {

    public boolean add(Student student) {
        open();
        String sql = "INSERT INTO students(reg_no, name, mobile, course_id, year, semester, avatar, fingerprint)\n" +
                "VALUES(?, ?, ?, ?, ?, ?, null, null);";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, student.getRegNo());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setString(3, student.getMobile());
            preparedStatement.setInt(4, student.getCourseId());
            preparedStatement.setInt(5, student.getYear());
            preparedStatement.setInt(6, student.getSemester());

            int result = preparedStatement.executeUpdate();


            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }

    /**
     * Get all the students from the database
     *
     * @return ResultSet containing all the students in the database
     */
    public ResultSet getAll() {

        open();
        String sql = "SELECT * FROM students";

        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getAllStudentsCount() {

        int studentsCount = 0;
        open();
        String sql = "SELECT COUNT(*) AS students_count FROM students;";

        try (Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery(sql);


            if (rs.next()) {
                studentsCount = rs.getInt("students_count");
            }

            close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return studentsCount;
    }

    public boolean save(Student student) {
        String sql = "UPDATE students \n" +
                "SET `name` = ?, `mobile` = ?, `course_id` = ?, `semester` = ?, `year` = ?\n" +
                "WHERE id = ?;";

        open();

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getMobile());
            preparedStatement.setInt(3, student.getCourseId());
            preparedStatement.setInt(4, student.getSemester());
            preparedStatement.setInt(5, student.getYear());
            preparedStatement.setInt(6, student.getId());

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getLocalizedMessage());
        } finally {
            close();
        }

        return false;
    }

    public boolean deleteById(int id) {

        open();
        String sql = "DELETE FROM students WHERE id = ?";

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


}
