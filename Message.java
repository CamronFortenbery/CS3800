import java.io.Serializable;

public class Message implements Serializable {

    private int msgType = -1;
    private String msg = null;

    // Might need to implement message type
    public int SIGN_OFF = 1;
    public int CHAT = 2;

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
