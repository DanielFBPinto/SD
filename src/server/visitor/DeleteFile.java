package server.visitor;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DeleteFile implements Visitor{
    private final String name;
    private final String path;

    public DeleteFile(String name, String path) throws RemoteException {
        this.name = name;
        this.path = path;
        export();
    }
    public void visit(File file){
        new File(file.getPath() + "/" + this.path + "/" + this.name).delete();
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
