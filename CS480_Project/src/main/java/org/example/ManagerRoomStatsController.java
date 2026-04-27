package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.ResultSet;

public class ManagerRoomStatsController {

    @FXML private TextArea resultArea;

    private int ssn;

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    private Stage stage() {
        return (Stage) resultArea.getScene().getWindow();
    }

    @FXML
    public void load() {
        try {
            ResultSet rs = Manager.getRoomBookingCounts();

            StringBuilder sb = new StringBuilder();

            while (rs != null && rs.next()) {
                sb.append("HotelID: ")
                        .append(rs.getInt("HotelID"))
                        .append(" | Room: ")
                        .append(rs.getInt("RoomNum"))
                        .append(" | Bookings: ")
                        .append(rs.getInt("booking_count"))
                        .append("\n");
            }

            resultArea.setText(sb.length() == 0 ? "No data" : sb.toString());

        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void back() {
        ManagerDashboardController c =
                SceneUtil.switchScene(stage(), "manager_dashboard.fxml", 750, 500);

        if (c != null) c.setSsn(ssn);
    }
}