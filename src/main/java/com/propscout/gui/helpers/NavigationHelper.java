package com.propscout.gui.helpers;

import com.propscout.data.models.*;
import com.propscout.gui.controllers.courses.BrowseController;
import com.propscout.gui.controllers.courses.EditController;
import com.propscout.gui.controllers.officers.attendance.AttendanceSheetController;
import com.propscout.gui.controllers.officers.attendance.ManageAttendanceController;
import com.propscout.gui.controllers.officers.attendance.ViewAttendanceController;
import com.propscout.gui.controllers.schedule.TimetableController;
import com.propscout.gui.controllers.students.EditStudentDetailsController;
import com.propscout.gui.controllers.units.AddController;
import com.propscout.gui.controllers.users.EditUserDetailsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class NavigationHelper {

    private static StackPane contentPane;
    private static Text titleText;

    public static void initNavigator(StackPane stackPane, Text titleText) {
        contentPane = stackPane;
        NavigationHelper.titleText = titleText;
    }

    /**
     * Starts a new stage in the application
     *
     * @param location URL location for the layout file to be loaded
     * @param title    for the stage to be shown
     * @throws IOException if the location URL passed is null
     */
    public static void startWindow(URL location, String title) throws IOException {
        Parent root = FXMLLoader.load(location);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void changeTitle(String title) {
        titleText.setText(title);
    }


    /**
     * Changes the content section to the pass URL Location
     *
     * @param location the location of the new view
     */
    public static void swapContent(URL location) {

        try {
            Parent root = FXMLLoader.load(location);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Swaps the current window at the admin dashboard from view all courses to a single courses units
     *
     * @param location the location file that need to loaded
     * @param course   the course of which you want to show the units
     */
    public static void swapToBrowseUnits(URL location, BrowseController.Course course) {

        FXMLLoader loader = new FXMLLoader(location);

        try {

            Parent root = loader.load();

            //Get the controller and send the data
            com.propscout.gui.controllers.units.BrowseController controller = loader.getController();

            if (course != null) {
                changeTitle(course.getName() + " Units");
            } else {
                changeTitle("Units");
            }

            controller.setCourse(course);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Swaps the current window at the admin dashboard from view all courses to a single courses units
     *
     * @param location the location file that need to loaded
     * @param course   the course of which you want to show the units
     */
    public static void swapToAddUnits(URL location, BrowseController.Course course) {

        FXMLLoader loader = new FXMLLoader(location);

        try {

            Parent root = loader.load();

            //Get the controller and send the data
            AddController controller = loader.getController();

            if (course == null) {
                AlertHelper.showErrorAlert("Adding a Unit", "To which course do you intent to add this unit?");
                return;
            }

            controller.setCourse(course);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            changeTitle("Add Unit");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param location        Manage Attendance Location
     * @param lectureSchedule The lectureSchedule details
     */
    public static void swapToManageAttendance(URL location, LectureSchedule lectureSchedule) {

        FXMLLoader loader = new FXMLLoader(location);

        try {

            Parent root = loader.load();

            //Get the controller and send the data
            ManageAttendanceController controller = loader.getController();

            if (lectureSchedule == null) {
                AlertHelper.showErrorAlert("Managing a Unit Attendance", "Didn't get the schedule and lecture details");
                return;
            }

            controller.setLectureSchedule(lectureSchedule);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            changeTitle("Manage Unit Attendance");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Swap the content in the content pane to show attendance for a particular lecture
     *
     * @param location    the location of the view attendance from the resources
     * @param unitLecture the object holding the lecture id for the attendance
     */
    public static void swapToViewAttendance(URL location, UnitLecture unitLecture) {

        FXMLLoader loader = new FXMLLoader(location);

        try {
            Parent root = loader.load();

            ViewAttendanceController viewAttendanceController = loader.getController();
            viewAttendanceController.setUnitLecture(unitLecture);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            changeTitle("Selected Unit Student Attendance");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Swapping the content pane to show UI for editing course
     *
     * @param location location of the file for editing the course
     * @param course   the instance of course to be updated
     */
    public static void swapToEditCourse(URL location, BrowseController.Course course) {
        FXMLLoader loader = new FXMLLoader(location);

        try {
            Parent root = loader.load();

            EditController editController = loader.getController();

            editController.setCourse(course);


            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            changeTitle("Edit Course Details");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper function to swap the contentPane content to the UI for editing a user
     *
     * @param location the resource location of the file for editing user UI
     * @param user     the user instance that is supposed to be edited
     */
    public static void swapToEditUser(URL location, User user) {

        FXMLLoader loader = new FXMLLoader(location);

        try {
            Parent root = loader.load();

            EditUserDetailsController editUserDetailsController = loader.getController();

            editUserDetailsController.setUser(user);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            changeTitle("Edit User Details");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void swapFromOfficerUnitToAttendanceSheet(URL location, com.propscout.gui.controllers.officers.units.BrowseController.Unit unit) {

        FXMLLoader loader = new FXMLLoader(location);

        try {
            Parent root = loader.load();

            AttendanceSheetController attendanceSheetController = loader.getController();

            attendanceSheetController.setUnit(unit);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            changeTitle("Attendance Sheet\n" + unit.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void swapToEditStudent(URL location, com.propscout.gui.controllers.students.BrowseController.Student student) {

        FXMLLoader loader = new FXMLLoader(location);

        try {
            Parent root = loader.load();

            EditStudentDetailsController editStudentDetailsController = loader.getController();

            editStudentDetailsController.setStudent(student);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            changeTitle("Edit " + student.getRegNo());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void swapToEditUnit(URL location, Unit unit) {

        FXMLLoader loader = new FXMLLoader(location);

        try {
            Parent root = loader.load();

            com.propscout.gui.controllers.units.EditController editController = loader.getController();

            editController.setUnit(unit);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            changeTitle("Edit " + unit.getCode());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void swapToEditSchedule(URL location, TimetableController.UnitSchedule unitSchedule) {

        FXMLLoader loader = new FXMLLoader(location);

        try {
            Parent root = loader.load();

            com.propscout.gui.controllers.schedule.EditController editController = loader.getController();
            editController.setUnitSchedule(unitSchedule);

            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);

            changeTitle("Reschedule " + unitSchedule.getCode());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
