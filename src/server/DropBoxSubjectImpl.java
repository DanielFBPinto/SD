package server;

import client.CheckFolderThread;
import client.DropBoxObserverRI;
import server.visitor.Visitor;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class DropBoxSubjectImpl implements DropBoxSubjectRI, Serializable {
    private File path;
    private User owner;
    private HashMap<File, Timestamp> currentState = new HashMap<>();
    private ArrayList<DropBoxObserverRI> observers = new ArrayList<>();
    private CheckObserverThread checkObserverThread;

    public File getPath() {
        return path;
    }

    @Override
    public User getOwner() throws RemoteException {
        return owner;
    }

    public DropBoxSubjectImpl(User owner, File path) throws RemoteException {
        super();
        this.owner = owner;
        this.path = path;
        checkObserverThread = new CheckObserverThread(this.observers);
        checkObserverThread.setPriority(Thread.MIN_PRIORITY);
        checkObserverThread.start();
        export();
    }

    private void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void attach(DropBoxObserverRI obsRI) throws RemoteException {
        this.observers.add(obsRI);
    }

    @Override
    public void detach(DropBoxObserverRI obsRI) throws RemoteException {
        this.observers.remove(obsRI);
    }

    @Override
    public void createFolder(String path, String name) throws RemoteException {
        new File(this.path.getPath() + "/" + path + "/" + name).mkdirs();
    }

    @Override
    public void deleteFolder(String path, String name) throws RemoteException {
        new File(this.path.getPath() + "/" + path + "/" + name).delete();
    }

    @Override
    public void renameFolder(String path, String oldname, String newName) throws RemoteException {
        File dir = new File(this.path.getPath() + "/" + path + "/" + oldname);
        File newDir = new File(dir.getParent() + "/" + newName);
        dir.renameTo(newDir);
    }

    private void notifyAll(Visitor visitor) throws RemoteException {
        System.out.println("Number of observers: " + this.observers.size());
        for (DropBoxObserverRI obs : this.observers) {
            if(obs != null)
                obs.accept(visitor);
        }
    }

    @Override
    public void accept(Visitor visitor) throws RemoteException {
//        this.currentState.put(t, visitor);
        visitor.visit(this.path);
        notifyAll(visitor);
    }

    public HashMap<File, Timestamp> getCurrentState() throws RemoteException {
        return currentState;
    }

    public void setCurrentState(HashMap<File, Timestamp> currentState) throws RemoteException {
        this.currentState = currentState;
    }
}
