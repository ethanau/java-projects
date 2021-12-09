import com.gray.scenario.Scenario;
import java.util.ArrayList;

/**
 * A class for processing the simulation command
 */
public class Auditor {

    // Some constants for representing the decision maker and parsing file type.
    private static final String ALGORITHM_DECISION = "Algorithm";
    private static final String USER_DECISION = "User";
    private static final String PARSING_FILE_TYPE = "log";

    // Some variables used for processing the algorithm audit.
    private ArrayList<CharacteristicSurvivalRatioPair> algorithmStatisticList = new ArrayList<>();
    private ArrayList<Scenario> algorithmScenarios = new ArrayList<>();
    private ArrayList<EthicalEngine.Decision> algorithmDecisions = new ArrayList<>();
    private double algorithmAverageAge;
    private int numberOfAlgorithmRuns;

    // Some variables used for processing the user audit.
    private ArrayList<CharacteristicSurvivalRatioPair> userStatisticList = new ArrayList<>();
    private ArrayList<Scenario> userScenarios = new ArrayList<>();
    private ArrayList<EthicalEngine.Decision> userDecisions = new ArrayList<>();
    private double userAverageAge;
    private int numberOfUserRuns;

    /**
     * Logic for showing the audit
     * @param logFilePath the log file path name
     */
    public void showAudit(String logFilePath) {

        parsingLogFile(logFilePath);

        if (numberOfAlgorithmRuns > 0) {
            displayStatisticList(ALGORITHM_DECISION);
            if (numberOfUserRuns > 0) {
                System.out.println();
            }
        }

        if (numberOfUserRuns > 0) {
            displayStatisticList(USER_DECISION);

        }

        if (numberOfAlgorithmRuns > 0 || numberOfUserRuns > 0) {
            System.out.println("That's all. Press Enter to return to main menu.");
        }
    }

    /**
     * Logic for displaying the statistic in audit
     * @param decisionMaker whether the decision made by user or algorithm
     */
    private void displayStatisticList(String decisionMaker) {

        System.out.println("======================================");
        System.out.println("# " + decisionMaker + " Audit");
        System.out.println("======================================");
        System.out.println("- % SAVED AFTER " +
                (decisionMaker.equalsIgnoreCase(ALGORITHM_DECISION) ? numberOfAlgorithmRuns : numberOfUserRuns) + " RUNS");

        ArrayList<CharacteristicSurvivalRatioPair> statisticList =
                decisionMaker.equalsIgnoreCase(ALGORITHM_DECISION) ? algorithmStatisticList : userStatisticList;

        for (CharacteristicSurvivalRatioPair pair : statisticList) {
            System.out.println(pair.getCharacteristic() + ": " + String.format("%.2f", pair.getSurvivalRatio()));
        }

        double averageAge = decisionMaker.equalsIgnoreCase(ALGORITHM_DECISION) ? algorithmAverageAge : userAverageAge;
        System.out.println("--");
        System.out.println("average age: " + String.format("%.2f", averageAge));

    }

    /**
     * Logic for parsing the log file
     * @param logFilePath the log file path name
     */
    private void parsingLogFile(String logFilePath) {

        ScenariosProcessor scenariosProcessor = new ScenariosProcessor();
        StatisticProcessor statisticProcessor = new StatisticProcessor();

        scenariosProcessor.parsingFile(logFilePath, PARSING_FILE_TYPE);

        ArrayList<Scenario> scenarios = scenariosProcessor.getScenarios();
        ArrayList<EthicalEngine.Decision> decisions = scenariosProcessor.getDecisions();
        ArrayList<String> scenariosTypeList = scenariosProcessor.getScenariosTypeList();

        for (int index = 0; index < scenarios.size(); index++) {

            if (scenariosTypeList.get(index).equalsIgnoreCase("decider:" + ALGORITHM_DECISION)) {
                algorithmScenarios.add(scenarios.get(index));
                algorithmDecisions.add(decisions.get(index));
            } else {
                userScenarios.add(scenarios.get(index));
                userDecisions.add(decisions.get(index));
            }

        }

        numberOfAlgorithmRuns = algorithmScenarios.size();
        numberOfUserRuns = userScenarios.size();

        statisticProcessor.computeStatistic(algorithmScenarios, algorithmDecisions, numberOfAlgorithmRuns - 1);
        algorithmStatisticList = statisticProcessor.getStatisticList();
        algorithmAverageAge = statisticProcessor.getAverageAge();

        statisticProcessor.computeStatistic(userScenarios, userDecisions, numberOfUserRuns - 1);
        userStatisticList = statisticProcessor.getStatisticList();
        userAverageAge = statisticProcessor.getAverageAge();

    }
}
