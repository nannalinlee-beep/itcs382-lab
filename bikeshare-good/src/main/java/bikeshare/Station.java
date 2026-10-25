package bikeshare;

public enum Station {
    LIBRARY,
    CANTEEN,
    DORM;

    public static Station fromText(String text) {
        return valueOf(text.toUpperCase());
    }
}
