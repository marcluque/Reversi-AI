package de.datasecs.reversi.network;

import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.map.MapLoader;
import de.datasecs.reversi.moves.Move;
import de.datasecs.reversi.util.Coordinate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

/**
 * Created by Marc Luque on 16.04.2019.
 */
public class Client {

    private String hostname;

    private int port;

    private boolean useAlphaBeta;

    private boolean useSorting;

    private boolean useAspiration;

    private String csvHeader;

    private Socket clientSocket;

    private DataOutputStream outputStream;

    private static int moveCount;

    private long timeForPrepareMap;

    private Timer timer;

    private int timerPuffer;

    private boolean allowOverrideStones;

    private Map map;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        timer = new Timer();
        timerPuffer = 350;
        allowOverrideStones = false;
    }

    public void start() {
        // Handshake with server
        try {
            clientSocket = new Socket(hostname, port);

            // Create output stream and attach to socket
            outputStream = new DataOutputStream(clientSocket.getOutputStream());

            // Prepare message (6 Bytes)
            // Set type of message (1 Byte)nice
            outputStream.writeByte(1);
            // Write length (4 Bytes)
            outputStream.writeInt(1);
            // Write group number (1 Byte = length)
            outputStream.writeByte(1);

            outputStream.flush();
        } catch(Exception e) {
            System.err.println("Failed to connect with server!");
            e.printStackTrace();
        }

        // Run in endless loop and wait for messages
        run();
    }

    private void run() {
        ByteBuffer byteBuffer;
        int type;
        int length;

        int player = 2;

        // Create input stream and attach to socket
        try (DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream())) {
            while (true) {
                // Reads 8-bit Integer = Byte
                type = inputStream.readUnsignedByte();
                System.out.println("TYPE: " + type);
                // Read length (4 Byte Integer)
                length = inputStream.readInt();
                System.out.println("LENGTH: " + length);
                // Read in bytes (length many)
                byteBuffer = ByteBuffer.wrap(inputStream.readNBytes(length));

                switch (type) {
                    // Sends map (as String)
                    case 2:
                        //System.out.println("MAP:\n" + new String(byteBuffer.array()));
                        map = MapLoader.generateMapFromString(new String(byteBuffer.array()));
                        break;
                    // Assigns player number
                    case 3:
                        player = byteBuffer.get();
                        System.out.println("PLAYER: " + player);

                        // Overhead for AI and data to store
                        long startPrepareMap = System.nanoTime();
                        //prepareMapData();
                        timeForPrepareMap = (System.nanoTime() - startPrepareMap) / 1000000;
                        break;
                    // Requests move from player
                    case 4:
                        moveCount++;
                        sendMoveResponse((char) (player + '0'));
                        break;
                    // Announces move of other player
                    case 6:
                        //handleMoveOfOtherPlayer(byteBuffer);
                        System.out.println("MOVE ANNOUCEMENT");
                        int x = byteBuffer.getShort();
                        int y = byteBuffer.getShort();
                        int specialField = byteBuffer.get();
                        char receivedPlayer = (char) ('0' + byteBuffer.get());

                        System.out.println("MOVE: (" + x + "," + y + ") with special " + specialField + " by player " + receivedPlayer);
                        break;
                    // Disqualification of a certain player
                    case 7:
                        int disqualifiedPlayer = byteBuffer.get();
//                        if (disqualifiedPlayer == gameMap.getPlayer()) {
//                            System.err.println("Client has been disqualified!");
//                        } else {
//                            System.out.printf("Player %d has been disqualified!%n", disqualifiedPlayer);
//                        }
                        break;
                    // Announces first phase has ended
                    case 8:
                        //gameMap.setPhase(2);
                        //prepareBombMapData();
                        System.out.println("PHASE 2 BEGINS");
                        break;
                    // Second phase has ended (game ends)
                    case 9:
                        // Close socket and leave infinite loop
                        System.out.println("Game ended!");
                        timer.cancel();
                        clientSocket.close();
                        return;
                    default:
                        System.err.println("Couldn't parse message type: " + type);
                }

                byteBuffer.clear();
                System.out.println("MESSAGE STOP");
            }
        } catch (IOException e) {
            System.err.println("Server failed while running!");
            timer.cancel();
            e.printStackTrace();
        }
    }

    private void sendMoveResponse(char player) {
        int x = 0;
        int y = 0;
        int specialTile = 0;

//        List<Coordinate> tiles = new LinkedList<>();
//        for (int i = 0; i < map.getGameField().length; i++) {
//            for (int j = 0; j < map.getGameField()[0].length; j++) {
//                if (Move.isMoveValid(map, i, j, player, tiles, 1)) {
//                    x = i;
//                    y = j;
//                    specialTile = 0;
//                }
//            }
//        }

        x = 50;
        y = 8;

        try {
            // Write type of message (1 Byte)
            outputStream.writeByte(5);

            // Write length (4 Bytes)
            outputStream.writeInt(5);

            // Send 2 Bytes for x-coord
            outputStream.writeShort(x);
            // Send 2 Bytes for y-coord
            outputStream.writeShort(y);
            // Send 1 Byte for special tile
            outputStream.writeByte(specialTile);

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}