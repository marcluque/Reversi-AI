package de.datasecs.reversi.network;

import de.datasecs.reversi.ai.search.AbstractSearch;
import de.datasecs.reversi.ai.search.IterativeDeepening;
import de.datasecs.reversi.map.Map;
import de.datasecs.reversi.map.MapLoader;
import de.datasecs.reversi.util.Move;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

public class Client {

    private String hostname;

    private int port;

    private Socket clientSocket;

    private Map map;

    private int phase;

    private DataOutputStream outputStream;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        phase = 1;
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
                    }

                    // Assigns player number
                    case 3 -> {
                        AbstractSearch.MAX = (char) ('0' + byteBuffer.get());
                        System.out.println("We are player " + AbstractSearch.MAX);
                    }

                    // Requests move from player
                    case 4 -> {
                        Move move = sendMoveResponse();
                        System.out.printf("OUR MOVE: (%d,%d) with special %d%n", move.getX(), move.getY(), move.getSpecialTile());
                    }

                    // Announces move of other player
                    case 6 -> {
                        //handleMoveOfOtherPlayer(byteBuffer);
                        int x = byteBuffer.getShort();
                        int y = byteBuffer.getShort();
                        int specialField = byteBuffer.get();
                        char receivedPlayer = (char) ('0' + byteBuffer.get());

                        System.out.println("OPPONENT MOVE: (" + x + "," + y + ") with special " + specialField + " by player " + receivedPlayer);
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

                            System.arraycopy(newOpponents, 0, AbstractSearch.OPPONENTS, 0, newOpponents.length);
                        }
                    }

                    // Announces first phase has ended
                    case 8 -> {
                        phase = 2;
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
        Move move = IterativeDeepening.iterativeDeepeningDepthLimit(5, );

        try {
            // Write type of message (1 Byte)
            outputStream.writeByte(5);

            // Write length (4 Bytes)
            outputStream.writeInt(5);

            // Send 2 Bytes for x-coord
            outputStream.writeShort(move.getX());
            // Send 2 Bytes for y-coord
            outputStream.writeShort(move.getY());
            // Send 1 Byte for special tile
            outputStream.writeByte(move.getSpecialTile());

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return move;
    }
}