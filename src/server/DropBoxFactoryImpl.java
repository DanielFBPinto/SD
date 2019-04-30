package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DropBoxFactoryImpl extends UnicastRemoteObject implements DropBoxFactoryRI {

    public DropBoxFactoryImpl() throws RemoteException {
        super();
    }

    @Override
    public DropBoxSessionRI Register(String username, String password) throws RemoteException {
        if(DB.users.containsKey(username)){
            return null;
        }
        User user=new User(username,password);
        DB.users.put(username,user);
        DropBoxSessionImpl sessionImpl=new DropBoxSessionImpl();
        DB.session.put(username,sessionImpl);
        return sessionImpl;


    }

    @Override
    public DropBoxSessionRI Login(String username, String password) throws RemoteException {
       if(DB.users.containsKey(username)){
           return DB.session.get(username);
       }
        return null;
    }
}
