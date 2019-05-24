package client;


import server.DropBoxFactoryRI;
import server.DropBoxSessionRI;
import server.DropBoxSubjectRI;
import util.rmisetup.SetupContextRMI;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBoxClient {

    /**
     * Context for connecting a RMI client to a RMI Servant
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold the Servant proxy
     */
    private DropBoxFactoryRI dropBoxFactoryRI;
    /**
     * Interface remota da sessão associada ao nosso client
     */
    private DropBoxSessionRI dropBoxSessionRI;
    /**
     * File da nossa pasta Dropbox(Cliente)
     */
    private static File path;
    /**
     * O estado atual de cada uma das pastas do cliente
     */
    private static HashMap<String, HashMap<File, Timestamp>> currentStates;

    public static void main(String[] args) {
        if (args != null && args.length < 2) {
            System.err.println("usage: java [options] edu.ufp.sd.calculator.server.FactoryStub <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            //1. ============ Setup client RMI context ============
            DropBoxClient hwc = new DropBoxClient(args);
            //2. ============ Lookup service ============
            hwc.lookupService();
            //3. ============ Play with service ============
            hwc.playService(args);
        }
    }

    public DropBoxClient(String args[]) {
        try {
            //List ans set args
            printArgs(args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(DropBoxClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Remote lookupService() {
        try {
            //Get proxy to rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going to lookup service @ {0}", serviceUrl);

                //============ Get proxy to HelloWorld service ============
                dropBoxFactoryRI = (DropBoxFactoryRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return dropBoxFactoryRI;
    }

    private void loadCurrentState() {
        if (new File(path + "/.conf").exists()) {
            try {
                ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(path + "/.conf"));
                Object obj = objIn.readObject();
                if (obj instanceof HashMap) {
                    currentStates = (HashMap<String, HashMap<File, Timestamp>>) obj;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Guarda localmente o estado das pastas de um certo utilizador, para utilizar quando fizer login novamente
     * @param cs
     * @param user
     */
    public static void insertCurrentState(HashMap cs, String user){
        if(currentStates == null)
            currentStates = new HashMap<>();
        currentStates.put(user,cs);
        try {
            FileOutputStream out = new FileOutputStream(path + "/.conf");
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(currentStates);
            objOut.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Função onde se chama as funcionalidades necessárias para registo, login e partilha de uma pasta com outro
     * utilizador, dependendo do que o utilizador enviar nos argumentos.
     * @param args
     */
    private void playService(String[] args) {
        /* Diretório do lado do cliente */
        path = new File(System.getProperty("user.dir") + "../../../../data/Cliente/Dropbox(" + args[4] + ")");
        loadCurrentState();
        try {
            /* Registar User */
            if (args[3].compareTo("register") == 0) {
                path.mkdirs();
                if (!this.dropBoxFactoryRI.register(args[4], args[5]))
                    System.out.println("Username já está a ser utilizado");
            } else if (args[3].compareTo("share") == 0) {        /* Sharing Folders */
                this.dropBoxSessionRI = this.dropBoxFactoryRI.login(args[4], args[5]);
                if (!this.dropBoxSessionRI.shareOwnerSubjectWith(args[6]))
                    System.out.println("Erro a partilhar");
            } else {     /* Login User */
                this.dropBoxSessionRI = this.dropBoxFactoryRI.login(args[4], args[5]);
                /* Receber Subject do owner */
                DropBoxSubjectRI mySubject = this.dropBoxSessionRI.getOwnerSubject();
                /* Criar Observer */
                if (currentStates != null && currentStates.containsKey(mySubject.getOwner().getUsername()))
                    new DropBoxObserverImpl(mySubject, path, currentStates.get(mySubject.getOwner().getUsername()));
                else
                    new DropBoxObserverImpl(mySubject, path);
                /* Criar um observer por folder partilhada */
                if (this.dropBoxSessionRI.listSub() != null) {
                    for (String user : this.dropBoxSessionRI.listSub()) {
                        if (currentStates != null && currentStates.containsKey(user))
                            new DropBoxObserverImpl(this.dropBoxSessionRI.getSubject(user), path, currentStates.get(user));
                        else
                            new DropBoxObserverImpl(this.dropBoxSessionRI.getSubject(user), path);
                    }
                }
            }
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "goint to finish, bye. ;)");
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printArgs(String args[]) {
        for (int i = 0; args != null && i < args.length; i++) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "args[{0}] = {1}", new Object[]{i, args[i]});
        }
    }
}
