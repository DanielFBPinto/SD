package server.visitor;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EditFile implements Visitor, Serializable {
    private final String name;
    private final String path;

    public EditFile(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public void visit(File file) {
        /* TODO */
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
