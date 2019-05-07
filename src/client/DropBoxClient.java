package client;


import server.DropBoxFactoryRI;
import server.DropBoxSessionRI;
import server.DropBoxSubjectRI;
import server.DropboxSubjectImpl;
import util.rmisetup.SetupContextRMI;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
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
     * Interface remota do Observador associado ao nosso client
     */
    private DropBoxObserverImpl dropBoxObserverImpl;
    /**
     * Interface remota da sessão associada ao nosso client
     */
    private DropBoxSessionRI dropBoxSessionRI;

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

    private void playService(String[] args) {
        /* Diretório do lado do cliente */
        String userPath = System.getProperty("user.dir") + "../../../../data/Cliente/Dropbox(" + args[4] + ")/" + args[4];
        try {
            /* Registar User */
            if (args[3].compareTo("register") == 0) {
                this.dropBoxSessionRI = this.dropBoxFactoryRI.register(args[4], args[5]);
                if (this.dropBoxSessionRI != null) {
                    new File(userPath).mkdirs();
                } else {
                    System.out.println("Username já a ser utilizado");
                }
            } else if (args[3].compareTo("share") == 0){        /* Sharing Folders */
                this.dropBoxSessionRI = this.dropBoxFactoryRI.login(args[4], args[5]);
                this.dropBoxSessionRI.shareOwnerSubjectWith(args[6]);
            }else{     /* Login User */
                this.dropBoxSessionRI = this.dropBoxFactoryRI.login(args[4], args[5]);
            }
            /* Login/Registo bem sucedidos */
            if (this.dropBoxSessionRI != null && !(args[3].compareTo("share") == 0)) {
                /* Receber Subject do owner */
                DropBoxSubjectRI mySubject = this.dropBoxSessionRI.getOwnerSubject();
                /* Criar Observer */
                this.dropBoxObserverImpl = new DropBoxObserverImpl(new File(userPath), mySubject);
                /* Dar attach do observador do cliente */
                mySubject.attach(this.dropBoxObserverImpl);
                /* Testar criação de pasta */
                this.dropBoxObserverImpl.createFolder(".", "jogos");
                /* Dar detach do observador do cliente */
                mySubject.detach(this.dropBoxObserverImpl);
            } else {
                System.out.println("Login incorreto");
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
