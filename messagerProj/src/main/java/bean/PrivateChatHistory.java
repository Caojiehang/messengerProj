package bean;

import lombok.Data;

import java.sql.Date;


/**
* @Author: Jiehang CAO
* @Description:
* @Date: 11:25 2019-03-17
*/
@Data
public class PrivateChatHistory  {

    private int messageId;

    private String content;

    private String sender;

    private String receiver;

    private Date sendTime;


    public PrivateChatHistory(String sender,String receiver,String content,Date sendTime) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sendTime = sendTime;
    }

    public PrivateChatHistory() {}


}
