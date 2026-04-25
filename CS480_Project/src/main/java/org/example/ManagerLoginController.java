package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ManagerLoginController {

    @FXML private TextField ssnField;
    @FXML private Label status;

    private Stage stage() {
        return (Stage) status.getScene().getWindow();
    }

    @FXML
    public void login() {
        try {
            int ssn = Integer.parseInt(ssnField.getText());

            if (Manager.login(ssn)) {
                SceneUtil.switchScene(stage(), "manager_dashboard.fxml", 500, 500);
            } else {
                status.setText("Not found");
            }

        } catch (Exception e) {
            status.setText("Invalid SSN");
        }
    }

    @FXML
    public void goRegister() {
        SceneUtil.switchScene(stage(), "manager_register.fxml", 400, 300);
    }

    @FXML
    public void back() {
        SceneUtil.switchScene(stage(), "main.fxml", 500, 350);
    }
}