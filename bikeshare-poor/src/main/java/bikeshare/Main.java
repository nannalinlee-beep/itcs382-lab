package bikeshare;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final int STUDENT = 1;
    public static final int STAFF = 2;

    public static String[] stations = {"Library", "Canteen", "Dorm"};
    public static List<String> bikeIds = new ArrayList<>();
    public static List<String> bikeStations = new ArrayList<>();
    public static List<String> bikeMembers = new ArrayList<>();
    public static List<Integer> bikeMemberTypes = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 6; i++) {
            bikeIds.add("B" + (i + 1));
            bikeStations.add(stations[i / 2]);
            bikeMembers.add(null);
            bikeMemberTypes.add(0);
        }
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Commands: rent <memberId> <student|staff> <station>, "
                    + "return <bikeId> <station> <minutes>, quit");
            while (in.hasNextLine()) {
                String[] w = in.nextLine().trim().split("\\s+");
                if (w[0].equals("quit")) {
                    break;
                } else if (w[0].equals("rent") || w[0].equals("return")) {
                    handle(w, w[0].equals("rent"));
                } else {
                    System.out.println("Unknown command");
                }
            }
        }
    }

    public static void handle(String[] w, boolean rent) {
        try {
            if (!List.of(stations).contains(w[rent ? 3 : 2])) {
                System.out.println("Unknown station");
                return;
            }
            if (rent) {
                int type = w[2].equals("staff") ? STAFF : STUDENT;
                for (int i = 0; i < bikeIds.size(); i++) {
                    if (bikeStations.get(i).equals(w[3]) && bikeMembers.get(i) == null) {
                        bikeMembers.set(i, w[1]);
                        bikeMemberTypes.set(i, type);
                        System.out.println("Member " + w[1] + " rented bike " + bikeIds.get(i) + " at " + w[3]);
                        return;
                    }
                }
                System.out.println("No free bike at " + w[3]);
            } else {
                int i = bikeIds.indexOf(w[1]);
                if (i < 0 || bikeMembers.get(i) == null) {
                    System.out.println("Bike " + w[1] + " is not rented");
                    return;
                }
                int minutes = Integer.parseInt(w[3]);
                int fee = minutes * (bikeMemberTypes.get(i) == STAFF ? 3 : 2);
                System.out.println("Receipt: bike " + w[1] + ", " + minutes + " minutes, fee " + fee + " baht");
                try (PrintWriter out = new PrintWriter(new FileWriter("rides.csv", true))) {
                    out.println(w[1] + "," + bikeMembers.get(i) + "," + bikeMemberTypes.get(i) + ","
                            + bikeStations.get(i) + "," + w[2] + "," + minutes + "," + fee);
                }
                bikeStations.set(i, w[2]);
                bikeMembers.set(i, null);
                bikeMemberTypes.set(i, 0);
            }
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
