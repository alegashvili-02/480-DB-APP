package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;

public class MainController {

    @FXML
    private Label dbStatus;

    @FXML
    public void testDB() {
        try (Connection conn = DBConnection.getConnection()) {
            dbStatus.setText("DB Connected");
        } catch (Exception e) {
            dbStatus.setText("Failed");
        }
    }

    @FXML
    public void goClient() {
        Stage stage = (Stage) dbStatus.getScene().getWindow();
        SceneUtil.switchScene(stage, "client_login.fxml", 400, 300);
    }

    @FXML
    public void goManager() {
        Stage stage = (Stage) dbStatus.getScene().getWindow();
        SceneUtil.switchScene(stage, "manager_login.fxml", 400, 300);
    }
}