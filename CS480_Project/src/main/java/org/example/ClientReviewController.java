package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ClientReviewController {

    @FXML private TextField hotelId;
    @FXML private TextField rating;
    @FXML private TextField message;
    @FXML private Label status;

    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    private Stage stage() {
        return (Stage) status.getScene().getWindow();
    }

    @FXML
    public void submit() {

        try {

            int hId = Integer.parseInt(hotelId.getText());
            int rate = Integer.parseInt(rating.getText());

            boolean ok = Client.submitReview(
                    email,
                    hId,
                    rate,
                    message.getText()
            );

            if (ok) {
                status.setText("Review submitted");
            } else {
                status.setText("You must stay in this hotel first");
            }

        } catch (Exception e) {
            status.setText("Invalid input");
        }
    }

    @FXML
    public void back() {
        ClientDashboardController c =
                SceneUtil.switchScene(stage(), "client_dashboard.fxml", 500, 500);

        if (c != null) c.setEmail(email);
    }
}