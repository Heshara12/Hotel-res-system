import java.time.LocalDate;
import java.util.*;

public class Hotel {
    private static Scanner scanner = new Scanner(System.in);
    private static ReservationSystem reservationSystem = new ReservationSystem();

    public static void main(String[] args) {
        populateHalls();
        startFunction();
    }

    private static void populateHalls() {
        // Add initial banquet halls
        reservationSystem.addBanquetHall(new BanquetHall("H001", "Grand Ballroom", "Colombo", 500, 1500.00));
        reservationSystem.addBanquetHall(new BanquetHall("H002", "Sunset Hall", "Kandy", 200, 800.00));
        reservationSystem.addBanquetHall(new BanquetHall("H003", "Ocean View Hall", "Galle", 300, 1200.00));
        reservationSystem.addBanquetHall(new BanquetHall("H004", "Mountain Peak Hall", "Nuwara Eliya", 150, 700.00));
        reservationSystem.addBanquetHall(new BanquetHall("H005", "City Lights Hall", "Colombo", 250, 900.00));
        // More halls can be added here as needed
    }

    private static void startFunction() {
        while (true) {
            System.out.println("------------------------------------------");
            System.out.println("|                                        |");
            System.out.println("|    WELCOME TO HOTEL BLUEMOON           |");
            System.out.println("|                                        |");
            System.out.println("------------------------------------------");
            System.out.println("|                                        |");
            System.out.println("| Please select your option from below   |");
            System.out.println("| (1). View Available Halls              |");
            System.out.println("| (2). Make a Reservation                |");
            System.out.println("| (3). Process Waiting List              |");
            System.out.println("| (4). Exit                              |");
            System.out.println("------------------------------------------");
            System.out.print("Select an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    viewAvailableHalls();
                    break;
                case 2:
                    makeReservation();
                    break;
                case 3:
                    processWaitingList();
                    break;
                case 4:
                    System.out.println("Thank you for using the system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewAvailableHalls() {
        System.out.print("Enter location: ");
        String location = scanner.nextLine();
        System.out.print("Enter reservation date (YYYY-MM-DD): ");
        LocalDate date;
        try {
            date = LocalDate.parse(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }
        System.out.print("Enter number of guests: ");
        int guestCount;
        try {
            guestCount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter an integer.");
            return;
        }

        List<BanquetHall> availableHalls = reservationSystem.searchAvailableHalls(location, date, guestCount);
        if (availableHalls.isEmpty()) {
            System.out.println("No halls available.");
        } else {
            for (BanquetHall hall : availableHalls) {
                System.out.println(hall);
            }
        }
    }

    private static void makeReservation() {
        System.out.print("Enter hall ID: ");
        String hallID = scanner.nextLine();
        System.out.print("Enter reservation date (YYYY-MM-DD): ");
        LocalDate date;
        try {
            date = LocalDate.parse(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        reservationSystem.makeReservation(hallID, date, email);
    }

    private static void processWaitingList() {
        reservationSystem.processWaitingList();
    }
}