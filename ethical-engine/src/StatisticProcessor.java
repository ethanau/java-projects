import com.gray.scenario.Animal;
import com.gray.scenario.Character;
import com.gray.scenario.Human;
import com.gray.scenario.Scenario;

import java.io.*;
import java.util.*;

/**
 * A class for processing the statistic
 */
public class StatisticProcessor {

    // Some variables for representing the statistic.
    private ArrayList<CharacteristicSurvivalRatioPair> statisticList = new ArrayList<>();
    private double averageAge;

    /**
     * Logic for showing the statistic
     * @param scenarios the scenarios need to be processed into statistics
     * @param decisions the decisions correspond to the scenarios
     * @param scenariosIndex indicate the index of scenarios to be processed
     */
    public void showStatistic(ArrayList<Scenario> scenarios, ArrayList<EthicalEngine.Decision> decisions, int scenariosIndex) {

        computeStatistic(scenarios, decisions, scenariosIndex);

        System.out.println("======================================");
        System.out.println("# Statistic");
        System.out.println("======================================");
        System.out.println("- % SAVED AFTER " +  (scenariosIndex + 1) + " RUNS");

        for (CharacteristicSurvivalRatioPair pair : statisticList) {
            System.out.println(pair.getCharacteristic() + ": " + String.format("%.2f", pair.getSurvivalRatio()));
        }

        System.out.println("--");
        System.out.println("average age: " + String.format("%.2f", getAverageAge()));
    }

    /**
     * Logic for computing the statistic
     * @param scenarios the scenarios need to be processed into statistics
     * @param decisions the decisions correspond to the scenarios
     * @param scenariosIndex indicate the index of scenarios to be processed
     */
    public void computeStatistic(ArrayList<Scenario> scenarios, ArrayList<EthicalEngine.Decision> decisions, int scenariosIndex) {

        HashMap<String,Integer> survivalsCharacteristics = new HashMap<>();
        HashMap<String,Integer> totalCharacteristics = new HashMap<>();
        HashMap<String, Double> statistic = new HashMap<>();
        statisticList = new ArrayList<>();

        int numberOfTotalHumanSurvivals = 0;
        int sumOfHumanSurvivalAge = 0;

        for (int index = 0; index <=  scenariosIndex; index++) {

            Scenario scenario = scenarios.get(index);
            ArrayList<Character> survivals = decisions.get(index).equals(EthicalEngine.Decision.PASSENGERS) ?
                    scenario.getPassengers() : scenario.getPedestrians();
            ArrayList<Character> deaths = decisions.get(index).equals(EthicalEngine.Decision.PASSENGERS) ?
                    scenario.getPedestrians() : scenario.getPassengers();

            int numberOfSurvivals = survivals.size();
            int numberOfDeaths = deaths.size();
            int numberOfTotalCharacter = numberOfSurvivals + numberOfDeaths;

            String survivalGroup = decisions.get(index).toString().toLowerCase();
            String deathGroup = survivalGroup.equals("passengers") ? "pedestrians" : "passengers";

            computeCharacteristics(survivals, survivalsCharacteristics);
            computeCharacteristics(survivals, totalCharacteristics);
            computeCharacteristics(deaths, totalCharacteristics);

            survivalsCharacteristics.merge(survivalGroup, numberOfSurvivals, (oldV, newV) -> oldV + newV);
            totalCharacteristics.merge(survivalGroup, numberOfSurvivals, (oldV, newV) -> oldV + newV);
            totalCharacteristics.merge(deathGroup, numberOfDeaths, (oldV, newV) -> oldV + newV);

            if (scenario.isLegalCrossing()) {

                survivalsCharacteristics.merge("green", numberOfSurvivals, (oldV, newV) -> oldV + newV);
                totalCharacteristics.merge("green", numberOfTotalCharacter, (oldV, newV) -> oldV + newV);

            } else {

                survivalsCharacteristics.merge("red", numberOfSurvivals, (oldV, newV) -> oldV + newV);
                totalCharacteristics.merge("red", numberOfTotalCharacter, (oldV, newV) -> oldV + newV);
            }

            for (Character character : survivals) {
                if (character instanceof Human) {
                    numberOfTotalHumanSurvivals += 1;
                    sumOfHumanSurvivalAge += character.getAge();

                }
            }
        }

        averageAge =  Math.ceil((double) sumOfHumanSurvivalAge * 100 / numberOfTotalHumanSurvivals) / 100;

        for (String characteristic : totalCharacteristics.keySet()) {

            double survivalRatio = 0;

            if (survivalsCharacteristics.containsKey(characteristic)) {
                survivalRatio = Math.ceil((double) survivalsCharacteristics.get(characteristic) * 100 /
                         totalCharacteristics.get(characteristic)) / 100;
            }
            statistic.put(characteristic, survivalRatio);
        }

        for (String characteristic :statistic.keySet()) {
            CharacteristicSurvivalRatioPair characteristicSurvivalRatioPair =
                    new CharacteristicSurvivalRatioPair(characteristic, statistic.get(characteristic));
            statisticList.add(characteristicSurvivalRatioPair);
        }

        Collections.sort(statisticList);
    }

    /**
     * Logic for computing the characteristics list to be showed in statistics
     * @param characters the characters need to be processed into statistics
     * @param characteristics the characteristics need to be showed in statistics
     */
    private void computeCharacteristics(ArrayList<Character> characters, HashMap<String, Integer> characteristics) {

        for (Character character : characters) {

            if (character instanceof Human) {

                Human human = (Human) character;
                characteristics.merge("human", 1, (oldV, newV) -> oldV + newV);

                String ageCategory = human.getAgeCategory().toString().toLowerCase();
                characteristics.merge(ageCategory, 1, (oldV, newV) -> oldV + newV);

                String gender = human.getGender().toString().toLowerCase();
                if (!gender.equalsIgnoreCase(Character.Gender.UNKNOWN.toString())) {
                    characteristics.merge(gender, 1, (oldV, newV) -> oldV + newV);
                }

                String bodyType = human.getBodyType().toString().toLowerCase();
                if (!bodyType.equalsIgnoreCase(Human.BodyType.UNSPECIFIED.toString())) {
                    characteristics.merge(bodyType, 1, (oldV, newV) -> oldV + newV);
                }

                String profession = human.getProfession().toString().toLowerCase();
                if (!profession.equalsIgnoreCase(Human.Profession.NONE.toString())) {
                    characteristics.merge(profession, 1, (oldV, newV) -> oldV + newV);
                }

                if (human.isPregnant()) {
                    characteristics.merge("pregnant", 1, (oldV, newV) -> oldV + newV);
                }

                if (human.isYou()) {
                    characteristics.merge("you", 1, (oldV, newV) -> oldV + newV);
                }

            } else if (character instanceof Animal) {

                Animal animal = (Animal) character;
                characteristics.merge("animal", 1, (oldV, newV) -> oldV + newV);

                String species = animal.getSpecies().toString().toLowerCase();
                if (!species.equalsIgnoreCase(Animal.Species.UNKNOWN.toString())) {
                    characteristics.merge(species, 1, (oldV, newV) -> oldV + newV);
                }

                if (animal.isPet()) {
                    characteristics.merge("pet", 1, (oldV, newV) -> oldV + newV);
                }
            }
        }
    }

    /**
     * Logic for saving the scenario and the decision into log file
     * @param logfileName the log file path name
     * @param scenario the scenario need to be saved
     * @param decision the decision need to be saved
     * @param decider whether the decision made by user or algorithm
     */
    public void saveJudgedScenario(String logfileName, Scenario scenario, EthicalEngine.Decision decision, String decider) {

        PrintWriter outputStream = null;

        try {

            outputStream = new PrintWriter(new FileOutputStream(logfileName, true));
            outputStream.println(scenario.saveFormatString(decider, decision.toString()));
            outputStream.close();

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: could not print results. Target directory does not exist.");

        } catch (Exception e) {
            System.out.println("An error occurred while printing results.");
        }

    }

    /**
     * Get statistic list
     * @return ArrayList statistic list
     */
    public ArrayList<CharacteristicSurvivalRatioPair> getStatisticList() {
        return statisticList;
    }

    /**
     * Get the average age
     * @return double the average age
     */
    public double getAverageAge() {
        return averageAge;
    }

}
