package bikeshare;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;

public class CsvRideStore implements RideStore {
    private final String fileName;

    public CsvRideStore(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void save(Ride ride) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
            out.println(String.join(",", ride.bikeId(), ride.memberId(), ride.memberType().name(),
                    String.valueOf(ride.minutes()), String.valueOf(ride.fee())));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save the ride to " + fileName, e);
        }
    }
}
