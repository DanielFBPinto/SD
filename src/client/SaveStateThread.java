package client;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;

public class SaveStateThread extends Thread {
    private boolean keepRunning = true;
    private final HashMap<File, Timestamp> currentState;
    private final String owner;

    /**
     * Guarda do lado do cliente o estado atual das suas pastas a cada 5s
     * @param owner
     * @param cs
     */
    public SaveStateThread(String owner, HashMap cs) {
        this.currentState = cs;
        this.owner = owner;
    }

    public void run() {
        while (keepRunning) {
            synchronized (currentState) {
                DropBoxClient.insertCurrentState(currentState, owner);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
