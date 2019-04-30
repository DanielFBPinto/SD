package server;

import java.util.HashMap;

public class DB {
    protected static HashMap<String,User> users =new HashMap<>();
    protected static HashMap<String,DropBoxSessionRI> session =new HashMap<>();
}
