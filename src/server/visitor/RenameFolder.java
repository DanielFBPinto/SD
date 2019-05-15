package server.visitor;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RenameFolder implements Visitor, Serializable {
    private final String name;
    private final String path;
    private final String newName;

    public RenameFolder(String name, String path, String newName) {
        this.name = name;
        this.path = path;
        this.newName = newName;
    }

    @Override
    public void visit(File file) {
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

}
