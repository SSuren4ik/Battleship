package org.example.battleship.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.example.battleship.server.SeaBattle;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class GameController implements ClientCallback {

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
        initializeDisconnection();
        initializeCellButtons();
        createGrid();
    }

    private void initializeDisconnection() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                unregisterClient();
                System.out.println("Клиент отключен при завершении программы");
            } catch (RemoteException e) {
                System.out.println("Ошибка при отключении: " + e.getMessage());
            }
        }));
    }

    private void initializeConnection() {
        try {
            Registry registry = LocateRegistry.getRegistry(HOST_NAME, PORT);
            seaBattle = (SeaBattle) registry.lookup(UNIQUE_BINDING_NAME);

            ClientCallback callback = (ClientCallback) UnicastRemoteObject.exportObject(this, 0);
            seaBattle.registerClient(callback);

            System.out.println("Подключен к серверу");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
            System.exit(1);
        }
    }

    private void unregisterClient() throws RemoteException {
        if (seaBattle != null) {
            seaBattle.unregisterClient(this);
            System.out.println("Клиент успешно удален из списка.");
        }
    }

    private void initializeCellButtons() {
        try {
            boolean[][] ships = seaBattle.getShipsField();
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    cellButtons[row][col] = getButtonAtPosition(row, col);
                    if (ships[row][col]) {
                        cellButtons[row][col].setStyle("-fx-background-color: black"); // Отмечаем корабли черным
                    }
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
        Platform.runLater(() -> {
            try {
                updateField(seaBattle.getShootsField());
            } catch (RemoteException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    @Override
    public void updateField(boolean[][] shots) throws RemoteException {
        Platform.runLater(() -> {
            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    if (shots[row][col]) {
                        cellButtons[row][col].setStyle("-fx-background-color: red");
                    }
                }
            }
        });
    }

    @FXML
    protected void onShootButtonClick() {
        if (selectedButton != null) {
            try {
                int row = GridPane.getRowIndex(selectedButton);
                int col = GridPane.getColumnIndex(selectedButton);

                seaBattle.shoot(row, col);
                selectedButton.setStyle(""); // Сброс стиля кнопки
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
    }
}
