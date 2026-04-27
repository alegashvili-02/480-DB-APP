package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ManagerHotelController {

    @FXML private TextField hotelId;
    @FXML private TextField name;
    @FXML private TextField city;
    @FXML private TextField street;
    @FXML private TextField number;
    @FXML private Label status;

    private int ssn;

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    private Stage stage() {
        return (Stage) status.getScene().getWindow();
    }

    @FXML
    public void insert() {
        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            String c = city.getText();
            String s = street.getText();
            int n = Integer.parseInt(number.getText());

            // insert address first
            PreparedStatement addr = conn.prepareStatement(
                    "INSERT INTO Address (City, StreetName, Number) VALUES (?, ?, ?) ON CONFLICT DO NOTHING");
            addr.setString(1, c);
            addr.setString(2, s);
            addr.setInt(3, n);
            addr.executeUpdate();

            // insert hotel
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Hotel VALUES (?, ?, ?, ?, ?)");

            ps.setInt(1, Integer.parseInt(hotelId.getText()));
            ps.setString(2, c);
            ps.setString(3, s);
            ps.setInt(4, n);
            ps.setString(5, name.getText());

            ps.executeUpdate();

            conn.commit();
            status.setText("Hotel inserted");

        } catch (Exception e) {
            e.printStackTrace();
            status.setText(e.getMessage());
        }
    }

    @FXML
    public void update() {
        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Hotel SET Name=?, City=?, StreetName=?, Number=? WHERE HotelID=?");

            ps.setString(1, name.getText());
            ps.setString(2, city.getText());
            ps.setString(3, street.getText());
            ps.setInt(4, Integer.parseInt(number.getText()));
            ps.setInt(5, Integer.parseInt(hotelId.getText()));

            ps.executeUpdate();
            status.setText("Updated");

        } catch (Exception e) {
            status.setText("Error");
        }
    }

    @FXML
    public void delete() {
        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            int id = Integer.parseInt(hotelId.getText());

            PreparedStatement ps1 = conn.prepareStatement(
                    "DELETE FROM Room WHERE HotelID=?");
            ps1.setInt(1, id);
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement(
                    "DELETE FROM Hotel WHERE HotelID=?");
            ps2.setInt(1, id);
            int rows = ps2.executeUpdate();

            conn.commit();

            if (rows > 0)
                status.setText("Hotel deleted");
            else
                status.setText("Hotel not found");

        } catch (Exception e) {
            e.printStackTrace();
            status.setText(e.getMessage());
        }
    }
    @FXML
    public void back() {
        ManagerDashboardController c =
                SceneUtil.switchScene(stage(), "manager_dashboard.fxml", 750, 500);
        if (c != null) c.setSsn(ssn);
    }
}