package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClientBookingsController {

    @FXML private TextArea resultArea;

    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    private Stage stage() {
        return (Stage) resultArea.getScene().getWindow();
    }

    @FXML
    public void load() {

        try {

            ResultSet rs = Client.getBookings(email);

            StringBuilder sb = new StringBuilder();

            while (rs != null && rs.next()) {
                sb.append("Hotel: ")
                        .append(rs.getString("HotelName"))
                        .append(" | Room: ")
                        .append(rs.getInt("RoomNum"))
                        .append(" | TotalPrice: ")
                        .append(rs.getInt("TotalPrice"))
                        .append(" | ")
                        .append(rs.getDate("StartDate"))
                        .append(" → ")
                        .append(rs.getDate("EndDate"))
                        .append("\n");
            }

            resultArea.setText(sb.length() == 0
                    ? "No bookings found"
                    : sb.toString());

        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void back() {
        ClientDashboardController c =
                SceneUtil.switchScene(stage(), "client_dashboard.fxml", 500, 500);

        if (c != null) c.setEmail(email);
    }
}