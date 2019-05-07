package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface DropBoxSessionRI extends Remote {
    public DropBoxSubjectRI getOwnerSubject() throws RemoteException;
    public ArrayList<DropboxSubjectImpl> getSharedSubjects() throws RemoteException;
    public boolean insertSubject(DropboxSubjectImpl d) throws RemoteException;
    public void shareOwnerSubjectWith(String user) throws RemoteException;
}
