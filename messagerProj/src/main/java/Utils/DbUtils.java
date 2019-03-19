package Utils;

import lombok.extern.slf4j.Slf4j;
import java.sql.*;

/**
* @Author: Jiehang CAO
* @Description: util for connect database
* @Date: 17:08 2019-03-03
*/
@Slf4j
public class DbUtils {


    public static String URL="jdbc:postgresql://localhost:5432/postgres";

    public static String USERNAME="postgres";

    public static String PASSWORD="19960706";

    public static String DRIVER = "org.postgresql.Driver";



    public DbUtils() {

    }
    static {
        try {
            Class.forName(DRIVER);
            log.info("success");
        }catch (ClassNotFoundException c) {
            c.printStackTrace();
        } catch (NoClassDefFoundError e) {
            System.out.println("error");
        }
    }

    /**
     * create connection
     * @return
     */
    public static Connection getConnections() {
        Connection con = null;
        try{
            con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Connection failed");
        }
        return con;
    }

    /**
     * close connection
     * @param rs
     * @param stat
     * @param conn
     */
    public static void close(ResultSet rs, Statement stat, Connection conn){
        try {
            if(rs!=null)rs.close();
            if(stat!=null)stat.close();
            if(conn!=null)conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
