package scenario;

import java.util.ArrayList;

/**
 * Represents a scenario to decide on
 */
public class Scenario {

    // Some constants for representing the algorithm decision score factor of the scenario.
    public static final int LEGAL_CROSSING_SCORE_FACTOR = 2;
    public static final int IS_YOU_IN_SCORE_FACTOR = 2;

    // Some variables for representing the passengers and the pedestrians of the scenario.
    private ArrayList<Character> passengers = new ArrayList<>();
    private ArrayList<Character> pedestrians = new ArrayList<>();

    // Some variables for representing the characteristics of the scenario.
    private boolean isLegalCrossing = false;
    private boolean isYouInPassengers  = false;
    private boolean isYouInPedestrians = false;

    /**
     * Constructor
     */
    public Scenario() {}

    /**
     * Constructor
     * @param passengers a list of passengers
     * @param pedestrians a list of pedestrians
     * @param isLegalCrossing whether the scenario is legal crossing
     */
    public Scenario(ArrayList<Character> passengers, ArrayList<Character> pedestrians, boolean isLegalCrossing) {
        this.passengers = passengers;
        this.pedestrians = pedestrians;
        this.isLegalCrossing = isLegalCrossing;
    }

    /**
     * Define how scenario is stored in the log file
     * @param deciderType whether the user or the algorithm makes the decision
     * @param decision whether the passengers or pedestrians are saved
     * @return String representing the scenario saved in the log file
     */
    public String saveFormatString(String deciderType, String decision) {

        StringBuilder formatString = new StringBuilder();
        formatString.append("scenario:" + (isLegalCrossing ? "green" : "red")
                + ",decider:" + deciderType.toLowerCase() + ",survival:" + decision.toLowerCase() + ",,,,,,,\n");

        for (Character character : passengers) {
            formatString.append(character.saveFormatString("passenger") + "\n");
        }

        int index = 0;
        for (Character character : pedestrians) {
            formatString.append(character.saveFormatString("pedestrian"));
            index++;
            if (index != pedestrians.size()) {
                formatString.append("\n");
            }
        }

        return formatString.toString();
    }

    /**
     * Return a string represent the characteristic of the scenario
     * @return String a string represent the characteristic of the scenario
     */
    @Override
    public String toString() {

        StringBuilder outputString = new StringBuilder();
        outputString.append("======================================\n");
        outputString.append("# Scenario\n");
        outputString.append("======================================\n");
        outputString.append("Legal Crossing: " + (isLegalCrossing() ? "yes" : "no") + "\n");

        outputString.append("Passengers" + " (" + passengers.size() + ")\n");
        for (Character passenger : getPassengers()) {
            outputString.append(passenger.toString());
        }
        outputString.append("Pedestrians" + " (" + pedestrians.size() + ")\n");
        for (Character pedestrian : getPedestrians()) {
            outputString.append(pedestrian.toString());
        }

        return outputString.toString();
    }

    /**
     * Add passenger to arraylist
     */
    public void addPassenger(Character passenger) {
        passengers.add(passenger);
    }

    /**
     * Add pedestrian to arraylist
     */
    public void addPedestrian(Character pedestrian) {
        pedestrians.add(pedestrian);
    }

    /**
     * Set whether the scenario is legal crossing
     */
    public void setIsLegalCrossing(boolean legalCrossing) {
        isLegalCrossing = legalCrossing;
    }

    /**
     * Set whether you are in passengers
     */
    public void setYouInPassengers(boolean youInPassengers) {
        isYouInPassengers = youInPassengers;
    }

    /**
     * Set whether you are in pedestrians
     */
    public void setYouInPedestrians(boolean youInPedestrians) {
        isYouInPedestrians = youInPedestrians;
    }

    /**
     * Get the list of passengers
     * @return ArrayList the list of passengers
     */
    public ArrayList<Character> getPassengers() {
        return passengers;
    }

    /**
     * Get the list of pedestrians
     * @return ArrayList the list of pedestrians
     */
    public ArrayList<Character> getPedestrians() {
        return pedestrians;
    }

    /**
     * Get whether you are in passengers
     * @return whether you are in passengers
     */
    public boolean isYouInPassengers() {
        return isYouInPassengers;
    }

    /**
     * Get whether you are in pedestrians
     * @return whether you are in pedestrians
     */
    public boolean isYouInPedestrians() {
        return isYouInPedestrians;
    }

    /**
     * Get whether the scenario is legal crossing
     * @return whether the scenario is legal crossing
     */
    public boolean isLegalCrossing() {
        return isLegalCrossing;
    }

}
