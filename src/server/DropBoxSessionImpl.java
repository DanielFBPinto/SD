package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBoxSessionImpl extends UnicastRemoteObject implements DropBoxSessionRI {
    protected DropBoxSessionImpl() throws RemoteException {
        super();
    }

    @Override
    public String print() throws RemoteException {
//        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "hello", new Object[]{});
        return "Hello";
    }
}
