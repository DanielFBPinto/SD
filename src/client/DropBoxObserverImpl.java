package client;

import server.DropBoxSubjectRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DropBoxObserverImpl implements DropBoxObserverRI{

    public DropBoxObserverImpl() throws RemoteException{
        super();
        export();
    }

    @Override
    public void update() throws RemoteException {

    }

    public void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }
}
