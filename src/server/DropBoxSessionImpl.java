package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DropBoxSessionImpl extends UnicastRemoteObject implements DropBoxSessionRI {
    protected DropBoxSessionImpl() throws RemoteException {
        super();
    }
}
