/**
 * A class implementing the Comparable interface used for sorting the statistic list
 */
public class CharacteristicSurvivalRatioPair implements Comparable<CharacteristicSurvivalRatioPair>{

    // Some variables for representing the content of the CharacteristicSurvivalRatioPair.
    private String characteristic;
    private Double survivalRatio;

    /**
     * Constructor
     * @param characteristic the characteristic
     * @param survivalRatio the survival ratio
     */
    public CharacteristicSurvivalRatioPair(String characteristic, Double survivalRatio) {
        this.characteristic = characteristic;
        this.survivalRatio = survivalRatio;
    }

    /**
     * implement the compareTo method
     * @param otherPair another CharacteristicSurvivalRatioPair object
     * @return int an integer indicate which object is larger
     */
    @Override
    public int compareTo(CharacteristicSurvivalRatioPair otherPair) {

        if (!survivalRatio.equals(otherPair.getSurvivalRatio())) {
            return survivalRatio.compareTo(otherPair.getSurvivalRatio()) * (-1);
        } else {
            return characteristic.compareTo(otherPair.getCharacteristic());
        }
    }

    /**
     * Get the characteristic
     * @return String the characteristic
     */
    public String getCharacteristic() {
        return characteristic;
    }

    /**
     * Get the survival ratio
     * @return Double the survival ratio
     */
    public Double getSurvivalRatio() {
        return survivalRatio;
    }

}
