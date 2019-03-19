package Dao;

import bean.PrivateChatHistory;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTest {

    public static void main(String[] args) {
        ChatHistoryDaoImpl chatHistoryDao =ChatHistoryDaoImpl.getInstance();
        List<PrivateChatHistory> chatHistoryList = new ArrayList<>();

        try {
             chatHistoryList= chatHistoryDao.find("cjh","zzy", Date.valueOf("2019-3-17"));
             for(PrivateChatHistory chatHistory:chatHistoryList) {
                 System.out.println(chatHistory.getContent());
             }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
