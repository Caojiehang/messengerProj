package Dao;

import Utils.DbUtils;
import bean.Account;
import lombok.extern.slf4j.Slf4j;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
* @Author: Jiehang CAO
* @Description: implementation foe AccountDao
* @Date: 17:37 2019-03-03
*/
@Slf4j
public class AccountDaoImpl implements AccountDao {

    private AccountDaoImpl() {
        super();
    }
    private  static AccountDaoImpl userDao;
    public static AccountDaoImpl getInstance(){
        if(userDao==null){
            synchronized (AccountDaoImpl.class){
                if(userDao == null){
                    userDao = new AccountDaoImpl();
                }
            }
        }
        return userDao;
    }
    /**
     * Add new data to database, id increases automatically
     * @param account
     * @throws SQLException
     */
    public void add(Account account) throws SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        String sql = "insert into account(username,password,email)values(?,?,?)";
        try{
            con = DbUtils.getConnections();
            pst = con.prepareStatement(sql);
            pst.setString(1,account.getUsername());
            pst.setString(2,account.getPassword());
            pst.setString(3,account.getEmail());
            pst.executeUpdate();

        } catch (SQLException e) {
            log.error("add failed");
        }finally {
            DbUtils.close(null,pst,con);
        }

    }

    /**
     * update database
     * @param account
     * @throws SQLException
     */
    public void update(Account account) throws SQLException {
        Connection con = null;
        PreparedStatement pst = null;

        String sql = "update account set password=? where username=?";
        try{
            con = DbUtils.getConnections();
            pst = con.prepareStatement(sql);
            //pst.setInt(1,account.getId());
            pst.setString(1, account.getPassword());
            pst.setString(2, account.getUsername());
            pst.executeUpdate();

        }catch (SQLException e) {
            log.error("Update failed" + "reason:" +e);
        }finally {
            DbUtils.close(null,pst,con);
        }

    }

    /**
     * Delete data from database
     * @param id
     * @throws SQLException
     */
    public void delete(int id) throws SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        String sql = "delete from account where id=?";
        try {
            con = DbUtils.getConnections();
            pst = con.prepareStatement(sql);
            pst.setInt(1,id);
            pst.executeUpdate();
        } catch (SQLException e) {
            log.error("failed deleting");
        }finally {
            DbUtils.close(null,pst,con);
        }
    }

    /**
     *
     * @param id
     * @return
     * @throws SQLException
     */

    public Account findById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Account account = null;
        String sql = "select username,password,email from account where id=?";
        try{
            conn = DbUtils.getConnections();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                account = new Account();
                account.setId(id);
                account.setUsername(rs.getString(1));
                account.setPassword(rs.getString(2));
                account.setEmail(rs.getString(3));
            }
        }catch(SQLException e){
          log.error("findbyid error: " + e);
        }finally{
            DbUtils.close(rs, ps, conn);
        }
        return account;
    }

    /**
     *
     * @param username
     * @return
     * @throws SQLException
     */
    public Account findByName(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Account account = null;
        String sql = "select id,password,email from account where username=?";
        try{
            conn = DbUtils.getConnections();
            ps = conn.prepareStatement(sql);
            ps.setString(1,username );
            rs = ps.executeQuery();
            if(rs.next()){
                account = new Account(rs.getInt(1),username,rs.getString(2),rs.getString(3));
            }
        }catch(SQLException e){
            log.error("error:" + e);
            throw new SQLException("根据name查询数据失败");
        }finally{
            DbUtils.close(rs, ps, conn);
        }
        return account;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public List<Account> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Account account = null;
        List<Account> persons = new ArrayList<Account>();
        String sql = "select id,username,password,email from account";
        try{
            conn = DbUtils.getConnections();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                account = new Account(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4));
                persons.add(account);
            }
        }catch(SQLException e){
            log.error("error: "+ e);
            throw new SQLException("查询所有数据失败");
        }finally{
            DbUtils.close(rs, ps, conn);
        }
        return persons;
    }

}
