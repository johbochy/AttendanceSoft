package com.propscout.gui.controllers.officers.attendance;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Table;
import com.propscout.data.adapters.AttendanceAdapter;
import com.propscout.data.adapters.AttendanceSheetAdapter;
import com.propscout.data.adapters.SettingsAdapter;
import com.propscout.data.models.AttendanceInfo;
import com.propscout.data.models.Settings;
import com.propscout.data.models.TableAttendanceInfo;
import com.propscout.data.models.UnitStudent;
import com.propscout.gui.controllers.officers.units.BrowseController;
import com.propscout.gui.helpers.AlertHelper;
import com.propscout.internals.printing.TablePrinter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AttendanceSheetController implements Initializable {

    @FXML
    private Button printSheetButton;
    @FXML
    private TableView<TableAttendanceInfo> attendanceSheetTable;
    @FXML
    private TableColumn<TableAttendanceInfo, String> studentNameCol;
    @FXML
    private TableColumn<TableAttendanceInfo, String> week1Col;
    @FXML
    private TableColumn<TableAttendanceInfo, String> week2Col;
    @FXML
    private TableColumn<TableAttendanceInfo, String> week3Col;
    @FXML
    private TableColumn<TableAttendanceInfo, String> week4Col;
    @FXML
    private TableColumn<TableAttendanceInfo, String> week5Col;

    private BrowseController.Unit unit;

    private Executor executor;
    private Settings settings;

    private SettingsAdapter settingsAdapter;
    private AttendanceSheetAdapter attendanceSheetAdapter;
    private AttendanceAdapter attendanceAdapter;

    private ObservableList<TableAttendanceInfo> tableAttendanceInfoList = FXCollections.observableArrayList();

    private List<AttendanceInfo> attendanceInfoList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Initialize Adapters
        settingsAdapter = new SettingsAdapter();
        attendanceSheetAdapter = new AttendanceSheetAdapter();
        attendanceAdapter = new AttendanceAdapter();

        //Initialize the executor
        executor = Executors.newCachedThreadPool(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });

        //Disable printing because not information yet
        printSheetButton.setDisable(true);

        //Perform the printing logic
        printSheetButton.setOnAction(actionEvent -> {

            try {

                //Choose a directory
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Select Location For the Report File");
                directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
                final File selectedDir = directoryChooser.showDialog(attendanceSheetTable.getScene().getWindow());

                //Dynamic filename
                String filename = selectedDir.getAbsolutePath() + "/" + unit.getCode().toLowerCase().replace(" ", "-").concat("-attendance-report.pdf");

                //Create a instance of Table printer passing the filename
                TablePrinter tablePrinter = new TablePrinter(filename);

                tablePrinter.initDocument(20, PageSize.A4.rotate());

                Table table = new Table(6);

                //Add the headers
                table.addHeaderCell("Name");
                table.addHeaderCell("Week 1");
                table.addHeaderCell("Week 2");
                table.addHeaderCell("Week 3");
                table.addHeaderCell("Week 4");
                table.addHeaderCell("Week 5");

                tableAttendanceInfoList.forEach(tableAttendanceInfo -> {
                    table.addCell(tableAttendanceInfo.getName());
                    table.addCell(tableAttendanceInfo.getWeek1());
                    table.addCell(tableAttendanceInfo.getWeek2());
                    table.addCell(tableAttendanceInfo.getWeek3());
                    table.addCell(tableAttendanceInfo.getWeek4());
                    table.addCell(tableAttendanceInfo.getWeek5());

                });

                tablePrinter.addBlockElement(table);

                tablePrinter.print();
                System.out.println("Attendance sheet printed successfully");

                AlertHelper.showSuccessAlert("Printing Attendance Sheet", "Report printed Successfully");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Printing failed");
            }
        });

        requestSettings();

        initTable();
    }

    private void initTable() {
        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        week1Col.setCellValueFactory(new PropertyValueFactory<>("week1"));
        week2Col.setCellValueFactory(new PropertyValueFactory<>("week2"));
        week3Col.setCellValueFactory(new PropertyValueFactory<>("week3"));
        week4Col.setCellValueFactory(new PropertyValueFactory<>("week4"));
        week5Col.setCellValueFactory(new PropertyValueFactory<>("week5"));
    }

    private void requestSettings() {

        //Request settings from the db
        Task<Settings> settingsTask = new Task<>() {
            @Override
            protected Settings call() throws Exception {

                final Optional<Settings> settingsOptional = settingsAdapter.findById(1);

                settingsOptional.orElseThrow(() -> new Exception("Admin has not initialized settings"));

                return settingsOptional.get();

            }
        };

        settingsTask.setOnFailed(workerStateEvent -> {
            System.out.println(settingsTask.getException().getLocalizedMessage());
            settingsTask.getException().printStackTrace();
        });

        settingsTask.setOnSucceeded(workerStateEvent -> {
            try {
                settings = settingsTask.get();

                //JUST FOR DEBUGGING
                //System.out.println(settings);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getLocalizedMessage());
                e.printStackTrace();
            }
        });

        executor.execute(settingsTask);
    }

    public void setUnit(BrowseController.Unit unit) {
        this.unit = unit;

        getStudentsForTheUnit();

        getAttendanceInformation();
    }

    private void getStudentsForTheUnit() {
        ObservableList<UnitStudent> unitStudents = attendanceAdapter.getStudentsByUnitCode(unit.getCode());

        //Populate names of the students to the table
        unitStudents.forEach(unitStudent -> {

            //Populate empty information  for attendance at first
            tableAttendanceInfoList.add(new TableAttendanceInfo(unitStudent.getName(), "", "", "", "", ""));
        });
    }

    private void getAttendanceInformation() {

        //Request For Attendance Information and Parse It Accordingly
        Task<List<AttendanceInfo>> attendanceInfoListATask = new Task<>() {
            @Override
            protected List<AttendanceInfo> call() throws Exception {
                return attendanceSheetAdapter.getAllAttendanceInformation(unit.getId());
            }
        };

        attendanceInfoListATask.setOnFailed(workerStateEvent -> {
            System.out.println("Error: " + attendanceInfoListATask.getException().getLocalizedMessage());
            attendanceInfoListATask.getException().printStackTrace();
        });

        attendanceInfoListATask.setOnSucceeded(workerStateEvent -> {
            //Process the List
            try {
                //Print the list of the attendance Information first
                attendanceInfoList = attendanceInfoListATask.get();

                //FOR DEBUGGING
                //attendanceInfoList.forEach(System.out::println);

                attendanceInfoList.forEach(attendanceInfo -> {

                    if (settings != null) {
                        int daysSinceSemesterStart = attendanceInfo.getCommencedAt().getDayOfYear() - settings.getCommenceDate().getDayOfYear();
                        int week = (daysSinceSemesterStart / 7) + 1;

                        //Search for the student and change attendance
                        tableAttendanceInfoList.parallelStream().filter(tableAttendanceInfo -> {
                            //Comparing the names
                            return tableAttendanceInfo.getName().equalsIgnoreCase(attendanceInfo.getName());
                        }).findFirst().ifPresentOrElse(tableAttendanceInfo -> {

                            switch (week) {
                                case 1:
                                    tableAttendanceInfo.setWeek1("1");
                                    break;
                                case 2:
                                    tableAttendanceInfo.setWeek2("1");
                                    break;
                                case 3:
                                    tableAttendanceInfo.setWeek3("1");
                                    break;
                                case 4:
                                    tableAttendanceInfo.setWeek4("1");
                                    break;
                                case 5:
                                    tableAttendanceInfo.setWeek5("1");
                                    break;
                                default:
                                    System.out.println("Extend the table please");
                            }

                        }, () -> System.err.println("Error: The student not found in the list of the onces taking the unit"));

                    }
                });

                //Enable printing after loading all attendance information
                printSheetButton.setDisable(false);

                attendanceSheetTable.setItems(tableAttendanceInfoList);

            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Error: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        });

        executor.execute(attendanceInfoListATask);
    }
}
