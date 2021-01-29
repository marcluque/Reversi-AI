package de.datasecs.reversi;

import de.datasecs.reversi.network.Client;

public class ReversiMain {

    public static void main(String[] args) {
        new Client("127.0.0.1", 8080).start();
    }
}