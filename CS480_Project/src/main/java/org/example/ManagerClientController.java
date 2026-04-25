package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ManagerClientController {

    @FXML private TextField emailField;
    @FXML private Label status;


    private int ssn;

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    private Stage stage() {
        return (Stage) status.getScene().getWindow();
    }


    @FXML
    public void delete() {
        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            String email = emailField.getText();

            // delete child tables first
            PreparedStatement ps1 = conn.prepareStatement(
                    "DELETE FROM ClientAddress WHERE Email=?");
            ps1.setString(1, email);
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                    "DELETE FROM CreditCard WHERE Email=?");
            ps2.setString(1, email);
            ps2.executeUpdate();

            PreparedStatement ps3 = conn.prepareStatement(
                    "DELETE FROM Review WHERE Email=?");
            ps3.setString(1, email);
            ps3.executeUpdate();

            PreparedStatement ps4 = conn.prepareStatement(
                    "DELETE FROM Booking WHERE Email=?");
            ps4.setString(1, email);
            ps4.executeUpdate();

            //parent last
            PreparedStatement ps5 = conn.prepareStatement(
                    "DELETE FROM Client WHERE Email=?");
            ps5.setString(1, email);

            int rows = ps5.executeUpdate();

            conn.commit();

            if (rows > 0)
                status.setText("Client removed");
            else
                status.setText("Client not found");

        } catch (Exception e) {
            e.printStackTrace();
            status.setText(e.getMessage());
        }
    }

    @FXML
    public void back() {
        ManagerDashboardController c =
                SceneUtil.switchScene(stage(), "manager_dashboard.fxml", 500, 500);
        if (c != null) c.setSsn(ssn);
    }
}