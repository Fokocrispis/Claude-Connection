package vacuumCleanerRobot;

public class PetModeOptimizer {
    private int suctionPower = 100; // default suction power
    
    public void boostSuction() {
        suctionPower = 150;
        System.out.println("[Pet Optimizer] Suction boosted to 150%");
    }

    public void scanForMesses() {
        System.out.println("[Pet Optimizer] Scanning for pet hair/spills...");
        // simulation of mess detection
        if (Math.random() > 0.7) {
            System.out.println("[Pet Optimizer] Detected pet mess!");
        }
    }

    public void trackHighRiskZones() {
        System.out.println("[Pet Optimizer] Prioritizing pet bowl area");
    }
}
