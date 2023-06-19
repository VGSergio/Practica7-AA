package MVC.Model;

/**
 * A class representing the status of a simulation. This class is used to keep
 * track of whether a simulation is idle, solving, or has been solved.
 *
 * The status of a simulation can be set and retrieved using methods in this
 * class.
 *
 * This class is used in the Model component of the Model-View-Controller
 * architecture.
 *
 * Possible values for the simulation status are: - Idle: The simulation is not
 * currently being solved. - Solving: The simulation is currently being solved.
 * - Solved: The simulation has been solved and a solution has been found.
 *
 * @author Sergio
 */
public class SimulationStatus {

    /**
     * An enumeration representing the possible values for the simulation
     * status.
     */
    private enum PossibleStatus {
        Idle, Solving, Solved
    };

    private PossibleStatus currentStatus;

    /**
     * Constructs a new instance of the SimulationStatus class and sets the
     * initial status to Idle.
     */
    public SimulationStatus() {
        currentStatus = PossibleStatus.Idle;
    }

    /**
     * Returns whether the simulation is currently idle.
     *
     * @return true if the simulation is idle, false otherwise
     */
    public boolean isIdle() {
        return currentStatus == PossibleStatus.Idle;
    }

    /**
     * Returns whether the simulation is currently being solved.
     *
     * @return true if the simulation is being solved, false otherwise
     */
    public boolean isSolving() {
        return currentStatus == PossibleStatus.Solving;
    }

    /**
     * Returns whether the simulation has been solved.
     *
     * @return true if the simulation has been solved, false otherwise
     */
    public boolean isSolved() {
        return currentStatus == PossibleStatus.Solved;
    }

    /**
     * Sets the simulation status to Idle.
     */
    public void setIdle() {
        currentStatus = PossibleStatus.Idle;
    }

    /**
     * Sets the simulation status to Solving.
     */
    public void setSolving() {
        currentStatus = PossibleStatus.Solving;
    }

    /**
     * Sets the simulation status to Solved.
     */
    public void setSolved() {
        currentStatus = PossibleStatus.Solved;
    }
}
