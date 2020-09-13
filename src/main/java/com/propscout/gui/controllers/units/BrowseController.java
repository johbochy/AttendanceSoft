package com.propscout.gui.controllers.units;

import com.propscout.data.adapters.UnitsAdapter;
import com.propscout.gui.controllers.courses.BrowseController.Course;
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
 * Browse Units that belong to a single course
 */
public class BrowseController implements Initializable {

    @FXML
    private Button refreshUnitsButton;
    @FXML
    private Button addUnitButton;

    @FXML
    private TableView<Unit> unitsTable;
    @FXML
    private TableColumn<Unit, Integer> idCol;
    @FXML
    private TableColumn<Unit, String> codeCol;
    @FXML
    private TableColumn<Unit, String> nameCol;
    @FXML
    private TableColumn<Unit, Integer> yearCol;
    @FXML
    private TableColumn<Unit, Integer> semesterCol;
    @FXML
    private TableColumn<Unit, Integer> lecturerCol;

    @FXML
    public MenuItem editUnit;

    @FXML
    public MenuItem deleteUnit;

    //The course that we are checking the units

    private Course course;

    private UnitsAdapter unitsAdapter;
    private ObservableList<Unit> unitsObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
         * Initialize the unit adapter
         */
        unitsAdapter = new UnitsAdapter();

        unitsObservableList = FXCollections.observableArrayList();

        addUnitButton.setOnAction(event -> {
            //Navigate to add unit screen
            NavigationHelper.swapToAddUnits(getClass().getResource("/layouts/units/Add.fxml"), course);
        });

        refreshUnitsButton.setOnAction(actionEvent -> {
            //Refresh units
            populateUnits();
            unitsTable.refresh();
        });

        editUnit.setOnAction(actionEvent -> {
            //Navigate to edit unit screen

            Unit unit = unitsTable.getSelectionModel().getSelectedItem();

            if (unit == null) {
                AlertHelper.showErrorAlert("Editing Unit", "Please select some unt to edit");
                return;

            }

            //Navigate to the window for editing units and pass the unit there
            NavigationHelper.swapToEditUnit(
                    getClass().getResource("/layouts/units/Edit.fxml"),
                    new com.propscout.data.models.Unit(
                            unit.getId(),
                            course.getId(),
                            0,
                            unit.getName(),
                            unit.getCode(),
                            unit.getYear(),
                            unit.getSemester()
                    ));
        });

        deleteUnit.setOnAction(actionEvent -> {

            //Delete the actual unit from the database

            Unit unit = unitsTable.getSelectionModel().getSelectedItem();

            if (unit == null) {
                AlertHelper.showErrorAlert("Editing Unit", "Please select some unit to edit");
                return;

            }

            if (unitsAdapter.deleteById(unit.getId())) {
                AlertHelper.showSuccessAlert("Deleting Unit", "The unit was deleted successfully");
            } else {
                AlertHelper.showErrorAlert("Deleting UNit", "The operation failed terribly check the logs for insights");
            }

        });
        initCols();
    }

    private void populateUnits() {
        unitsObservableList.clear();

        ResultSet rs = unitsAdapter.getUnitsByCourseId(course.getId());

        try {

            while (rs.next()) {
                int id = rs.getInt("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                String lecturer = rs.getString("lecturer");
                int year = rs.getInt("year");
                int semester = rs.getInt("semester");

                unitsObservableList.add(new Unit(id, code, name, year, semester, lecturer));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        unitsTable.setItems(unitsObservableList);
    }

    private void initCols() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        lecturerCol.setCellValueFactory(new PropertyValueFactory<>("lecturer"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
    }

    public void setCourse(Course course) {
        this.course = course;

        populateUnits();
    }

    /**
     * Unit property binding class
     */
    public static class Unit {

        private final SimpleIntegerProperty id;
        private final SimpleStringProperty code;
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty year;
        private final SimpleIntegerProperty semester;
        private final SimpleStringProperty lecturer;

        public Unit(int id, String code, String name, int year, int semester, String lecturer) {

            this.id = new SimpleIntegerProperty(id);
            this.code = new SimpleStringProperty(code);
            this.name = new SimpleStringProperty(name);
            this.year = new SimpleIntegerProperty(year);
            this.semester = new SimpleIntegerProperty(semester);
            this.lecturer = new SimpleStringProperty(lecturer);
        }

        public int getId() {
            return id.get();
        }

        public String getCode() {
            return code.get();
        }

        public String getName() {
            return name.get();
        }

        public int getYear() {
            return year.get();
        }

        public int getSemester() {
            return semester.get();
        }

        public String getLecturer() {
            return lecturer.get();
        }
    }
}
