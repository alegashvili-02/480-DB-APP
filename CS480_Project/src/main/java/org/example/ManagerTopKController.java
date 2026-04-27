package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;

public class ManagerTopKController {

    @FXML private TextField kField;
    @FXML private TextArea resultArea;

    private int ssn;

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    private Stage stage() {
        return (Stage) resultArea.getScene().getWindow();
    }

    @FXML
    public void search() {
        try {
            int k = Integer.parseInt(kField.getText());

            ResultSet rs = Manager.topKClients(k);

            StringBuilder sb = new StringBuilder();

            while (rs != null && rs.next()) {
                sb.append("Email: ")
                        .append(rs.getString("Email"))
                        .append(" | Name: ")
                        .append(rs.getString("Name"))
                        .append(" | Bookings: ")
                        .append(rs.getInt("booking_count"))
                        .append("\n");
            }

            resultArea.setText(sb.length() == 0 ? "No data" : sb.toString());

        } catch (Exception e) {
            resultArea.setText("Invalid input");
        }
    }

    @FXML
    public void back() {
        ManagerDashboardController c =
                SceneUtil.switchScene(stage(), "manager_dashboard.fxml", 750, 500);

        if (c != null) c.setSsn(ssn);
    }
}