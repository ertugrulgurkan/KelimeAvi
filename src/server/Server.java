package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    public static Hashtable<String, Socket> Player = new Hashtable<String, Socket>();
    public static Hashtable<Socket, String> Player2 = new Hashtable<Socket, String>();
    public static Hashtable<String, Socket> nowPlayer = new Hashtable<String, Socket>();
    public static Hashtable<String, Socket> watchPlayer = new Hashtable<String, Socket>();
    public static Hashtable<String, Integer> Score = new Hashtable<String, Integer>();
    public static ArrayList<String> Validate = new ArrayList<String>();
    public static ArrayList<String> AllValidate = new ArrayList<String>();
    public static boolean Start = false;
    public static String[][] game = new String[20][20];
    //public static int counter=0;
    public static int number = 0;
    public static int responseCounter = 0;
    public static int passCounter = 0;
    public static String turnName = null;
    public static String[] nameList = new String[10000];
    public static Server server;
    public static int gameSpaceSize = 0;
    public static int unavailableCellNumber = 0;
    public static int twoPointCellNumber = 0;
    public static int threePointCellNumber = 0;
    public static int winningPoint = 9999999;
    public static String unavailableCells = "";
    public static String twoPointCells = "";
    public static String threePointCells = "";
    public static Set<Integer> initializingLetter = new LinkedHashSet<Integer>();

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public static void main(String[] args) {
        String port = args[0];
        initGame(args);
        // Establish socket
        ServerSocket listen = null;
        try {
            listen = new ServerSocket(Integer.valueOf(port));
            System.out.println(Thread.currentThread().getName()
                    + "-server listening on port " + port
                    + " for a connection");
            int clientNum = 0;
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("IP Address: " + address.getHostAddress());
            while (true) {
                Socket lis1 = listen.accept();
                System.out.println(Thread.currentThread().getName()
                        + "-Client connection accepted");
                clientNum++;
                Connection con = new Connection(lis1);
                con.setName("Thread " + clientNum);
                con.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (listen != null) {
                try {
                    listen.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void initGame(String[] args) {
        gameSpaceSize = Integer.valueOf(args[1]);
        unavailableCellNumber = Integer.valueOf(args[2]);
        twoPointCellNumber = Integer.valueOf(args[3]);
        threePointCellNumber = Integer.valueOf(args[4]);
        winningPoint = Integer.valueOf(args[5]);
        game = new String[gameSpaceSize][gameSpaceSize];
        for (int i = 0; i < gameSpaceSize; i++)
            for (int j = 0; j < gameSpaceSize; j++)
                game[i][j] = "";

        for (int i = 0; i < 10000; i++) {
            nameList[i] = null;
        }
        Random rnd = new Random();
        while (initializingLetter.size() < 2) {
            Integer next = rnd.nextInt(Server.gameSpaceSize);
            initializingLetter.add(next);
        }
        while (initializingLetter.size() < 3) {
            Integer next = rnd.nextInt(26);
            initializingLetter.add(next);
        }
        unavailableCells = Connection.getRandomValues(Server.unavailableCellNumber, Server.gameSpaceSize);
        twoPointCells = Connection.getRandomValues(Server.twoPointCellNumber, Server.gameSpaceSize);
        threePointCells = Connection.getRandomValues(Server.threePointCellNumber, Server.gameSpaceSize);
    }
}