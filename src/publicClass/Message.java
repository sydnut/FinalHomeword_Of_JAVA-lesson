package publicClass;

import java.io.Serializable;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/1
 */
public class Message implements Serializable {
    private MessageType type;
    private String content;
    private String sender;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
