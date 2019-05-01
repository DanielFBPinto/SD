package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBoxFactoryImpl extends UnicastRemoteObject implements DropBoxFactoryRI {

    public DropBoxFactoryImpl() throws RemoteException {
        super();
    }

    @Override
    public DropBoxSessionRI register(String username, String password) throws RemoteException {
//        if(DB.users.containsKey(username)){
//            return null;
//        }
       // User user=new User(username,password);
       // DB.users.put(username,user);
        System.out.println("entrei");
        DropBoxSessionRI sessionImpl=new DropBoxSessionImpl();
        System.out.println("sai");
       // DropBoxSessionRI sessionRI= new DropBoxSessionImpl();
        //DB.session.put(username,sessionImpl);
        return sessionImpl;


    }

    @Override
    public DropBoxSessionRI login(String username, String password) throws RemoteException {
       if(DB.users.containsKey(username)){
           return DB.session.get(username);
       }
        return null;
    }
    public void print() throws RemoteException {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "hello", new Object[]{});

    }
}
