package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ManagerDashboardController {

    @FXML private Label status;

    private int ssn;

    public void setSsn(int ssn) {
        this.ssn = ssn;
        status.setText("Welcome Manager");
    }

    private Stage stage() {
        return (Stage) status.getScene().getWindow();
    }

    @FXML
    public void logout() {
        SceneUtil.switchScene(stage(), "main.fxml", 500, 350);
    }

    @FXML
    public void openHotelPage() {
        ManagerHotelController c =
                SceneUtil.switchScene(stage(), "manager_hotel.fxml", 500, 500);
        if (c != null) c.setSsn(ssn);
    }

    @FXML
    public void openRoomPage() {
        ManagerRoomController c =
                SceneUtil.switchScene(stage(), "manager_room.fxml", 500, 500);
        if (c != null) c.setSsn(ssn);
    }

    @FXML
    public void openClientPage() {
        ManagerClientController c =
                SceneUtil.switchScene(stage(), "manager_client.fxml", 500, 500);
        if (c != null) c.setSsn(ssn);
    }
}