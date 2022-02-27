import java.io.Serializable;

/**
 * Passenger class that holds name and seat information
 * @author Channon Zuo
 * @version 02/20/2022
 */
public class Passenger implements Serializable {
    private String name;
    private Seat seat;
    private boolean isIndividual;
    String groupName;

    /**
     * Constructs a Passenger with a name. Defaults to individual passenger
     * @param name Name of Passenger
     */
    public Passenger(String name) {
        this.name = name;
        groupName = "";
        isIndividual = true;
    }

    /**
     * Gets the Name of the Passenger
     * @return String name
     */
    public String getName() { return name; }

    /**
     * Sets the seat of the Passenger
     * @param seat Seat object of passenger
     * @postcondition: this.seat is no longer null
     */
    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    /**
     * Gets the group name of the passenger
     * @return a String group name
     */
    public String getGroupName() { return groupName; }

    /**
     * Sets the group name of a passenger
     * @param s String group name
     * @precondition: this.passenger is a group member
     */
    public void setGroupName(String s) {
        groupName = s;
        isIndividual = false;
    }

    /**
     * Sets the status if the passenger is an individual or not
     * @param b a boolean
     * @postcondition: isIndividual is changed to the boolean
     */
    public void isIndividualPassenger(boolean b) {
        isIndividual = b;
    }

    /**
     * Gets the status of a passenger if it's an individual or not
     * @return this.isIndividual
     */
    public boolean isIndividualPassenger() {
        return isIndividual;
    }
}
