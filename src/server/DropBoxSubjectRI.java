package server;

import client.DropBoxObserverImpl;
import client.DropBoxObserverRI;
import server.visitor.Visitor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.HashMap;

public interface DropBoxSubjectRI extends Remote {
    public User getOwner() throws RemoteException;
    public void attach(DropBoxObserverRI obsRI) throws RemoteException;
    public void detach(DropBoxObserverRI obsRI) throws RemoteException;
    public void createFolder(String path, String name) throws RemoteException;
    public void deleteFolder(String path, String name) throws RemoteException;
    public void renameFolder(String path, String oldname, String newName) throws RemoteException;
    public void accept(Visitor visitor) throws RemoteException;
    public HashMap<Timestamp, Visitor> getCurrentState() throws RemoteException;
}
