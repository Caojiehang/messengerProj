package Client.Controller;

import Client.MainApp;
import Dao.AccountDaoImpl;
import Utils.ControllerStage;
import Utils.StageController;
import bean.Account;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
* @Author: Jiehang CAO
* @Description:
* @Date: 11:47 2019-03-14
*/
@Slf4j
public class ResetPassController implements ControllerStage, Initializable {

    StageController stageController;


    static Account account;



    static  AccountDaoImpl accountDao = AccountDaoImpl.getInstance();

    public void setStageController(StageController stageController) {
       this.stageController = stageController;
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML private TextField username;

    @FXML private TextField email;

    @FXML private PasswordField newPassword;
    @FXML private PasswordField confirmPassword;


    @FXML protected void resetPass() throws SQLException {
        if(checkInfo(username.getText(),email.getText())) {
            if (checkPass(newPassword.getText(),confirmPassword.getText())){
                account.setPassword(newPassword.getText());
                accountDao.update(account);
            }
        }

    }

    @FXML protected void backtoLogin() {
        stageController.setStage(MainApp.loginViewID,MainApp.resetPassViewID);
        cleanAllInfo();

    }

    private static boolean checkInfo(String username,String email) {
       try {
           account = accountDao.findByName(username);
           if(account != null) {
               if(email.equals(account.getEmail())) {
                   return true;
               } else {
                   return false;
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return false;
    }

    private static boolean checkPass(String pass,String confirmPass) {
           if(confirmPass.equals(pass)) {
               return true;
           } else {
               return false;
           }
    }

    private void cleanAllInfo() {
        username.clear();
        newPassword.clear();
        confirmPassword.clear();
        email.clear();
    }

}
