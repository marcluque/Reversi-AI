package de.marcluque.reversi.network;

import de.marcluque.reversi.ai.evaluation.HeuristicEvaluation;
import de.marcluque.reversi.ai.evaluation.Metrics;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.map.GameInstance;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.map.MapLoader;
import de.marcluque.reversi.util.Logger;
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
            Logger.error("Failed to connect with server!");
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
                length = inputStream.readInt();
                byteBuffer = ByteBuffer.wrap(inputStream.readNBytes(length));

                // Message types
                switch (type) {

                    // Sends map
                    case 2 -> {
                        GameInstance.setMap(MapLoader.generateMapFromString(new String(byteBuffer.array())));
                        Logger.print("MAP:\n%s", new String(byteBuffer.array()));
                    }

                    // Assigns player number
                    case 3 -> {
                        AbstractSearch.MAX_NUMBER = byteBuffer.get();
                        AbstractSearch.MAX = MapUtil.intToPlayer(AbstractSearch.MAX_NUMBER);

                        IntStream.rangeClosed(1, Map.getNumberOfPlayers())
                                .filter(i -> i != AbstractSearch.MAX_NUMBER)
                                .forEach(i -> AbstractSearch.OPPONENTS.add(MapUtil.intToPlayer(i)));

                        IntStream.rangeClosed(1, Map.getNumberOfPlayers())
                                .forEach(i -> AbstractSearch.ACTIVE_PLAYERS.add(MapUtil.intToPlayer(i)));

                        Logger.print("WE ARE PLAYER: %s", AbstractSearch.MAX);

                        HeuristicEvaluation.initHeuristics(GameInstance.getMap());
                        Metrics.initNumberMetrics();
                        Metrics.initBombEffect();
                        Metrics.initNumberPlayableTiles();
                    }

                    // Requests move from player
                    case 4 -> {
                        // Time limit is in milliseconds
                        // timeLimit == 0 -> no time limit
                        GameInstance.setTimeLimit(byteBuffer.getInt());
                        if (GameInstance.getTimeLimit() != 0) {
                            GameInstance.setStartTime(System.currentTimeMillis() + GameInstance.TIME_BUFFER);
                            Logger.print("TIME LIMIT: %d ms".formatted(GameInstance.getTimeLimit()));
                        }

                        int depthLimit = byteBuffer.get();
                        GameInstance.setDepthLimit(depthLimit);
                        if (depthLimit != 0) {
                            Logger.print("DEPTH LIMIT: %d ms".formatted(depthLimit));
                        }

                        Move move = sendMoveResponse();
                        Logger.print("OUR MOVE: (%d,%d) with special %d%n", move.getX(), move.getY(),
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
                            Logger.error("Client has been disqualified!");
                        } else {
                            AbstractSearch.OPPONENTS.remove(disqualifiedPlayer);
                            AbstractSearch.ACTIVE_PLAYERS.remove(disqualifiedPlayer);
                            Logger.print("Player %d has been disqualified!%n", disqualifiedPlayer);
                        }
                    }

                    // Announces first phase has ended
                    case 8 -> {
                        Map.setPhase(2);
                        Logger.print("PHASE 2 BEGINS");
                    }

                    // Second phase has ended (game ends)
                    case 9 -> {
                        Logger.print("GAME OVER");
                        clientSocket.close();
                        return;
                    }

                    default -> Logger.error("Couldn't parse message type: %d%n", type);
                }

                byteBuffer.clear();
            }
        } catch (IOException e) {
            Logger.error("Server failed while running!");
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