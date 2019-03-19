package Client.Controller;

import Client.MainApp;
import Client.model.ClientModel;
import Dao.AccountDaoImpl;
import Utils.ControllerStage;
import Utils.StageController;
import bean.Account;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
/**
* @Author: Jiehang CAO
* @Description: login view Controller
* @Date: 23:13 2019-03-13
*/
@Slf4j
public class LoginController implements ControllerStage, Initializable {

    StageController stageController;


    public void setStageController(StageController stageController) {
        this.stageController = stageController;
        model = ClientModel.getInstance();
    }

    public void initialize(URL location, ResourceBundle resources) {
        try {
            IPAddress.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * username
     */
    @FXML
    private TextField username;
    /**
     * password
     */
    @FXML
    private PasswordField passwordField;
    /**
     * socket ip address
     */
    @FXML TextField IPAddress;

    ClientModel model;

    /**
     * login button event
     * @param event
     */
    @FXML
    protected synchronized void submitmethod(ActionEvent event) {
        StringBuffer buf = new StringBuffer();
         if(model.CheckLogin(username.getText(),IPAddress.getText(),passwordField.getText(),buf)) {
           goToMain();
         } else {
             showError(buf.toString());
           log.info(buf.toString());
        }
    }

    /**
     * to reset password stage
     * @param event
     */
    @FXML
    protected void jumptoReset(MouseEvent event) {
        stageController.loadStage(MainApp.resetPassViewID,MainApp.resetPassRes);
        stageController.setStage(MainApp.resetPassViewID,MainApp.loginViewID);
    }

    /**
     * to chat stage
     */
    public void goToMain() {
        stageController.loadStage(MainApp.mainViewID,MainApp.mainRes);
        stageController.setStage(MainApp.mainViewID,MainApp.loginViewID);
        stageController.getStage(MainApp.mainViewID).setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                model.disConnect();
            }
        });
    }

    /**
     * to register stage
     * @param event
     * @throws IOException
     */
    @FXML
    protected void jumptoRegister(ActionEvent event) throws IOException {
        stageController.loadStage(MainApp.registerViewID,MainApp.registerRes);
        stageController.setStage(MainApp.registerViewID,MainApp.loginViewID);
       //  cleanAllInfo();
    }

    /**
     * show error dialog
     * @param error
     */
    public void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wechat");
        alert.setContentText("登录失败 " + error);
        alert.show();
    }


//    /**
//     * @param username
//     * @param password
//     * @return
//     */
//    public boolean checklogin(String username, String password) {
//        try {
//            Account client = accountDao.findByName(username);
//            if (client.getPassword().equals(password)) {
//                client.setStatus("Online");
//                return true;
//            } else {
//                return false;
//            }
//
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return false;
//    }

//    private void cleanAllInfo() {
//        username.clear();
//        passwordField.clear();
//    }


}
