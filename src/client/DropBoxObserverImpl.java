package client;

import server.DropBoxSubjectRI;
import server.visitor.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
        this.lastState = currentState;
        update();
        checkThread = new CheckFolderThread(this.path, this.currentState, lastState, dropBoxSubjectRI);
        checkThread.setPriority(Thread.MIN_PRIORITY);
        checkThread.start();
        saveStateThread = new SaveStateThread(dropBoxSubjectRI.getOwner().getUsername(), currentState);
        saveStateThread.setPriority(Thread.MIN_PRIORITY);
        saveStateThread.start();
        export();
    }

    public void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
        this.dropBoxSubjectRI.attach(this);
    }

    @Override
    public void update() throws RemoteException {
        File files[] = this.path.listFiles();
        for (File f : files) {
            currentState.put(f, new Timestamp(f.lastModified()));
        }
        compareStates();
        this.currentState = (HashMap) dropBoxSubjectRI.getCurrentState().clone();
        compareStates();
    }


    @Override
    public void accept(Visitor visitor) throws RemoteException {
//        this.currentState = t;
//        this.dropBoxSessionRI.getCurrentStates().put(this.dropBoxSubjectRI.getOwner(), t);
        visitor.visit(this.path);
//        DB.saveSessions();
    }

    public void compareStates() throws RemoteException {
        for (File f : currentState.keySet()) {
            System.out.println("CurrentState: " + f.getName());
            if (!lastState.containsKey(f)) {
                if (f.isDirectory()) {
                    System.out.println("Teste 1");
                    Visitor visitor = new CreateFolder(f.getName(), f.getParent().replace(this.path.getPath(), ""));
                    this.dropBoxSubjectRI.accept(visitor);
                    this.dropBoxSubjectRI.setCurrentState(currentState);
                } else {
                    try {
                        System.out.println("Teste 2");
                        byte[] fileContent = Files.readAllBytes(f.toPath());
                        Visitor visitor = new CreateFile(f.getName(), f.getParent().replace(this.path.getPath(), ""), fileContent);
                        this.dropBoxSubjectRI.accept(visitor);
                        this.dropBoxSubjectRI.setCurrentState(currentState);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for (File f : lastState.keySet()) {
            System.out.println("LastState: " + f.getName());
            if (!currentState.containsKey(f)) {
                if (f.isDirectory()) {
                    System.out.println("Teste 3");
                    Visitor visitor = new DeleteFolder(f.getName(), f.getParent().replace(this.path.getPath(), ""));
                    this.dropBoxSubjectRI.accept(visitor);
                    this.dropBoxSubjectRI.setCurrentState(currentState);
                } else {
                    System.out.println("Teste 4");
                    Visitor visitor = new DeleteFile(f.getName(), f.getParent().replace(this.path.getPath(), ""));
                    this.dropBoxSubjectRI.accept(visitor);
                    this.dropBoxSubjectRI.setCurrentState(currentState);
                }
            }
        }
        lastState.clear();
        lastState.putAll(currentState);
    }

    public boolean getStatus() throws RemoteException {
        return true;
    }
}
