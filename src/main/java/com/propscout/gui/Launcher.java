package com.propscout.gui;

import com.propscout.data.adapters.AbstractDatabaseAdapter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/layouts/auth/Login.fxml"));

        primaryStage.setTitle("Login - Attendance Software Jfx");
        primaryStage.setScene(new Scene(root));

        new Thread(AbstractDatabaseAdapter.DatabaseHandler::getInstance).start();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
