package Dao;

import bean.PrivateChatHistory;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ChatHistoryDao  {
    public void add(PrivateChatHistory privateChatHistory) throws SQLException;

    public List<PrivateChatHistory> find(String receiver, String sender, Date send_time) throws SQLException;


}
