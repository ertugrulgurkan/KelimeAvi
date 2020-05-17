package server;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;


public class SetupServer extends Application {

    private static int gameSpaceSize;
    private static int unavailableCellNumber;
    private static int twoPointCellNumber;
    private static int threePointCellNumber;
    private static String creationPort;
    private static int winningPoint;
    boolean isWinningPointValid = false;
    boolean isGmeSpaceValid = false;
    boolean isUnavailableCellValid = false;
    boolean isTwoPointCellValid = false;
    boolean isThreePointCellValid = false;
    boolean isPortValid = false;
    private GridPane gridPane = new GridPane();


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Giriş Ekranı");
        gridPane.setAlignment(Pos.CENTER);

        Text title = new Text("Kelime Avına Hoş Geldin!");
        title.setTextAlignment(TextAlignment.LEFT);
        title.setFill(Color.DARKBLUE);
        title.setFont(Font.font("Times", FontWeight.BOLD, 25));
        gridPane.add(title, 0, 0, 3, 1);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Text setTheGame = new Text("Oyunu Kurun");
        setTheGame.setTextAlignment(TextAlignment.LEFT);
        setTheGame.setFill(Color.DARKBLUE);
        setTheGame.setFont(Font.font("Times", FontWeight.BOLD, 15));
        gridPane.add(setTheGame, 0, 1, 3, 1);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label gameSpaceLabel = new Label();
        gameSpaceLabel.setText("Oyun Alanı Büyüklüğü: (eg.16)");
        gameSpaceLabel.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        gridPane.add(gameSpaceLabel, 1, 3, 1, 1);

        TextField gameSpaceText = new TextField();
        gameSpaceText.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        gameSpaceText.setStyle("-fx-background-color: null;" +
                "-fx-border-width:  0.5;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
        gameSpaceText.setMaxWidth(70);
        gridPane.add(gameSpaceText, 2, 3, 1, 1);

        Label unavailableBlockCountLabel = new Label();
        unavailableBlockCountLabel.setText("Kullanılamaz Bölge Sayısı:");
        unavailableBlockCountLabel.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        gridPane.add(unavailableBlockCountLabel, 1, 4, 1, 1);

        TextField unavailableBlockCountText = new TextField();
        unavailableBlockCountText.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        unavailableBlockCountText.setStyle("-fx-background-color: null;" +
                "-fx-border-width:  0.5;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
        unavailableBlockCountText.setMaxWidth(70);
        gridPane.add(unavailableBlockCountText, 2, 4, 1, 1);

        Label winingPointLabel = new Label();
        winingPointLabel.setText("Kazanma Puanı:");
        winingPointLabel.setFont(Font.font("Times", FontWeight.NORMAL, 18));

        gridPane.add(winingPointLabel, 1, 5, 1, 1);

        TextField winingPointText = new TextField();
        winingPointText.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        winingPointText.setStyle("-fx-background-color: null;" +
                "-fx-border-width:  0.5;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
        winingPointText.setMaxWidth(70);
        gridPane.add(winingPointText, 2, 5, 1, 1);


        Label twoPointCellLabel = new Label();
        twoPointCellLabel.setText("2x Sayısı:");
        twoPointCellLabel.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        gridPane.add(twoPointCellLabel, 1, 6, 1, 1);

        TextField twoPointCellText = new TextField();
        twoPointCellText.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        twoPointCellText.setStyle("-fx-background-color: null;" +
                "-fx-border-width:  0.5;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
        twoPointCellText.setMaxWidth(50);
        gridPane.add(twoPointCellText, 2, 6, 1, 1);

        Label threePointCellLabel = new Label();
        threePointCellLabel.setText("3x Sayısı:");
        threePointCellLabel.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        gridPane.add(threePointCellLabel, 1, 7, 1, 1);

        TextField threePointCellText = new TextField();
        threePointCellText.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        threePointCellText.setStyle("-fx-background-color: null;" +
                "-fx-border-width:  0.5;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
        threePointCellText.setMaxWidth(50);
        gridPane.add(threePointCellText, 2, 7, 1, 1);


        Label portLabel1 = new Label();
        portLabel1.setText("Port");
        portLabel1.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        gridPane.add(portLabel1, 1, 8, 1, 1);

        TextField portText1 = new TextField();
        portText1.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        portText1.setStyle("-fx-background-color: null;" +
                "-fx-border-width:  0.5;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
        portText1.setMaxWidth(70);
        gridPane.add(portText1, 2, 8, 1, 1);


        Button createGameButton = new Button();
        createGameButton.setText("Oyun Kur");
        createGameButton.setFont(Font.font("Times", FontWeight.NORMAL, 16));

        Button exitButton = new Button();
        exitButton.setText("Çıkış");
        exitButton.setFont(Font.font("Times", FontWeight.NORMAL, 16));

        HBox hBox = new HBox();
        hBox.setSpacing(35);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().add(createGameButton);
        hBox.getChildren().add(exitButton);
        gridPane.add(hBox, 1, 9, 2, 1);


        createGameButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    isWinningPointValid = false;
                    isGmeSpaceValid = false;
                    isUnavailableCellValid = false;
                    isTwoPointCellValid = false;
                    isThreePointCellValid = false;
                    isPortValid = false;
                    gameSpaceSize = Integer.valueOf(gameSpaceText.getText());
                    unavailableCellNumber = Integer.valueOf(unavailableBlockCountText.getText());
                    twoPointCellNumber = Integer.valueOf(twoPointCellText.getText());
                    threePointCellNumber = Integer.valueOf(threePointCellText.getText());
                    winningPoint = Integer.valueOf(winingPointText.getText());
                    creationPort = portText1.getText();
                    if(winningPoint>=10 && winningPoint<=100){
                        isWinningPointValid = true;
                    }
                    if(gameSpaceSize>=8 && gameSpaceSize<=22){
                        isGmeSpaceValid = true;
                    }
                    if (unavailableCellNumber<=15 && unavailableCellNumber>=0){
                        isUnavailableCellValid = true;
                    }
                    if (twoPointCellNumber<=15 && twoPointCellNumber>=0){
                        isTwoPointCellValid = true;
                    }
                    if (threePointCellNumber<=15 && threePointCellNumber>=0){
                        isThreePointCellValid = true;
                    }
                    if(Integer.valueOf(creationPort) >= 0){
                        isPortValid = true;
                    }
                    if (isWinningPointValid && isGmeSpaceValid && isUnavailableCellValid && isTwoPointCellValid && isThreePointCellValid && isPortValid){
                        String[] args = {creationPort, String.valueOf(gameSpaceSize), String.valueOf(unavailableCellNumber), String.valueOf(twoPointCellNumber), String.valueOf(threePointCellNumber), String.valueOf(winningPoint)};
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Server Başlatıldı");
                        alert.setHeaderText("Çalışan port: " + creationPort);
                        alert.showAndWait();
                        primaryStage.close();
                        Server.getInstance().main(args);
                    }
                    else if(!(isThreePointCellValid && isTwoPointCellValid)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Hata");
                        alert.setHeaderText("Input Hatası!");
                        alert.setContentText("x2 veya x3 değerleri geçersiz (0-15 arasında)");
                        alert.showAndWait();
                        twoPointCellText.setText("");
                        threePointCellText.setText("");
                    }
                    else if(!isUnavailableCellValid){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Hata");
                        alert.setHeaderText("Input Hatası!");
                        alert.setContentText("Kullanılamaz bölge girdisi geçersiz (0-15 arasında)");
                        alert.showAndWait();
                        unavailableBlockCountText.setText("");
                    }
                    else if(!isGmeSpaceValid){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Hata");
                        alert.setHeaderText("Input Hatası!");
                        alert.setContentText("Geçerli bir oyun alanı giriniz (8-22 arasında)");
                        alert.showAndWait();
                        gameSpaceText.setText("");
                    }
                    else if(!isWinningPointValid){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Hata");
                        alert.setHeaderText("Input Hatası!");
                        alert.setContentText("Kazanma puanı geçersiz (10-100 arasında)");
                        alert.showAndWait();
                        winingPointText.setText("");
                    }
                    else if(!isPortValid){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Hata");
                        alert.setHeaderText("Input Hatası!");
                        alert.setContentText("Port Numarası 0'dan büyük olmak zorunda");
                        alert.showAndWait();
                        portText1.setText("");
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Hata!");
                        alert.setHeaderText("input hatası");
                        alert.setContentText("Geçersiz input");
                        alert.showAndWait();
                        gameSpaceText.setText("");
                        unavailableBlockCountText.setText("");
                        twoPointCellText.setText("");
                        threePointCellText.setText("");
                        winingPointText.setText("");
                    }

                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Hata!");
                    alert.setHeaderText("input hatası");
                    alert.setContentText("Geçersiz input");
                    alert.showAndWait();
                    gameSpaceText.setText("");
                    unavailableBlockCountText.setText("");
                    twoPointCellText.setText("");
                    threePointCellText.setText("");
                    winingPointText.setText("");
                    System.out.println("exception successful");
                }

            }
        });

        exitButton.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.exit(0); // Exit the system
            }
        });

        Scene scene = new Scene(gridPane, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}