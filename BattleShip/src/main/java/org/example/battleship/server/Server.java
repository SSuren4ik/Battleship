package org.example.battleship.server;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static final String UNIQUE_BINDING_NAME = "server.sea_battle";
    private static final int PORT = 2732;

    public static void main(String[] args) {
        try {
            final SeaBattleImpl server = new SeaBattleImpl();
            final Registry registry = LocateRegistry.createRegistry(PORT);

            Remote stub = UnicastRemoteObject.exportObject(server, 0);
            registry.bind(UNIQUE_BINDING_NAME, stub);

            System.out.println("Сервер запущен...");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
