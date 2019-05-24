package client;

import server.DropBoxSubjectRI;
import server.visitor.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.HashMap;

public class DropBoxObserverImpl implements DropBoxObserverRI {
    private DropBoxSubjectRI dropBoxSubjectRI;
    private File path;
    private HashMap<File, Timestamp> currentState = new HashMap<>();
    private HashMap<File, Timestamp> lastState = new HashMap<>();
    private CheckFolderThread checkThread;
    private SaveStateThread saveStateThread;

    /**
     * Construtor para o caso em que o utlizador não tem um estado atual da pasta gravado, logo a pasta deverá ser
     * reescrita/escrita pela primeira vez.
     * @param dropBoxSubjectRI
     * @param path
     * @throws RemoteException
     */
    public DropBoxObserverImpl(DropBoxSubjectRI dropBoxSubjectRI, File path) throws RemoteException {
        super();
        this.dropBoxSubjectRI = dropBoxSubjectRI;
        this.path = new File(path.getPath() + "/" + dropBoxSubjectRI.getOwner().getUsername());
        this.path.mkdirs();
        update();
        checkThread = new CheckFolderThread(this.path, this.path, currentState, lastState, dropBoxSubjectRI);
        checkThread.setPriority(Thread.MIN_PRIORITY);
        checkThread.start();
        saveStateThread = new SaveStateThread(dropBoxSubjectRI.getOwner().getUsername(), currentState);
        saveStateThread.setPriority(Thread.MIN_PRIORITY);
        saveStateThread.start();
        export();
    }

    /**
     * Construtor para o caso em que o utilizador tem um estado atual da pasta e então aí é feito envio das alterações
     * do utilizador no tempo que esteve offline e posteriormente carregadas as alterações que houveram no tempo que
     * teve offline
     * @param dropBoxSubjectRI
     * @param path
     * @param currentState
     * @throws RemoteException
     */
    public DropBoxObserverImpl(DropBoxSubjectRI dropBoxSubjectRI, File path, HashMap<File, Timestamp> currentState) throws RemoteException {
        super();
        this.dropBoxSubjectRI = dropBoxSubjectRI;
        this.path = new File(path.getPath() + "/" + dropBoxSubjectRI.getOwner().getUsername());
        this.path.mkdirs();
        this.lastState = currentState;
        update();
        checkThread = new CheckFolderThread(this.path, this.path, this.currentState, lastState, dropBoxSubjectRI);
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

    /**
     * Atualização da pasta do utilizador após login (primeiro login não atualiza porque não tem ainda nada).
     * @throws RemoteException
     */
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

    /**
     * Aceita o visitor e executa o que houver
     * @param visitor
     */
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this.path);
    }

    /**
     * Função usada para comparar os estados das pastas e proceder as atualizações necessárias
     * @throws RemoteException
     */
    public void compareStates() throws RemoteException {
        for (File f : currentState.keySet()) {
            if (!lastState.containsKey(f)) {
                if (f.isDirectory()) {
                    Visitor visitor = new CreateFolder(f.getName(), f.getParent().replace(this.path.getPath(), ""));
                    this.dropBoxSubjectRI.accept(visitor);
                    this.dropBoxSubjectRI.setCurrentState(currentState);
                } else {
                    try {
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
            if (!currentState.containsKey(f)) {
                if (f.isDirectory()) {
                    Visitor visitor = new DeleteFolder(f.getName(), f.getParent().replace(this.path.getPath(), ""));
                    this.dropBoxSubjectRI.accept(visitor);
                    this.dropBoxSubjectRI.setCurrentState(currentState);
                } else {
                    Visitor visitor = new DeleteFile(f.getName(), f.getParent().replace(this.path.getPath(), ""));
                    this.dropBoxSubjectRI.accept(visitor);
                    this.dropBoxSubjectRI.setCurrentState(currentState);
                }
            }
        }
        lastState.clear();
        lastState.putAll(currentState);
    }

    /**
     * Função usada para verificar se o observador atual continua ativo
     * @return
     */
    public boolean getStatus() {
        return true;
    }
}
