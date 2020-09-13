package com.propscout.data.adapters;

import com.propscout.data.models.Attendance;
import com.propscout.data.models.LectureSchedule;
import com.propscout.data.models.StudentAttendance;
import com.propscout.data.models.UnitStudent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttendanceAdapter extends AbstractDatabaseAdapter {

    public boolean insert(Attendance attendance) {

        open();

        String sql = "INSERT INTO attendance(lecture_id, reg_no)\n" +
                "VALUES (?, ?);";

        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {

            pStmt.setInt(1, attendance.getLectureId());
            pStmt.setString(2, attendance.getRegNo());

            int result = pStmt.executeUpdate();

            close();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Attendance getDatabaseInstanceOrNull(Attendance attendance) {
        open();
        String sql = "SELECT * FROM attendance\n" +
                "WHERE lecture_id = ? AND reg_no = ?\n" +
                "LIMIT 1;";

        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setInt(1, attendance.getLectureId());
            pStmt.setString(2, attendance.getRegNo());

            ResultSet rs = pStmt.executeQuery();

            Attendance dbAttendance = null;

            if (rs.next()) {
                dbAttendance = new Attendance(
                        rs.getInt("lecture_id"),
                        rs.getString("reg_no"),
                        rs.getString("arrived_at")
                );
            }

            close();

            return dbAttendance;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean delete(Attendance attendance) {
        open();
        String sql = "DELETE FROM attendance\n" +
                "WHERE lecture_id = ? AND reg_no = ?;";


        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {

            pStmt.setInt(1, attendance.getLectureId());
            pStmt.setString(2, attendance.getRegNo());

            int result = pStmt.executeUpdate();

            close();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;

    }

    public ObservableList<UnitStudent> getStudentsByUnitCode(String code) {
        open();
        String sql = "SELECT students.*, courses.name AS course\n" +
                "FROM students\n" +
                "INNER JOIN courses\n" +
                "ON students.course_id = courses.id\n" +
                "INNER JOIN units\n" +
                "ON students.course_id = units.course_id\n" +
                "WHERE students.year = units.year \n" +
                "AND students.semester = units.semester\n" +
                "AND units.code = ?;";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, code);

            ResultSet rs = preparedStatement.executeQuery();

            ObservableList<UnitStudent> returnList = FXCollections.observableArrayList();

            while (true) {

                try {
                    if (!rs.next()) break;

                    String regNo = rs.getString("reg_no");
                    String name = rs.getString("name");
                    String mobile = rs.getString("mobile");
                    String course = rs.getString("course");
                    int year = rs.getInt("year");
                    int semester = rs.getInt("semester");

                    returnList.add(new UnitStudent(name, regNo, mobile, course, semester, year));

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

    public LectureSchedule getCurrentLectureScheduleOrNullScheduleId(int scheduleId) {
        open();

        String sql = "SELECT lectures.id as lecture_id, lectures.lecture_number, lectures.commenced_at, schedule.*\n" +
                "FROM lectures\n" +
                "INNER JOIN schedule\n" +
                "ON lectures.schedule_id = schedule.id\n" +
                "WHERE schedule_id = ? AND DATE(commenced_at) = DATE(CURRENT_TIMESTAMP)\n" +
                "LIMIT 1;";


        try (PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, scheduleId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                int lectureId = resultSet.getInt("lecture_id");
                int lectureNumber = resultSet.getInt("lecture_number");
                String commencedAt = resultSet.getString("commenced_at");
                int unitId = resultSet.getInt("unit_id");
                int duration = resultSet.getInt("duration_hrs");
                String weekday = resultSet.getString("week_day");
                String startTime = resultSet.getString("start_time");

                LectureSchedule lectureSchedule = new LectureSchedule(
                        lectureId,
                        scheduleId,
                        unitId,
                        startTime, duration, weekday, lectureNumber, commencedAt);

                close();

                return lectureSchedule;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }


    public ObservableList<UnitStudent> getStudentsByUnitId(int unitId) {
        open();
        String sql = "SELECT students.*, courses.name AS course\n" +
                "FROM students\n" +
                "INNER JOIN courses\n" +
                "ON students.course_id = courses.id\n" +
                "INNER JOIN units\n" +
                "ON students.course_id = units.course_id\n" +
                "WHERE students.year = units.year \n" +
                "AND students.semester = units.semester\n" +
                "AND units.id = ?;";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, unitId);

            ResultSet rs = preparedStatement.executeQuery();

            ObservableList<UnitStudent> returnList = FXCollections.observableArrayList();

            while (true) {

                try {
                    if (!rs.next()) break;

                    String regNo = rs.getString("reg_no");
                    String name = rs.getString("name");
                    String mobile = rs.getString("mobile");
                    String course = rs.getString("course");
                    int year = rs.getInt("year");
                    int semester = rs.getInt("semester");

                    returnList.add(new UnitStudent(name, regNo, mobile, course, semester, year));

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

    public ObservableList<StudentAttendance> getAllStudentsThatAttendedALecture(int lectureId) {
        open();


        ObservableList<StudentAttendance> returnList = FXCollections.observableArrayList();

        String sql = "SELECT attendance.*, students.name, students.mobile, TIME(attendance.arrived_at) - TIME(lectures.commenced_at) AS delayed_for\n" +
                "FROM attendance\n" +
                "INNER JOIN students\n" +
                "ON attendance.reg_no = students.reg_no\n" +
                "INNER JOIN lectures\n" +
                "ON lectures.id = attendance.lecture_id\n" +
                "WHERE lecture_id = ?;";

        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {

            //Bind parameters
            pStmt.setInt(1, lectureId);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {

                String regNo = rs.getString("reg_no");
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");
                String arrivedAt = rs.getString("arrived_at");
                int seconds = rs.getInt("delayed_for");
                String delayedFor = String.format("%02d:%02d", (int) (seconds / 60), (seconds % 60));

                returnList.add(new StudentAttendance(regNo, name, mobile, arrivedAt, delayedFor));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnList;
    }


}
