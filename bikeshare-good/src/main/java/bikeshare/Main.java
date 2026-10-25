package bikeshare;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BikeShare bikeShare = new BikeShare(new CsvRideStore("rides.csv"));
        CommandLine commandLine = new CommandLine(bikeShare);
        try (Scanner in = new Scanner(System.in)) {
            commandLine.run(in);
        }
    }
}
