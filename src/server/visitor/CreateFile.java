package server.visitor;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CreateFile implements Visitor {
    private final String name;
    private final String path;

    public CreateFile(String name, String path) throws RemoteException{
        this.name = name;
        this.path = path;
        export();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public void visit(File file) {
        new File(file.getPath() + this.path+"/"+this.name).mkdirs();
    }
    private void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }
}
