package server.visitor;

import java.io.File;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DeleteFolder implements Visitor, Serializable {
    private final String name;
    private final String path;

    public DeleteFolder(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public void visit(File file) {
        System.out.println("Lets go delete " + this.name);
        File rec = new File(file.getPath() + "/" + this.getPath() + "/" + this.getName());
        deleteFolder(rec);
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

}
