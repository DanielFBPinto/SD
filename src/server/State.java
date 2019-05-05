package server;

import java.io.Serializable;

public class State implements Serializable {
    //////////// POR ALTERAR
    private String user;
    private String msg;

    public State(String user, String msg) {
        this.user = user;
        this.msg = msg;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
