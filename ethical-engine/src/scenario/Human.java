package scenario;

/**
 * A class for holding the characteristics and method specific to the human
 */
public class Human extends Character{

    // Some enumerated types for holding the age category types and the profession types of the human.
    public enum AgeCategory {BABY, CHILD, ADULT, SENIOR};
    public enum Profession {DOCTOR, CEO, TEACHER, PROGRAMMER, ARTIST, CRIMINAL, HOMELESS, UNEMPLOYED, NONE};

    // Some constants for representing the maximum and default age and the algorithm decision score of the human.
    public static final int MAXIMUM_AGE = 120;
    public static final int DEFAULT_AGE = 15;
    private static final int BODY_TYPE_ATHLETIC_SCORE = 4;
    private static final int BODY_TYPE_AVERAGE_SCORE = 2;
    private static final int AGE_CATEGORY_YOUNG_SENIOR_SCORE = 6;
    private static final int PROFESSION_ELITE_SCORE = 4;
    private static final int PROFESSION_NORMAL_SCORE = 2;
    private static final int GENDER_FEMALE_SCORE = 3;
    private static final int PREGNANT_SCORE = 8;

    // Some variables for representing the characteristics of the human.
    private AgeCategory ageCategory;
    private Profession profession;
    private boolean isYou;
    private boolean isPregnant;

    /**
     * Constructor
     */
    public Human() {
        super();
    }

    /**
     * Constructor
     * @param gender the human's gender
     * @param age the human's age
     * @param bodyType the human's bodyType
     * @param profession the human's profession
     * @param isPregnant whether the human is pregnant
     * @param isYou whether the human is you
     */
    public Human(Gender gender, int age, BodyType bodyType, Profession profession, boolean isPregnant, boolean isYou) {
        super(gender, age);
        initializeAgeCategory(age);
        setBodyType(bodyType);
        this.profession = profession;
        this.isPregnant = isPregnant;
        this.isYou = isYou;
    }

    /**
     * Compute the human's decision algorithm score based on characteristics
     */
    @Override
    public void computeDecisionAlgorithmScore() {

        int bodyTypeScope = 0;
        int ageCategoryScore = 0;
        int professionScore = 0;
        int genderScore = 0;
        int pregnantScore = 0;

        switch (getBodyType()) {
            case ATHLETIC:
                bodyTypeScope = BODY_TYPE_ATHLETIC_SCORE;
                break;
            case AVERAGE:
                bodyTypeScope = BODY_TYPE_AVERAGE_SCORE;
                break;
        }
        switch (getAgeCategory()) {
            case BABY:
            case CHILD:
            case SENIOR:
                ageCategoryScore = AGE_CATEGORY_YOUNG_SENIOR_SCORE;
                break;
        }
        switch (getProfession()) {
            case CEO:
            case DOCTOR:
            case TEACHER:
                professionScore = PROFESSION_ELITE_SCORE;
                break;
            case PROGRAMMER:
            case ARTIST:
                professionScore = PROFESSION_NORMAL_SCORE;
                break;
        }
        switch (getGender()) {
            case FEMALE:
                genderScore = GENDER_FEMALE_SCORE;
                break;
        }

        if (isPregnant()) {
            pregnantScore = PREGNANT_SCORE;
        }

        int decisionAlgorithmScore = bodyTypeScope + ageCategoryScore + professionScore + genderScore + pregnantScore;

        setDecisionAlgorithmScore(decisionAlgorithmScore);
    }

    /**
     * Define how human is stored in the log file
     * @param role whether the animal is passenger or pedestrian
     * @return String representing the human saved in the log file
     */
    @Override
    public String saveFormatString(String role) {

        StringBuilder formatString = new StringBuilder();
        formatString.append("human,");
        formatString.append(getGender().toString().toLowerCase() + ",");
        formatString.append(getAge() + ",");
        formatString.append(getBodyType().toString().toLowerCase() + ",");
        formatString.append(getProfession().toString().toLowerCase() + ",");
        formatString.append(Boolean.toString(isPregnant()).toUpperCase() + ",");
        formatString.append(Boolean.toString(isYou()).toUpperCase() + ",");
        formatString.append(",," + role);

        return formatString.toString();
    }

    /**
     * Initialize the age category based on the age
     * @param age the age of the human
     */
    private void initializeAgeCategory(int age) {

        if (age >= 0 && age <= 4) {
            ageCategory = AgeCategory.BABY;
        } else if (age >= 5 && age <= 16) {
            ageCategory = AgeCategory.CHILD;
        } else if (age >= 17 && age <= 68) {
            ageCategory = AgeCategory.ADULT;
        } else if (age > 68) {
            ageCategory = AgeCategory.SENIOR;
        }
    }

    /**
     * Return a string represent the characteristic of the human
     * @return String a string represent the characteristic of the human
     */
    @Override
    public String toString() {

        StringBuilder outputString = new StringBuilder();
        outputString.append("- ");
        outputString.append(isYou() ? "you " : "");
        outputString.append(getBodyType().toString().toLowerCase());
        outputString.append(" " + getAgeCategory().toString().toLowerCase());
        if (!profession.equals(Profession.NONE)) {
            outputString.append(" " + getProfession().toString().toLowerCase());
        }
        outputString.append(" " + getGender().toString().toLowerCase());
        outputString.append((isPregnant() ? " pregnant" : ""));
        outputString.append("\n");

        return outputString.toString();
    }

    /**
     * Get the human's age category
     * @return AgeCategory the human's age category
     */
    public AgeCategory getAgeCategory() {
        return ageCategory;
    }

    /**
     * Get the human's profession
     * @return Profession the human's profession
     */
    public Profession getProfession() {
        return profession;
    }

    /**
     * Get whether the human is you
     * @return boolean whether the human is you
     */
    public boolean isYou() {
        return isYou;
    }

    /**
     * Get whether the human is pregnant
     * @return boolean whether the human is pregnant
     */
    public boolean isPregnant() {
        return isPregnant;
    }

}
