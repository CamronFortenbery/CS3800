package client;

import java.io.Serializable;

public class Message implements Serializable {

    private int msgType = -1;
    private String msg = null;

    // Message types
    public static final int SIGN_OFF = 1;
    public static final int CHAT = 2;
    public static final int SIGN_IN = 3;
    public static final int USERNAME = 4;

    //getters
    public String getMsg() {
        return this.msg;
    }

    public int getMsgType() {
        return this.msgType;
    }

    //setters
    public void setMsg(String m) {
        this.msg = m;
    }

    public void setMsgType(int type) {
        this.msgType = type;
    }

    // constructor
    public Message() {

    }

} 