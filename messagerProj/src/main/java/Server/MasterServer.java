package Server;

import Dao.AccountDaoImpl;
import bean.Account;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
* @Author: Jiehang CAO
* @Description:
* @Date: 13:57 2019-03-16
*/
public class MasterServer {
    /**
     * 用户列表
     */
    private ArrayList<Account> users;

    public ServerSocket masterServer;
    public BackEndServer workServer;

    private int port = 8888;

    public void start() {
        users = new ArrayList<Account>();
        try {
            masterServer = new ServerSocket(port);
            try {
                users = (ArrayList<Account>) AccountDaoImpl.getInstance().findAll();
                for (Account u:users) {
                    u.setStatus("offline");
                }
                System.out.println("get user"+users.size());
            } catch (SQLException e) {
                System.out.println("userList init failed");
                e.printStackTrace();
            }
            System.out.println("server loading");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                workServer = new BackEndServer(masterServer.accept(), users);
                workServer.start();
                System.out.println("workServer product");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
