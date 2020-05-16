package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import server.Server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


public class Game {

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public Socket socket;
    private int gameSpaceSize;
    public int unavailableCellNumber;
    public int twoPointCellNumber;
    public int threePointCellNumber;
    public List<Coords> unavailableCoords = new ArrayList<>();
    public List<Coords> twoPointCoords = new ArrayList<>();
    public List<Coords> threePointCoords = new ArrayList<>();
    public Coords randomLetter = new Coords();
    public int randomIndex;
    public static List<Coords> newLetterCoords = new ArrayList<>();
    public static List<Coords> letterCoords = new ArrayList<>();


    /**
     * Initialize socket
     */

    public void init(Socket socket, BufferedWriter writer, BufferedReader reader) {
        this.socket = socket;
        this.bufferedReader = reader;
        this.bufferedWriter = writer;
    }

    public void init(Socket socket, BufferedWriter writer, BufferedReader reader, int gameSpaceSize, int unavailableCellNumber, int twoPointCellNumber, int threePointCellNumber, List<Coords> unavailableCoords, List<Coords> twoPointCoords, List<Coords> threePointCoords, Coords randomLetter, int randomIndex) {
        this.socket = socket;
        this.bufferedReader = reader;
        this.bufferedWriter = writer;
        this.gameSpaceSize = gameSpaceSize;
        this.unavailableCellNumber = unavailableCellNumber;
        this.twoPointCellNumber = twoPointCellNumber;
        this.threePointCellNumber = threePointCellNumber;
        this.unavailableCoords = unavailableCoords;
        this.twoPointCoords = twoPointCoords;
        this.threePointCoords = threePointCoords;
        this.randomLetter = randomLetter;
        this.randomIndex = randomIndex;
    }


    /**
     * Write message to the server
     */

    public synchronized void write(String msg) {
        try {
            bufferedWriter.write(msg);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (SocketException e) {
            // Alert window
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText("Connection Error!");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Game game;

    public static Game getInstance() {
        if (game == null) {
            game = new Game();
        }
        return game;
    }


    /**
     * Clear the game
     */

    public void clear() {
        if (game != null) {
            game = null;
        }
    }

    private static int position_x;
    private static int position_y;
    private static int HsentPosition_xL;
    private static int HsentPosition_xR;
    private static int HsentPosition_yL;
    private static int HsentPosition_yR;
    private static int VsentPosition_xL;
    private static int VsentPosition_xR;
    private static int VsentPosition_yL;
    private static int VsentPosition_yR;

    private static int fistLetterX;
    private static int fistLetterY;
    private static int lastLetterX;
    private static int lastLetterY;

    private static String Hword;
    private static String Vword;
    private static String finalA;
    private static int round = 1;
    private boolean success = false;

    private GridPane gridPane = new GridPane();
    private GridPane subGridPane = new GridPane();
    private GridPane firstRowDisplay = new GridPane();
    private GridPane secondRowDisplay = new GridPane();
    public TextArea textArea = new TextArea();
    public Stage stage = new Stage();
    public ObservableList<String> observableList;
    public ListView<String> listView = new ListView<String>();

    public Label[][] labels;
    public Label display = new Label();
    public Button submit = new Button();
    public Button pass = new Button();
    public Button quit = new Button();
    public Label label = new Label();
    public Button sendTheWord = new Button();
    Button clearTheWord = new Button();
    TextField wordField = new TextField() {
        @Override
        public void replaceText(int start, int end, String text) {
            super.replaceText(start, end, text.toUpperCase());
        }
    };
    public boolean isLeftToRight;
    public boolean isSent = false;
    public char[] alphabet = new char[26];
    Random rd = new Random();


    /**
     * Initialize StartAGame Stage
     */

    public void startAGame() {
        labels = new Label[gameSpaceSize][gameSpaceSize];
        stage.setTitle("Kelime Avı");
        gridPane.setAlignment(Pos.CENTER);

        label.setFont(Font.font("Times", FontWeight.BOLD, 50));
        label.setAlignment(Pos.CENTER);
        label.setTextFill(Color.DARKBLUE);

        display.setFont(Font.font("Times", FontWeight.SEMI_BOLD, 23));
        pass.setText("Pass");
        pass.setFont(Font.font("Times", FontWeight.NORMAL, 16));

        HBox hBox = new HBox();
        hBox.setSpacing(35);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().add(display);
        hBox.getChildren().add(pass);

        VBox play = new VBox();
        play.setSpacing(20);
        play.setAlignment(Pos.CENTER_LEFT);
        play.getChildren().add(hBox);
        play.getChildren().add(subGridPane);


        quit.setText("Çıkış");
        quit.setFont(Font.font("Times", FontWeight.NORMAL, 16));

        HBox buttons = new HBox();
        buttons.setSpacing(50);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().add(quit);
        gridPane.add(buttons, 3, 13, 3, 1);

        Label score = new Label();
        score.setText("Players' Score");
        gridPane.add(score, 3, 8, 1, 1);
        score.setFont(Font.font("Times", FontWeight.SEMI_BOLD, 20));

        textArea.setPrefSize(250, 400);

        VBox scoreView = new VBox();
        scoreView.setSpacing(10);
        scoreView.setAlignment(Pos.CENTER_LEFT);
        scoreView.getChildren().add(score);
        scoreView.getChildren().add(textArea);
        gridPane.add(scoreView, 3, 9, 1, 1);

        // 26 single-character letters
        char[] letterF = new char[26];
        for (int i = 0; i < letterF.length; i++) {
            letterF[i] = (char) ('A' + i);
        }
        wordField.setFont(Font.font("Times", FontWeight.NORMAL, 18));
        wordField.setStyle("-fx-background-color: null;" +
                "-fx-border-width:  0.5;" +
                "-fx-border-insets: 2;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: black;");
        wordField.getSelectedText().toUpperCase();
        clearTheWord.setText("Temizle");
        clearTheWord.setFont(Font.font("Times", FontWeight.NORMAL, 16));
        sendTheWord.setText("Yerleştir");
        sendTheWord.setFont(Font.font("Times", FontWeight.NORMAL, 16));
        submit.setText("Gönder");
        submit.setFont(Font.font("Times", FontWeight.NORMAL, 16));
        HBox hBox2 = new HBox();
        hBox2.setSpacing(15);
        hBox2.setAlignment(Pos.CENTER_LEFT);
        hBox2.getChildren().add(wordField);
        hBox2.getChildren().add(clearTheWord);
        hBox2.getChildren().add(sendTheWord);
        hBox2.getChildren().add(submit);

        //gridPane.add(hBox2,3,15,3,1);

        VBox displayArea = new VBox();
        displayArea.setSpacing(15);
        displayArea.setAlignment(Pos.CENTER_RIGHT);
        displayArea.getChildren().add(scoreView);
        displayArea.getChildren().add(firstRowDisplay);
        displayArea.getChildren().add(secondRowDisplay);
        displayArea.getChildren().add(buttons);
        displayArea.getChildren().add(hBox2);

        HBox area = new HBox();
        area.setSpacing(60);
        area.setAlignment(Pos.CENTER);
        area.getChildren().add(play);
        area.getChildren().add(displayArea);

        VBox wholePage = new VBox();
        wholePage.setSpacing(30);
        wholePage.setAlignment(Pos.CENTER);
        wholePage.getChildren().add(label);
        wholePage.getChildren().add(area);
        gridPane.add(wholePage, 1, 0, 5, 20);


        // Labels Lengtgh Grid
        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                Label grid = new Label();
                labels[i][j] = grid;
                labels[i][j].setFont(Font.font("Times", FontPosture.REGULAR, 20));
                labels[i][j].setMinSize(30, 30);
                labels[i][j].setAlignment(Pos.CENTER);
                subGridPane.add(labels[i][j], j + 1, i + 1, 1, 1);
            }
        }


        ///initialize random
        for (int i = 0; i < alphabet.length; i++) {
            alphabet[i] = (char) ('A' + i);
        }
        labels[randomLetter.getX()][randomLetter.getY()].setText(String.valueOf(alphabet[randomIndex]));


        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                int finalI = i;
                int finalJ = j;
                Coords coords1 = unavailableCoords.stream().
                        filter(p -> p.getX() == finalI && p.getY() == finalJ).
                        findAny().orElse(null);
                Coords coords2 = twoPointCoords.stream().
                        filter(p -> p.getX() == finalI && p.getY() == finalJ).
                        findAny().orElse(null);
                Coords coords3 = threePointCoords.stream().
                        filter(p -> p.getX() == finalI && p.getY() == finalJ).
                        findAny().orElse(null);
                // Bonus grid style
                if (coords1 != null && labels[i][j].getText().equals("")) { //buraya random degerler gelecek
                    labels[i][j].setStyle("-fx-background-color: #000;" +
                            "-fx-border-width:  0.2;" +
                            "-fx-border-insets: 1;" +
                            "-fx-border-radius: 5;" +
                            "-fx-border-color: black;");
                } else if (coords2 != null) {
                    labels[i][j].setStyle("-fx-background-color: #f1ff00;" +
                            "-fx-border-width:  0.2;" +
                            "-fx-border-insets: 1;" +
                            "-fx-border-radius: 5;" +
                            "-fx-border-color: black;");
                } else if (coords3 != null) {
                    labels[i][j].setStyle("-fx-background-color: #2aff30;" +
                            "-fx-border-width:  0.2;" +
                            "-fx-border-insets: 1;" +
                            "-fx-border-radius: 5;" +
                            "-fx-border-color: black;");
                } else {
                    labels[i][j].setStyle("-fx-background-color: white;" +
                            "-fx-border-width:  0.2;" +
                            "-fx-border-insets: 1;" +
                            "-fx-border-radius: 5;" +
                            "-fx-border-color: black;");
                }
            }
        }

        /**
         *
         * The action after clicking button Submit
         */
        submit.setDisable(true);

        for (int i = 0; i < labels.length; i++) {
            for (int j = 0; j < labels[i].length; j++) {
                int finalJ = j;
                int finalI = i;
                labels[i][j].addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (!wordField.getText().equals("")) {
                            newLetterCoords.clear();
                            letterCoords.clear();
                            String text = wordField.getText();
                            char[] chars = text.toCharArray();
                            boolean isEmpty = true;
                            boolean isMatch = false;
                            if ((!isLeftToRight && chars.length + finalI > labels.length) || (isLeftToRight && chars.length + finalJ > labels.length))
                                isEmpty = false;
                            else {
                                for (int k = 0; k < chars.length; k++) {
                                    if (!isLeftToRight) { //todo burada çakışan sayısı alınıp puan da hesaplanabilir
                                        if (labels[finalI + k][finalJ].getText().equals(String.valueOf(chars[k]))) {
                                            isMatch = true;
                                        } else {
                                            newLetterCoords.add(new Coords(finalI + k, finalJ));
                                        }
                                        letterCoords.add(new Coords(finalI + k, finalJ));
                                        if (labels[finalI + k][finalJ].getStyle().contains("background-color: #000"))
                                            isEmpty = false;
                                    } else {
                                        if (labels[finalI][finalJ + k].getText().equals(String.valueOf(chars[k])) && !labels[finalI][finalJ + k].getStyle().contains("background-color: #000")) {
                                            isMatch = true;
                                        } else {
                                            newLetterCoords.add(new Coords(finalI, finalJ + k));
                                        }
                                        letterCoords.add(new Coords(finalI, finalJ + k));
                                        if (labels[finalI][finalJ + k].getStyle().contains("background-color: #000"))
                                            isEmpty = false;
                                    }
                                }
                            }

                            if (isMatch && isEmpty) {
                                fistLetterX = finalI;
                                fistLetterY = finalJ;
                                for (int k = 0; k < chars.length; k++) {
                                    if (!isLeftToRight) {
                                        position_x = finalI + k;
                                        position_y = finalJ;
                                        labels[finalI + k][finalJ].setText(String.valueOf(chars[k]));
                                        String content = labels[finalI + k][finalJ].getText();
                                        System.out.println("this is the position that should be updated" + content);
                                        write("position|" + position_x + "|" + position_y + "|" + content);
                                    } else {
                                        position_x = finalI;
                                        position_y = finalJ + k;
                                        labels[finalI][finalJ + k].setText(String.valueOf(chars[k]));
                                        String content = labels[finalI][finalJ + k].getText();
                                        System.out.println("this is the position that should be updated" + content);
                                        write("position|" + position_x + "|" + position_y + "|" + content);
                                    }
                                }
                                if (!isLeftToRight) {
                                    lastLetterX = finalI + chars.length - 1;
                                    lastLetterY = finalJ;
                                } else {
                                    lastLetterX = finalI;
                                    lastLetterY = finalJ + chars.length - 1;
                                }
                                submit.setDisable(false);
                                //isSent = true;
                                //} else {
                                //    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                //    alert.setTitle("Hata!");
                                //    alert.setHeaderText("Girdiğiniz kelimenin baş harfi yerleştireceğiniz harfle eşleşmelidir");
                                //    ButtonType confirm = new ButtonType("OK");
                                //    alert.getButtonTypes().setAll(confirm);
                                //    alert.showAndWait();
                                //}

                            } else {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Hata!");
                                alert.setHeaderText("Boş olan veya geçerli bir kısma yerleştirme yapınız");
                                ButtonType confirm = new ButtonType("OK");
                                alert.getButtonTypes().setAll(confirm);
                                alert.showAndWait();
                            }
                        }


                        //} else {
                        //    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        //    alert.setTitle("Hata!");
                        //    alert.setHeaderText("Zaten daha önce yerleştirme yaptınız");
                        //    ButtonType confirm = new ButtonType("OK");
                        //    alert.getButtonTypes().setAll(confirm);
                        //    alert.showAndWait();
                        //}

                    }
                });
            }

        }


        //if (wordField.getText() != null && wordField.getText().length()>0){
        sendTheWord.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                // Alert window
                if (wordField.getText() != null && wordField.getText().trim().length() > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Yön");
                    alert.setHeaderText("yukarıdan aşağı mı soldan sağa mi");
                    ButtonType topToBottom = new ButtonType("Yukarıdan Aşağı");
                    ButtonType leftToRight = new ButtonType("Soldan Sağa");
                    alert.getButtonTypes().setAll(topToBottom, leftToRight);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == topToBottom) {
                        System.out.println("yukarıdan aşağı");
                        isLeftToRight = false;
                    } else if (result.get() == leftToRight) {
                        System.out.println("soldan sağa");
                        isLeftToRight = true;
                    }
                    //wordField.setDisable(true);
                    //sendTheWord.setDisable(true);
                    //clearTheWord.setDisable(true);
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Hata!");
                    alert.setHeaderText("Geçerli bir kelime girmediniz.");
                    ButtonType confirm = new ButtonType("OK");
                    alert.getButtonTypes().setAll(confirm);
                    alert.showAndWait();
                }
            }
        });
        clearTheWord.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                wordField.setText("");
            }
        });


        submit.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String position = "";
                String chooseWords = wordField.getText();
                int multi = 1;
                if (!chooseWords.equals("")) {
                    String letterCords = "";
                    for (Coords c : newLetterCoords) {
                        letterCords += c.toString() + "-";
                    }
                    for (Coords c : letterCoords) {
                        if (labels[c.getX()][c.getY()].getStyle().contains("#f1ff00"))
                            multi = multi * 2;
                        else if (labels[c.getX()][c.getY()].getStyle().contains("#2aff30"))
                            multi = multi * 3;
                    }
                    String msg1 = "submit|" + chooseWords + "|" + multi;
                    System.out.println(msg1);
                    String substring = "";
                    if (letterCords != "")
                        substring = letterCords.substring(0, letterCords.length() - 1);
                    write("newLetterCoords|" + chooseWords + "|" + substring + "|" + newLetterCoords.size());
                    write(msg1);
                } else {
                    // Alert window
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Warning!");
                    alert.setHeaderText("You do not choose word!");

                    ButtonType confirm = new ButtonType("OK");
                    alert.getButtonTypes().setAll(confirm);
                    alert.showAndWait();
                }
            }
        });


        /**
         * The action after clicking button Pass
         */

        pass.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                write("pass|");
            }
        });


        quit.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override

            public void handle(MouseEvent event) {
                // Alert window
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Çık");
                alert.setHeaderText("Oyundan çıkmak istediğinize emin misiniz?");
                alert.setContentText(null);

                ButtonType accept = new ButtonType("Evet");
                ButtonType deny = new ButtonType("Hayır");
                alert.getButtonTypes().setAll(accept, deny);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == accept) {
                    System.out.println("watch quit");
                    write("quit|");
                } else if (result.get() == deny) {

                }
            }
        });

        Scene scene = new Scene(gridPane, 1500, 1200);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}