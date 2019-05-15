package client;

import server.DropBoxSubjectRI;
import server.visitor.CreateFolder;
import server.visitor.DeleteFolder;
import server.visitor.RenameFolder;
import server.visitor.Visitor;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.HashMap;

public class DropBoxObserverImpl implements DropBoxObserverRI {
    private DropBoxSubjectRI dropBoxSubjectRI;
    private File path;

    public DropBoxObserverImpl(DropBoxSubjectRI dropBoxSubjectRI, File path) throws RemoteException {
        super();
        this.dropBoxSubjectRI = dropBoxSubjectRI;
        this.path = new File(path.getPath() + "/" + dropBoxSubjectRI.getOwner().getUsername());
        this.path.mkdirs();
        update();
        export();
    }


    public void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
        this.dropBoxSubjectRI.attach(this);
    }

    @Override
    public void update() throws RemoteException {
//        HashMap<Timestamp, Visitor> lastState = this.dropBoxSubjectRI.getCurrentState();
//        for (Timestamp t : lastState.keySet()) {
//            if(this.currentState.before(t)) {
//                System.out.println("HELLO");
//                System.out.println("current: " + currentState + " t: " + t);
//                accept(lastState.get(t), t);
//            }
//        }
    }

    @Override
    public void createFolder(String path, String name) throws RemoteException {
        new File(this.path.getPath() + "/" + path + "/" + name).mkdirs();
        Visitor visitor = new CreateFolder(name, path);
//        Timestamp t = new Timestamp(System.currentTimeMillis());
//        this.currentState = t;
        this.dropBoxSubjectRI.accept(visitor);
//        DB.saveSessions();
        // this.dropBoxSubjectRI.createFolder(path, name);
    }

    @Override
    public void deleteFolder(String path, String name) throws RemoteException {
        new File(this.path.getPath() + "/" + path + "/" + name).delete();
        this.dropBoxSubjectRI.deleteFolder(path, name);
        Visitor visitor = new DeleteFolder(name, path);
//        Timestamp t = new Timestamp(System.currentTimeMillis());
//        this.currentState = t;
//        this.dropBoxSubjectRI.accept(visitor, t);
    }

    @Override
    public void renameFolder(String path, String oldname, String newName) throws RemoteException {
//        File dirC = new File(this.path.getPath() + "/" + path + "/" + oldname);
//        File newDirC = new File(dirC.getParent() + "/" + newName);
//        dirC.renameTo(newDirC);
//        Visitor visitor = new RenameFolder(oldname, path, newName);
//        this.dropBoxSubjectRI.renameFolder(path, oldname, newName);
//        Timestamp t = new Timestamp(System.currentTimeMillis());
//        this.currentState = t;
//        this.dropBoxSubjectRI.accept(visitor, t);
    }

    @Override
    public void accept(Visitor visitor) throws RemoteException {
//        this.currentState = t;
//        this.dropBoxSessionRI.getCurrentStates().put(this.dropBoxSubjectRI.getOwner(), t);
        visitor.visit(this.path);
//        DB.saveSessions();
    }
}
