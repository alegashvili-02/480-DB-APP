package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClientUpdateController {

    @FXML private TextField name;
    @FXML private TextField city;
    @FXML private TextField street;
    @FXML private TextField number;
    @FXML private TextField card;

    @FXML private Label status;

    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    private Stage stage() {
        return (Stage) status.getScene().getWindow();
    }

    @FXML
    public void update() {
        try {
            boolean ok = Client.updateAll(
                    email,
                    name.getText(),
                    city.getText(),
                    street.getText(),
                    Integer.parseInt(number.getText()),
                    card.getText()
            );

            status.setText(ok ? "Updated" : "Failed");

        } catch (Exception e) {
            status.setText("Invalid input");
        }
    }

    @FXML
    public void back() {
        ClientDashboardController controller =
                SceneUtil.switchScene(stage(), "client_dashboard.fxml", 500, 350);

        if (controller != null) {
            controller.setEmail(email);
        }
    }
}