package client;

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
import java.net.Socket;
import java.net.SocketException;
import java.util.Optional;


public class Login extends Application {

    private static String ipAddress;
    private static int port;
    private GridPane gridPane = new GridPane();
    public BufferedReader bufferedReader;
    public BufferedWriter bufferedWriter;


    public static void main(String[] args) {
        launch(args);
    }


    /**
     * send message to the server
     */

    public synchronized void write(String msg) {
        try {
            bufferedWriter.write(msg + "\n");
            bufferedWriter.flush();
        } catch (SocketException e) {
            // Alert window
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Connection Error!");
            alert.setContentText("Port Number is not correct or Invalid input!");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *  Login Stage
     */

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

        Label label2 = new Label();
        label2.setText("IP Address");
        label2.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        gridPane.add(label2, 1, 3, 1, 1);

        TextField textField2 = new TextField();
        textField2.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        textField2.setStyle("-fx-background-color: null;" +
                "-fx-border-width:  0.5;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
        textField2.setText("10.0.75.1");
        gridPane.add(textField2, 2, 3, 1, 1);

        Label label3 = new Label();
        label3.setText("Port");
        label3.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        gridPane.add(label3, 1, 4, 1, 1);

        TextField textField3 = new TextField();
        textField3.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        textField3.setStyle("-fx-background-color: null;" +
                "-fx-border-width:  0.5;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
        gridPane.add(textField3, 2, 4, 1, 1);

        Button button1 = new Button();
        button1.setText("Login");
        button1.setFont(Font.font("Times", FontWeight.NORMAL, 16));

        Button button2 = new Button();
        button2.setText("Cancel");
        button2.setFont(Font.font("Times", FontWeight.NORMAL, 16));

        HBox hBox = new HBox();
        hBox.setSpacing(35);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().add(button1);
        hBox.getChildren().add(button2);
        gridPane.add(hBox, 2, 6, 2, 1);


        /**
         *
         * The action after clicking button Login
         */

        button1.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override

            public void handle(MouseEvent event) {
                try {
                    port = Integer.valueOf(textField3.getText());
                    ipAddress = textField2.getText();
                } catch (Exception e) {
                    System.out.println("exception successful");
                }
                try {
                    Socket socket = new Socket(ipAddress, port);
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));

                    MessageListener.getInstance().init(socket, bufferedWriter, bufferedReader);
                    MessageListener.getInstance().messageListener.start();

                    // listen to the feedback from the server
                    System.out.println("Establish socket successfully");
                    IdentifyUsername.getInstance().Identify(); // IdentifyUsername stage

                    /**
                     *
                     * The action after clicking button Validate
                     */

                    IdentifyUsername.getInstance().validate.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override

                        public void handle(MouseEvent event) {
                            try {
                                if (!IdentifyUsername.getInstance().textField1.getText().equals("")) {
                                    write("\n");
                                    // write message to the server
                                    write("validate" + "|" + IdentifyUsername.getInstance().textField1.getText() + "\n");
                                } else {
                                    // Alert window
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Warning!");
                                    alert.setHeaderText("Lütfen kullanıcı adınızı giriniz!");
                                    alert.setHeight(50);
                                    alert.setWidth(40);
                                    ButtonType confirm = new ButtonType("Tamam");
                                    alert.getButtonTypes().setAll(confirm);
                                    Optional<ButtonType> result = alert.showAndWait();
                                }
                            } catch (Exception e) {
                                // Alert window
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Hata!");
                                alert.setHeaderText("Bağlantı veya input hatası");
                                alert.setContentText("Port numarası doğru değil veya geçersiz input");
                                alert.showAndWait();
                                textField2.setText("");
                                textField3.setText("");
                            }
                        }
                    });
                    primaryStage.close();
                } catch (SocketException e) {
                    // Alert window
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText("Connection Error or Input Error!");
                    alert.setContentText("Port Number is not correct or Invalid input!");
                    alert.showAndWait();
                    textField2.setText("");
                    textField3.setText("");
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText("Connection Error or Input Error!");
                    alert.setContentText("Port Number is not correct or Invalid input!");
                    alert.showAndWait();
                    textField2.setText("");
                    textField3.setText("");
                }
            }
        });


        /**
         *  Cancel button
         */

        button2.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override

            public void handle(MouseEvent event) {
                System.exit(0); // Exit the system
            }
        });

        Scene scene = new Scene(gridPane, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}