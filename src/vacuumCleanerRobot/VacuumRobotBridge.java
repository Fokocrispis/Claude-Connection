package vacuumCleanerRobot;

import java.awt.Point;
import java.util.Observable;
import java.util.Observer;
import vacuumCleanerRobot.TestObject;
import vacuumCleanerRobot.Vector2D;

/**
 * Bridge class that connects the vacuum cleaner control app with the 2D simulation.
 * This class implements the communication interface between both systems.
 */
@SuppressWarnings("deprecation")
public class VacuumRobotBridge extends Observable implements Observer {
    private static VacuumRobotBridge instance;
    private TestObject robotSimulation;
    private boolean isActive = false;
    private String currentMode = "AUTO";
    private CleaningMode activeCleaningMode;
    private CleaningModeFactory modeFactory;
    
    private VacuumRobotBridge() {
        this.modeFactory = new CleaningModeFactory();
    }
    
    public static synchronized VacuumRobotBridge getInstance() {
        if (instance == null) {
            instance = new VacuumRobotBridge();
        }
        return instance;
    }
    
    /**
     * Connect the simulation robot to this bridge
     */
    public void connectSimulation(TestObject robotSimulation) {
        this.robotSimulation = robotSimulation;
    }
    
    /**
     * Start cleaning with specified mode
     */
    public void startCleaning(String mode) {
        this.currentMode = mode;
        this.activeCleaningMode = modeFactory.createMode(mode);
        this.isActive = true;
        
        // Execute cleaning mode logic
        activeCleaningMode.executeCleaning();
        
        // Apply mode-specific behavior to simulation
        applyModeToSimulation(mode);
        
        // Notify observers
        setChanged();
        notifyObservers("Cleaning started in " + mode + " mode");
    }
    
    /**
     * Stop cleaning
     */
    public void stopCleaning() {
        this.isActive = false;
        if (robotSimulation != null) {
            robotSimulation.setSpeed(new Vector2D(0, 0));
        }
        
        setChanged();
        notifyObservers("Cleaning stopped");
    }
    
    /**
     * Apply mode-specific behavior to the simulation
     */
    private void applyModeToSimulation(String mode) {
        if (robotSimulation == null) return;
        
        if ("PET".equalsIgnoreCase(mode)) {
            // Pet mode: slower, more thorough movement
            robotSimulation.setSpeed(new Vector2D(1.5, 1.0));
            System.out.println("[Bridge] Applied PET mode parameters to simulation");
        } else {
            // Auto mode: standard movement
            robotSimulation.setSpeed(new Vector2D(2.2, 1.3));
            System.out.println("[Bridge] Applied AUTO mode parameters to simulation");
        }
    }
    
    /**
     * Get current robot position from simulation
     */
    public Point getRobotPosition() {
        return robotSimulation != null ? robotSimulation.getPosition() : new Point(0, 0);
    }
    
    /**
     * Get current robot speed from simulation
     */
    public Vector2D getRobotSpeed() {
        return robotSimulation != null ? robotSimulation.getSpeed() : new Vector2D(0, 0);
    }
    
    /**
     * Check if robot is currently active
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Get current cleaning mode
     */
    public String getCurrentMode() {
        return currentMode;
    }
    
    /**
     * Manually control robot movement (for remote control)
     */
    public void setRobotSpeed(double x, double y) {
        if (robotSimulation != null && isActive) {
            robotSimulation.setSpeed(new Vector2D(x, y));
        }
    }
    
    /**
     * Move robot to specific position (for scheduling/navigation)
     */
    public void moveRobotTo(Point target) {
        if (robotSimulation != null && isActive) {
            robotSimulation.setAddress(target);
            setChanged();
            notifyObservers("Robot moved to position: " + target);
        }
    }
    
    /**
     * Observer method to receive updates from other components
     */
    @Override
    public void update(Observable o, Object arg) {
        // Handle updates from other components if needed
        System.out.println("[Bridge] Received update: " + arg);
    }
    
    /**
     * Get cleaning statistics
     */
    public String getCleaningStats() {
        Point pos = getRobotPosition();
        Vector2D speed = getRobotSpeed();
        return String.format("Position: (%d,%d), Speed: (%.1f,%.1f), Mode: %s, Active: %s", 
                           pos.x, pos.y, speed.VectorX(), speed.VectorY(), currentMode, isActive);
    }
}
