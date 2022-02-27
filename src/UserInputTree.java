import java.util.*;
/**
 * Menu Navigator for reservation system
 * @author Channon Zuo
 * @version 1.0 02/21/2022
 */
public class UserInputTree {
    private Node baseNode;
    private Node currentNode;

    /**
     * Contructs every menu option instructed by Assignment 2
     */
    public UserInputTree() {
        Node mainMenu = new Node("\nAdd [P]assenger, Add [G]roup, [C]ancel Reservations, Print Seating [A]vailability Chart, Print [M]anifest, [Q]uit");

        // Executes adding individual passenger
        Node addPassenger = new Node("Name:");
        addPassenger.setAction(() -> {
            String name = getStringInput();
            Passenger passenger = new Passenger(name);
            Seat.Cabin cabin = getCabinInput();
            Seat.Type type = getTypeInput(cabin);
            Reservation reservation = new Reservation(passenger, cabin, type);
            try {
                ReservationSystem.instance.addPassenger(reservation);
            } catch (Exception e) {
                System.out.println("No available seats");
            }
            returnToMainMenu();
        });

        // Executes adding group of people
        // @precondition: group is 2 or more people. Group Name is not repeating
        Node addGroup = new Node("Group Name: ");
        addGroup.setAction(() -> {
            String groupName = getStringInput();
            System.out.print("Full names of group members seperated with a comma: ");
            String groupMembers = getStringInput();
            groupMembers = groupMembers.replaceAll("\\s+", " ");
            String[] arr = groupMembers.split(",");
            Seat.Cabin cabin = getCabinInput();

            if (Reservation.enoughSeats(arr.length, cabin)) {
                int counter = 0;
                while (counter < arr.length) {
                    List<Seat> seatList = Reservation.adjacentSeats(arr.length - counter, cabin);
                    for (int i = 0; i < seatList.size(); i++) {
                        String name = arr[counter];
                        Passenger passenger = new Passenger(name);
                        passenger.isIndividualPassenger(false);
                        passenger.setGroupName(groupName);

                        seatList.get(i).setPassenger(passenger);
                        counter++;
                    }
                }
                System.out.println("Group members added");
            }
            else {
                System.out.println("Not enough seats to add members");
            }
            returnToMainMenu();
        });

        // Executes Cancel reservation(s)
        Node cancelReservationMenu = new Node("Cancel [I]ndividual or [G]roup?");
        Node cancelIndividualReservation = new Node("Name (case sensitive): ");
        Node cancelGroupReservation = new Node("Group Name (case sensitive): ");

        cancelReservationMenu.addNextNode("i", cancelIndividualReservation);
        cancelReservationMenu.addNextNode("g", cancelGroupReservation);

        // Cancels an individual reservation
        cancelIndividualReservation.setAction(() -> {
            String name = getStringInput();
            if (ReservationSystem.instance.deleteReservation(name))
                System.out.println("Reservation successfully deleted");
            else
                System.out.println("Reservation not found");
            returnToMainMenu();
        });

        // Cancels a group reservation
        cancelGroupReservation.setAction(() -> {
            String name = getStringInput();
            if (ReservationSystem.instance.deleteGroupReservation(name))
                System.out.println("Group reservation successfully deleted");
            else
                System.out.println("Reservation not found");
            returnToMainMenu();
        });

        // Executes printing availability chart
        Node availabilityChart = new Node("");
        availabilityChart.setAction(() -> {
            Seat.Cabin cabin = getCabinInput();
            switch (cabin) {
                case First:
                    ReservationSystem.instance.printFirstClassAvailabilityChart();
                    break;
                case Economy :
                    ReservationSystem.instance.printEconomyAvailabilityChart();
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }

            returnToMainMenu();
        });

        // Executes printing manifest list
        Node manifestList = new Node("");
        manifestList.setAction(() -> {
            ReservationSystem.instance.printManifestList();
            returnToMainMenu();
        });

        // Executes quit the program
        Node quitNode = new Node("");
        quitNode.setAction(() -> {throw new UserQuitException();});


        mainMenu.addNextNode("p", addPassenger);
        mainMenu.addNextNode("g", addGroup);
        mainMenu.addNextNode("c", cancelReservationMenu);
        mainMenu.addNextNode("a", availabilityChart);
        mainMenu.addNextNode("m", manifestList);
        mainMenu.addNextNode("q", quitNode);

        baseNode = mainMenu;
        currentNode = mainMenu;
    }

    /**
     * Prints out the node's text
     */
    public void printCurrentNode() {
        System.out.println(currentNode.displayText);
    }

    /**
     * Returns to main menu display and prints it
     * @postcondition: currentNode is mainMenu
     */
    public void returnToMainMenu() {
        currentNode = baseNode;
        printCurrentNode();
    }

    /**
     * Updates current node based on String input
     * @param s The user input
     * @pastcondition: calls navigate(Node n)
     * @throws Exception if user's input is invalid
     */
    public void navigate(String s) throws Exception {
        if (!currentNode.inputToNextNode.containsKey(s))
            throw new IllegalArgumentException(s + " is not a valid input");
        Node n = currentNode.inputToNextNode.get(s);
        navigate(n);
    }

    /**
     * Sets current node, activates action, and prints it
     * @param n Requested node from menu based on user input
     * @throws Exception if n is null
     */
    public void navigate(Node n) throws Exception {
        currentNode = n;
        printCurrentNode();
        if (currentNode.action != null)
            currentNode.action.activate();
    }

    /**
     * Asks for user input
     * @postcondition: navigates to the requested node
     */
    public void run() {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            input = input.toLowerCase();
            try {
                navigate(input);
            } catch (UserQuitException e) {
                System.out.println("Good bye!");
                break;
            } catch (Exception e) {
                System.out.println(e);
                returnToMainMenu();
            }
        }
    }
    private String getStringInput() {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();

        return s;
    }

    private Seat.Cabin getCabinInput() {
        Scanner in = new Scanner(System.in);
        Seat.Cabin cabin = null;
        while (cabin == null) {
            System.out.print("Service Class [First] or [Economy]: ");
            String classPreference = in.nextLine();
            classPreference = classPreference.toLowerCase();
            if (classPreference.equals("first"))
                cabin = Seat.Cabin.First;
            else if (classPreference.equals("economy"))
                cabin = Seat.Cabin.Economy;
            else
                System.out.println("Invalid input, please try again");
        }
        return cabin;
    }

    private Seat.Type getTypeInput(Seat.Cabin cabin) {
        Scanner in = new Scanner(System.in);
        Seat.Type type = null;
        while (type == null) {
            if (cabin == Seat.Cabin.Economy) {
                System.out.print("Seat Preference [W]indow, [C]enter, or [A]isle: ");
                String typePreference = in.nextLine().toLowerCase();
                try {
                    type = Seat.Type.fromLetter(typePreference);
                } catch (Exception e) {
                    System.out.println("Invalid format :( Try again!");
                }
            }
            else {
                System.out.print("Seat Preference [W]indow or [A]isle: ");
                String typePreference = in.nextLine().toLowerCase();
                try {
                    type = Seat.Type.fromLetter(typePreference);
                } catch (Exception e) {
                    System.out.println("Invalid format :( Try again!");
                }
            }
        }
        return type;
    }

    private class Node {
        String displayText;
        Map<String, Node> inputToNextNode;
        Action action;

        private Node(String s) {
            displayText = s;
            inputToNextNode = new HashMap<>();
        }
        private void addNextNode(String s, Node n) { inputToNextNode.put(s,n); }
        private void setAction(Action a) { action = a; }
    }
    /**
     * Action of a node
     */
    public interface Action {
        void activate() throws Exception;
    }
}
