package server.visitor;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DeleteFolder implements Visitor {
    private final String name;
    private final String path;

    public DeleteFolder(String name, String path) throws RemoteException {
        this.name = name;
        this.path = path;
        export();
    }

    @Override
    public void visit(File file) throws RemoteException {
        new File(file.getPath() + "/" + this.getPath() + "/" + this.getName()).delete();
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
