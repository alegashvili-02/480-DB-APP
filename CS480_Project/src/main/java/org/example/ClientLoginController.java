package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClientLoginController {

    @FXML private TextField emailField;
    @FXML private Label status;

    private Stage stage() {
        return (Stage) status.getScene().getWindow();
    }

    @FXML
    public void login() {

        String email = emailField.getText();

        if (Client.login(email)) {

            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/client_dashboard.fxml")
                );

                Parent root = loader.load();

                // get controller
                ClientDashboardController controller = loader.getController();
                controller.setEmail(email);

                stage().setScene(new Scene(root, 400, 300));

            } catch (Exception e) {
                e.printStackTrace();
                status.setText("Load error");
            }

        } else {
            status.setText("Login failed");
        }
    }

    @FXML
    public void goRegister() {
        SceneUtil.switchScene(stage(), "client_register.fxml", 500, 400);
    }

    @FXML
    public void back() {
        SceneUtil.switchScene(stage(), "main.fxml", 500, 350);
    }
}