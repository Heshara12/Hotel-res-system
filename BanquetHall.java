import java.time.LocalDate;

public class BanquetHall {
    private String hallID;
    private String name;
    private String location;
    private int maxCapacity;
    private LocalDate reservationDate;
    private double menuPrice;

    // Constructor
    public BanquetHall(String hallID, String name, String location, int maxCapacity, double menuPrice) {
        this.hallID = hallID;
        this.name = name;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.menuPrice = menuPrice;
        this.reservationDate = null; // Initially, the hall is not reserved
    }

    // Getters
    public String getHallID() {
        return hallID;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public double getMenuPrice() {
        return menuPrice;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    // Check if the hall is available on the given date
    public boolean isAvailable(LocalDate date) {
        return reservationDate == null || !reservationDate.equals(date);
    }

    // Reserve the hall for a specific date
    public void reserveHall(LocalDate date) {
        this.reservationDate = date;
    }

    // Cancel the reservation
    public void cancelReservation() {
        this.reservationDate = null;
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Location: %s, Capacity: %d, Price: %.2f", 
                             hallID, name, location, maxCapacity, menuPrice);
    }
}