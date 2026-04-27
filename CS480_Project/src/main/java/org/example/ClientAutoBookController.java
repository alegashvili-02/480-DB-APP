package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class ClientAutoBookController {

    @FXML private TextField hotelName;
    @FXML private TextField startDate;
    @FXML private TextField endDate;
    @FXML private TextArea resultArea;

    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    private Stage stage() {
        return (Stage) resultArea.getScene().getWindow();
    }

    @FXML
    public void autoBook() {
        try {
            Date start = Date.valueOf(startDate.getText());
            Date end = Date.valueOf(endDate.getText());

            String result = Client.autoBook(
                    email,
                    hotelName.getText(),
                    start,
                    end
            );

            resultArea.setText(result);

        } catch (Exception e) {
            resultArea.setText("Use format: YYYY-MM-DD");
        }
    }

    @FXML
    public void back() {
        ClientDashboardController c =
                SceneUtil.switchScene(stage(), "client_dashboard.fxml", 500, 500);

        if (c != null) c.setEmail(email);
    }
}