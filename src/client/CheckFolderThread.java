package client;

import server.DropBoxSubjectRI;
import server.visitor.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckFolderThread extends Thread {
    private boolean keepRunning = true;
    private final File root;
    private final File folder;
    private final HashMap<File, Timestamp> currentState;
    private final HashMap<File, Timestamp> lastState;
    private final DropBoxSubjectRI dropBoxSubjectRI;

    public CheckFolderThread(File root, File folder, HashMap cs, HashMap ls, DropBoxSubjectRI ds) {
        this.root = root;
        this.folder = folder;
        this.currentState = cs;
        this.lastState = ls;
        this.dropBoxSubjectRI = ds;
    }

    public void run() {
        while (keepRunning) {
            File files[] = folder.listFiles();
            synchronized (currentState) {
                synchronized (lastState) {
                    lastState.clear();
                    lastState.putAll(currentState);
                    currentState.clear();
                    for (File f : files) {
                        currentState.put(f, new Timestamp(f.lastModified()));
//                        if (f.isDirectory())
//                            new CheckFolderThread(root, f, currentState, lastState, dropBoxSubjectRI).start();
                    }
                    try {
                        compareStates();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void compareStates() throws RemoteException {
        for (File f : currentState.keySet()) {
            if (!lastState.containsKey(f)) {
                if (f.isDirectory()) {
                    Visitor visitor = new CreateFolder(f.getName(), f.getParent().replace(this.root.getPath(), ""));
                    this.dropBoxSubjectRI.accept(visitor);
                    this.dropBoxSubjectRI.setCurrentState(currentState);
                } else {
                    try {
                        byte[] fileContent = Files.readAllBytes(f.toPath());
                        Visitor visitor = new CreateFile(f.getName(), f.getParent().replace(this.root.getPath(), ""), fileContent);
                        this.dropBoxSubjectRI.accept(visitor);
                        this.dropBoxSubjectRI.setCurrentState(currentState);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Timestamp lastT = lastState.get(f);
                Timestamp currentT = currentState.get(f);
                if (currentT.after(lastT)) {
                    System.out.println(this.getClass().getName() + " - compareStates(): FIcheiro foi modificado");
                    try {
                        byte[] fileContent = Files.readAllBytes(f.toPath());
                        Visitor visitor = new EditFile(f.getName(), f.getParent().replace(this.root.getPath(), ""), fileContent);
                        this.dropBoxSubjectRI.accept(visitor);
                        this.dropBoxSubjectRI.setCurrentState(currentState);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for (File f : lastState.keySet()) {
            if (!currentState.containsKey(f)) {
                if (f.isDirectory()) {
                    Visitor visitor = new DeleteFolder(f.getName(), f.getParent().replace(this.root.getPath(), ""));
                    this.dropBoxSubjectRI.accept(visitor);
                    this.dropBoxSubjectRI.setCurrentState(currentState);
                } else {
                    Visitor visitor = new DeleteFile(f.getName(), f.getParent().replace(this.root.getPath(), ""));
                    this.dropBoxSubjectRI.accept(visitor);
                    this.dropBoxSubjectRI.setCurrentState(currentState);
                }
            }
        }
    }
}
