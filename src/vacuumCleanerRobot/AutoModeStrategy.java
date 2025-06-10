package vacuumCleanerRobot;

public class AutoModeStrategy implements CleaningMode {
    @Override
    public void executeCleaning() {
        System.out.println("Executing standard cleaning pattern");
    }
}