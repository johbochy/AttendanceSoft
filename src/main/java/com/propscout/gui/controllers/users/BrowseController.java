package com.propscout.gui.controllers.users;

import com.propscout.data.adapters.UsersAdapter;
import com.propscout.data.models.User;
import com.propscout.gui.helpers.NavigationHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Handles showing all the added users of the system
 */
public class BrowseController implements Initializable {

    @FXML
    public Button addUserButton;

    @FXML
    public ListView<User> usersListView;

    @FXML
    private Button refreshUsersButton;

    private UsersAdapter usersAdapter;

    private ObservableList<User> usersObservableList;
    private Executor executor;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Set cell factory for the ListView
        usersListView.setCellFactory(param -> new Cell());

        usersAdapter = new UsersAdapter();

        usersObservableList = FXCollections.observableArrayList();

        //Long task executor
        executor = Executors.newCachedThreadPool(runnable -> {

            Thread thread = new Thread(runnable);
            thread.setDaemon(true);

            return thread;
        });


        //Add User Button Action Handler
        addUserButton.setOnAction(event -> {
            NavigationHelper.swapContent(getClass().getResource("/layouts/users/Add.fxml"));
            NavigationHelper.changeTitle("Add User");
        });

        refreshUsersButton.setOnAction(actionEvent -> {
            //Refresh the users
            populateUsers();
        });

        populateUsers();

    }

    private void populateUsers() {
        usersObservableList.clear();

        ResultSet rs = usersAdapter.getAllUsers();

        while (true) {
            try {
                if (!rs.next()) break;

                int id = rs.getInt("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String mobile = rs.getString("mobile");
                String email = rs.getString("email");
                String role = rs.getString("role");
                String password = rs.getString("password");
                String avatar = rs.getString("avatar");

                usersObservableList.add(new User(id, name, username, email, mobile, role, avatar, null, password, 0, null, null));

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        usersListView.setItems(usersObservableList);
    }
}
