import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds passenger's reservation information
 * @author Channon Zuo
 * @version 1.0 02/21/2022
 */
public class Reservation implements Serializable {
    private Passenger passenger;
    private Seat.Cabin cabin;
    private Seat.Type type;

    public Reservation(Passenger passenger, Seat.Cabin cabin, Seat.Type type) {
        this.passenger = passenger;
        this.cabin = cabin;
        this.type = type;
    }

    /**
     * Checks if there's enough seats in the Cabin
     * @param integer size of group
     * @param cabin Preferred cabin type
     * @return true if there's enough room, false if not
     */
    public static boolean enoughSeats(int integer, Seat.Cabin cabin){
        List<Seat> seatList;
        int size = integer;

        if (cabin == Seat.Cabin.First)
            seatList = ReservationSystem.instance.firstClassSeatList;
        else
            seatList = ReservationSystem.instance.economyClassSeatList;

        return (size <= seatList.stream().filter((s) -> s.getPassenger() == null).count());
    }

    /**
     * Returns a list of available adjacent seats. Prioritizes filling out 1 row (ABCD or ABCDEF) instead of half sections (AB/CD or ABC/DEF)
     * @param groupMembersSize Size of group
     * @param cabin Preferred cabin type
     * @return a list of available adjacent seats
     */
    public static List<Seat> adjacentSeats(int groupMembersSize, Seat.Cabin cabin) {
        List<Seat> seatList;
        List<Seat> finalAvailableAdjacentSeats = new ArrayList<>();
        List<Seat> tempSeats = new ArrayList<>();

        if (cabin == Seat.Cabin.First) {
            seatList = ReservationSystem.instance.firstClassSeatList;

            for (int i = 0; i < seatList.size(); i++) {
                if (seatList.get(i).getPassenger() != null || i % 4 == 0) {
                    if (finalAvailableAdjacentSeats.size() < tempSeats.size())
                        finalAvailableAdjacentSeats = tempSeats;
                    tempSeats = new ArrayList<>();
                }

                if (seatList.get(i).getPassenger() == null) {
                    tempSeats.add(seatList.get(i));
                    if (tempSeats.size() >= groupMembersSize) {
                        return tempSeats;
                    }
                }
            }
        }
        else {
            seatList = ReservationSystem.instance.economyClassSeatList;

            for (int i = 0; i < seatList.size(); i++) {
                if (seatList.get(i).getPassenger() != null || i % 6 == 0) {
                    if (finalAvailableAdjacentSeats.size() < tempSeats.size())
                        finalAvailableAdjacentSeats = tempSeats;
                    tempSeats = new ArrayList<>();
                }

                if (seatList.get(i).getPassenger() == null) {
                    tempSeats.add((seatList.get(i)));
                    if (tempSeats.size() >= groupMembersSize) {
                        return tempSeats;
                    }
                }
            }
        }
        return finalAvailableAdjacentSeats;
    }

    /**
     * Finds an available seat for an individual based on preference
     * @return an available seat
     */
    public Seat availableSeat() {
        List<Seat> seatList;
        List<Seat.Column> columnList = new ArrayList<>();
        if (cabin == Seat.Cabin.First) {
            seatList = ReservationSystem.instance.firstClassSeatList;
            switch (type) {
                case Window:
                    columnList.add(Seat.Column.A);
                    columnList.add(Seat.Column.D);
                    break;
                case Aisle:
                    columnList.add(Seat.Column.B);
                    columnList.add(Seat.Column.C);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid seat preference");
            }
        }
        else {
            seatList = ReservationSystem.instance.economyClassSeatList;
            switch (type) {
                case Window:
                    columnList.add(Seat.Column.A);
                    columnList.add(Seat.Column.F);
                    break;
                case Center:
                    columnList.add(Seat.Column.B);
                    columnList.add(Seat.Column.E);
                    break;
                case Aisle:
                    columnList.add(Seat.Column.C);
                    columnList.add(Seat.Column.D);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid seat preference");
            }
        }

        List<Seat> availableSeats = seatList.stream().filter(s -> s.getPassenger() == null && columnList.contains(s.getColumn()))
                .collect(Collectors.toList());
        return availableSeats.get(0);
    }

    /**
     * Gets the passenger of a reservation
     * @return a Passenger object
     */
    public Passenger getPassenger() {
        return passenger;
    }
}
