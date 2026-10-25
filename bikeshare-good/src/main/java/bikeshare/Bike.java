package bikeshare;

public class Bike {
    private final String id;
    private Station station;
    private boolean rented;

    public Bike(String id, Station station) {
        this.id = id;
        this.station = station;
    }

    public String getId() {
        return id;
    }

    public boolean isAvailableAt(Station place) {
        return !rented && station == place;
    }

    public boolean isRented() {
        return rented;
    }

    public void rent() {
        rented = true;
    }

    public void returnTo(Station place) {
        station = place;
        rented = false;
    }
}
