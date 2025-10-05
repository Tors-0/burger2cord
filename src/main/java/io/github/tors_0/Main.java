package io.github.tors_0;

import org.kohsuke.args4j.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static Song currentSong;

    public static final Main MAIN = new Main();

    private final long startTime;

    @Option(name="-t",usage = "seconds until the show goes live (advertise beforehand!)", required = false)
    private int timeDelay = 0; // default value


    @Option(name="-show", usage="which show is running (burg/pick/hobby)", required = false)
    private String show = "burg"; // default value

    @Argument
    private List<String> arguments = new ArrayList<String>();

    public static void main(String[] args) throws IOException {
        MAIN.doMain(args);

        new UserActivity().initializeRPC();
    }

    /**
     * https://github.com/kohsuke/args4j/blob/master/args4j/examples/SampleMain.java
     *
     * @author Kohsuke Kawaguchi (kk@kohsuke.org)
     * @param args program args to parse for named arguments
     */
    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            // parse the arguments.
            parser.parseArgument(args);

            // you can parse additional arguments if you want.
            // parser.parseArgument("more","args");

            // after parsing arguments, you should check
            // if enough arguments are given.

        } catch( CmdLineException e ) {
            // if there's a problem in the command line,
            // you'll get this exception. this will report
            // an error message.
            System.err.println(e.getMessage());
            System.err.println("java burger2cord [options...] arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();

            // print option sample. This is useful some time
            System.err.println("  Example: java burger2cord"+parser.printExample(OptionHandlerFilter.ALL));

            return;
        }

        if( timeDelay>=0 )
            System.out.println("-t was "+timeDelay);
        if (show != null) {
            System.out.println("-show was " + show);
        }

        // access non-option arguments
        System.out.println("other arguments are:");
        for( String s : arguments )
            System.out.println(s);
    }

    public Main() {
        startTime = System.currentTimeMillis();
        if (!show.equals("burg") && !show.equals("hobby") && !show.equals("pick")) {
            show = "burg";
        }
    }

    public long getStartTimeMillis() {
        return startTime;
    }

    public int getTimeDelaySeconds() {
        return timeDelay;
    }

    public String getShow() {
        return show;
    }
    public String getShowName() {
        return switch (show) {
            case "burg"-> "The Burg";
            case "hobby"-> "Hobby Heights";
            case "pick"-> "The DJ's Pick";
            default -> throw new IllegalStateException("Unexpected value: " + show);
        };
    }
}