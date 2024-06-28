import java.time.LocalDate;

public class ReservationRequest {
    private String hallID;
    private LocalDate date;
    private String customerEmail;

    public ReservationRequest(String hallID, LocalDate date, String customerEmail) {
        this.hallID = hallID;
        this.date = date;
        this.customerEmail = customerEmail;
    }

    public String getHallID() {
        return hallID;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}