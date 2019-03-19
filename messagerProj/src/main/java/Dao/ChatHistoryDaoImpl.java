package Dao;

import Utils.DbUtils;
import bean.PrivateChatHistory;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
* @Author: Jiehang CAO
* @Description:
* @Date: 11:32 2019-03-17
*/
@Slf4j
public class ChatHistoryDaoImpl implements ChatHistoryDao {


    private ChatHistoryDaoImpl() {
        super();
    }
    private  static ChatHistoryDaoImpl chatHistoryDao;
    public static ChatHistoryDaoImpl getInstance(){
        if(chatHistoryDao==null){
            synchronized (AccountDaoImpl.class){
                if(chatHistoryDao == null){
                    chatHistoryDao = new ChatHistoryDaoImpl();
                }
            }
        }
        return chatHistoryDao;
    }


    @Override
    public void add(PrivateChatHistory ChatHistory) throws SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        String sql = "insert into messages(receiver,sender,message_text,send_time)values(?,?,?,?)";
        try{
            con = DbUtils.getConnections();
            pst = con.prepareStatement(sql);
            pst.setString(1,ChatHistory.getReceiver());
            pst.setString(2,ChatHistory.getSender());
            pst.setString(3,ChatHistory.getContent());
            pst.setDate(4, (Date) ChatHistory.getSendTime());
            pst.executeUpdate();

        } catch (SQLException e) {
            log.error("add failed");
        }finally {
            DbUtils.close(null,pst,con);
        }


    }

    @Override
    public List<PrivateChatHistory> find(String sender, String reciver, Date send_time) throws SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet set = null;
        PrivateChatHistory chatHistory = null;

        List<PrivateChatHistory> chatList = new ArrayList<>();
        String sql = "select message_text from messages where sender = ? and receiver =?  and send_time = ?";
        try {
            con = DbUtils.getConnections();
            pst = con.prepareStatement(sql);
            pst.setString(1,sender);
            pst.setString(2,reciver);
            pst.setDate(3,send_time);
            set = pst.executeQuery();
            while (set.next()) {
                chatHistory = new PrivateChatHistory();
                chatHistory.setContent(set.getString(1));
                chatHistory.setSender(sender);
                chatHistory.setReceiver(reciver);
                chatHistory.setSendTime(send_time);
                chatList.add(chatHistory);
            }
         } catch(SQLException e){
            log.error("find  error: " + e);
        }finally{
            DbUtils.close(set, pst, con);
        }
        return chatList;
    }

}
