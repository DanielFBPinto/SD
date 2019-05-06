package server;

import client.DropBoxObserverImpl;
import client.DropBoxObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DropBoxSubjectRI extends Remote {
    public void attach(DropBoxObserverRI obsRI) throws RemoteException;
    public void detach(DropBoxObserverRI obsRI) throws RemoteException;
    public void createFolder(String path, String name) throws RemoteException;
    public void deleteFolder(String path, String name) throws RemoteException;
    public void editFolder(String path, String oldname, String newName) throws RemoteException;
}
