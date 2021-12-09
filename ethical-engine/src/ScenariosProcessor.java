import com.gray.customexception.InvalidCharacteristicException;
import com.gray.customexception.InvalidDataFormatException;
import com.gray.scenario.Animal;
import com.gray.scenario.Character;
import com.gray.scenario.Human;
import com.gray.scenario.Scenario;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class for processing the scenario
 */
public class ScenariosProcessor {

    // Some constants for generating a random scenario and parsing the config file.
    private static final double RANDOM_HUMAN_RATIO = 0.8;
    private static final int RANDOM_MAXIMUM_CHARACTERS = 30;
    private static final int RANDOM_DEFAULT_SCENARIOS_NUMBER = 3;
    private static final int CONFIG_FILE_ROW_VALUES = 10;

    // Some variables for representing the scenarios and the decisions after processing.
    private ArrayList<Scenario> scenarios = new ArrayList<>();
    private ArrayList<String> scenariosTypeList = new ArrayList<>();
    private ArrayList<EthicalEngine.Decision> decisions = new ArrayList<>();
    private boolean isRandomYouExist = false;

    // A random instance used for generating a random scenario.
    Random random = new Random();

    /**
     * Logic for parsing the config file or log file
     * @param filePath the file path name
     * @param fileType whether the file is a config file or log file
     */
    public void parsingFile(String filePath, String fileType) {

        if (filePath.equals("DEFAULT")) {
            scenarios = randomScenariosGenerator
                    (ScenariosProcessor.RANDOM_DEFAULT_SCENARIOS_NUMBER);

        } else {
            int scenarioCount = 0;
            int lineNumber = 0;

            BufferedReader inputStream = null;

            try {

                inputStream = new BufferedReader(new FileReader(filePath));

                String inputLine = inputStream.readLine();
                lineNumber++;

                while (inputLine != null) {

                    String[] inputLineSplit = inputLine.split(",", -1);

                    try {

                        if (inputLineSplit.length != CONFIG_FILE_ROW_VALUES) {

                            throw new InvalidDataFormatException();

                        } else if (inputLineSplit[0].equals("scenario:green") ||
                                inputLineSplit[0].equals("scenario:red")) {

                            String scenarioType = inputLineSplit[1];
                            Scenario scenario = new Scenario();
                            scenarioCount++;

                            if (inputLineSplit[0].equals("scenario:green")) {
                                scenario.setIsLegalCrossing(true);
                            } else {
                                scenario.setIsLegalCrossing(false);
                            }

                            scenarios.add(scenario);

                            if (!scenarioType.equals("")) {
                                scenariosTypeList.add(scenarioType);
                                EthicalEngine.Decision decision = inputLineSplit[2].equals("survival:passengers") ?
                                        EthicalEngine.Decision.PASSENGERS : EthicalEngine.Decision.PEDESTRIANS;
                                decisions.add(decision);
                            }

                        } else if (inputLineSplit[0].equalsIgnoreCase("human")) {

                            int age;
                            boolean isPregnant;
                            boolean isYou;

                            try {
                                age = Integer.parseInt(inputLineSplit[2]);
                                isPregnant = Boolean.parseBoolean(inputLineSplit[5]);
                                isYou = Boolean.parseBoolean(inputLineSplit[6]);
                            }
                            catch (NumberFormatException e) {
                                System.out.println("WARNING: invalid number format in config file in line " + lineNumber);
                                age = Human.DEFAULT_AGE;
                                isPregnant = false;
                                isYou = false;
                            }

                            Human.Gender gender;
                            Human.BodyType bodyType;
                            Human.Profession profession = Human.Profession.NONE;

                            try {

                                if (!isHumanFieldValuesValid(inputLineSplit[1], inputLineSplit[3], inputLineSplit[4])) {
                                    throw new InvalidCharacteristicException();
                                } else {

                                    gender = Human.Gender.valueOf(inputLineSplit[1].toUpperCase());
                                    bodyType = Human.BodyType.valueOf(inputLineSplit[3].toUpperCase());

                                    if (inputLineSplit[4] != "") {
                                        profession = (age >= 17 && age <= 68) ?
                                                Human.Profession.valueOf(inputLineSplit[4].toUpperCase()) : Human.Profession.NONE;
                                    }
                                }
                            }
                            catch (InvalidCharacteristicException e) {
                                System.out.println("WARNING: invalid characteristic in config file in line " + lineNumber);
                                gender = Character.Gender.UNKNOWN;;
                                bodyType = Character.BodyType.UNSPECIFIED;
                                profession = Human.Profession.NONE;
                            }

                            Human human = new Human(gender, age, bodyType, profession, isPregnant, isYou);

                            if (inputLineSplit[9].equalsIgnoreCase("passenger")) {
                                scenarios.get(scenarioCount - 1).addPassenger(human);
                                if (isYou) scenarios.get(scenarioCount - 1).setYouInPassengers(true);
                            } else {
                                scenarios.get(scenarioCount - 1).addPedestrian(human);
                                if (isYou) scenarios.get(scenarioCount - 1).setYouInPedestrians(true);
                            }

                        } else if (inputLineSplit[0].equalsIgnoreCase("animal")) {

                            int age;
                            boolean isPet;

                            try {
                                age = Integer.parseInt(inputLineSplit[2]);
                                isPet = Boolean.parseBoolean(inputLineSplit[8]);
                            }
                            catch (NumberFormatException e) {
                                System.out.println("WARNING: invalid number format in config file in line " + lineNumber);
                                age = Animal.DEFAULT_AGE;
                                isPet = false;
                            }

                            Character.Gender gender;
                            Animal.Species species;

                            try {

                                if (!isAnimalFieldValuesValid(inputLineSplit[1], inputLineSplit[7])) {
                                    throw new InvalidCharacteristicException();
                                } else {
                                    gender = Character.Gender.valueOf(inputLineSplit[1].toUpperCase());
                                    species = Animal.Species.valueOf(inputLineSplit[7].toUpperCase());
                                }
                            }
                            catch (InvalidCharacteristicException e) {
                                System.out.println("WARNING: invalid characteristic in config file in line " + lineNumber);
                                gender = Character.Gender.UNKNOWN;
                                species = Animal.Species.UNKNOWN;
                            }

                            Animal animal = new Animal(gender, age, species, isPet);

                            if (inputLineSplit[9].equalsIgnoreCase("passenger")) {
                                scenarios.get(scenarioCount - 1).addPassenger(animal);
                            } else {
                                scenarios.get(scenarioCount - 1).addPedestrian(animal);
                            }

                        }
                    }
                    catch (InvalidDataFormatException e) {

                        System.out.println("WARNING: invalid data format in config file in line " + lineNumber);
                    }

                    inputLine = inputStream.readLine();
                    lineNumber++;

                }
                inputStream.close();

            } catch (FileNotFoundException e) {
                if (fileType.equalsIgnoreCase("config")) {
                    System.out.println("ERROR: could not find config file.");
                    System.exit(0);
                } else {
                    System.out.println("No history found. Press enter to return to main menu.");
                }
            }
            catch (Exception e) {
                System.out.println("An error occurred while reading the file.");
                System.exit(0);
            }
        }
    }

    /**
     * Logic for generating scenarios based on the number provided
     * @param numberOfScenarios the number of random scenarios
     * @return ArrayList a list of random scenarios
     */
    public ArrayList<Scenario> randomScenariosGenerator(int numberOfScenarios) {

        for (int i = 0; i < numberOfScenarios; i++) {
            scenarios.add(singleScenarioGenerator());
        }

        return scenarios;
    }

    /**
     * Logic for generating a random scenario
     * @return Scenario a random scenario
     */
    private Scenario singleScenarioGenerator() {

        ArrayList<Character> passengers = new ArrayList<>();
        ArrayList<Character> pedestrians = new ArrayList<>();
        boolean isLegalCrossing = random.nextBoolean();

        int numberOfPassengers = random.nextInt(RANDOM_MAXIMUM_CHARACTERS) + 1;
        do {
            Character character = randomCharacterGenerator();
            passengers.add(character);
            numberOfPassengers--;
        } while (numberOfPassengers > 0);

        int numberOfPedestrians = random.nextInt(RANDOM_MAXIMUM_CHARACTERS) + 1;
        do {
            pedestrians.add(randomCharacterGenerator());
            numberOfPedestrians--;
        } while (numberOfPedestrians > 0);

        Scenario scenario = new Scenario(passengers, pedestrians, isLegalCrossing);
        locateYou(scenario);
        return scenario;
    }

    /**
     * Logic for generating a human or an animal
     * @return Character a random character
     */
    private Character randomCharacterGenerator() {

        if (Math.random() < RANDOM_HUMAN_RATIO) {
            return randomHumanGenerator();
        } else {
            return randomAnimalGenerator();
        }
    }

    /**
     * Logic for generating a human
     * @return Human a random human
     */
    private Human randomHumanGenerator() {

        Human.Gender gender = Human.Gender.values()[random.nextInt(Human.Gender.values().length - 1)];

        int age = random.nextInt(Human.MAXIMUM_AGE);

        Human.BodyType bodyType = Human.BodyType.values()[random.nextInt(Human.BodyType.values().length)];

        Human.Profession profession = (age >= 17 && age <= 68) ?
                Human.Profession.values()[random.nextInt(Human.Profession.values().length -1)] : Human.Profession.NONE;

        boolean isPregnant = gender.equals(Human.Gender.FEMALE) ? random.nextBoolean() : false;

        boolean isYou = isRandomYouExist() ? false : random.nextBoolean();
        if (isYou) setRandomYouExist(true);

        Human human = new Human(gender, age, bodyType, profession, isPregnant, isYou);

        return human;
    }

    /**
     * Logic for generating a animal
     * @return Animal a random animal
     */
    private Animal randomAnimalGenerator() {

        Animal.Gender gender = Animal.Gender.values()[random.nextInt(Animal.Gender.values().length - 1)];

        int age = random.nextInt(Animal.MAXIMUM_AGE);

        Animal.Species species = Animal.Species.values()[random.nextInt(Animal.Species.values().length)];

        boolean isPet = false;
        if (species.equals(Animal.Species.CAT) || species.equals(Animal.Species.DOG) ||
                species.equals(Animal.Species.RABBIT) || species.equals(Animal.Species.BIRD)) {
            isPet = true;
        }

        Animal animal = new Animal(gender, age, species, isPet);

        return animal;
    }

    /**
     * Logic for checking whether you in the passengers or pedestrians
     * @param scenario a scenario used to be locate is you in passengers or pedestrians
     */
    private void locateYou(Scenario scenario) {

        for (Character character : scenario.getPassengers()) {
            if (character instanceof Human) {
                if (((Human) character).isYou()) {
                    scenario.setYouInPassengers(true);
                }
            }
        }

        for (Character character : scenario.getPedestrians()) {
            if (character instanceof Human) {
                if (((Human) character).isYou()) {
                    scenario.setYouInPedestrians(true);
                }
            }
        }
    }

    /**
     * Logic for checking whether the human field values in config file are valid
     * @param genderValue the gender value
     * @param bodyTypeValue the body type value
     * @param professionValue the profession value
     * @return boolean represent whether the human field values in config file are valid
     */
    private boolean isHumanFieldValuesValid(String genderValue, String bodyTypeValue, String professionValue) {

        boolean isGenderValueValid = false;
        boolean isBodyTypeValueValid = false;
        boolean isProfessionValueValid = false;

        for (Character.Gender gender : Character.Gender.values()) {
            if (gender.toString().equalsIgnoreCase(genderValue)) {
                isGenderValueValid = true;
            }
        }

        for (Character.BodyType bodyType : Character.BodyType.values()) {
            if (bodyType.toString().equalsIgnoreCase(bodyTypeValue)) {
                isBodyTypeValueValid = true;
            }
        }

        if (professionValue == "") professionValue = "none";
        for (Human.Profession profession : Human.Profession.values()) {
            if (profession.toString().equalsIgnoreCase(professionValue)) {
                isProfessionValueValid = true;
            }
        }

        if (isGenderValueValid && isBodyTypeValueValid && isProfessionValueValid) {
            return true;
        }

        return false;
    }

    /**
     * Logic for checking whether the animal field values in config file are valid
     * @param genderValue the gender value
     * @param speciesValue the species value
     * @return boolean represent whether the animal field values in config file are valid
     */
    private boolean isAnimalFieldValuesValid(String genderValue, String speciesValue) {

        boolean isGenderValueValid = false;
        boolean isSpeciesValueValid = false;

        for (Character.Gender gender : Character.Gender.values()) {
            if (gender.toString().equalsIgnoreCase(genderValue)) {
                isGenderValueValid = true;
            }
        }

        for (Animal.Species species : Animal.Species.values()) {
            if (species.toString().equalsIgnoreCase(speciesValue)) {
                isSpeciesValueValid = true;
            }
        }

        if (isGenderValueValid && isSpeciesValueValid) {
            return true;
        }

        return false;
    }

    /**
     * Set whether a random you already exist
     */
    public void setRandomYouExist(boolean randomYouExist) {
        isRandomYouExist = randomYouExist;
    }

    /**
     * Get whether a random you already exist
     * @return boolean whether a random you already exist
     */
    public boolean isRandomYouExist() {
        return isRandomYouExist;
    }

    /**
     * Get the scenarios' type list
     * @return ArrayList the scenarios' type list
     */
    public ArrayList<String> getScenariosTypeList() {
        return scenariosTypeList;
    }

    /**
     * Get the scenarios
     * @return ArrayList the scenarios
     */
    public ArrayList<Scenario> getScenarios() {
        return scenarios;
    }

    /**
     * Get the decisions
     * @return ArrayList the decisions
     */
    public ArrayList<EthicalEngine.Decision> getDecisions() {
        return decisions;
    }

}
