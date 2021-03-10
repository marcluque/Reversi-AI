package de.marcluque.reversi.network;

import de.marcluque.reversi.ai.evaluation.Evaluation;
import de.marcluque.reversi.ai.evaluation.rules.Rules;
import de.marcluque.reversi.ai.search.AbstractSearch;
import de.marcluque.reversi.ai.search.IterativeDeepening;
import de.marcluque.reversi.ai.search.strategies.brs.BestReplySearch;
import de.marcluque.reversi.map.Map;
import de.marcluque.reversi.map.MapLoader;
import de.marcluque.reversi.moves.AbstractMove;
import de.marcluque.reversi.util.Coordinate;
import de.marcluque.reversi.util.Move;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private final String hostname;

    private final int port;

    private Socket clientSocket;

    private Map map;

    private int depthLimit;

    private static long timeLimit;

    private static long startTime;

    private static final long TIME_BUFFER = 200;

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
                        map = MapLoader.generateMapFromString(new String(byteBuffer.array()));
                        System.out.println("MAP:\n" + new String(byteBuffer.array()));

                        Evaluation.initHeuristics(map);
                    }

                    // Assigns player number
                    case 3 -> {
                        AbstractSearch.MAX = (char) ('0' + byteBuffer.get());
                        System.out.println("WE ARE PLAYER: " + AbstractSearch.MAX);
                    }

                    // Requests move from player
                    case 4 -> {
                        // Time limit is in milliseconds
                        timeLimit = byteBuffer.getInt();
                        // timeLimit == 0 -> no time limit
                        if (timeLimit != 0) {
                            startTime = System.currentTimeMillis() + TIME_BUFFER;
                        }
                        depthLimit = byteBuffer.get();

                        Move move = sendMoveResponse();
                        System.out.printf("OUR MOVE: (%d,%d) with special %d%n", move.getX(), move.getY(),
                                move.getSpecialTile());
                    }

                    // Announces move of other player
                    case 6 -> {
                        int x = byteBuffer.getShort();
                        int y = byteBuffer.getShort();
                        int specialField = byteBuffer.get();
                        char receivedPlayer = (char) ('0' + byteBuffer.get());

                        List<Coordinate> capturableTiles = new ArrayList<>();
                        if (AbstractMove.isMoveValidImpl(map, x, y, receivedPlayer, false,
                                Rules.OVERRIDE_STONES, capturableTiles)) {
                            AbstractMove.executeMove(map, x, y, receivedPlayer, capturableTiles);
                        } else {
                            System.err.println("OPPONENT MOVE: (" + x + "," + y + ") with special " + specialField
                                    + " by player " + receivedPlayer + " wasn't valid!");
                        }

                        System.out.println("OPPONENT MOVE: (" + x + "," + y + ") with special " + specialField
                                + " by player " + receivedPlayer);
                    }

                    // Disqualification of a player
                    case 7 -> {
                        int disqualifiedPlayer = byteBuffer.get();
                        if (disqualifiedPlayer == AbstractSearch.MAX) {
                            System.err.println("Client has been disqualified!");
                        } else {
                            System.out.printf("Player %d has been disqualified!%n", disqualifiedPlayer);
                            char[] newOpponents = new char[AbstractSearch.OPPONENTS.length - 1];
                            int counter = 0;
                            for (char opponent : AbstractSearch.OPPONENTS) {
                                if (opponent != disqualifiedPlayer) {
                                    newOpponents[counter++] = opponent;
                                }
                            }

                            System.arraycopy(newOpponents, 0, AbstractSearch.OPPONENTS, 0,
                                    newOpponents.length);
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
        ////////////////////////
        //    RULE UPDATES    //
        ////////////////////////
        Rules.updateOverrideStoneRule(map);

        ////////////////////////
        //     SEARCH MOVE    //
        ////////////////////////
        Move responseMove;
        if (timeLimit == 0) {
            responseMove = IterativeDeepening.iterativeDeepeningDepthLimit(depthLimit, (totalStates) -> {
                return BestReplySearch.search(map, depthLimit, Rules.OVERRIDE_STONES, totalStates);
            });
        } else {
            responseMove = IterativeDeepening.iterativeDeepeningTimeLimit((totalStates) -> {
                return BestReplySearch.search(map, depthLimit, Rules.OVERRIDE_STONES, totalStates);
            });
        }

        ////////////////////////
        //    SEND RESPONSE   //
        ////////////////////////
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

    public static long getLeftTime() {
        return timeLimit - (startTime - System.currentTimeMillis());
    }
}