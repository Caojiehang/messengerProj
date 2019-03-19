package Client.Controller;

import Dao.ChatHistoryDaoImpl;
import Utils.ControllerStage;
import Utils.StageController;
import bean.PrivateChatHistory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
* @Author: Jiehang CAO
* @Description:
* @Date: 12:03 2019-03-17
*/
public class HistorySearchController implements ControllerStage, Initializable {

    StageController stageController;

    static ChatHistoryDaoImpl chatHistoryDao =ChatHistoryDaoImpl.getInstance();

    //PrivateChatHistory chatHistory  = new PrivateChatHistory();


    @Override
    public void setStageController(StageController stageController) {
       this.stageController = stageController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML private TextField sender;
    @FXML private TextField receiver;
    @FXML private DatePicker send_time;
    @FXML private ListView<String> chatInfo;

    private List<PrivateChatHistory> list = new ArrayList<>();


    private ObservableList<String> info = FXCollections.observableArrayList();
//todo to update the page and should consider how to display chat history well;
    @FXML protected  void  search() {
        try {
          list = chatHistoryDao.find(sender.getText(),receiver.getText(), Date.valueOf(send_time.getEditor().getText()));
          for(PrivateChatHistory chatHistory:list) {
              info.add(chatHistory.getContent());
          }
          chatInfo.setItems(info);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
