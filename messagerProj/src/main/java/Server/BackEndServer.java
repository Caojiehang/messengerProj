package Server;

import Dao.AccountDaoImpl;
import Utils.GsonUtils;
import bean.Account;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static Utils.Constants.*;

/**
* @Author: Jiehang CAO
* @Description: server thread to obtain client message and reply message to client
* @Date: 11:23 2019-03-16
*/
public class BackEndServer extends Thread {

    private Account onlineUser;

    private Socket socket;

    private ArrayList<Account> users;

    private BufferedReader reader;

    private PrintWriter writer;

    private boolean isLogOut = false;

    private long currentTime = 0;

    private Gson gson;

    public BackEndServer(Socket socket,ArrayList users) {
        super();
        gson = new Gson();
        this.socket = socket;
        this.users = users;
    }

    /**
     * Server works
     */
    @Override
    public void run() {
        try {
            currentTime = new Date().getTime();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(),true);
            String readLine;

            while (true) {
                //socket heart listening
                long newTime = new Date().getTime();
                if(newTime - currentTime > 2000) {
                    logOut();
                } else {
                    currentTime = newTime;
                }
                readLine = reader.readLine();
                if(readLine == null) {
                    logOut();
                } else {
                    handleMessage(readLine);
                    sendMessageToClient();
                }
                if(isLogOut) {

                    //close io stream
                    reader.close();
                    writer.close();
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            logOut();
        }


    }

    /**
     *
     * @param readLine
     */
    private void handleMessage(String readLine) {
        System.out.println("Handle message" + readLine);
        Map<Integer,Object> gsonMap = GsonUtils.GsonToMap(readLine);
        /**
         * message partID
         */
        Integer command = GsonUtils.Double2Integer((Double)gsonMap.get(COMMAND));

        HashMap map = new HashMap();
        String userName;
        String password;
        String email;
            switch (command) {
                /**
                 * to get online user
                 */
            case COM_GROUP:
                writer.println(getGroup());
                System.out.println(onlineUser.getUsername() + " request for online users info..");
                break;
                /**
                 * when client log in it will send password and account to server to check
                 */
            case COM_LOGIN:
                userName = (String) gsonMap.get(USERNAME);
                password = (String) gsonMap.get(PASSWORD);
                boolean find = false;
                /**
                 * check login
                 */
                for(Account account: users) {
                    if(account.getUsername().equals(userName)) {
                        if(!account.getPassword().equals(password)) {
                            map.put(COM_DESCRIPTION,"wrong account or password");
                            break;
                        }
                        if(account.getStatus().equals("online")) {
                            map.put(COM_DESCRIPTION,"Account has log in");
                            break;
                        }
                        currentTime = new Date().getTime();
                        map.put(COM_RESULT,SUCCESS);
                        map.put(COM_DESCRIPTION,userName+"login success");
                        account.setStatus("online");
                        writer.println(gson.toJson(map));
                        onlineUser = account;
                        broadcast(getGroup(),COM_SIGNUP);
                        find = true;
                        System.out.println("user: " + userName + " is online now!");
                        break;
                    }
                }
                /**
                 * can find the account
                 */
                if(!find) {
                    map.put(COM_RESULT,FAILED);
                    if (!map.containsKey(COM_DESCRIPTION))
                        map.put(COM_DESCRIPTION, userName + "didn't register account");
                    writer.println(gson.toJson(map)); //返回消息给服务器
                }
                break;
                /**
                 * client try to register it will send info to server; and server do operations
                 */
                case COM_SIGNUP:
                    userName = (String) gsonMap.get(USERNAME);
                    password = (String) gsonMap.get(PASSWORD);
                    email = (String) gsonMap.get(EMAIL);
                    map.put(COMMAND, COM_RESULT);
                    if (createUser(userName, password,email)) {
                        //需要马上变更心跳
                        currentTime = new Date().getTime();
                        //存储信息
                        map.put(COM_RESULT, SUCCESS);
                        map.put(COM_DESCRIPTION, "success");
                        writer.println(gson.toJson(map));
                        broadcast(getGroup(),COM_SIGNUP);
                        System.out.println("user: " + userName + "register and now online");
                    } else {
                        map.put(COM_RESULT, FAILED);
                        map.put(COM_DESCRIPTION, userName + "has been registered");
                        writer.println(gson.toJson(map)); //返回消息给服务器
                        System.out.println(userName + " has been registered");
                    }
                    break;
                /**
                 * when client try to chat with others, send message  to  server and server process the message
                 *
                 */
            case COM_CHATWITH:
                String receiver = (String) gsonMap.get(RECEIVER);
                map = new HashMap();
                map.put(COMMAND,COM_CHATWITH);
                map.put(SPEAKER,gsonMap.get(SPEAKER));
                map.put(RECEIVER,gsonMap.get(RECEIVER));
                map.put(CONTENT,gsonMap.get(CONTENT));
                map.put(TIME,getFormatDate());
                for(Account account:users) {
                    if(account.getUsername().equals(receiver)) {
                        account.addMsg(gson.toJson(map));
                        break;
                    }
                }
                onlineUser.addMsg(gson.toJson(map));
                break;
                /**
                 * chat with all people
                 */
            case COM_CHATALL:
                map = new HashMap();
                map.put(COMMAND, COM_CHATALL);
                map.put(SPEAKER, onlineUser.getUsername());
                map.put(TIME, getFormatDate());
                map.put(CONTENT, gsonMap.get(CONTENT));
                broadcast(gson.toJson(map), COM_MESSAGEALL);
                break;
                }
        }

    /**
     *
     */
        private void sendMessageToClient() {
            String message;
            if (onlineUser != null)
                while ((message = onlineUser.getMsg()) != null) {
                    writer.println(message); //write it will  auto flush.
                    System.out.println(onlineUser.getUsername() + "的数据仓发送 message: " + message + "剩余 size" + onlineUser.getSession().size());
                }

    }

    /**
     *
     */
    private void logOut() {
        if (onlineUser == null)
            return;
        System.out.println("user: " + onlineUser.getUsername() + " is offline");
        // still hold this user and change it's status
        onlineUser.setStatus("offline");
        for (Account account : users) {
            if (account.getUsername().equals(onlineUser.getUsername()))
                account.setStatus("offline");
        }
        broadcast(getGroup(), COM_LOGOUT);
        isLogOut = true;
    }

    /**
     *
     * @param message
     * @param type
     */
    private void broadcast(String message, int type) {
        System.out.println(onlineUser.getUsername() + " begin broadcast " + message);

        switch (type) {
            case COM_MESSAGEALL:
                for (Account u : users) {
                    u.addMsg(message);
                }
                break;
            case COM_LOGOUT:
            case COM_SIGNUP:
                for (Account u : users) {
                    if (!u.getUsername().equals(onlineUser.getUsername())) {
                        u.addMsg(message);
                    }
                }
                break;
        }


    }

    /**
     *
     * @return
     */
    private String getGroup() {
        String[] userlist = new String[users.size() * 2];
        int j = 0;
        for (int i = 0; i < users.size(); i++, j++) {
            userlist[j] = users.get(i).getUsername();
            userlist[++j] = users.get(i).getStatus();
        }
        HashMap map = new HashMap();
        map.put(COMMAND, COM_GROUP);
        map.put(COM_GROUP, userlist);
        return gson.toJson(map);
    }

    /**
     *
     * @return
     */
    public String getFormatDate() {
        Date date = new Date();
        long times = date.getTime();//时间戳
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * register method
     * @param userName
     * @param password
     * @param email
     * @return
     */
    private boolean createUser(String userName, String password,String email) {
        for (Account user : users) {
            if (user.getUsername().equals(userName)) {
                return false;
            }
        }
        //add user to userList
        Account newUser = new Account(userName, password,email);
        newUser.setStatus("online");
        users.add(newUser);
        //  add user to db
        try {
            AccountDaoImpl.getInstance().add(newUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onlineUser = newUser;
        return true;
    }

}
