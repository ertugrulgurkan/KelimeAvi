package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class MainPage {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    /**
     * Initialize socket
     */

    public void init(Socket socket, BufferedWriter writer, BufferedReader reader){
        this.socket = socket;
        this.bufferedReader = reader;
        this.bufferedWriter = writer;
    }


    /**
     * Write message to the server
     */

    public synchronized void write(String msg){
        try {
            bufferedWriter.write(msg);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch(SocketException e){
            // Alert window
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Connection Error!");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read message from the server
     */

    public synchronized void Read() {
        try {
            message = bufferedReader.readLine();
        } catch(SocketException e){
            // Alert window
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Connection Error!");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GridPane gridPane = new GridPane();
    public Button invite = new Button();
    public Button start = new Button();
    public Button refresh=new Button();
    public Button quit = new Button();
    public TextArea textArea = new TextArea();
    private ListView<String> listView = new ListView<String>();
    public String message;
    public Label label = new Label();

    public static MainPage mainPage;
    public Stage stage = new Stage();


    /**
     * Close the stage
     */

    public void closeStage(){
        this.stage.close();
    }

    public static MainPage getInstance(){
        if(mainPage == null){
            mainPage = new MainPage();
        }
        return mainPage;
    }


    /**
     * Initialize MainPage Stage
     */

    public void step2(){
        stage.setTitle("Ana Ekran");
        gridPane.setAlignment(Pos.CENTER);

        label.setFont(Font.font("Times", FontWeight.BOLD, 30));
        label.setAlignment(Pos.CENTER);
        label.setTextFill(Color.DARKBLUE);
        gridPane.add(label,3,0,2,1);

        Label label1 = new Label();
        label1.setText("Online Kullanıcılar");
        label1.setFont(Font.font("Times", FontWeight.BLACK, 23));
        gridPane.add(label1,2,1,1,1);

        Label label2 = new Label();
        label2.setText("Kullanıcı Durumları");
        label2.setFont(Font.font("Times", FontWeight.BLACK, 23));
        gridPane.add(label2,6,1,1,1);

        invite.setText("Davet Et");
        invite.setFont(Font.font("Times",FontWeight.NORMAL,16));

        start.setText("Başlat");
        start.setFont(Font.font("Times",FontWeight.NORMAL,16));

        refresh.setText("Yenile");
        refresh.setFont(Font.font("Times",FontWeight.NORMAL,16));

        quit.setText("Çıkış");
        quit.setFont(Font.font("Times",FontWeight.NORMAL,16));

        VBox vBox = new VBox();
        vBox.setSpacing(50);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(start);
        vBox.getChildren().add(invite);
        vBox.getChildren().add(refresh);
        vBox.getChildren().add(quit);
        gridPane.add(vBox,4,5,2,1);

        textArea.setMaxSize(250,600);
        gridPane.add(textArea,6,5,3,1);

        // Write message to the server
        write("updateOnePlayer|"+"\n");
        // Read message from the server
        Read();

        ArrayList<String> list = new ArrayList<String>();
        String [] msg=null;
        System.out.println(message);
        msg = message.split("\\|");
        for (int i = 1; i < msg.length; i++) {
            list.add(msg[i]);
        }

        // list all the online users
        ObservableList<String> observableList = FXCollections.observableArrayList(list);
        listView.setItems(observableList);
        gridPane.add(listView,2,5,1,1);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        /**
         * The action after clicking button Invite
         */

        invite.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override

            public void handle(MouseEvent event) {
                String a ="";
                ObservableList<String> selectItems = listView.getSelectionModel().getSelectedItems();
                for(Object o : selectItems){
                    a = a + o+ "|";
                }
                if(!a.equals("")) {
                    String msg1 = "invite|" + a;
                    System.out.println(msg1);
                    write(msg1);
                }
                else{
                    // Alert window
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Uyarı");
                    alert.setHeaderText("Kimseyi Davet Etmedin");
                    ButtonType confirm = new ButtonType("OK");
                    alert.getButtonTypes().setAll(confirm);
                    alert.showAndWait();
                }
            }
        });


        /**
         * The action after clicking button Start
         */

        start.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override

            public void handle(MouseEvent event) {
                write("fillGameBoard|");
                write("fillRandomValues|");
                write("startGame|");
            }
        });




        /**
         *
         * The action after clicking button Refresh
         */

        refresh.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override

            public void handle(MouseEvent event) {
                try {
                    write("updateOnePlayer|" + "\n");
                    Read();
                    System.out.println(message);
                    ArrayList<String> list = new ArrayList<String>();
                    String[] msg = null;
                    msg = message.split("\\|");
                    for (int i = 1; i < msg.length; i++) {
                        list.add(msg[i]);
                    }

                    // List all the online users after refreshing
                    ObservableList<String> observableList = FXCollections.observableArrayList(list);
                    listView.setItems(observableList);
                    listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });


        /**
         *
         * The action after clicking button Quit
         */

        quit.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.close();
                try {
                    write("remove|");
                    System.exit(0);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        Scene scene = new Scene(gridPane,800,600);
        stage.setScene(scene);
        stage.show();

    }
}