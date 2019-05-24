package server;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class DB {
    /**
     * Caminho para os ficheiros da base de dados
     */
    private static String path = System.getProperty("user.dir") + "../../../../db";
    /**
     * Hashmap para associação dos subjects com os seus owners
     */
    private static HashMap<String, DropBoxSubjectRI> subjects = new HashMap<>();
    /**
     * Hashmap onde guardo para cada utilizador a lista de utilizadores que partilharam a sua pasta com ele
     */
    private static HashMap<String, ArrayList<String>> shared = new HashMap<>();

    /**
     * Verifica se é possível inserir um utilizador e insere
     * @param user
     */
    public static void putUser(User user) {
        try (FileInputStream in = new FileInputStream(path + "/users.properties")) {
            Properties prop = new Properties();
            prop.load(in);
            in.close();
            FileOutputStream out = new FileOutputStream(path + "/users.properties");
            prop.setProperty(user.getUsername(), user.getPassword());
            prop.store(out, null);
            out.close();
        } catch (Exception ex) {
            Properties prop = new Properties();
            prop.setProperty(user.getUsername(), user.getPassword());
            try {
                prop.store(new FileOutputStream(path + "/users.properties"), null);
            } catch (IOException e) {
                System.out.println("Sorry, unable to find users.properties");
            }
        }
    }

    /**
     * Retorno um utilizador dado um username
     * @param name
     * @return
     */
    public static User getUser(String name) {
        try (InputStream input = new FileInputStream(path + "/users.properties")) {

            Properties prop = new Properties();

            //load a properties file from class path, inside static method
            prop.load(input);
            if (prop.containsKey(name))
                return new User(name, prop.getProperty(name));
            return null;

        } catch (IOException ex) {
            System.out.println("Sorry, unable to find users.properties");
        }
        return null;
    }

    /**
     * Guardo num ficheiro as pastas partilhadas de todos os utilizadores
     */
    public static void saveShared() {
        try {
            FileOutputStream out = new FileOutputStream(path + "/shared.bin");
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(shared);
            objOut.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Carrego as pastas partilhadas para o servidor
     */
    public static void loadShared() {
        if (new File(path + "shared.bin").exists()) {
            try {
                ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(path + "/shared.bin"));
                Object obj = objIn.readObject();
                if (obj instanceof HashMap) {
                    shared = (HashMap<String, ArrayList<String>>) obj;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static HashMap<String, DropBoxSubjectRI> getSubjects() {
        return subjects;
    }

    public static HashMap<String, ArrayList<String>> getShared() {
        return shared;
    }


}
