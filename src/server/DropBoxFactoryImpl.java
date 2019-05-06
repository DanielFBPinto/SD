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
        /* Verifica se já existe o username na DB */
        if (DB.getUser(username) != null) {
            return null;
        }
        // Adicionar dados do User na base de dados
        User user = new User(username, password);
        DB.putUser(user);
        // Criar pasta do User no Server
        String serverPath = System.getProperty("user.dir") + "../../../../data/Server/" + username;
        File path = new File(serverPath);
        path.mkdirs();
        // Criar Subject e associa-lo ao folder do user
        DropboxSubjectImpl dropboxSubjectImpl = new DropboxSubjectImpl(user, path);
        // Criar Sessão e associa-la ao subject do user
        DropBoxSessionImpl sessionImpl = new DropBoxSessionImpl(dropboxSubjectImpl);
        // Guardar sessão na base de dados
        DB.insertSession(username, sessionImpl);
        // retornar sessão
        return sessionImpl;
    }

    @Override
    public DropBoxSessionRI login(String username, String password) throws RemoteException {
        return DB.getSession(username, password);
    }
}
