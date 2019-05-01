package server;

import java.io.File;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DropBoxFactoryImpl extends UnicastRemoteObject implements DropBoxFactoryRI {

    public DropBoxFactoryImpl() throws RemoteException {
        super();
    }

    @Override
    public DropBoxSessionRI register(String username, String password) throws RemoteException {
        if (DB.users.containsKey(username)) {
            return null;
        }
        User user = new User(username, password);
        DB.users.put(username, user);
        DropBoxSessionImpl sessionImpl = new DropBoxSessionImpl();
        DB.session.put(username, sessionImpl);
        System.out.println(System.getProperty("user.dir"));
        new File(System.getProperty("user.dir") + "../../../../Users/" + username).mkdirs();
        return sessionImpl;

    }

    @Override
    public DropBoxSessionRI login(String username, String password) throws RemoteException {
        if (DB.users.containsKey(username)) {
            return DB.session.get(username);
        }
        return null;
    }
}
