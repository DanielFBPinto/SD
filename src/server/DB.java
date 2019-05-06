package server;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class DB {
    private static String path = System.getProperty("user.dir") + "../../../../db";
    private static HashMap<String, DropBoxSessionImpl> session = new HashMap<>();

    public static void putUser(User user) {
        Properties properties = new Properties();
        properties.setProperty(user.getUsername(), user.getPassword());
        try {
            properties.store(new FileOutputStream(path + "/users.properties"), null);
        } catch (IOException e) {
            System.out.println("Sorry, unable to find users.properties");
        }
    }

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

    public static void saveSessions() {
        try {
            FileOutputStream out = new FileOutputStream(path + "/sessions.txt");
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(session);
            objOut.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void loadSessions() {
        try {
            ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(path + "/sessions.txt"));
            Object obj = objIn.readObject();
            if (obj instanceof HashMap) {
                session = (HashMap<String, DropBoxSessionImpl>) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertSession(String key, DropBoxSessionImpl value) {
        if(session == null)
            loadSessions();
        session.put(key,value);
        saveSessions();
    }

    public static DropBoxSessionRI getSession(String username, String password) {
        if(session.isEmpty())
            loadSessions();
        if(getUser(username).getPassword().compareTo(password) == 0)
            return session.get(username);
        return null;
    }
}
