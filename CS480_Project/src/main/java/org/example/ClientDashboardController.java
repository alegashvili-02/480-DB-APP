package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ClientDashboardController {

    @FXML private Label status;

    private String currentEmail;

    public void setEmail(String email) {
        this.currentEmail = email;
        status.setText("Welcome " + email);
    }

    private Stage stage() {
        return (Stage) status.getScene().getWindow();
    }

    @FXML
    public void updateInfo() {

        ClientUpdateController controller =
                SceneUtil.switchScene(stage(), "client_update.fxml", 500, 400);

        if (controller != null) {
            controller.setEmail(currentEmail);
        }
    }

    @FXML
    public void logout() {
        SceneUtil.switchScene(stage(), "main.fxml", 500, 350);
    }
}