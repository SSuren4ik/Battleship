package org.example.battleship.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.battleship.server.SeaBattle;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client extends Application {

    private static final String UNIQUE_BINDING_NAME = "server.sea_battle";
    private static final String HOST_NAME = "localhost";
    private static final int PORT = 2732;

    @Override
    public void start(Stage stage) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setFillWidth(false);

        Label nameLabel = new Label("Enter your name:");
        TextField nameField = new TextField();
        Button loginButton = new Button("Login");

        vbox.getChildren().addAll(nameLabel, nameField, loginButton);

        SeaBattle seaBattle;

        try {
            Registry registry = LocateRegistry.getRegistry(HOST_NAME, PORT);
            seaBattle = (SeaBattle) registry.lookup(UNIQUE_BINDING_NAME);
        } catch (IOException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(vbox, 750, 380);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

        loginButton.setOnAction(actionEvent -> {
            String clientName = nameField.getText();
            try {
                if (seaBattle.foundName(clientName) || clientName.trim().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Name cannot be empty or is already in use. Please enter a valid and unique name.");
                    alert.showAndWait();
                    return;
                }

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/battleship/hello-view.fxml"));
                Scene mainScene = new Scene(fxmlLoader.load(), 600, 500);

                GameController gameController = fxmlLoader.getController();
                gameController.initController(clientName);
                fxmlLoader.setController(gameController);

                stage.setTitle("Sea Battle Client");
                stage.setScene(mainScene);
                stage.show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    public static void main(String[] args) {
        launch();
    }

}
