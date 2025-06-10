package vacuumCleanerRobot;

public class CleaningModeFactory {
    public CleaningMode createMode(String modeType) {
        if (modeType.equalsIgnoreCase("PET")) {
            return new PetModeStrategy();
        }
        return new AutoModeStrategy();
    }
}
