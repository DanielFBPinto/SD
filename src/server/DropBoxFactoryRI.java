package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DropBoxFactoryRI extends Remote {
    public DropBoxSessionRI Register(String username,String password) throws RemoteException;
    public DropBoxSessionRI Login(String username,String password) throws RemoteException;

}
