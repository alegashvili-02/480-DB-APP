package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.ResultSet;

public class ClientSearchController {

    @FXML private TextField startDate;
    @FXML private TextField endDate;
    @FXML private TextArea resultArea;


    @FXML private TextField hotelIdField;
    @FXML private TextField roomNumField;

    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    private Stage stage() {
        return (Stage) resultArea.getScene().getWindow();
    }

    // ================= SEARCH =================
    @FXML
    public void search() {
        try {
            Date start = Date.valueOf(startDate.getText());
            Date end = Date.valueOf(endDate.getText());

            ResultSet rs = Client.searchAvailableRooms(start, end);

            StringBuilder sb = new StringBuilder();

            while (rs.next()) {
                sb.append("Hotel: ")
                        .append(rs.getString("Name"))
                        .append(" | HotelID: ")
                        .append(rs.getInt("HotelID"))
                        .append(" | Room: ")
                        .append(rs.getInt("RoomNum"))
                        .append("\n");
            }

            if (sb.length() == 0) {
                resultArea.setText("No available rooms");
            } else {
                resultArea.setText(sb.toString());
            }

        } catch (Exception e) {
            resultArea.setText("Use format: YYYY-MM-DD");
        }
    }

    // ================= BOOK =================
    @FXML
    public void book() {
        try {
            int hotelId = Integer.parseInt(hotelIdField.getText());
            int roomNum = Integer.parseInt(roomNumField.getText());

            Date start = Date.valueOf(startDate.getText());
            Date end = Date.valueOf(endDate.getText());

            boolean ok = Client.bookRoom(email, hotelId, roomNum, start, end);

            if (ok) {
                resultArea.setText("Booking successful");
            } else {
                resultArea.setText("Room not available");
            }

        } catch (Exception e) {
            resultArea.setText("Invalid input");
        }
    }

    // ================= BACK =================
    @FXML
    public void back() {

        ClientDashboardController c =
                SceneUtil.switchScene(stage(), "client_dashboard.fxml", 500, 500);

        if (c != null) {
            c.setEmail(email);
        }
    }
}