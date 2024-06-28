import java.time.LocalDate;
import java.util.*;

public class hj {
    private static Scanner scanner = new Scanner(System.in);
    private static ReservationSystem reservationSystem = new ReservationSystem();
    private static Stack<MenuOption> menuStack = new Stack<>(); // Stack for menu navigation
    private static Stack<ReservationAction> actionStack = new Stack<>(); // Stack for undo functionality

    public static void main(String[] args) {
        populateHalls();
        menuStack.push(MenuOption.MAIN_MENU); // Initial menu
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
            MenuOption currentMenu = menuStack.peek(); // Get the current menu
            switch (currentMenu) {
                case MAIN_MENU:
                    displayMainMenu();
                    break;
                case VIEW_HALLS:
                    viewAvailableHalls();
                    break;
                case MAKE_RESERVATION:
                    makeReservation();
                    break;
                case PROCESS_WAITING_LIST:
                    processWaitingList();
                    break;
            }
        }
    }

    private static void displayMainMenu() {
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
        System.out.println("| (4). Undo Last Action                  |");
        System.out.println("| (5). Exit                              |");
        System.out.println("------------------------------------------");
        System.out.print("Select an option: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }

        switch (choice) {
            case 1:
                menuStack.push(MenuOption.VIEW_HALLS);
                break;
            case 2:
                menuStack.push(MenuOption.MAKE_RESERVATION);
                break;
            case 3:
                menuStack.push(MenuOption.PROCESS_WAITING_LIST);
                break;
            case 4:
                undoLastAction();
                break;
            case 5:
                System.out.println("Thank you for using the system. Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid option. Please try again.");
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
            menuStack.pop(); // Go back to the previous menu
            return;
        }
        System.out.print("Enter number of guests: ");
        int guestCount;
        try {
            guestCount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter an integer.");
            menuStack.pop(); // Go back to the previous menu
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
        menuStack.pop(); // Return to the previous menu
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
            menuStack.pop(); // Go back to the previous menu
            return;
        }
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        reservationSystem.makeReservation(hallID, date, email);
        actionStack.push(new ReservationAction(hallID, date, email, ActionType.RESERVE)); // Push the action onto the stack
        menuStack.pop(); // Return to the previous menu
    }

    private static void processWaitingList() {
        reservationSystem.processWaitingList();
        menuStack.pop(); // Return to the previous menu
    }

    private static void undoLastAction() {
        if (!actionStack.isEmpty()) {
            ReservationAction lastAction = actionStack.pop();
            switch (lastAction.getActionType()) {
                case RESERVE:
                    reservationSystem.cancelReservation(lastAction.getHallID(), lastAction.getDate(), lastAction.getEmail());
                    System.out.println("Last reservation action undone.");
                    break;
                // Additional action types can be handled here
                default:
                    System.out.println("No action to undo.");
            }
        } else {
            System.out.println("No actions to undo.");
        }
    }
}

class ReservationSystem {
    private List<BanquetHall> banquetHalls = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public void addBanquetHall(BanquetHall hall) {
        banquetHalls.add(hall);
    }

    public List<BanquetHall> searchAvailableHalls(String location, LocalDate date, int guestCount) {
        // Example search logic
        List<BanquetHall> availableHalls = new ArrayList<>();
        for (BanquetHall hall : banquetHalls) {
            if (hall.getLocation().equalsIgnoreCase(location) && hall.getCapacity() >= guestCount && isAvailable(hall, date)) {
                availableHalls.add(hall);
            }
        }
        return availableHalls;
    }

    public void makeReservation(String hallID, LocalDate date, String email) {
        for (BanquetHall hall : banquetHalls) {
            if (hall.getId().equals(hallID)) {
                reservations.add(new Reservation(hallID, date, email));
                System.out.println("Reservation made successfully.");
                return;
            }
        }
        System.out.println("Hall not found.");
    }

    public void cancelReservation(String hallID, LocalDate date, String email) {
        for (Reservation reservation : reservations) {
            if (reservation.getHallID().equals(hallID) && reservation.getDate().equals(date) && reservation.getEmail().equals(email)) {
                reservations.remove(reservation);
                System.out.println("Reservation cancelled successfully.");
                return;
            }
        }
        System.out.println("Reservation not found.");
    }

    public void processWaitingList() {
        // Example logic to process the waiting list
        System.out.println("Processing waiting list...");
    }

    private boolean isAvailable(BanquetHall hall, LocalDate date) {
        for (Reservation reservation : reservations) {
            if (reservation.getHallID().equals(hall.getId()) && reservation.getDate().equals(date)) {
                return false;
            }
        }
        return true;
    }
}

class BanquetHall {
    private String id;
    private String name;
    private String location;
    private int capacity;
    private double price;

    public BanquetHall(String id, String name, String location, int capacity, double price) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return "BanquetHall [ID=" + id + ", Name=" + name + ", Location=" + location + ", Capacity=" + capacity + ", Price=" + price + "]";
    }
}

class Reservation {
    private String hallID;
    private LocalDate date;
    private String email;

    public Reservation(String hallID, LocalDate date, String email) {
        this.hallID = hallID;
        this.date = date;
        this.email = email;
    }

    public String getHallID() {
        return hallID;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }
}

class ReservationAction {
    private String hallID;
    private LocalDate date;
    private String email;
    private ActionType actionType;

    public ReservationAction(String hallID, LocalDate date, String email, ActionType actionType) {
        this.hallID = hallID;
        this.date = date;
        this.email = email;
        this.actionType = actionType;
    }

    public String getHallID() {
        return hallID;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }

    public ActionType getActionType() {
        return actionType;
    }
}

enum ActionType {
    RESERVE,
    CANCEL
}

enum MenuOption {
    MAIN_MENU,
    VIEW_HALLS,
    MAKE_RESERVATION,
    PROCESS_WAITING_LIST
}
