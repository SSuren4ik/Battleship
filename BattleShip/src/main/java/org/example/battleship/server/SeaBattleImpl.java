package org.example.battleship.server;

import java.rmi.RemoteException;
import java.util.Random;

public class SeaBattleImpl implements SeaBattle {
    private final boolean[][] ships = new boolean[10][10];
    private final boolean[][] shots = new boolean[10][10];

    protected SeaBattleImpl() throws RemoteException {
        super();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            ships[x][y] = true;
        }
    }

    @Override
    public boolean shoot(int x, int y) throws RemoteException {
        if (x >= 0 && x < 10 && y >= 0 && y < 10 && !shots[x][y]) {
            shots[x][y] = true;
            return ships[x][y];
        }
        return false;
    }

    @Override
    public boolean[][] getField() throws RemoteException {
        return shots;
    }
}
