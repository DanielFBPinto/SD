package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class DropBoxSessionImpl implements DropBoxSessionRI {

    private String owner;

    public DropBoxSessionImpl(String owner) throws RemoteException {
        this.owner = owner;
        export();
    }

    private void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    /**
     * Retorna ao Utilizador o seu próprio subject
     * @return
     * @throws RemoteException
     */
    @Override
    public DropBoxSubjectRI getOwnerSubject() throws RemoteException {
        return DB.getSubjects().get(this.owner);
    }


    /**
     * Retorna ao Utilizador todos os subjects que são partilhados com ele
     * @return
     * @throws RemoteException
     */
    @Override
    public ArrayList<String> listSub() throws RemoteException {
        return DB.getShared().get(owner);
    }

    /**
     * Retorna ao utilizador um subject dado o owner enviado, antes de ser enviado é verificado se o utilizador tem
     * acesso ao subject indicado
     * @param owner
     * @return
     * @throws RemoteException
     */
    @Override
    public DropBoxSubjectRI getSubject(String owner) throws RemoteException {
        if (DB.getShared().get(this.owner).contains(owner))
            return DB.getSubjects().get(owner);
        return null;
    }

    /**
     * Partilha a própria pasta com outro utilizador
     * @param user
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean shareOwnerSubjectWith(String user) throws RemoteException {
        if (DB.getUser(user) == null) {
            return false;
        }
        ArrayList<String> a = DB.getShared().get(user);
        if (a == null)
            a = new ArrayList<>();
        a.add(owner);
        DB.getShared().put(user, a);
        DB.saveShared();
        return true;
    }
}
