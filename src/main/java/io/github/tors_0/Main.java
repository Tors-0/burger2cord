package io.github.tors_0;

import java.io.IOException;

public class Main {
    public static Song currentSong;

    public static void main(String[] args) throws IOException {
        try {
            currentSong = Parser.parse();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        new UserActivity().initializeRPC();
    }
}