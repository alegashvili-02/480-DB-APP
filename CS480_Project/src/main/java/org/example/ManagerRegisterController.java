package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ManagerRegisterController {

    @FXML private TextField ssn;
    @FXML private TextField name;
    @FXML private TextField email;
    @FXML private Label status;

    private Stage stage() {
        return (Stage) status.getScene().getWindow();
    }

    @FXML
    public void register() {
        try {
            boolean ok = Manager.register(
                    Integer.parseInt(ssn.getText()),
                    name.getText(),
                    email.getText()
            );

            status.setText(ok ? "Registered" : "Failed");

        } catch (Exception e) {
            status.setText("Invalid input");
        }
    }

    @FXML
    public void back() {
        SceneUtil.switchScene(stage(), "manager_login.fxml", 400, 300);
    }
}