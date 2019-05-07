package server.visitor;
import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Visitor extends Remote {
    public void visit(File file) throws RemoteException;
}
