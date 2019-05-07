package server.visitor;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RenameFile implements Visitor  {
    private final String name;
    private final String path;
    private final String newName;

    public RenameFile(String name, String path, String newName) throws RemoteException {
        this.name = name;
        this.path = path;
        this.newName = newName;
        export();
    }
    public void visit(File file){
        File dirC = new File(file.getPath() + "/" + this.getPath() + "/" + this.getName());
        File newDirC = new File(dirC.getParent() + "/" + this.getNewName());
        dirC.renameTo(newDirC);
    }
    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getNewName() {
        return newName;
    }
    private void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }
}
