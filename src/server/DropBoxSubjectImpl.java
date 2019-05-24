package server;

import client.DropBoxObserverRI;
import server.visitor.Visitor;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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

    /**
     * Retorna o owner do subject
     * @return
     */
    @Override
    public User getOwner() {
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
    public void attach(DropBoxObserverRI obsRI) {
        this.observers.add(obsRI);
    }

    @Override
    public void detach(DropBoxObserverRI obsRI) {
        this.observers.remove(obsRI);
    }

    /**
     * Propaga as alterações recebidas por todos os observers
     * @param visitor
     * @throws RemoteException
     */
    private void notifyAll(Visitor visitor) throws RemoteException {
        for (DropBoxObserverRI obs : this.observers) {
            if (obs != null)
                obs.accept(visitor);
        }
    }

    /**
     * Aplica as alterações recebidas chamando no final o notifyAll para a propagação das mesmas
     * @param visitor
     * @throws RemoteException
     */
    @Override
    public void accept(Visitor visitor) throws RemoteException {
        visitor.visit(this.path);
        notifyAll(visitor);
    }

    public HashMap<File, Timestamp> getCurrentState() {
        return currentState;
    }

    public void setCurrentState(HashMap<File, Timestamp> currentState) {
        this.currentState = currentState;
    }
}
