package com.propscout.gui.controllers.students;

import com.propscout.data.adapters.StudentsAdapter;
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

public class BrowseController implements Initializable {

    @FXML
    public Button registerStudentButton;

    @FXML
    public TableView<Student> studentsTable;
    @FXML
    public TableColumn<Student, Integer> idCol;
    @FXML
    public TableColumn<Student, String> regNoCol;
    @FXML
    public TableColumn<Student, String> nameCol;
    @FXML
    public TableColumn<Student, String> mobileCol;
    @FXML
    public TableColumn<Student, Integer> yearCol;
    @FXML
    public TableColumn<Student, Integer> semesterCol;

    @FXML
    private MenuItem editStudentMenuItem;
    @FXML
    private MenuItem deleteStudentMenuItem;

    //Students Adapter
    private StudentsAdapter studentsAdapter;

    //Students observation list
    private ObservableList<Student> studentsObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        studentsAdapter = new StudentsAdapter();

        studentsObservableList = FXCollections.observableArrayList();

        registerStudentButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/students/Register.fxml"));
            NavigationHelper.changeTitle("Register Student");
        });

        initCols();

        populateStudents();

        editStudentMenuItem.setOnAction(actionEvent -> {

            //Get student to edit
            Student student = studentsTable.getSelectionModel().getSelectedItem();

            //Check if a student is selected
            if (student == null) {
                AlertHelper.showErrorAlert("Editing Student", "Please select a student to edit");
                return;
            }

            //Navigate to edit the student details
            NavigationHelper.swapToEditStudent(getClass().getResource("/layouts/students/EditStudentDetails.fxml"), student);
        });

        deleteStudentMenuItem.setOnAction(actionEvent -> {
            //Get student to edit
            Student student = studentsTable.getSelectionModel().getSelectedItem();


            //Check if a student is selected
            if (student == null) {
                AlertHelper.showErrorAlert("Deleting Student", "Please select a student to delete");
                return;
            }

            //Delete the student details
            if (studentsAdapter.deleteById(student.getId())) {
                AlertHelper.showSuccessAlert("Deleting Student", "Student deleted successfully");

                studentsTable.getItems().remove(student);

                studentsTable.refresh();

            } else {
                AlertHelper.showErrorAlert("Deleting Student", "Student deletion failed, check the logs for more information");
            }

        });

    }

    /**
     * Clear the current list of students showing on the table, populate the list and show it well in the table
     */
    private void populateStudents() {
        studentsObservableList.clear();

        try (
                ResultSet rs = studentsAdapter.getAll()
        ) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String regNo = rs.getString("reg_no");
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");
                int year = rs.getInt("year");
                int semester = rs.getInt("semester");

                studentsObservableList.add(new Student(id, regNo, name, mobile, year, semester));
            }

            studentsTable.setItems(studentsObservableList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initCols() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        regNoCol.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
    }


    public static class Student {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty regNo;
        private final SimpleStringProperty name;
        private final SimpleStringProperty mobile;
        private final SimpleIntegerProperty year;
        private final SimpleIntegerProperty semester;

        public Student(int id, String regNo, String name, String mobile, int year, int semester) {
            this.id = new SimpleIntegerProperty(id);
            this.regNo = new SimpleStringProperty(regNo);
            this.name = new SimpleStringProperty(name);
            this.mobile = new SimpleStringProperty(mobile);
            this.year = new SimpleIntegerProperty(year);
            this.semester = new SimpleIntegerProperty(semester);
        }

        public int getId() {
            return id.get();
        }

        public String getRegNo() {
            return regNo.get();
        }


        public String getName() {
            return name.get();
        }

        public String getMobile() {
            return mobile.get();
        }

        public int getYear() {
            return year.get();
        }

        public int getSemester() {
            return semester.get();
        }
    }
}
