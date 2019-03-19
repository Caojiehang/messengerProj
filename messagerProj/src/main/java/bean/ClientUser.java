package bean;

import lombok.Data;

import java.io.Serializable;

/**
* @Author: Jiehang CAO
* @Description:
* @Date: 11:17 2019-03-16
*/
@Data
public class ClientUser implements Serializable {
    /**
     * user name
     */
    private String userName;
    /**
     * online or offline
     */
    private String status;
    /**
     * whether the message has been read
     */
    private boolean notify;


    public ClientUser() {

    }
}
