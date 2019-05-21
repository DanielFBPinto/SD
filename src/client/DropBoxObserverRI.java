package client;

import server.DropBoxSubjectRI;
import server.visitor.Visitor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;

public interface DropBoxObserverRI extends Remote {
    public void update() throws RemoteException;
    public void accept(Visitor visitor) throws RemoteException;
    public boolean getStatus() throws RemoteException;
}
