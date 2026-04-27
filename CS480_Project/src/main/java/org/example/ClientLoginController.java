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

                Scene scene = new Scene(root, 500, 500);
                scene.getStylesheets().add(
                        getClass().getResource("/style.css").toExternalForm()
                );
                stage().setScene(scene);

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
        SceneUtil.switchScene(stage(), "client_register.fxml", 500, 500);
    }

    @FXML
    public void back() {
        SceneUtil.switchScene(stage(), "main.fxml", 500, 500);
    }
}