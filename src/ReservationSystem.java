import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ReservationSystem for Assignment 2
 * @author Channon Zuo
 * @version 1.0 02/20/2022
 */
public class ReservationSystem implements Serializable {
    private static int ECONOMY_ROW_OFFSET = 9;
    private static UserInputTree USER_INPUT_TREE = new UserInputTree();

    public static ReservationSystem instance;
    public static int FIRST_CLASS_ROWS = 2;
    public static int ECONOMY_CLASS_ROWS = 20;
    public List<Seat> firstClassSeatList, economyClassSeatList;

    /**
     * Constructs a ReservationSystem and sets the static reference to it
     */
    public ReservationSystem() {
        instance = this;
    }

    public ReservationSystem(SaveData saveData) {
        firstClassSeatList = saveData.getFirstClassSeatList();
        economyClassSeatList = saveData.getEconomyClassSeatList();
        instance = this;
    }

    /**
     * Creates every seat in the airplane
     * @precondition: firstClassSeatList and economyClassSeatList are empty
     * @postcondition: firstClassSeatList and economyClassSeatList are filled with Seat objects
     */
    public void generateSeats(){
        // creates empty first class seats
        firstClassSeatList = new ArrayList<>();
        for (int i = 1; i <= FIRST_CLASS_ROWS; i++) {
           for (int j = 0; j < 4; j++) {
                firstClassSeatList.add(new Seat(i, Seat.Column.fromNumber(j), Seat.Cabin.First));
           }
        }

        // creates empty economy seats
        economyClassSeatList = new ArrayList<>();
        for (int i = ECONOMY_ROW_OFFSET + 1; i <= ECONOMY_CLASS_ROWS + ECONOMY_ROW_OFFSET; i++) {
            for (int j = 0; j < 6; j++) {
                economyClassSeatList.add(new Seat(i, Seat.Column.fromNumber(j), Seat.Cabin.Economy));
            }
        }
    }

    /**
     * Prints the seats without a passenger
     */
    public void printFirstClassAvailabilityChart() {
        System.out.println("First Class:");
        for (int i = 1; i <= FIRST_CLASS_ROWS; i++) {
            int row = i;

            // https://www.baeldung.com/java-stream-filter-lambda
            String rowEmpty = i + ": ";
            List<String> emptySeats = firstClassSeatList.stream()
                    .filter( s -> {
                        return s.getRow() == row && s.getPassenger() == null;
                    })
                    .map(s -> {
                        return s.getColumn().toString();
                    })
                    .collect(Collectors.toList());
            rowEmpty += String.join(",", emptySeats);
            System.out.println(rowEmpty);
        }
    }

    /**
     * Prints out the available seats in Economy
     */
    public void printEconomyAvailabilityChart() {
        System.out.println("Economy Class:");
        for (int i = ECONOMY_ROW_OFFSET + 1; i <= ECONOMY_CLASS_ROWS + ECONOMY_ROW_OFFSET; i++) {
            int row = i;

            // https://www.baeldung.com/java-stream-filter-lambda
            String rowEmpty = i + ": ";
            List<String> emptySeats = economyClassSeatList.stream()
                    .filter( s -> {
                        return s.getRow() == row && s.getPassenger() == null;
                    })
                    .map(s -> {
                        return s.getColumn().toString();
                    })
                    .collect(Collectors.toList());
            rowEmpty += String.join(",", emptySeats);
            System.out.println(rowEmpty);
        }
    }

    /**
     * Prints seats that have a passenger along with the passenger's full name
     */
    public void printManifestList() {
        System.out.println("First Class:");
        firstClassSeatList.stream().filter( s -> s.getPassenger() != null)
                .forEach( s -> System.out.println(s.getRow() + s.getColumn().toString() + ": " + s.getPassenger().getName()));

        System.out.println("Economy Class:");
        economyClassSeatList.stream().filter( s -> s.getPassenger() != null)
                .forEach( s -> System.out.println(s.getRow() + s.getColumn().toString() + ": " + s.getPassenger().getName()));
    }

    /**
     * Adds a passenger to a seat
     * @param reservation Reservation object that has the passenger's reservation information
     * @throws Exception if no seats are available
     * @postcondition: The available seat now has a passenger assigned to it
     */
    public void addPassenger(Reservation reservation) throws Exception {
        Seat availableSeat = reservation.availableSeat();
        if (reservation.availableSeat().getCabin() == Seat.Cabin.First) {
            for (Seat s: firstClassSeatList) {
                if (s == availableSeat)
                    s.setPassenger(reservation.getPassenger());
            }
        } else {
            for (Seat s: economyClassSeatList) {
                if (s == availableSeat)
                    s.setPassenger(reservation.getPassenger());
            }
        }
        System.out.println("Passenger added");
    }

    /**
     * Removes an individual passenger from a seat
     * @param name The passenger's name that's going to be removed
     * @return True if the passenger is successfully deleted, false if passenger not found
     * @precondition: Name is a valid name
     * @postcondition: the seat's passenger is set to null
     */
    public boolean deleteReservation(String name) {
        for (Seat s: firstClassSeatList) {
            if (s.getPassenger() == null)
                continue;
            if (s.getPassenger().getName().equals(name) && s.getPassenger().isIndividualPassenger()) {
                s.removePassenger();
                return true;
            }
        }
        for (Seat s: economyClassSeatList) {
            if (s.getPassenger() == null)
                continue;
            if (s.getPassenger().getName().equals(name) && s.getPassenger().isIndividualPassenger()) {
                s.removePassenger();
                return true;
            }
        }
        return false;
    }

    /**
     *Removes a group's reservation from the seats
     * @param groupName Name of Group
     * @return if group successfully is deleted
     * @precondition: group name is a valid name
     * @postcondition: group members' seats' passenger is set to null
     */
    public boolean deleteGroupReservation(String groupName) {
        boolean flag = false;
        for (Seat s: firstClassSeatList) {
            if (s.getPassenger() == null)
                continue;
            if (s.getPassenger().getGroupName().equals(groupName) && !s.getPassenger().isIndividualPassenger()) {
                s.removePassenger();
            }
        }

        for (Seat s: economyClassSeatList) {
            if (s.getPassenger() == null)
                continue;
            if (s.getPassenger().getGroupName().equals(groupName) && !s.getPassenger().isIndividualPassenger()) {
                s.removePassenger();
            }
        }
        return true;
    }

    /**
     * Prints out the main menu
     * @precondition: USER_INPUT_TREE.currentNode is set to mainMenu node
     */
    public static void printCurrentNode() {USER_INPUT_TREE.printCurrentNode();}

    public static void main(String[] args) {
        String fileName;
        if (args.length == 0 ) {
            fileName = "output";
        }
        else
            fileName = args[0];

        ReservationSystem rs = new ReservationSystem();
        rs.generateSeats();
        System.out.println("Seats generated");

        // Read Object from file
        // https://www.youtube.com/watch?v=COx_SUgKJCc
        File file = new File(fileName + ".dat");

        // Checks if there's a file that has data to load from
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(fileName + ".dat");
                ObjectInputStream ois = new ObjectInputStream(fis);
                rs = new ReservationSystem((SaveData) ois.readObject());

                ois.close(); fis.close();
            } catch (IOException e) {
                System.err.println("Error opening file");
            } catch (ClassNotFoundException e) {
                System.err.println("Object read is not a ReservationSystem");
            }
        }

        printCurrentNode();
        USER_INPUT_TREE.run();

        // Saves the ReservationSystem firstClassSeatList and economyClassSeatList to reuse
        SaveData saveData = new SaveData();
        saveData.setFirstClassSeatList(rs.firstClassSeatList);
        saveData.setEconomyClassSeatList(rs.economyClassSeatList);

        // Saves Object to file
        // https://www.youtube.com/watch?v=COx_SUgKJCc
        try {
            FileOutputStream fos = new FileOutputStream(fileName + ".dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(saveData);
            oos.close(); fos.close();
        } catch(IOException e) {
            System.err.println("Error saving to file");
        }
    }
}
