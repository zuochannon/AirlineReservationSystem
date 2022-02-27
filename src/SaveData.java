import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for storing data
 * @author Channon Zuo
 * @version 1.0 02/24/2022
 */
public class SaveData implements Serializable {
    private List<Seat> firstClassSeat, economyClassSeat;

    /**
     * Constructor that creates new array lists
     */
    public SaveData(){
        firstClassSeat = new ArrayList<>();
        economyClassSeat = new ArrayList<>();
    }

    /**
     * Returns a list of first class seats
     */
    public List<Seat> getFirstClassSeatList(){ return firstClassSeat; }

    /**
     * Returns a list of economy class seats
     */
    public List<Seat> getEconomyClassSeatList(){ return economyClassSeat; }

    /**
     * Sets a list of first class seats
     * @param list First class seats
     * @precondition: list is a first class seat list
     * @postcondition: firstClassSeat has seats
     */
    public void setFirstClassSeatList(List<Seat> list) { firstClassSeat = list; }

    /**
     * Sets a list of economy class seats
     * @param list Economy class seats
     * @precondition: list is an economy seat list
     * @postcondition: economyClassSeat has seats
     */
    public void setEconomyClassSeatList(List<Seat> list) { economyClassSeat = list; }
}
