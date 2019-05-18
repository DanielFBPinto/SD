package server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class DropBoxSessionImpl implements DropBoxSessionRI {

    private String owner;

    public DropBoxSessionImpl(String owner) throws RemoteException {
        this.owner = owner;
        export();
    }

    private void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public DropBoxSubjectRI getOwnerSubject() throws RemoteException {
        for (DropBoxSubjectRI d : DB.getSubjects().values()) {
            System.out.println("Subjects: " + d.getOwner().getUsername());
        }
        return DB.getSubjects().get(this.owner);
    }

    @Override
    public ArrayList<String> listSub() throws RemoteException {
        return DB.getShared().get(owner);
    }

    @Override
    public DropBoxSubjectRI getSubject(String owner) throws RemoteException {
        if (DB.getShared().get(this.owner).contains(owner))
            return DB.getSubjects().get(owner);
        return null;
    }

    @Override
    public boolean shareOwnerSubjectWith(String user) throws RemoteException {
        if (DB.getUser(user) == null) {
            return false;
        }
        ArrayList<String> a = DB.getShared().get(user);
        if (a == null)
            a = new ArrayList<>();
        a.add(owner);
        DB.getShared().put(user, a);
        for (String s : DB.getShared().get(user)) {
            System.out.println(user + " tem a pasta: " + s);
        }
        DB.saveShared();
        return true;
    }
}
