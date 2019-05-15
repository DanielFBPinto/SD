package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public interface DropBoxSessionRI extends Remote {
    public DropBoxSubjectRI getOwnerSubject() throws RemoteException;
    public ArrayList<String> listSub() throws RemoteException;
    public DropBoxSubjectRI getSubject(String owner) throws RemoteException;
    public boolean shareOwnerSubjectWith(String user) throws RemoteException;
}
