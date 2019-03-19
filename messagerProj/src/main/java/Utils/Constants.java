package Utils;

/**
* @Author: Jiehang CAO
* @Description: this is to store message id and status
* @Date: 23:06 2019-03-13
*/
public class Constants {
    /**
     * private message
     */
    public final static boolean SINGLE = true;
    /**
     * group chatting
     */
    public final static boolean GROUP = false;

    /**
     * operation success id
     */
    public final static int SUCCESS = 0x01;
    /**
     * operation failed id
     */
    public final static int FAILED = 0x02;

    /**
     * message head id
     */
    public static Integer  COMMAND = 0x10;
    /**
     * time info id
     */
    public static Integer  TIME = 0x11;
    /**
     * user name id
     */
    public static Integer  USERNAME = 0x12;
    /**
     * password id
     */
    public static Integer  PASSWORD = 0x13;
    /**
     * speaker id
     */
    public static Integer  SPEAKER = 0x14;
    /**
     * receiver id
     */
    public static Integer  RECEIVER = 0x15;
    /**
     * content id
     */
    public static Integer  CONTENT= 0x16;
    public static Integer EMAIL = 0X17;

    /**
     * log in id
     */
    public final static int COM_LOGIN = 0x20;
    /**
     * sign up id
     */
    public final static int COM_SIGNUP = 0x21;
    /**
     * result id
     */
    public final static int COM_RESULT = 0x22;
    /**
     * some descriptions id
     */
    public final static int COM_DESCRIPTION = 0x23;
    /**
     * log out id
     */
    public final static int COM_LOGOUT =0x24;

    public final static int COM_CHATWITH = 0x25;

    public final static int COM_GROUP = 0x26;

    public final static int COM_CHATALL = 0x27;
    public final static int COM_KEEP = 0x28;
    public final static int COM_MESSAGEALL = 0X29;


}
