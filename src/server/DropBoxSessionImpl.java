package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBoxSessionImpl implements DropBoxSessionRI, Serializable {
    DropboxSubjectImpl ownerSubject;

    public DropBoxSessionImpl(DropboxSubjectImpl ownerSubject) throws RemoteException {
        this.ownerSubject = ownerSubject;
        export();
    }

    private void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public DropboxSubjectImpl getOwnerSubject() throws RemoteException {
        return ownerSubject;
    }
}
