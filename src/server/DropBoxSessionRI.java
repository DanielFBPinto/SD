package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DropBoxSessionRI extends Remote {
    public void print() throws RemoteException;
}
