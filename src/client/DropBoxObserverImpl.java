package client;

import server.DropBoxSubjectRI;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DropBoxObserverImpl implements DropBoxObserverRI{
    private File path;
    private DropBoxSubjectRI dropBoxSubjectRI;

    public DropBoxObserverImpl(File path, DropBoxSubjectRI dropBoxSubjectRI) throws RemoteException{
        super();
        this.path = path;
        this.dropBoxSubjectRI = dropBoxSubjectRI;
        export();
    }

    @Override
    public void update() throws RemoteException {

    }

    public void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void createFolder(String path, String name) throws RemoteException {
        new File(this.path.getPath() + "/" + path + "/" + name).mkdirs();
        this.dropBoxSubjectRI.createFolder(path, name);
    }

    @Override
    public void deleteFolder(String path, String name) throws RemoteException {
        new File(this.path.getPath() + "/" + path + "/" + name).delete();
        this.dropBoxSubjectRI.deleteFolder(path, name);
    }

    @Override
    public void renameFolder(String path, String oldname, String newName) throws RemoteException {
        File dirC = new File(this.path.getPath() + "/" + path + "/" + oldname);
        File newDirC = new File(dirC.getParent() + "/" + newName);
        dirC.renameTo(newDirC);
        this.dropBoxSubjectRI.renameFolder(path, oldname, newName);
    }
}
