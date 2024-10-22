package org.example.battleship.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.example.battleship.server.SeaBattle;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class GameController {
    SeaBattle seaBattle;
    @FXML
    private GridPane grid;

    @FXML
    private Button[][] cellButtons = new Button[10][10];

    private Button selectedButton;

    private static final String UNIQUE_BINDING_NAME = "server.sea_battle";
    private static final String HOST_NAME = "localhost";

    private static final int PORT = 2732;

    @FXML
    public void initialize() {
        initializeConnection();
        initializeCellButtons();
        Platform.runLater(this::createGrid);
    }

    private void initializeConnection() {
        try {
            Registry registry = LocateRegistry.getRegistry(HOST_NAME, PORT);
            seaBattle = (SeaBattle) registry.lookup(UNIQUE_BINDING_NAME);
            Random random = new Random();
            seaBattle.shoot(random.nextInt(10), random.nextInt(10));
            seaBattle.shoot(random.nextInt(10), random.nextInt(10));
            seaBattle.shoot(random.nextInt(10), random.nextInt(10));

            System.out.println("Подключен к серверу");
        } catch (Exception e) {
            System.out.println("Ошибка подключения к серверу: " + e.getMessage());
        }
    }

    private void initializeCellButtons() {
        try {
            boolean[][] ships = seaBattle.getShipsField();
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    cellButtons[row][col] = getButtonAtPosition(row, col);
                    if (ships[row][col])
                        cellButtons[row][col].setStyle("-fx-background-color: black");
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private Button getButtonAtPosition(int row, int col) {
        for (var node : grid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (Button) node;
            }
        }
        return null;
    }

    private void createGrid() {
        try {
            boolean[][] field = seaBattle.getShootsField();

            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    cellButtons[row][col].setText(field[row][col] ? "True" : "False");
                }
            }
        } catch (RemoteException e) {
            System.out.println("Ошибка получения поля: " + e.getMessage());
        }
    }

    @FXML
    protected void onShootButtonClick() {
        if (selectedButton != null) {
            try {
                int row = GridPane.getRowIndex(selectedButton);
                int col = GridPane.getColumnIndex(selectedButton);

                seaBattle.getShootsField()[row][col] = true;

                selectedButton.setText("true");
                selectedButton.setStyle("");
                selectedButton = null;
            } catch (RemoteException e) {
                System.out.println("Ошибка при стрельбе: " + e.getMessage());
            }
        } else {
            System.out.println("Пожалуйста, выберите ячейку для стрельбы.");
        }
    }

    @FXML
    public void onCellClick(javafx.event.ActionEvent event) {
        if (selectedButton != null) {
            selectedButton.setStyle(""); // Сброс стиля до стандартного
        }
        selectedButton = (Button) event.getSource(); // Запоминаем выбранную кнопку
        selectedButton.setStyle("-fx-background-color: yellow"); // Выделяем выбранную кнопку
        printFieldShots();
    }

    public void printFieldShots() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                String text = cellButtons[row][col].getText();
                System.out.print(text + " ");
            }
            System.out.println("");
        }
        System.out.println();
    }

}
