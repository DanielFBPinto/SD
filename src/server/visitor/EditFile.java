package server.visitor;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EditFile implements Visitor {
    private final String name;
    private final String path;

    public EditFile(String name, String path) throws RemoteException {
        this.name = name;
        this.path = path;
        export();
    }
    public void visit(File file){
        /* TODO */
    }
    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
    private void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }
}
