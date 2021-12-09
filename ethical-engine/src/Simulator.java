import com.gray.scenario.Scenario;

import java.util.ArrayList;

/**
 * A class for processing the simulation command
 */
public class Simulator {

    // A StatisticProcessor instance and a ScenariosProcessor instance used in simulation.
    private StatisticProcessor statisticProcessor = new StatisticProcessor();
    private ScenariosProcessor scenariosProcessor = new ScenariosProcessor();

    // Some variables for representing the scenarios and the decisions.
    private ArrayList<Scenario> scenarios = new ArrayList<>();
    private ArrayList<EthicalEngine.Decision> decisions = new ArrayList<>();

    // A constant for representing the decision maker.
    private static final String DECISION_MAKER = "Algorithm";

    /**
     * Logic for running random scenarios
     * @param numberOfScenarios the number of random scenarios
     * @param logFilePath the log file path name
     */
    public void runRandom(int numberOfScenarios, String logFilePath) {

        scenarios = scenariosProcessor.randomScenariosGenerator(numberOfScenarios);
        runScenarios(logFilePath);
    }

    /**
     * Logic for running scenarios in config file
     * @param configFilePath the config file path name
     * @param logFilePath the log file path name
     */
    public void runConfigFile(String configFilePath, String logFilePath) {

        scenariosProcessor.parsingFile(configFilePath, "config");
        scenarios = scenariosProcessor.getScenarios();
        runScenarios(logFilePath);
    }

    /**
     * Logic for processing scenarios in simulation
     * @param logFilePath the log file path name
     */
    private void runScenarios(String logFilePath) {

        for (Scenario scenario : scenarios) {

            EthicalEngine.Decision decision = EthicalEngine.decide(scenario);
            decisions.add(decision);
            statisticProcessor.saveJudgedScenario(logFilePath, scenario, decision, DECISION_MAKER);
        }

        statisticProcessor.showStatistic(scenarios, decisions, scenarios.size() - 1);
    }

}
