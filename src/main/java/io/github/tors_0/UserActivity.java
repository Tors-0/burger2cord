package io.github.tors_0;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class UserActivity {
    DiscordRPC lib = DiscordRPC.INSTANCE;
    String applicationId = "1305492408477159425";
    String steamId = "";
    DiscordEventHandlers handlers = new DiscordEventHandlers();
    Long start_time = System.currentTimeMillis() / 1000;

    Integer times = 0;
    Timer t = new Timer();

    public void initializeRPC() {

        handlers.ready = (user) -> System.out.println("Ready!");
        lib.Discord_Initialize(applicationId, handlers, true, steamId);

        basicPresence();
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {
                }
            }
        }, "RPC-Callback-Handler").start();

        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                updatePresence();
            }
        }, 5000, 5000);
    }

    private void basicPresence() {

        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = start_time; // epoch second
        presence.details = "Unknown Song";
        presence.state = "Unknown Artist";
        presence.instance = 1;
        lib.Discord_UpdatePresence(presence);

    }

    private void updatePresence() {
        try {
            Main.currentSong = Parser.parse();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        if (Main.currentSong != null) {
            times++;

            DiscordRichPresence presence = new DiscordRichPresence();

            presence.startTimestamp = start_time;
            presence.instance = 1;

            if (!Main.currentSong.getArtist().equals("Production")) {
                presence.details = Main.currentSong.getTitle().isBlank() ? "Unknown Song" : Main.currentSong.getTitle();
                presence.state = "by " + (Main.currentSong.getArtist().isBlank() ? "Unknown Artist" : Main.currentSong.getArtist());
            } else {
                presence.details = "Sponsor Break";
                presence.state = "88.1 FM KCWU";
            }

            presence.largeImageKey = "881-dark";

            lib.Discord_UpdatePresence(presence);

        } else {
            basicPresence();
        }
    }
}
