import com.gray.scenario.Scenario;
import java.util.ArrayList;

/**
 * A class for processing the judge command
 */
public class Judge {

    // A StatisticProcessor instance used for processing the statistic.
    private StatisticProcessor statisticProcessor = new StatisticProcessor();

    // Some variables for representing the scenarios and the decisions.
    private ArrayList<Scenario> scenarios = new ArrayList<>();
    private ArrayList<EthicalEngine.Decision> decisions = new ArrayList<>();

    // A variable for recording the user consent.
    private boolean isUserConsent = false;

    /**
     * Logic for showing the statistic
     * @param scenariosIndex indicate the index of scenarios to be processed
     */
    public void showStatistic(int scenariosIndex) {
        statisticProcessor.showStatistic(scenarios, decisions, scenariosIndex);
    }

    /**
     * Logic for adding the decision judged, and whether save the scenario and the decision
     * @param scenariosIndex indicate the index of scenarios to be processed
     */
    public void addDecision(EthicalEngine.Decision decision, String logFilePath, int scenariosIndex) {

        decisions.add(decision);

        if (isUserConsent) {
            statisticProcessor.saveJudgedScenario(logFilePath, scenarios.get(scenariosIndex), decision, "User");
        }

    }

    /**
     * Set the scenarios
     */
    public void setScenarios(ArrayList<Scenario> scenarios) {
        this.scenarios = scenarios;
    }

    /**
     * Set whether user is consent
     */
    public void setUserConsent(boolean userConsent) {
        isUserConsent = userConsent;
    }

    /**
     * Get a list of scenarios
     * @return ArrayList a list of scenarios
     */
    public ArrayList<Scenario> getScenarios() {
        return scenarios;
    }

}
