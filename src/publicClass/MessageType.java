package publicClass;

import java.io.Serializable;

/**
 * @author sydnut
 * @version 1.0
 * @time 2024/11/1
 */
public enum MessageType implements Serializable {
    MSG_LOGIN_SUCCESS(1),MSG_LOGIN_FAILURE(2),MSG_ASK_ONLINE_LIST(3),MSG_SIGN_UP(4),
    MSG_TEST_1(5),MSG_TEST_2(6);
        private final int MSG_TYPE;
        MessageType(int type){
            MSG_TYPE=type;
        }
}
