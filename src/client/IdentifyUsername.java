package client;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class IdentifyUsername {

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket socket;


    /**
     * Initialize socket
     */

    public void init(Socket socket, BufferedWriter writer, BufferedReader reader){
        this.socket = socket;
        this.bufferedReader = reader;
        this.bufferedWriter = writer;
    }

    public static IdentifyUsername identifyUsername;
    public Button validate = new Button();
    public TextField textField1 = new TextField();
    private GridPane gridPane = new GridPane();
    public Stage stage = new Stage();


    /**
     * Close the stage
     */

    public void closeStage(){
        this.stage.close();
    }

    public static IdentifyUsername getInstance(){
        if(identifyUsername == null){
            identifyUsername = new IdentifyUsername();
        }
        return identifyUsername;
    }


    /**
     * Initialize IdentifyUsername Stage
     */

    public void Identify(){
        stage.setTitle("Kullanıcı Adı Belirle");
        gridPane.setAlignment(Pos.CENTER);

        Text title = new Text("Kullanıcı Adınızı Giriniz:");
        title.setTextAlignment(TextAlignment.LEFT);
        title.setFont(Font.font("Times", FontWeight.BOLD,25));
        title.setFill(Color.DARKBLUE);

        Label label1 = new Label();
        label1.setText("Kullanıcı Adı");
        label1.setFont(Font.font("Times",FontWeight.NORMAL,18));

        textField1.setFont(Font.font("Times",FontWeight.NORMAL,18));
        textField1.setStyle("-fx-background-color: null;" +
                "-fx-border-width:  0.5;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");

        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(label1);
        hBox.getChildren().add(textField1);

        validate.setText("Onayla");
        validate.setAlignment(Pos.CENTER);
        validate.setFont(Font.font("Times",FontWeight.NORMAL,16));

        VBox vBox = new VBox();
        vBox.setSpacing(30);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(title);
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(validate);
        gridPane.add(vBox,0,0,3,3);

        Scene scene = new Scene(gridPane,400,200);
        stage.setScene(scene);
        stage.show();
    }
}