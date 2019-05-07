package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBoxSessionImpl implements DropBoxSessionRI, Serializable {
    private DropboxSubjectImpl ownerSubject;
    private ArrayList<DropboxSubjectImpl> sharedSubjects = new ArrayList<>();

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

    @Override
    public ArrayList<DropboxSubjectImpl> getSharedSubjects() throws RemoteException {
        return sharedSubjects;
    }

    @Override
    public boolean insertSubject(DropboxSubjectImpl d) throws RemoteException {
        return this.sharedSubjects.add(d);
    }

    @Override
    public void shareOwnerSubjectWith(String user) throws RemoteException {
        User u = DB.getUser(user);
        if (u != null) {
            if(DB.getSession(u.getUsername(), u.getPassword()).insertSubject(this.ownerSubject))
                DB.saveSessions();
        }
    }

}
