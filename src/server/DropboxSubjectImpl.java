package server;

import client.DropBoxObserverRI;
import server.visitor.Visitor;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class DropboxSubjectImpl implements DropBoxSubjectRI , Serializable {
    private File path;
    private User owner;
    private State state;
    private ArrayList<DropBoxObserverRI> observers = new ArrayList<>();

    public File getPath() {
        return path;
    }

    public DropboxSubjectImpl(User owner, File path) throws RemoteException {
        this.owner = owner;
        this.path = path;
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
    private void notifyAll(Visitor visitor){
        for (DropBoxObserverRI obs:this.observers
             ) {
            obs.accept(visitor);
        }
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this.getPath());

    }
}
