package com.example.testoviyfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Home-page.fxml")));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            //stage.getIcons().add(new Image("C:/Programmer/Java Projects/testoviyFx/src/main/resources/Al.png"));
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}