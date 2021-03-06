package server;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DropBoxFactoryImpl extends UnicastRemoteObject implements DropBoxFactoryRI {

    private File path = new File(System.getProperty("user.dir") + "../../../../data/Server");

    public DropBoxFactoryImpl() throws RemoteException {
        super();
        runFunction();
    }

    /**
     * Função usada para carregar todos os subjects existentes para o servidor
     * @throws RemoteException
     */
    private void runFunction() throws RemoteException {
        for (File f : this.path.listFiles()) {
            DropBoxSubjectImpl d = new DropBoxSubjectImpl(DB.getUser(f.getName()), f);
            DB.getSubjects().put(f.getName(), d);
        }
        DB.loadShared();
    }

    /**
     * Regista o utilizador na base de dados, cria o seu path do lado do servidor e associa ao seu utilizador um subject
     * na base de dados
     * @param username
     * @param password
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean register(String username, String password) throws RemoteException {
        /* Verifica se já existe o username na DB */
        if (DB.getUser(username) != null) {
            return false;
        }
        // Adicionar dados do User na base de dados
        User user = new User(username, password);
        DB.putUser(user);
        // Criar pasta do User no Server
        File path = new File(this.path.getPath() + "/" + username);
        path.mkdirs();
        /* Criar subject */
        DropBoxSubjectImpl d = new DropBoxSubjectImpl(user, path);
        DB.getSubjects().put(username, d);
        // retorna verdade
        return true;
    }

    /**
     * Retorna ao utilizador uma sessão associada.
     * @param username
     * @param password
     * @return
     * @throws RemoteException
     */
    @Override
    public DropBoxSessionRI login(String username, String password) throws RemoteException {
        if (DB.getUser(username).getPassword().compareTo(password) == 0)
            return new DropBoxSessionImpl(username);
        return null;
    }
}
