package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBoxSessionImpl extends UnicastRemoteObject implements DropBoxSessionRI {
    DropboxSubjectImpl ownerSubject;

    public DropBoxSessionImpl(DropboxSubjectImpl ownerSubject) throws RemoteException {
        this.ownerSubject = ownerSubject;
//        export(this);
    }
//    Export causava erro -> Descobrir se é mesmo necessário usar export aqui
//    private void export(DropBoxSessionRI p) throws RemoteException {
//        UnicastRemoteObject.exportObject(p, 0);
//    }

    @Override
    public DropboxSubjectImpl getOwnerSubject() throws RemoteException {
        return ownerSubject;
    }
}
