package bikeshare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BikeShare {
    private static final int BIKES_PER_STATION = 2;

    private final List<Bike> bikes = new ArrayList<>();
    private final Map<String, Rental> rentals = new HashMap<>();
    private final RideStore rideStore;

    private record Rental(String memberId, MemberType memberType) {
    }

    public BikeShare(RideStore rideStore) {
        this.rideStore = rideStore;
        int number = 1;
        for (Station station : Station.values()) {
            for (int i = 0; i < BIKES_PER_STATION; i++) {
                bikes.add(new Bike("B" + number++, station));
            }
        }
    }

    public Optional<String> rent(String memberId, MemberType memberType, Station station) {
        for (Bike bike : bikes) {
            if (bike.isAvailableAt(station)) {
                bike.rent();
                rentals.put(bike.getId(), new Rental(memberId, memberType));
                return Optional.of(bike.getId());
            }
        }
        return Optional.empty();
    }

    public Optional<Ride> returnBike(String bikeId, Station station, int minutes) {
        Rental rental = rentals.remove(bikeId);
        if (rental == null) {
            return Optional.empty();
        }
        for (Bike bike : bikes) {
            if (bike.getId().equals(bikeId) && bike.isRented()) {
                bike.returnTo(station);
            }
        }
        Ride ride = new Ride(bikeId, rental.memberId(), rental.memberType(), minutes,
                rental.memberType().feeFor(minutes));
        rideStore.save(ride);
        return Optional.of(ride);
    }
}
