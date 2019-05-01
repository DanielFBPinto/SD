package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBoxSessionImpl implements DropBoxSessionRI {
    public DropBoxSessionImpl() throws RemoteException {
      //  super();
        this.export(this);
    }
    private void export(DropBoxSessionImpl o) throws RemoteException {
        UnicastRemoteObject.exportObject(o, 0);
    }
    @Override
    public void print() throws RemoteException {
        System.out.println("entrei na session");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "hello", new Object[]{});

    }
}
