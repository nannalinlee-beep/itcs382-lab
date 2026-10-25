package bikeshare;

public enum MemberType {
    STUDENT(2),
    STAFF(3);

    private final int feePerMinute;

    MemberType(int feePerMinute) {
        this.feePerMinute = feePerMinute;
    }

    public int feeFor(int minutes) {
        return feePerMinute * minutes;
    }

    public static MemberType fromText(String text) {
        return valueOf(text.toUpperCase());
    }
}
