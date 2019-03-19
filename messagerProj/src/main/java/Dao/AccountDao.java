package Dao;

import bean.Account;

import java.sql.SQLException;
import java.util.List;

/**
* @Author: Jiehang CAO
* @Description:
* @Date: 17:32 2019-03-03
*/
public interface AccountDao {

    //add
    void add(Account account) throws SQLException;

    //update
    public void update(Account account)throws SQLException;

    //delete
    public void delete(int id)throws SQLException;

    //query
    public Account findById(int id)throws SQLException;

    public Account findByName(String username)throws SQLException;

    //find all
    public List<Account> findAll()throws SQLException;




}
