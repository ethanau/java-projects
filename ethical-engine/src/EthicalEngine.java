import com.gray.customexception.InvalidInputException;
import com.gray.scenario.Character;
import com.gray.scenario.Scenario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 */
public class EthicalEngine {

    // An enumerated type for holding the decision types.
    public enum Decision {PASSENGERS, PEDESTRIANS}

    /**
     * Decides whether to save the passengers or the pedestrians
     * @param scenario: the ethical dilemma
     * @return Decision: which group to save
     */
    public static Decision decide(Scenario scenario) {

        // a decision algorithm considers 2 characteristics from the scenario
        // and 6 characteristics from the characters,
        // detailed in the character's computeDecisionAlgorithmScore method.

        int passengersDecisionAlgorithmScore = 0;
        int pedestriansDecisionAlgorithmScore = 0;

        for (Character character : scenario.getPassengers()) {
            character.computeDecisionAlgorithmScore();
            passengersDecisionAlgorithmScore += character.getDecisionAlgorithmScore();
        }

        for (Character character : scenario.getPedestrians()) {
            character.computeDecisionAlgorithmScore();
            pedestriansDecisionAlgorithmScore += character.getDecisionAlgorithmScore();
        }
        if (scenario.isLegalCrossing()) {
            pedestriansDecisionAlgorithmScore *= Scenario.LEGAL_CROSSING_SCORE_FACTOR;
        }

        if (scenario.isYouInPassengers()) {
            passengersDecisionAlgorithmScore *= Scenario.IS_YOU_IN_SCORE_FACTOR;
        } else if (scenario.isYouInPedestrians()) {
            pedestriansDecisionAlgorithmScore *= Scenario.IS_YOU_IN_SCORE_FACTOR;
        }

        return passengersDecisionAlgorithmScore > pedestriansDecisionAlgorithmScore ?
                Decision.PASSENGERS : Decision.PEDESTRIANS;

    }

    /**
     * Program entry
     */
    public static void main(String[] args) {

        EthicalEngine ethicalEngine = new EthicalEngine();
        String configFilePath = "DEFAULT";
        String logFilePath = "ethicalengine.log";

        if (args.length == 0) {
            ethicalEngine.runUserInputGuide(ethicalEngine, configFilePath, logFilePath);

        } else if (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help") ||
                args[0].equals("-c") || args[0].equals("--config") || args[0].equals("--l") || args[0].equals("--log"))) {
            ethicalEngine.displayHelp();

        } else if (args.length == 2) {
            if (args[0].equals("-c") || args[0].equals("--config")) {
                configFilePath = args[1];
            } else if (args[0].equals("-l") || args[0].equals("--log")) {
                logFilePath = args[1];
            }
            ethicalEngine.runUserInputGuide(ethicalEngine, configFilePath, logFilePath);

        } else if (args.length == 4) {

            if (args[0].equals("-c") || args[0].equals("--config")) {
                configFilePath = args[1];
                logFilePath = args[3];

            } else if (args[0].equals("-l") || args[0].equals("--log")) {
                configFilePath = args[3];
                logFilePath = args[1];
            }
            ethicalEngine.runUserInputGuide(ethicalEngine, configFilePath, logFilePath);
        }
                
    }

    /**
     *  Logic for guiding user run the ethical engine.
     *  @param ethicalEngine  the ethicalEngine object.
     *  @param configFilePath  the config file path name.
     *  @param logFilePath  the log file path name.
     */
    private void runUserInputGuide(EthicalEngine ethicalEngine, String configFilePath, String logFilePath) {

        ScenariosProcessor scenariosProcessor = new ScenariosProcessor();
        scenariosProcessor.parsingFile(configFilePath, "config");
        ArrayList<Scenario> scenarios = scenariosProcessor.getScenarios();

        Scanner scanner = new Scanner(System.in);
        ethicalEngine.displayMainMenu(scenarios);

        while (true) {
            String userInput = scanner.nextLine();

            switch (userInput) {

                case "judge":
                case "j":

                    Judge judge = new Judge();
                    System.out.println("Do you consent to have your decisions saved to a file? (yes/no)");
                    userInput = scanner.nextLine();

                    while (true) {

                        try {
                            if (userInput.equalsIgnoreCase("yes")) {
                                judge.setUserConsent(true);
                                break;
                            } else if (userInput.equalsIgnoreCase("no")) {
                                judge.setUserConsent(false);
                                break;
                            } else {
                                throw new InvalidInputException();
                            }
                        }
                        catch (InvalidInputException e) {
                            System.out.println("Invalid response. Do you consent to have your decisions saved to a file? (yes/no)");
                            userInput = scanner.nextLine();
                        }
                    }

                    judge.setScenarios(scenarios);

                    int scenariosIndex = 0;
                    boolean isAnyScenarioRemain = true;

                    while (scenariosIndex < judge.getScenarios().size()) {

                        for (int i = 0; i < 3; i++) {

                            Scenario scenario = judge.getScenarios().get(scenariosIndex);
                            Decision decision = Decision.PEDESTRIANS;

                            System.out.print(scenario);
                            System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2])");
                            userInput = scanner.nextLine();

                            if (userInput.equals("passenger") || userInput.equals("passengers") || userInput.equals("1")) {
                                decision = Decision.PASSENGERS;
                            }

                            judge.addDecision(decision, logFilePath, scenariosIndex);

                            if (scenariosIndex == judge.getScenarios().size() - 1) {
                                isAnyScenarioRemain = false;
                                break;
                            }

                            scenariosIndex++;
                        }

                        if (!isAnyScenarioRemain) {

                            judge.showStatistic(scenariosIndex);

                            System.out.println("That's all. Press Enter to return to main menu.");
                            scanner.nextLine();

                            ethicalEngine.displayMainMenu(scenarios);
                            break;

                        } else {

                            judge.showStatistic(scenariosIndex - 1);
                            System.out.println("Would you like to continue? (yes/no)");
                            userInput = scanner.nextLine();

                            if (userInput.equalsIgnoreCase("no")) {

                                ethicalEngine.displayMainMenu(scenarios);
                                break;
                            }

                        }

                    }
                    break;

                case "run":
                case "r":

                    Simulator simulator = new Simulator();

                    if (configFilePath.equals("DEFAULT")) {
                        System.out.println("How many scenarios should be run?");
                        userInput = scanner.nextLine();

                        int numberOfScenarios;

                        while (true) {

                            try {
                                numberOfScenarios = Integer.parseInt(userInput);
                                break;
                            }
                            catch (NumberFormatException e) {
                                System.out.println("Invalid input. How many scenarios should be run?");
                                userInput = scanner.nextLine();
                            }
                        }

                        simulator.runRandom(numberOfScenarios, logFilePath);

                    } else {

                        simulator.runConfigFile(configFilePath, logFilePath);
                    }

                    System.out.println("That's all. Press Enter to return to main menu.");
                    scanner.nextLine();

                    ethicalEngine.displayMainMenu(scenarios);
                    break;

                case "audit":
                case "a":

                    Auditor auditor = new Auditor();
                    auditor.showAudit(logFilePath);
                    scanner.nextLine();
                    ethicalEngine.displayMainMenu(scenarios);
                    break;

                case "quit":
                case "q":
                    System.exit(0);
            }
        }
    }

    /**
     *  Logic for displaying the main menu.
     *  @param scenarios  a list of scenarios object.
     */
    public void displayMainMenu(ArrayList<Scenario> scenarios) {

        try {

            BufferedReader inputStream = new BufferedReader(new FileReader("welcome.ascii"));
            String inputLine = inputStream.readLine();

            while (inputLine != null) {
                System.out.println(inputLine);
                inputLine = inputStream.readLine();
            }
            inputStream.close();

        } catch (Exception e) {
            System.out.println("An error occurred while loading the file.");
        }

        System.out.println();
        if (scenarios.size() > 0) {
            System.out.println(scenarios.size() + " Scenarios imported.");
        }
        System.out.println("Please enter one of the following commands to continue:");
        System.out.println("- judge scenarios: [judge] or [j]");
        System.out.println("- run simulations with the in-built decision algorithm: [run] or [r]");
        System.out.println("- show audit from history: [audit] or [a]");
        System.out.println("- quit the program: [quit] or [q]");
        System.out.print("> ");

    }

    /**
     *  Logic for displaying the help text.
     */
    public void displayHelp() {

        System.out.println("EthicalEngine - COMP90041 - Final Project");
        System.out.println();
        System.out.println("Usage: java EthicalEngine [arguments]");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("    -c or --config        Optional: path to config file");
        System.out.println("    -h or --help          Optional: print Help (this message) and exit");
        System.out.println("    -l or --log           Optional: path to data log file");
        System.exit(0);
    }
}
