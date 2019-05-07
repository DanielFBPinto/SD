package client;

import server.DropBoxSubjectRI;
import server.visitor.CreateFolder;
import server.visitor.DeleteFolder;
import server.visitor.RenameFolder;
import server.visitor.Visitor;

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
        Visitor visitor= new CreateFolder(name,path);
        this.dropBoxSubjectRI.accept(visitor);
       // this.dropBoxSubjectRI.createFolder(path, name);
    }

    @Override
    public void deleteFolder(String path, String name) throws RemoteException {
        new File(this.path.getPath() + "/" + path + "/" + name).delete();
        this.dropBoxSubjectRI.deleteFolder(path, name);
        Visitor visitor= new DeleteFolder(name,path);
        this.dropBoxSubjectRI.accept(visitor);
    }

    @Override
    public void renameFolder(String path, String oldname, String newName) throws RemoteException {
        File dirC = new File(this.path.getPath() + "/" + path + "/" + oldname);
        File newDirC = new File(dirC.getParent() + "/" + newName);
        dirC.renameTo(newDirC);
        Visitor visitor= new RenameFolder(oldname,path,newName);
        this.dropBoxSubjectRI.renameFolder(path, oldname, newName);

        this.dropBoxSubjectRI.accept(visitor);
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this.path);
    }
}
