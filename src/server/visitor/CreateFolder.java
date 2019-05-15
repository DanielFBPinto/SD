package server.visitor;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CreateFolder implements Visitor {
    private final String name;
    private final String path;


    public CreateFolder(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public void visit(File file) {
        new File(file.getPath() + "/" + this.path + "/" + this.name).mkdirs();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
