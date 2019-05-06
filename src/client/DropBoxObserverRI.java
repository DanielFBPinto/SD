package client;

import server.DropBoxSubjectRI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DropBoxObserverRI extends Remote {
    public void update() throws RemoteException;
}
