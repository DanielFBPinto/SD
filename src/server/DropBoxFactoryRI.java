package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DropBoxFactoryRI extends Remote {
    public boolean register(String username,String password) throws RemoteException;
    public DropBoxSessionRI login(String username,String password) throws RemoteException;
}
