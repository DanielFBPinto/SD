package client;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;

public class SaveStateThread extends Thread {
    private boolean keepRunning = true;
    private final HashMap<File, Timestamp> currentState;
    private final String owner;

    public SaveStateThread(String owner, HashMap cs) {
        this.currentState = cs;
        this.owner = owner;
    }

    public void run() {
        while (keepRunning) {
            synchronized (currentState) {
                DropBoxClient.insertCurrentState(currentState, owner);
//                System.out.println("Estado guardado!");
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
