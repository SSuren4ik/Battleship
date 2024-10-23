package org.example.battleship.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote {
    void updateField(boolean[][] shots) throws RemoteException;

    String getName() throws RemoteException;
}
