package bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
* @Author: Jiehang CAO
* @Description: Message bean
* @Date: 23:13 2019-03-14
*/
@Data
public class Message implements Serializable{
    /**
     * message content
     */
    private String content = null;
    /**
     * message speaker
     */
    private String speaker = null;
    /**
     * message time
     */
    private String timer = null;

    private ArrayList<String> imageList = null;


}
