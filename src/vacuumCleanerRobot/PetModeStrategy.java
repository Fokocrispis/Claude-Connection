package vacuumCleanerRobot;

public class PetModeStrategy implements CleaningMode {
    private final PetModeOptimizer optimizer = new PetModeOptimizer();

    @Override
    public void executeCleaning() {
        optimizer.trackHighRiskZones();
        optimizer.boostSuction();
        optimizer.scanForMesses();
        System.out.println("[Pet Mode] Executing deep-cleaning routine");
    }
}