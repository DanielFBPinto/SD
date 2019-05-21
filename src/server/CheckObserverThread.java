package server;

import client.DropBoxClient;
import client.DropBoxObserverRI;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckObserverThread extends Thread {
    private boolean keepRunning = true;
    private final ArrayList<DropBoxObserverRI> observers;

    public CheckObserverThread(ArrayList<DropBoxObserverRI> obs) {
        this.observers = obs;
    }

    public void run() {
        while (keepRunning) {
            synchronized (observers) {
                for (DropBoxObserverRI o : observers) {
                    try {
                        o.getStatus();
                    } catch (Exception e) {
                        observers.remove(o);
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
