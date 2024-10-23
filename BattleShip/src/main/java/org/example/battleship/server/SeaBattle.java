package org.example.battleship.server;

import org.example.battleship.client.ClientCallback;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SeaBattle extends Remote {

    boolean shoot(int x, int y) throws RemoteException;

    boolean[][] getShipsField() throws RemoteException;

    boolean[][] getShootsField() throws RemoteException;

    void registerClient(ClientCallback client) throws RemoteException;

    void unregisterClient(ClientCallback client) throws RemoteException;

    boolean foundName(String name)throws RemoteException;
}