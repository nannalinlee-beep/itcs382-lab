package bikeshare;

import java.io.PrintStream;
import java.util.Scanner;

public class CommandLine {
    private final BikeShare bikeShare;
    private final PrintStream out = System.out;

    public CommandLine(BikeShare bikeShare) {
        this.bikeShare = bikeShare;
    }

    public void run(Scanner in) {
        out.println("Commands: rent <memberId> <student|staff> <station>, "
                + "return <bikeId> <station> <minutes>, quit");
        while (in.hasNextLine()) {
            String[] words = in.nextLine().trim().split("\\s+");
            if (words[0].equals("quit")) {
                return;
            }
            try {
                handle(words);
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                out.println("Invalid command: " + String.join(" ", words));
            }
        }
    }

    private void handle(String[] words) {
        switch (words[0]) {
            case "rent" -> rent(words[1], MemberType.fromText(words[2]), Station.fromText(words[3]));
            case "return" -> returnBike(words[1], Station.fromText(words[2]), Integer.parseInt(words[3]));
            default -> out.println("Unknown command");
        }
    }

    private void rent(String memberId, MemberType memberType, Station station) {
        bikeShare.rent(memberId, memberType, station).ifPresentOrElse(
                bikeId -> out.println("Member " + memberId + " rented bike " + bikeId + " at " + station),
                () -> out.println("No free bike at " + station));
    }

    private void returnBike(String bikeId, Station station, int minutes) {
        bikeShare.returnBike(bikeId, station, minutes).ifPresentOrElse(
                ride -> out.println("Receipt: bike " + ride.bikeId() + ", " + ride.minutes()
                        + " minutes, fee " + ride.fee() + " baht"),
                () -> out.println("Bike " + bikeId + " is not rented"));
    }
}
