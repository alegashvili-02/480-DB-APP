package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClientRegisterController {

    @FXML private TextField email;
    @FXML private TextField name;
    @FXML private TextField city;
    @FXML private TextField street;
    @FXML private TextField number;
    @FXML private TextField card;
    @FXML private Label status;

    private Stage stage() {
        return (Stage) status.getScene().getWindow();
    }

    @FXML
    public void register() {
        try {
            boolean ok = Client.registerFull(
                    email.getText(),
                    name.getText(),
                    city.getText(),
                    street.getText(),
                    Integer.parseInt(number.getText()),
                    card.getText(),
                    city.getText(),
                    street.getText(),
                    Integer.parseInt(number.getText())
            );

            status.setText(ok ? "Registered" : "Failed");

        } catch (Exception e) {
            status.setText("Invalid input");
        }
    }

    @FXML
    public void back() {
        SceneUtil.switchScene(stage(), "client_login.fxml", 400, 300);
    }
}