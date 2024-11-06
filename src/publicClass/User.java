package publicClass;

import java.io.Serializable;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/1
 */
public class User implements Serializable {
    private String id;
    private String pwd;
    private MessageType messageType;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
