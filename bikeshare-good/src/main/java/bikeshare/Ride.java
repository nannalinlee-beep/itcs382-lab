package bikeshare;

public record Ride(String bikeId, String memberId, MemberType memberType, int minutes, int fee) {
}
