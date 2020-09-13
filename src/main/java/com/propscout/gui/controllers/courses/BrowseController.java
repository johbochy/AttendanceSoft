package com.propscout.gui.controllers.courses;

import com.propscout.data.adapters.CoursesAdapter;
import com.propscout.gui.helpers.AlertHelper;
import com.propscout.gui.helpers.NavigationHelper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Handles browsing operation for the courses
 */
public class BrowseController implements Initializable {

    @FXML
    private Button addCourseButton;

    @FXML
    private TableView<Course> coursesTable;

    @FXML
    private TableColumn<Course, Integer> idCol;

    @FXML
    private TableColumn<Course, String> aliasCol;

    @FXML
    private TableColumn<Course, String> nameCol;

    @FXML
    private MenuItem viewUnits;

    @FXML
    private MenuItem updateCourse;

    @FXML
    private MenuItem deleteCourse;

    /**
     * @var available courses observable list
     */
    private ObservableList<Course> coursesObservableList;

    private CoursesAdapter coursesAdapter;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Init courses observable list
        coursesObservableList = FXCollections.observableArrayList();

        coursesAdapter = new CoursesAdapter();

        //Add click listener to the add course button
        addCourseButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/courses/Add.fxml"));
            NavigationHelper.changeTitle("Add Course");
        });

        //View units action handler
        viewUnits.setOnAction(event -> {
            Course course = coursesTable.getSelectionModel().getSelectedItem();

            if (course == null) {
                AlertHelper.showErrorAlert("Viewing Units", "Select a course to view the units please");
                return;
            }

            NavigationHelper.swapToBrowseUnits(getClass().getResource("/layouts/units/Browse.fxml"), course);

        });

        deleteCourse.setOnAction(actionEvent -> {

            Course course = coursesTable.getSelectionModel().getSelectedItem();

            if (course == null) {
                AlertHelper.showErrorAlert("Viewing Units", "Select a course to view the units please");
                return;
            }

            //Delete the course
            if (coursesAdapter.deleteCourseById(course.getId())) {
                //Delete course
                AlertHelper.showSuccessAlert("Deleting Courses", "Course successfully deleted");

                coursesObservableList.remove(course);

                coursesTable.refresh();
            } else {

                AlertHelper.showErrorAlert("Deleting Courses", "Course deletion failed for some reason check the logs to fix that");
            }
        });

        initCols();

        populateCourses();
    }

    private void initCols() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        aliasCol.setCellValueFactory(new PropertyValueFactory<>("alias"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void populateCourses() {
        coursesObservableList.clear();

        try {

            ResultSet rs = coursesAdapter.getAll();

            while (rs.next()) {
                int id = rs.getInt("id");
                String alias = rs.getString("alias");
                String name = rs.getString("name");

                coursesObservableList.add(new Course(id, alias, name));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        coursesTable.setItems(coursesObservableList);

        updateCourse.setOnAction(actionEvent -> {
            Course course = coursesTable.getSelectionModel().getSelectedItem();

            assert course != null;

            NavigationHelper.swapToEditCourse(getClass().getResource("/layouts/courses/Edit.fxml"), course);

        });
    }


    public static class Course {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty alias;
        private final SimpleStringProperty name;

        public Course(int id, String alias, String name) {
            this.id = new SimpleIntegerProperty(id);
            this.alias = new SimpleStringProperty(alias);
            this.name = new SimpleStringProperty(name);
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public void setAlias(String alias) {
            this.alias.set(alias);
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public int getId() {
            return id.get();
        }

        public String getAlias() {
            return alias.get();
        }

        public String getName() {
            return name.get();
        }
    }
}
