package Client.Controller;

import Client.MainApp;
import Client.model.ClientModel;
import Dao.ChatHistoryDaoImpl;
import Utils.ControllerStage;
import Utils.StageController;
import bean.ClientUser;
import bean.Message;
import bean.PrivateChatHistory;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ResourceBundle;

import static Utils.Constants.*;

/**
 * @Author: Jiehang CAO
 * @Description:
 * @Date: 23:30 2019-03-13
 */
public class ChatController implements ControllerStage, Initializable {

    private StageController stageController;


    /**
     * Userlist to display clientUsers
     */
    @FXML
    private ListView UserList;
    /**
     * Type messages
     */
    @FXML
    private TextArea message;

    /**
     * display chat messages
     */

    @FXML
    private ListView records;
    /**
     * display this client user name
     */
    @FXML
    private Label labUserName;


    private Gson gson = new Gson();

    private ClientModel model;

    /**
     * initialize the chat model to group chat for every connection
     */
    private boolean pattern = GROUP;

    private static String thisUser;


    /**
     * choose private user to chat or chat in group
     */
    private String selectUser = "[group]";

    /**
     * to store chat listview items
     */
    private ObservableList<Message> record = FXCollections.observableArrayList();

    /**
     * to store users listview items
     */
    private ObservableList<ClientUser> clients = FXCollections.observableArrayList();

    /**
     * button to send messages
     */
    @FXML
    private Button send_bt;

    /**
     * jump to chat history search
     *
     * @param event
     */
    @FXML
    protected void searchChat(ActionEvent event) {
        stageController.loadStage(MainApp.chatHistoryViewID, MainApp.chatHistoryViewRes);
        stageController.setStage(MainApp.chatHistoryViewID);
    }

    /**
     * to get stageController to manage various stages
     *
     * @param stageController
     */
    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }

    /**
     * loading the stage and initialization stage
     *
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        model = ClientModel.getInstance();
        clients = model.getUserList();
        record = model.getChatRecord();
        UserList.setItems(clients);
        records.setItems(record);
        thisUser = model.getThisUser();
        labUserName.setText(thisUser);

        send_bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pattern == GROUP) {
                    HashMap map = new HashMap();
                    map.put(COMMAND, COM_CHATALL);
                    map.put(CONTENT, message.getText().trim());
                    model.sentMessage(gson.toJson(map));
                } else if (pattern == SINGLE) {
                    HashMap map = new HashMap();
                    map.put(COMMAND, COM_CHATWITH);
                    map.put(RECEIVER, selectUser);
                    map.put(SPEAKER, model.getThisUser());
                    map.put(CONTENT, message.getText().trim());
                    model.sentMessage(gson.toJson(map));
                    insertMessage(thisUser,selectUser,message.getText(),Date.valueOf(getFormatDate()));
                }

                message.setText("");
            }
        });

        UserList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ClientUser user = (ClientUser) newValue;
            System.out.println("You are selecting " + user.getUserName());
            if (user.getUserName().equals("[group]")) {
                pattern = GROUP;
                if (!selectUser.equals("[group]")) {
                    model.setChatUser("[group]");
                    selectUser = "[group]";
                }
            } else {
                pattern = SINGLE;
                if (!selectUser.equals(user.getUserName())) {
                    model.setChatUser(user.getUserName());
                    selectUser = user.getUserName();
                }
            }
        });
        records.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new ChatCell();
            }
        });

        UserList.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                return new UserCell();
            }
        });
    }

    public static class UserCell extends ListCell<ClientUser> {
        @Override
        protected void updateItem(ClientUser item, boolean empty) {
            super.updateItem(item, empty);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (item != null) {
                        HBox hbox = new HBox();
                        ImageView imageHead = new ImageView(new Image("image/head.png"));
                        imageHead.setFitHeight(20);
                        imageHead.setFitWidth(20);
                        ClientUser user = (ClientUser) item;
                        ImageView imageStatus;
                        if (user.getUserName().equals("[group]")) {
                            imageStatus = new ImageView(new Image("image/online.png"));
                        } else if (user.isNotify() == true) {
                            imageStatus = new ImageView(new Image("image/message.png"));
                        } else {
                            if (user.getStatus().equals("online")) {
                                imageStatus = new ImageView(new Image("image/online.png"));
                            } else {
                                imageStatus = new ImageView(new Image("image/offline.png"));
                            }
                        }
                        imageStatus.setFitWidth(20);
                        imageStatus.setFitHeight(20);
                        Label label = new Label(user.getUserName());
                        hbox.getChildren().addAll(imageHead, label, imageStatus);
                        setGraphic(hbox);
                    } else {
                        setGraphic(null);
                    }
                }
            });
        }
    }

    public static class ChatCell extends ListCell<Message> {
        @Override
        protected void updateItem(Message item, boolean empty) {
            super.updateItem(item, empty);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //inorder to avoid the
                    if (item != null) {
                        VBox box = new VBox();
                        HBox hbox = new HBox();
                        TextFlow textFlow = new TextFlow(new TextField(item.getContent()));
                        Label labUser = new Label(item.getSpeaker() + "[" + item.getTimer() + "]");
                        labUser.setStyle("-fx-background-color: #7bc5cd; -fx-text-fill: white;");
                        ImageView image = new ImageView(new Image("image/head.png"));
                        image.setFitHeight(20);
                        image.setFitWidth(20);
                        hbox.getChildren().addAll(image, labUser);
                        if (item.getSpeaker().equals(thisUser)) {
                            textFlow.setTextAlignment(TextAlignment.RIGHT);
                            hbox.setAlignment(Pos.CENTER_RIGHT);
                            box.setAlignment(Pos.CENTER_RIGHT);
                        }
                        box.getChildren().addAll(hbox, textFlow);
                        setGraphic(box);
                    } else {
                        setGraphic(null);
                    }
                }
            });
        }
    }

    public String getFormatDate() {
        java.util.Date date = new java.util.Date();
        long times = date.getTime();//时间戳
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    private void insertMessage(String sender, String receiver, String content, Date send_time) {
        PrivateChatHistory privateChatHistory = new PrivateChatHistory(sender, receiver, content, send_time);
        try {
            ChatHistoryDaoImpl.getInstance().add(privateChatHistory);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
