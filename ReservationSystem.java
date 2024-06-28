import java.util.*;
import java.time.LocalDate;

public class ReservationSystem {
    private Map<String, BanquetHall> banquetHalls = new HashMap<>();
    private Queue<ReservationRequest> waitingList = new LinkedList<>();

    // Add a banquet hall
    public void addBanquetHall(BanquetHall hall) {
        banquetHalls.put(hall.getHallID(), hall);
    }

    // View available halls
    public List<BanquetHall> searchAvailableHalls(String location, LocalDate date, int guestCount) {
        List<BanquetHall> availableHalls = new ArrayList<>();
        for (BanquetHall hall : banquetHalls.values()) {
            if (hall.getLocation().equalsIgnoreCase(location) && hall.isAvailable(date) && hall.getMaxCapacity() >= guestCount) {
                availableHalls.add(hall);
            }
        }
        return availableHalls;
    }

    // Make a reservation
    public void makeReservation(String hallID, LocalDate date, String customerEmail) {
        BanquetHall hall = banquetHalls.get(hallID);
        if (hall != null && hall.isAvailable(date)) {
            hall.reserveHall(date);
            sendNotification(customerEmail, "Reservation successful for hall: " + hall.getName());
        } else {
            sendNotification(customerEmail, "Hall not available. Adding to waiting list.");
            waitingList.add(new ReservationRequest(hallID, date, customerEmail));
        }
    }

    // Process waiting list
    public void processWaitingList() {
        Queue<ReservationRequest> newWaitingList = new LinkedList<>();
        
        while (!waitingList.isEmpty()) {
            ReservationRequest request = waitingList.poll();
            BanquetHall hall = banquetHalls.get(request.getHallID());

            if (hall != null && hall.isAvailable(request.getDate())) {
                hall.reserveHall(request.getDate());
                sendNotification(request.getCustomerEmail(), "Reservation successful for hall: " + hall.getName());
            } else {
                sendNotification(request.getCustomerEmail(), "Hall " + request.getHallID() + " is still not available on " + request.getDate() + ". Keeping in the waiting list.");
                newWaitingList.add(request);
            }
        }
        
        waitingList = newWaitingList; // Update the waiting list with the unresolved requests
    }

    // Send a notification to the customer
    private void sendNotification(String customerEmail, String message) {
        // This is a placeholder for an actual email/SMS sending logic.
        System.out.println("Sending notification to " + customerEmail + ": " + message);
    }
}