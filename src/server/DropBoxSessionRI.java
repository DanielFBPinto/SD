package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DropBoxSessionRI extends Remote {
    public String print() throws RemoteException;
}
