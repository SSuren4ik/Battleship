package org.example.battleship.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.example.battleship.server.SeaBattle;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientController {
    @FXML
    private Label welcomeText;

    @FXML
    private GridPane grid;

    private SeaBattle seaBattle;

    private static final String HOST = "localhost";
    private static final int PORT = 2732;
    private static final String SERVER_NAME = "server.sea_battle";

    @FXML
    public void initialize() {
        try {
            Registry registry = LocateRegistry.getRegistry(HOST, PORT);
            seaBattle = (SeaBattle) registry.lookup(SERVER_NAME);

            Platform.runLater(this::createGrid);
        } catch (Exception e) {
            System.err.println("Не удалось подключиться к серверу: " + e.getMessage());
            welcomeText.setText("Ошибка подключения к серверу: " + e.getMessage());
        }
    }

    public void createGrid() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Button button = new Button();
                button.setId("cell_" + row + "_" + col);
                button.setMinSize(30, 30);
                button.setOnAction(event -> onCellClick(button));
                grid.add(button, col, row);
            }
        }
    }

    private void onCellClick(Button button) {
        String cellId = button.getId();
        welcomeText.setText("Clicked on " + cellId);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
