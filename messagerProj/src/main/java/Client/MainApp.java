package Client;

import Utils.StageController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
* @Author: Jiehang CAO
* @Description:
* @Date: 11:21 2019-03-14
*/
public class MainApp extends Application {
    /**
     * ChatView id and resource file name
     */
    public static String mainViewID = "ChatView";
    public static String mainRes = "/Chat.fxml";
    /**
     * LoginView id and resource file name
     */
    public static String loginViewID = "LoginView";
    public static String loginRes = "/Login.fxml";
    /**
     * RegisterView id and resource file name
     */
    public static String registerViewID = "RegisterView";
    public static String registerRes = "/Register.fxml";
    /**
     * resetPassView id and resource file name
     */
    public static String resetPassViewID = "ResetView";
    public static String resetPassRes = "/ForgetPassword.fxml";


    /**
     * chat history view id and resource file name
     */
    public static String chatHistoryViewID = "ChatHistoryView";
    public static String chatHistoryViewRes = "/ChatHistorySearch.fxml";

    private StageController stageController;



    public void start(Stage primaryStage) throws Exception {
        stageController = new StageController();

        stageController.setPrimaryStage("primaryStage",primaryStage);

        stageController.loadStage(loginViewID,loginRes);

        stageController.setStage(loginViewID);



    }
    public static void main(String[] args) {
        launch();
    }
}
