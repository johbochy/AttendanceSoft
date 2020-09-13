package com.propscout.data.adapters;

import com.propscout.data.models.Course;
import com.propscout.gui.controllers.courses.BrowseController;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CoursesAdapter extends AbstractDatabaseAdapter {

    /**
     * Adds a new course to the database
     *
     * @param course an instance of course to be persisted to the database
     * @return whether the course is added to the database or the operation failed
     */
    public boolean add(Course course) {

        open();

        String sql = "INSERT INTO courses(alias, name) VALUES (?, ?);";

        try (
                PreparedStatement preparedStatement = conn.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, course.getAlias());
            preparedStatement.setString(2, course.getName());

            int result = preparedStatement.executeUpdate();

            close();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets all courses from the database
     *
     * @return ResultSet of all the courses
     */
    public ResultSet getAll() {

        open();
        String sql = "SELECT * FROM courses;";

        try {
            Statement statement = conn.createStatement();

            return statement.executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Course getCourseByAlias(String alias) {
        open();
        String sql = "SELECT * FROM courses WHERE alias = ? LIMIT 1";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, alias);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                alias = rs.getString("alias");
                String name = rs.getString("name");

                return new Course(id, alias, name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Course getCourseById(int courseId) {
        open();
        String sql = "SELECT * FROM courses WHERE id = ? LIMIT 1;";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, courseId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {

                int id = rs.getInt("id");
                String alias = rs.getString("alias");
                String name = rs.getString("name");

                return new Course(id, alias, name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateCourse(BrowseController.Course course) {
        open();
        String sql = "UPDATE courses \n" +
                "SET alias = ?, name = ?\n" +
                "WHERE id = ?;";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, course.getAlias());
            preparedStatement.setString(2, course.getName());
            preparedStatement.setInt(3, course.getId());

            int result = preparedStatement.executeUpdate();

            close();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public int getAllCoursesCount() {

        int coursesCount = 0;
        open();
        String sql = "SELECT COUNT(*) AS courses_count FROM courses;";

        try (Statement statement = conn.createStatement()) {

            ResultSet rs = statement.executeQuery(sql);


            if (rs.next()) {
                coursesCount = rs.getInt("courses_count");
            }

            close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coursesCount;
    }

    public boolean deleteCourseById(int id) {

        open();
        String sql = "DELETE FROM courses WHERE id = ?";

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
