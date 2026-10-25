package parcelpoint;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ParcelService service = new ParcelService();
        Scanner in = new Scanner(System.in);
        System.out.println("Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit");
        while (in.hasNextLine()) {
            String[] words = in.nextLine().trim().split(" ");
            if (words[0].equals("store")) {
                store(service, words);
            } else if (words[0].equals("pickup")) {
                System.out.println("Fee: " + service.pickUp(words[1], Integer.parseInt(words[2])));
            } else if (words[0].equals("list")) {
                list(service);
            } else if (words[0].equals("staff")) {
                staff(service);
            } else if (words[0].equals("quit")) {
                break;
            }
        }
    }

    private static void store(ParcelService service, String[] words) {
        Parcel parcel = new Parcel(words[1], words[2], ParcelSize.fromCode(Integer.parseInt(words[3])));
        Locker locker = service.storeParcel(parcel);
        if (locker == null) {
            System.out.println("No free locker");
        } else {
            System.out.println("SMS to " + parcel.getStudentId() + ": parcel "
                    + parcel.getCode() + " is in locker " + locker.getNumber());
            System.out.println("Locker " + locker.getNumber());
        }
    }

    private static void list(ParcelService service) {
        for (Parcel parcel : service.getWaitingParcels()) {
            System.out.println(parcel.getCode() + " is waiting");
        }
    }

    private static void staff(ParcelService service) {
        for (Parcel parcel : service.getAllParcels()) {
            System.out.println(parcel.getCode() + " " + parcel.getStudentId() + " " + parcel.getStatus());
        }
        System.out.println("Free lockers: " + service.countFreeLockers());
    }
}
