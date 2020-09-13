package com.propscout.data.adapters;

import com.propscout.data.models.AttendanceInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttendanceSheetAdapter extends AbstractDatabaseAdapter {

    public List<AttendanceInfo> getAllAttendanceInformation(int unitId) {

        open();

        String sql = "SELECT name, arrived_at, commenced_at, start_time, week_day FROM attendance\n" +
                "INNER JOIN lectures\n" +
                "ON attendance.lecture_id = lectures.id\n" +
                "INNER JOIN schedule\n" +
                "ON lectures.schedule_id = schedule.id\n" +
                "INNER JOIN students\n" +
                "ON attendance.reg_no = students.reg_no\n" +
                "WHERE schedule.unit_id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setInt(1, unitId);

            ResultSet rs = preparedStatement.executeQuery();

            List<AttendanceInfo> attendanceInfoList = new ArrayList<>();

            while (rs.next()) {
                String name = rs.getString("name");
                LocalDateTime commencedAt = rs.getTimestamp("commenced_at").toLocalDateTime();
                LocalDateTime arrivedAt = rs.getTimestamp("arrived_at").toLocalDateTime();
                LocalTime startTime = rs.getTime("start_time").toLocalTime();
                String weekday = rs.getString("week_day");

                attendanceInfoList.add(
                        new AttendanceInfo(
                                name,
                                arrivedAt,
                                commencedAt,
                                startTime,
                                weekday
                        )
                );
            }

            return attendanceInfoList;

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        return Collections.emptyList();

    }
}
