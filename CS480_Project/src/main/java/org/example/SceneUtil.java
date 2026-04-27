package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneUtil {

    public static <T> T switchScene(Stage stage, String fxml, double w, double h) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneUtil.class.getResource("/" + fxml)
            );

            Parent root = loader.load();

            Scene scene = new Scene(root, w, h);

            scene.getStylesheets().add(
                    SceneUtil.class.getResource("/style.css").toExternalForm()
            );

            stage.setScene(scene);

            return loader.getController();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}