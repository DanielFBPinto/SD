package server.visitor;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CreateFile implements Visitor, Serializable {
    private final String name;
    private final String path;
    private final byte[] fileContent;

    public CreateFile(String name, String path, byte[] fileContent) {
        this.name = name;
        this.path = path;
        this.fileContent = fileContent;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public void visit(File file) {
        try {
            File f = new File(file.getPath() + this.path + "/" + this.name);
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            out.write(fileContent);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
