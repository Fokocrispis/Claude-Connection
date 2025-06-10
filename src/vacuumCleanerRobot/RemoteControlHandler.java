
package vacuumCleanerRobot;

public class RemoteControlHandler {
    private CleaningModeFactory modeFactory = new CleaningModeFactory();
    private VacuumRobotBridge bridge = VacuumRobotBridge.getInstance();

    public void triggerCleaning(String modeType) {
        // Execute cleaning mode logic
        CleaningMode mode = modeFactory.createMode(modeType);
        mode.executeCleaning();
        
        // Start simulation through bridge
        bridge.startCleaning(modeType);
    }
    
    public void stopCleaning() {
        bridge.stopCleaning();
    }
    
    public String getRobotStatus() {
        return bridge.getCleaningStats();
    }
}