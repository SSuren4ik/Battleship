package org.example.battleship.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SeaBattle extends Remote {
    boolean shoot(int x, int y) throws RemoteException;

    boolean[][] getShipsField() throws RemoteException;
    boolean[][] getShootsField() throws RemoteException;
}
