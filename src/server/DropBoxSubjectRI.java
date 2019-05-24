package server;

import client.DropBoxObserverRI;
import server.visitor.Visitor;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.HashMap;

public interface DropBoxSubjectRI extends Remote {
    User getOwner() throws RemoteException;
    void attach(DropBoxObserverRI obsRI) throws RemoteException;
    void detach(DropBoxObserverRI obsRI) throws RemoteException;
    void accept(Visitor visitor) throws RemoteException;
    HashMap<File, Timestamp> getCurrentState() throws RemoteException;
    void setCurrentState(HashMap<File, Timestamp> currentState) throws RemoteException;
}
