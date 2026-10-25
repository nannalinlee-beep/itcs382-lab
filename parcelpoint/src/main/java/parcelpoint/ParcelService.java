package parcelpoint;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class ParcelService {
    private static final int FREE_DAYS = 3;

    private List<Locker> lockers = new ArrayList<>();
    private List<Parcel> parcels = new ArrayList<>();

    public ParcelService() {
        lockers.add(new Locker(1, ParcelSize.SMALL));
        lockers.add(new Locker(2, ParcelSize.MEDIUM));
        lockers.add(new Locker(3, ParcelSize.LARGE));
    }

    public long countFreeLockers() {
        return lockers.stream().filter(Locker::isFree).count();
    }

    public Locker storeParcel(Parcel parcel) {
        for (Locker locker : lockers) {
            if (locker.isFree() && parcel.getSize().fitsIn(locker.getSize())) {
                locker.setParcel(parcel);
                parcel.status = "STORED";
                parcels.add(parcel);
                saveToFile();
                return locker;
            }
        }
        return null;
    }

    public double pickUp(String code, int days) {
        for (Locker locker : lockers) {
            Parcel parcel = locker.getParcel();
            if (parcel != null && parcel.getCode().equals(code)) {
                locker.setParcel(null);
                parcel.status = "PICKED_UP";
                saveToFile();
                return fee(parcel, days);
            }
        }
        return 0;
    }

    public double fee(Parcel parcel, int days) {
        if (days <= FREE_DAYS) {
            return 0;
        }
        return (days - FREE_DAYS) * parcel.getSize().getDailyFee();
    }

    public List<Parcel> getAllParcels() {
        return List.copyOf(parcels);
    }

    public List<Parcel> getWaitingParcels() {
        return parcels.stream().filter(parcel -> parcel.status.equals("STORED")).toList();
    }

    private void saveToFile() {
        try (FileWriter writer = new FileWriter("parcels.csv")) {
            for (Parcel parcel : parcels) {
                writer.write(parcel.getCode() + "," + parcel.status + "\n");
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save parcels.csv", e);
        }
    }
}
