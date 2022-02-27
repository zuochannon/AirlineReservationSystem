import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.Serializable;

/**
 * Seat information
 * @author Channon Zuo
 * @version 1.0 02/20/2022
 */
public class Seat implements Serializable {
    private int row;
    private Column column;
    private Cabin cabin;
    private Passenger passenger;

    /**
     * All the available seat letters in an airplane
     */
    public enum Column {
        A,B,C,D,E,F;
        private static Column[] arr = {A,B,C,D,E,F};

        // Converts a number to a Column
        public static Column fromNumber(int column) {
            return arr[column];
        }
    }

    /**
     * Different types of seats in an airplane
     */
    public enum Type {
        Window, Center, Aisle;

        public static Type fromLetter(String s) throws IllegalArgumentException {
            if (s.equals("w"))
                return Window;
            else if (s.equals("c"))
                return Center;
            else if (s.equals("a"))
                return Aisle;
            else
                throw new IllegalArgumentException("Invalid input");
        }
    }

    /**
     * The service classes of an airplane
     */
    public enum Cabin { First, Economy }

    /**
     * Constructs a seat based on the row, seat letter, and service class
     * @param row The row number
     * @param column The seat letter
     * @param cabin The service class
     */
    public Seat(int row, Column column, Cabin cabin) {
        this.row = row;
        this.column = column;
        this.cabin = cabin;
    }

    /**
     * Gets the row number of the seat
     * @return an integer that's the row number
     */
    public int getRow() { return row; }

    /**
     * Gets the seat letter of the seat
     * @return a Column that's the seat letter
     */
    public Column getColumn() { return column; }

    /**
     * Gets the service class of the seat
     * @return a Cabin that's the service class
     */
    public Cabin getCabin() { return cabin; }

    /**
     * Gets the passenger of the seat
     * @return A Passenger object
     */
    public Passenger getPassenger() { return passenger; }

    /**
     * Sets the passenger of the seat
     * @param passenger Who the seat belongs to
     * @postcondition: this.passenger is no longer null
     */
    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
        passenger.setSeat(this);
    }

    /**
     * Removes a passenger from seat
     * @postcondition: this.passenger is null
     */
    public void removePassenger() { passenger = null; }
}
