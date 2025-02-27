package io.github.tors_0;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

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

        t.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        updatePresence();
                    }
                },
                15_000,
                15_000);

        t.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {try {
                        Main.currentSong = Parser.parse();
                    } catch (Exception e) {
                        System.err.println(e.toString());
                    }
                    }
                },
                5_000 + (Main.MAIN.getTimeDelaySeconds() * 1_000L),
                5_000);
    }

    private void basicPresence() {
        DiscordRichPresence presence = new DiscordRichPresence();

        // get time diff from start to now and convert to minutes
        long x = (long) ( ( Main.MAIN.getTimeDelaySeconds() - ( ( System.currentTimeMillis() - Main.MAIN.getStartTimeMillis() ) / 1000d ) ) / 60d );

        presence.startTimestamp = start_time; // epoch second
        presence.details = "LIVE in " + x + " minutes on 88.1 FM";
        presence.state = "Tune in now!";
        presence.instance = 1;

        lib.Discord_UpdatePresence(presence);
    }

    private void updatePresence() {
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
