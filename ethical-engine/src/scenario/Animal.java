package scenario;

/**
 * A class for holding the characteristics and method specific to the animal
 */
public class Animal extends Character{

    // An enumerated type for holding the species types of the animal.
    public enum Species {CAT, DOG, RABBIT, BIRD, COW, SHEEP, HORSE, MONKEY, PIG, FERRET, UNKNOWN};

    // Some constants for representing the maximum and default age and the algorithm decision score of the animal.
    public static final int MAXIMUM_AGE = 20;
    public static final int DEFAULT_AGE = 2;
    private static final int PET_SCORE = 2;

    // Some variables for representing the characteristics of the animal.
    private Species species;
    private boolean isPet;

    /**
     * Constructor
     */
    public Animal() {
        super();
    }

    /**
     * Constructor
     * @param gender the animal's gender
     * @param age the animal's age
     * @param species the animal's species
     * @param isPet whether the animal is pet
     */
    public Animal(Gender gender, int age, Species species, boolean isPet) {
        super(gender, age);
        this.species = species;
        this.isPet = isPet;
    }

    /**
     * Compute the animal's decision algorithm score based on characteristics
     */
    @Override
    public void computeDecisionAlgorithmScore() {

        if (isPet()) {
            setDecisionAlgorithmScore(PET_SCORE);
        }
    }

    /**
     * Define how animal is stored in the log file
     * @param role whether the animal is passenger or pedestrian
     * @return String representing the animal saved in the log file
     */
    @Override
    public String saveFormatString(String role) {

        StringBuilder formatString = new StringBuilder();
        formatString.append("animal,");
        formatString.append(getGender().toString().toLowerCase() + ",");
        formatString.append(getAge() + ",");
        formatString.append(",,FALSE,FALSE,");
        formatString.append(getSpecies().toString().toLowerCase() + ",");
        formatString.append(Boolean.toString(isPet()).toUpperCase() + ",");
        formatString.append(role);

        return formatString.toString();
    }

    /**
     * Return a string represent the characteristic of the animal
     * @return String a string represent the characteristic of the animal
     */
    @Override
    public String toString() {

        StringBuilder outputString = new StringBuilder();
        outputString.append("- " + getSpecies().toString().toLowerCase() + (isPet() ? " is pet" : ""));
        outputString.append("\n");

        return outputString.toString();
    }

    /**
     * Get animal's species
     * @return Species the animal's species
     */
    public Species getSpecies() {
        return species;
    }

    /**
     * Get whether animal is pet
     * @return boolean whether animal is pet
     */
    public boolean isPet() {
        return isPet;
    }

}
