package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBoxSessionImpl implements DropBoxSessionRI {
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
