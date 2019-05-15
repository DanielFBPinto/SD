package server.visitor;
import java.io.File;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Visitor extends Serializable {
    public void visit(File file);
}
