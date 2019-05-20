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
            DropBoxClient.insertCurrentState(currentState, owner);
        }
        try {
            Thread.sleep(17500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
