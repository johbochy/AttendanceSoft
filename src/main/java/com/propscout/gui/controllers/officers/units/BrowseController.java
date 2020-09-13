package com.propscout.gui.controllers.officers.units;

import com.propscout.data.adapters.UnitsAdapter;
import com.propscout.data.models.User;
import com.propscout.gui.helpers.AlertHelper;
import com.propscout.gui.helpers.NavigationHelper;
import com.propscout.utils.AuthHelper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    public TableView<Unit> unitsTable;
    public TableColumn<Unit, Integer> idCol;
    public TableColumn<Unit, String> codeCol;
    public TableColumn<Unit, String> nameCol;
    public TableColumn<Unit, Integer> yearCol;
    public TableColumn<Unit, Integer> semesterCol;
    public TableColumn<Unit, String> courseCol;
    public MenuItem viewAttendanceSheet;

    private User user;

    private UnitsAdapter unitsAdapter;
    private ObservableList<Unit> unitsObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        unitsAdapter = new UnitsAdapter();

        unitsObservableList = FXCollections.observableArrayList();

        if (AuthHelper.isLoggedIn()) {
            user = AuthHelper.getLoggedInUser();

            populateUnits();
        }

        initCols();

        viewAttendanceSheet.setOnAction(actionEvent -> {

            //Get the selected unit
            Unit unit = unitsTable.getSelectionModel().getSelectedItem();

            if (unit == null) {
                AlertHelper.showErrorAlert("View Attendance Sheet", "Select A Unit to View Attendance Sheet");
                return;
            }
            //Swap the content to show attendance sheet for the select unit
            NavigationHelper.swapFromOfficerUnitToAttendanceSheet(getClass().getResource("/layouts/officers/attendance/Sheet.fxml"), unit);

        });
    }

    private void populateUnits() {
        unitsObservableList.clear();

        assert user != null;

        ResultSet rs = unitsAdapter.getUnitsByUserId(user.getId());

        try {

            while (rs.next()) {
                int id = rs.getInt("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                int year = rs.getInt("year");
                int semester = rs.getInt("semester");
                String course = rs.getString("course");

                unitsObservableList.add(new Unit(id, code, name, year, semester, course));
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
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));
    }


    public static class Unit {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty code;
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty year;
        private final SimpleIntegerProperty semester;
        private final SimpleStringProperty course;


        public Unit(int id, String code, String name, int year, int semester, String course) {

            this.id = new SimpleIntegerProperty(id);
            this.code = new SimpleStringProperty(code);
            this.name = new SimpleStringProperty(name);
            this.year = new SimpleIntegerProperty(year);
            this.semester = new SimpleIntegerProperty(semester);
            this.course = new SimpleStringProperty(course);
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

        public String getCourse() {
            return course.get();
        }
    }
}
