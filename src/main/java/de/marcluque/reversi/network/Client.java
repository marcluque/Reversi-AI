package de.marcluque.reversi.network;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.ai.evaluation.metrics.Metrics;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.map.MapLoader;
import de.marcluque.reversi.util.MapUtil;
import de.marcluque.reversi.util.Move;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.stream.IntStream;

/*
 * Created with <3 by marcluque, March 2021
 */
public class Client {

    private final String hostname;

    private final int port;

    private Socket clientSocket;

    private DataOutputStream outputStream;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        Map.setPhase(1);
    }

    public void start() {
        // Handshake with server
        try {
            clientSocket = new Socket(hostname, port);
            outputStream = new DataOutputStream(clientSocket.getOutputStream());

            // Prepare message (6 Bytes)
            // Set type of message (1 Byte)nice
            outputStream.writeByte(1);
            // Write length (4 Bytes)
            outputStream.writeInt(1);
            // Write group number (1 Byte)
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

        try (DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream())) {
            while (true) {
                type = inputStream.readUnsignedByte();
                System.out.println("TYPE: " + type);
                length = inputStream.readInt();
                System.out.println("LENGTH: " + length);
                byteBuffer = ByteBuffer.wrap(inputStream.readNBytes(length));

                // Message types
                switch (type) {

                    // Sends map
                    case 2 -> {
                        GameInstance.setMap(MapLoader.generateMapFromString(new String(byteBuffer.array())));
                        System.out.println("MAP:\n" + new String(byteBuffer.array()));

                        HeuristicEvaluation.initHeuristics(GameInstance.getMap());
                        Metrics.initNumberFreeTiles();
                    }

                    // Assigns player number
                    case 3 -> {
                        AbstractSearch.MAX = MapUtil.intToPlayer(byteBuffer.get());

                        IntStream.rangeClosed(1, Map.getNumberOfPlayers())
                                .filter(i -> i != MapUtil.playerToInt(AbstractSearch.MAX))
                                .forEach(i -> AbstractSearch.OPPONENTS.add(MapUtil.intToPlayer(i)));

                        IntStream.rangeClosed(1, Map.getNumberOfPlayers())
                                .forEach(i -> AbstractSearch.ACTIVE_PLAYERS.add(MapUtil.intToPlayer(i)));

                        System.out.println("WE ARE PLAYER: " + AbstractSearch.MAX);
                    }

                    // Requests move from player
                    case 4 -> {
                        // Time limit is in milliseconds
                        GameInstance.setTimeLimit(byteBuffer.getInt());
                        // timeLimit == 0 -> no time limit
                        if (GameInstance.getTimeLimit() != 0) {
                            GameInstance.setStartTime(System.currentTimeMillis() + GameInstance.TIME_BUFFER);
                        }
                        GameInstance.setDepthLimit(byteBuffer.get());

                        Move move = sendMoveResponse();
                        System.out.printf("OUR MOVE: (%d,%d) with special %d%n", move.getX(), move.getY(),
                                move.getSpecialTile());
                    }

                    // Announces move of (an) opponent
                    case 6 -> {
                        int x = byteBuffer.getShort();
                        int y = byteBuffer.getShort();
                        int specialField = byteBuffer.get();
                        char opponent = (char) ('0' + byteBuffer.get());

                        GameInstance.processOpponentMove(x, y, specialField, opponent);
                    }

                    // Disqualification of a player
                    case 7 -> {
                        int disqualifiedPlayer = byteBuffer.get();
                        if (disqualifiedPlayer == AbstractSearch.MAX) {
                            System.err.println("Client has been disqualified!");
                        } else {
                            AbstractSearch.OPPONENTS.remove(disqualifiedPlayer);
                            AbstractSearch.ACTIVE_PLAYERS.remove(disqualifiedPlayer);
                            System.out.printf("Player %d has been disqualified!%n", disqualifiedPlayer);
                        }
                    }

                    // Announces first phase has ended
                    case 8 -> {
                        Map.setPhase(2);
                        System.out.println("PHASE 2 BEGINS");
                    }

                    // Second phase has ended (game ends)
                    case 9 -> {
                        System.out.println("GAME OVER");
                        clientSocket.close();
                        return;
                    }

                    default -> System.err.println("Couldn't parse message type: " + type);
                }

                byteBuffer.clear();
            }
        } catch (IOException e) {
            System.err.println("Server failed while running!");
            e.printStackTrace();
        }
    }

    private Move sendMoveResponse() {
        Move responseMove = GameInstance.generateMoveResponse();

        try {
            // Write type of message (1 Byte)
            outputStream.writeByte(5);

            // Write length (4 Bytes)
            outputStream.writeInt(5);

            // Send 2 Bytes for x-coord
            outputStream.writeShort(responseMove.getX());
            // Send 2 Bytes for y-coord
            outputStream.writeShort(responseMove.getY());
            // Send 1 Byte for special tile
            outputStream.writeByte(responseMove.getSpecialTile());

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseMove;
    }
}