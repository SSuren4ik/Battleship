package org.example.battleship.server;

import org.example.battleship.client.ClientCallback;

import java.lang.reflect.Executable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SeaBattleImpl implements SeaBattle {

    private final boolean[][] ships = new boolean[10][10];
    private final boolean[][] shots = new boolean[10][10];
    private final List<ClientCallback> clients = new ArrayList<>();
    private final static int MAX_CLIENTS = 2;

    protected SeaBattleImpl() throws RemoteException {
        super();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            ships[x][y] = true;
        }
    }

    public boolean shoot(int x, int y) throws RemoteException {
        if (x >= 0 && x < 10 && y >= 0 && y < 10 && !shots[x][y]) {
            shots[x][y] = true;
            notifyClients();
            return ships[x][y];
        }
        return false;
    }

    @Override
    public boolean[][] getShipsField() throws RemoteException {
        return ships;
    }

    @Override
    public boolean[][] getShootsField() throws RemoteException {
        return shots;
    }

    @Override
    public void registerClient(ClientCallback client) throws RemoteException {
        if (clients.size() < MAX_CLIENTS)
            clients.add(client);
        else
            throw new RemoteException("More clients");
    }

    @Override
    public void unregisterClient(ClientCallback client) throws RemoteException {
        clients.remove(client);
    }

    private void notifyClients() {
        for (ClientCallback client : clients) {
            try {
                client.updateField(shots);
            } catch (RemoteException e) {
                System.err.println("Ошибка уведомления клиента: " + e.getMessage());
            }
        }
    }
}
