package server;

import client.DropBoxObserverImpl;
import client.DropBoxObserverRI;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class DropboxSubjectImpl implements DropBoxSubjectRI {
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
        new File(path + "/" + name).mkdirs();
        new File(this.path.getPath() + "/" + name).mkdirs();
    }

    @Override
    public void deleteFolder(String path, String name) throws RemoteException {
        new File(path + "/" + name).delete();
        new File(this.path.getPath() + "/" + name).delete();
    }

    @Override
    public void editFolder(String path, String oldname, String newName) throws RemoteException {
        File dirC = new File(path + "/" + oldname);
        File newDirC = new File(dirC.getParent() + "/" + newName);
        dirC.renameTo(newDirC);

        File dirS = new File(this.path.getPath() + "/" + oldname);
        File newDirS = new File(dirS.getParent() + "/" + newName);
        dirS.renameTo(newDirS);
    }
}
