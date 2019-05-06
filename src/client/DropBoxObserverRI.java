package client;

import server.DropBoxSubjectRI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DropBoxObserverRI extends Remote {
    public void update() throws RemoteException;
    public void createFolder(String path, String name) throws RemoteException;
    public void deleteFolder(String path, String name) throws RemoteException;
    public void editFolder(String path, String oldname, String newName) throws RemoteException;
}
