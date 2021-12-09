package scenario;

/**
 * An abstract class for handling the common characteristics and method of the characters
 */
public abstract class Character {

    // Some enumerated types for holding the gender types and the body types of the character.
    public enum Gender {FEMALE, MALE, UNKNOWN};
    public enum BodyType {AVERAGE, ATHLETIC, OVERWEIGHT, UNSPECIFIED};

    // A constant for representing the default age of the character.
    private static final int DEFAULT_AGE = 0;

    // Some variables for representing the characteristics of the character.
    private int age;
    private Gender gender;
    private BodyType bodyType ;

    // A variable for representing the decision algorithm score of the character.
    private int decisionAlgorithmScore;

    /**
     * Constructor
     */
    public Character() {}

    /**
     * Constructor
     * @param gender the character's gender
     * @param age the character's age
     */
    public Character(Gender gender, int age) {
        this.gender = gender;
        this.age = age >= 0? age : DEFAULT_AGE;
    }

    /**
     * An abstract method for computing the decision algorithm score based on character's characteristics
     */
    public abstract void computeDecisionAlgorithmScore();

    /**
     * An abstract method for defining how character is stored in the log file
     * @param role whether the character is passenger or pedestrian
     * @return String representing the character saved in the log file
     */
    public abstract String saveFormatString(String role);

    /**
     * Set Character's age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Set Character's gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Set Character's bodyType
     */
    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    /**
     * Set Character's decision algorithm score
     */
    public void setDecisionAlgorithmScore(int decisionAlgorithmScore) {
        this.decisionAlgorithmScore = decisionAlgorithmScore;
    }

    /**
     * Get the character's age
     * @return int the character's age
     */
    public int getAge() {
        return age;
    }

    /**
     * Get the character's gender
     * @return int the character's gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Get the character's bodyType
     * @return int the character's bodyType
     */
    public BodyType getBodyType() {
        return bodyType;
    }

    /**
     * Get the character's decision algorithm score
     * @return int the character's decision algorithm score
     */
    public int getDecisionAlgorithmScore() {
        return decisionAlgorithmScore;
    }

}
