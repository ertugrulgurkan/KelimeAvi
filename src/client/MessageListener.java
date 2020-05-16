package client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class MessageListener extends Thread {
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    private static Socket socket;
    public static int gameSpaceSize = 0;
    public static int unavailableCellNumber = 0;
    public static int twoPointCellNumber = 0;
    public static int threePointCellNumber = 0;
    public static List<Coords> unavailableCoords = new ArrayList<>();
    public static List<Coords> twoPointCoords = new ArrayList<>();
    public static List<Coords> threePointCoords = new ArrayList<>();
    public static Coords randomLetter = new Coords();
    public static int letterIndex = 0;
    public static List<Coords> enteredLetterCoords = new ArrayList<>();


    /**
     * Initialize socket
     */

    public void init(Socket socket, BufferedWriter writer, BufferedReader reader) {
        this.socket = socket;
        this.bufferedReader = reader;
        this.bufferedWriter = writer;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MessageListener messageListener;

    public static MessageListener getInstance() {
        if (messageListener == null) {
            messageListener = new MessageListener();
        }
        return messageListener;
    }


    /**
     * Read message from the server
     */

    @Override

    public void run() {
        while (true) {
            try {
                String message = null;
                while ((message = bufferedReader.readLine()) != null) {

                    System.out.println(message);
                    execute(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Execute operations
     */

    public static void execute(String command) {
        String[] parts = command.split("\\|");
        String op = parts[0];
        if (op.equals("invalid message!")) {

        } else if (op.equals("duplicate name")) {
            Platform.runLater(new Runnable() {
                @Override

                public void run() {
                    // Alert window
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Message");
                    alert.setHeaderText("Invalid Username!");
                    alert.setContentText("The username has already existed!");
                    alert.showAndWait();
                    IdentifyUsername.getInstance().textField1.setText("");
                }
            });
        } else if (op.equals("Name successful")) {
            Platform.runLater(new Runnable() {
                @Override

                public void run() {
                    // Verify user name successfully, then close IdentifyUsername stage and open MainPage stage
                    IdentifyUsername.getInstance().closeStage();
                    MainPage.getInstance().init(socket, bufferedWriter, bufferedReader);
                    MainPage.getInstance().step2();
                    MainPage.getInstance().label.setText(parts[1] + " ana ekran");
                }
            });
        } else if (op.equals("updatePlayer")) {
            updatePlayer(parts);
        } else if (op.equals("notinvite")) {
            notinvite();
        } else if (op.equals("startGame")) {
            start(parts);
        } else if (op.equals("fillGameBoard")) {
            fillGameBoard(parts);
        } else if (op.equals("fillRandomValues")) {
            fillRandomValues(parts);
        } else if (op.equals("letterCoords")) {
            letterCoords(parts);
        } else if (op.equals("invite")) {
            invite(parts);
        } else if (op.equals("accept")) {
            inviteAccept(parts);
        } else if (op.equals("notstart")) {
            notStart(parts);
        } else if (op.equals("notturn")) {

        } else if (op.equals("deny")) {
            inviteDeny(parts);
        } else if (op.equals("vote")) {
            vote(parts);
        } else if (op.equals("updateGame")) {
            updateGame(parts);
        } else if (op.equals("updateOneScore")) {
            updateOneScore(parts);
        } else if (op.equals("game over")) {
            Rank(parts);
        } else if (op.equals("nowatch")) {
            nowatch();
        } else if (op.equals("notaccept")) {
            notaccept();
        } else if (op.equals("inviter")) {
            Platform.runLater(new Runnable() {
                @Override

                public void run() {
                    // Alert window
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Warning");
                    alert.setHeaderText(parts[1]);

                    ButtonType confirm = new ButtonType("Ok");
                    alert.showAndWait();
                }
            });
        }
    }


    /**
     * No game is processing
     */

    public static void nowatch() {

        Platform.runLater(new Runnable() {
            @Override

            public void run() {
                // Alert window
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText("No game is existed in process!!!!!");

                ButtonType confirm = new ButtonType("Ok");
                alert.getButtonTypes().setAll(confirm);
                alert.showAndWait();
            }
        });
    }


    /**
     * Error message during the process of invitation
     */

    public static void notinvite() {

        Platform.runLater(new Runnable() {
            @Override

            public void run() {

                // Alert window
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Warning!");
                alert.setHeaderText("You cannot invite anyone!");

                ButtonType confirm = new ButtonType("OK");
                alert.getButtonTypes().setAll(confirm);
                alert.showAndWait();
            }
        });
    }


    /**
     * Error message during the process of start a game
     */

    public static void notaccept() {
        System.out.println("not accept function");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Alert window
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Warning!");
                alert.setHeaderText("A game has already started!");

                ButtonType confirm = new ButtonType("OK");
                alert.getButtonTypes().setAll(confirm);
                alert.showAndWait();
            }
        });
    }


    /**
     * Game stage after changing into another player
     */

    public static void updatePlayer(String[] parts) {

        Platform.runLater(new Runnable() {
            @Override

            public void run() {

                //Game.getInstance().submit.setDisable(false);
                Game.getInstance().pass.setDisable(false);
                Game.getInstance().clearTheWord.setDisable(false);
                Game.getInstance().sendTheWord.setDisable(false);
                Game.getInstance().wordField.setDisable(false);
                String turnName = parts[1];
                Game.getInstance().init(socket, bufferedWriter, bufferedReader);
                Game.getInstance().display.setText(turnName + "'s turn to play"); // abc's turn to play--- update abc

                if (parts[2].equals("close")) {
                    Game.getInstance().submit.setDisable(true);
                    Game.getInstance().pass.setDisable(true);
                    Game.getInstance().clearTheWord.setDisable(true);
                    Game.getInstance().sendTheWord.setDisable(true);
                    Game.getInstance().wordField.setDisable(true);
                    Game.getInstance().wordField.setText("");
                } else if (parts[2].equals("open")) {
                    Game.getInstance().submit.setDisable(true);
                }
            }
        });
    }


    /**
     * Invite players
     */

    public static void invite(String[] parts) {

        String inviter = parts[1];
        Platform.runLater(new Runnable() {
            @Override

            public void run() {
                MainPage.getInstance().init(socket, bufferedWriter, bufferedReader);

                // Alert window
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invite");
                alert.setHeaderText(inviter + " adlı oyuncu seni oyuna davet ediyor!");
                alert.setContentText("Oyuna katılmak istiyor musun?");

                ButtonType accept = new ButtonType("Kabul Et");
                ButtonType deny = new ButtonType("Reddet");
                alert.getButtonTypes().setAll(accept, deny);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == accept) {
                    MessageListener.getInstance().write("fillGameBoard|");
                    MessageListener.getInstance().write("fillRandomValues|");
                    MessageListener.getInstance().write("inviteResponse|accept|" + inviter);

                } else if (result.get() == deny) {
                    //MessageListener.getInstance().write("fillGameBoard|");
                    //MessageListener.getInstance().write("fillRandomValues|");
                    MessageListener.getInstance().write("inviteResponse|deny|" + inviter);
                }
            }
        });

    }


    /**
     * Accept the invitation
     */

    public static void inviteAccept(String[] parts) {

        String a = "";
        for (int i = 1; i < parts.length; i++) {
            a = a + parts[i] + "\n";
        }
        MainPage.getInstance().init(socket, bufferedWriter, bufferedReader);
        MainPage.getInstance().textArea.setText(a);
        //MessageListener.getInstance().write("fillGameBoard|");
        //MessageListener.getInstance().write("fillRandomValues|");

    }


    /**
     * Deny the invitation
     */

    public static void inviteDeny(String[] parts) {
        System.out.println(428 + "inviteDeny function");
        System.out.println("deny");
        Platform.runLater(new Runnable() {
            @Override

            public void run() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Hata");
                alert.setHeaderText("Kimse oyuna katılmıyor");

                ButtonType confirm = new ButtonType("OK");
                alert.getButtonTypes().setAll(confirm);
                alert.showAndWait();
            }
        });
    }

    /**
     * Start a game
     */

    public static void start(String[] parts) {

        Platform.runLater(new Runnable() {
            @Override

            public void run() {
                MainPage.getInstance().closeStage();

                String turnName = parts[1];
                Game.getInstance().init(socket, bufferedWriter, bufferedReader, gameSpaceSize, unavailableCellNumber, twoPointCellNumber, threePointCellNumber, unavailableCoords, twoPointCoords, threePointCoords, randomLetter, letterIndex);
                Game.getInstance().startAGame();
                Game.getInstance().display.setText(turnName + " adlı oyuncunun sırası"); // abc's turn to play--- update abc
                Game.getInstance().label.setText(parts[3] + " adlı oyuncunun ekranı");
                if (parts[2].equals("close")) {
                    Game.getInstance().submit.setDisable(true);
                    Game.getInstance().pass.setDisable(true);
                    Game.getInstance().clearTheWord.setDisable(true);
                    Game.getInstance().sendTheWord.setDisable(true);
                    Game.getInstance().wordField.setDisable(true);
                    Game.getInstance().wordField.setText("");
                } else if (parts[2].equals("open")) {
                    Game.getInstance().submit.setDisable(true);
                    Game.getInstance().pass.setDisable(false);
                    Game.getInstance().clearTheWord.setDisable(false);
                    Game.getInstance().sendTheWord.setDisable(false);
                    Game.getInstance().wordField.setDisable(false);
                }

            }
        });
    }

    public static void fillGameBoard(String[] parts) {
        Platform.runLater(new Runnable() {
            @Override

            public void run() {
                gameSpaceSize = Integer.valueOf(parts[1]);
                unavailableCellNumber = Integer.valueOf(parts[2]);
                twoPointCellNumber = Integer.valueOf(parts[3]);
                threePointCellNumber = Integer.valueOf(parts[4]);
            }
        });
    }

    public static void fillRandomValues(String[] parts) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String fistRandomLetter = parts[1];
                String[] items = fistRandomLetter.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
                if (items.length == 3) {
                    randomLetter.setX(Integer.parseInt(items[0]));
                    randomLetter.setY(Integer.parseInt(items[1]));
                    letterIndex = (Integer.parseInt(items[2]));
                }

                String[] split2 = parts[2].split("-");
                String unavailableX = split2[0];
                String unavailableY = split2[1];
                String[] unavailableXItems = unavailableX.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
                String[] unavailableYItems = unavailableY.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
                for (int i = 0; i < unavailableXItems.length; i++) {
                    try {
                        unavailableCoords.add(new Coords(Integer.parseInt(unavailableXItems[i]), Integer.parseInt(unavailableYItems[i])));
                    } catch (NumberFormatException nfe) {
                        System.out.println(nfe);
                    }
                }
                String[] split3 = parts[3].split("-");
                String twoPointX = split3[0];
                String twoPointY = split3[1];
                String[] twoPointXItems = twoPointX.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
                String[] twoPointYItems = twoPointY.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
                for (int i = 0; i < twoPointYItems.length; i++) {
                    try {
                        twoPointCoords.add(new Coords(Integer.parseInt(twoPointXItems[i]), Integer.parseInt(twoPointYItems[i])));
                    } catch (NumberFormatException nfe) {
                        System.out.println(nfe);
                    }
                }
                String[] split4 = parts[4].split("-");
                String threePointX = split4[0];
                String threePointY = split4[1];
                String[] threePointXItems = threePointX.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
                String[] threePointYItems = threePointY.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");
                for (int i = 0; i < threePointXItems.length; i++) {
                    try {
                        threePointCoords.add(new Coords(Integer.parseInt(threePointXItems[i]), Integer.parseInt(threePointYItems[i])));
                    } catch (NumberFormatException nfe) {
                        System.out.println(nfe);
                    }
                }
            }
        });
    }

    public static void letterCoords(String[] parts) {
        Platform.runLater(new Runnable() {
            @Override

            public void run() {
                enteredLetterCoords.clear();
                String word = parts[1];
                String[] split = parts[2].split("-");
                for (int i = 0; i < split.length; i++) {
                    String[] coords = split[i].split(",");
                    Coords c = new Coords(Integer.valueOf(coords[0]), Integer.valueOf(coords[1]));
                    enteredLetterCoords.add(c);
                }
                if (!isWordValid(word)) {
                    for (Coords c : enteredLetterCoords) {
                        Game.getInstance().labels[c.getX()][c.getY()].setText("");
                    }
                }
            }
        });

    }

    /**
     * Error message during the process of start a game
     */

    public static void notStart(String[] parts) {

        String s = parts[1];

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Uyarı");
                alert.setHeaderText("Oyunu Başlatamazsınız");
                alert.setContentText("Oyun Zaten Başladı");
                alert.showAndWait();
            }
        });
    }


    /**
     * Vote for the chosen word
     */

    public static void vote(String[] parts) {

        String name = parts[1];
        String word = parts[2];
        Platform.runLater(new Runnable() {
            @Override

            public void run() {
                MessageListener.getInstance().init(socket, bufferedWriter, bufferedReader);
                boolean wordValid = isWordValid(word);

                if (wordValid) {
                    MessageListener.getInstance().write("wordValidation|" + word + "|true");
                } else {
                    MessageListener.getInstance().write("wordValidation|" + word + "|false");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Hata!");
                    alert.setHeaderText("Kelime mevcut değil!");
                    ButtonType confirm = new ButtonType("OK");
                    alert.getButtonTypes().setAll(confirm);
                    alert.showAndWait();
                }
            }
        });
    }


    /**
     * Update the score for each turn
     */

    public static void updateOneScore(String[] parts) {

        String a = "";
        for (int i = 1; i < parts.length; i++) {
            a = a + parts[i] + " scores \n";
        }
        System.out.println(a);
        Game.getInstance().init(socket, bufferedWriter, bufferedReader);
        Game.getInstance().textArea.setText(a);
    }


    /**
     * Concurrent game
     */

    public static void updateGame(String[] parts) {
        Platform.runLater(new Runnable() {
            @Override

            public void run() {
                int x = Integer.valueOf(parts[1]);
                int y = Integer.valueOf(parts[2]);
                String content = parts[3];
                Game.getInstance().labels[x][y].setText(content);
            }
        });
    }


    /**
     * All the players' rank
     */

    public static void Rank(String[] parts) {

        Platform.runLater(new Runnable() {
            @Override

            public void run() {
                // Alert window
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Sıralama");
                alert.setHeaderText("Kazanan Oyuncu: " + parts[1]);
                alert.setContentText("Skoru: " + parts[2]);

                ButtonType confirm = new ButtonType("OK");
                alert.getButtonTypes().setAll(confirm);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == confirm) {
                    Game.getInstance().stage.close();
                    MainPage.getInstance().stage.show();
                    MainPage.getInstance().textArea.setText("");
                    Game.getInstance().clear();
                }
            }
        });
    }

    public static boolean isWordValid(String word) {
        String w = word.toLowerCase();
        boolean isValid = false;
        String path = System.getProperty("user.dir");
        File file = new File(path + "/libs/dictionary.txt");
        try {
            URI uri = new File(path + "/libs/dictionary.txt").toURI();
            List<String> alist = Files.lines(Paths.get(uri), Charset.forName("windows-1251"))
                    .filter(line -> line.equals(w))
                    .collect(Collectors.toList());
            if (alist.size() > 0) {
                isValid = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isValid;
    }
}