package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ManagerRoomController {

    @FXML private TextField hotelId;
    @FXML private TextField roomNum;
    @FXML private TextField windows;
    @FXML private TextField year;
    @FXML private TextField access;
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

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Room (RoomNum, HotelID, NumWindows, YearOfLastRenovation, AccessType) " +
                            "VALUES (?, ?, ?, ?, ?)"
            );

            ps.setInt(1, Integer.parseInt(roomNum.getText()));
            ps.setInt(2, Integer.parseInt(hotelId.getText()));
            ps.setInt(3, Integer.parseInt(windows.getText()));
            ps.setInt(4, Integer.parseInt(year.getText()));
            ps.setString(5, access.getText());

            ps.executeUpdate();
            status.setText("Inserted");

        } catch (Exception e) {
            e.printStackTrace();
            status.setText(e.getMessage());
        }
    }

    @FXML
    public void update() {
        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE Room SET NumWindows=?, YearOfLastRenovation=?, AccessType=? " +
                            "WHERE HotelID=? AND RoomNum=?");

            ps.setInt(1, Integer.parseInt(windows.getText()));
            ps.setInt(2, Integer.parseInt(year.getText()));
            ps.setString(3, access.getText());
            ps.setInt(4, Integer.parseInt(hotelId.getText()));
            ps.setInt(5, Integer.parseInt(roomNum.getText()));

            ps.executeUpdate();
            status.setText("Updated");

        } catch (Exception e) {
            e.printStackTrace();
            status.setText(e.getMessage());
        }
    }

    @FXML
    public void delete() {
        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM Room WHERE HotelID=? AND RoomNum=?");

            ps.setInt(1, Integer.parseInt(hotelId.getText()));
            ps.setInt(2, Integer.parseInt(roomNum.getText()));

            ps.executeUpdate();
            status.setText("Deleted");

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