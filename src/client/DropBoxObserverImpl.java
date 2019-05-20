package client;

import server.DropBoxSubjectRI;
import server.visitor.CreateFolder;
import server.visitor.DeleteFolder;
import server.visitor.Visitor;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class DropBoxObserverImpl implements DropBoxObserverRI {
    private DropBoxSubjectRI dropBoxSubjectRI;
    private File path;
    private HashMap<File, Timestamp> currentState = new HashMap<>();
    private HashMap<File, Timestamp> lastState = new HashMap<>();
    private CheckFolderThread checkThread;
    private SaveStateThread saveStateThread;

    public DropBoxObserverImpl(DropBoxSubjectRI dropBoxSubjectRI, File path) throws RemoteException {
        super();
        this.dropBoxSubjectRI = dropBoxSubjectRI;
        this.path = new File(path.getPath() + "/" + dropBoxSubjectRI.getOwner().getUsername());
        this.path.mkdirs();
        update();
        checkThread = new CheckFolderThread(this.path, currentState, lastState, dropBoxSubjectRI);
        checkThread.setPriority(Thread.MIN_PRIORITY);
        checkThread.start();
        saveStateThread = new SaveStateThread(dropBoxSubjectRI.getOwner().getUsername(), currentState);
        saveStateThread.setPriority(Thread.MIN_PRIORITY);
        saveStateThread.start();
        export();
    }

    public DropBoxObserverImpl(DropBoxSubjectRI dropBoxSubjectRI, File path, HashMap<File, Timestamp> currentState) throws RemoteException {
        super();
        this.dropBoxSubjectRI = dropBoxSubjectRI;
        this.path = new File(path.getPath() + "/" + dropBoxSubjectRI.getOwner().getUsername());
        this.path.mkdirs();
        for (File f : currentState.keySet()) {
            System.out.println(f.getName());
        }
        update();
//        checkThread = new CheckFolderThread(this.path, currentState, lastState, dropBoxSubjectRI);
//        checkThread.setPriority(Thread.MIN_PRIORITY);
//        checkThread.start();
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
        File p = new File(this.path.getPath() + "/" + path + "/" + name);
        p.mkdirs();
//        Visitor visitor = new CreateFolder(name, path);
//        Timestamp t = new Timestamp(System.currentTimeMillis());
//        this.currentState = t;
//        this.dropBoxSubjectRI.accept(visitor);
        insertCurrentState(p, new Timestamp(System.currentTimeMillis()));
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

    public Timestamp geMostRecentTs(HashMap<File, Timestamp> state) {
        Timestamp mostRecent = new Timestamp(0);
        for (Timestamp t : state.values()) {
            if (mostRecent == null || t.after(mostRecent))
                mostRecent = t;
        }
        return mostRecent;
    }

    public Set<File> filesAfter(HashMap<File, Timestamp> state, Timestamp t) {
        return state.keySet().stream()
                .filter(key -> state.get(key).after(t))
                .collect(Collectors.toSet());
    }

    public void compareStates() throws RemoteException {
//        if (!this.currentState.equals(this.lastState)) {
//            if (currentState.size() > lastState.size()) {
//                Timestamp tMax = geMostRecentTs(lastState);
//                Set<File> changes = filesAfter(currentState, tMax);
//                for (File f : changes) {
//                    System.out.println(f.getPath().replace(this.path.getPath(), ""));
//                    Visitor visitor = new CreateFolder(f.getName(), f.getParent().replace(this.path.getPath(), ""));
//                    this.dropBoxSubjectRI.accept(visitor);
//                }
//            }
//        }
    }

    public void insertCurrentState(File file, Timestamp time) throws RemoteException {
//        this.currentState.put(file, time);
//        compareStates();
    }
}
