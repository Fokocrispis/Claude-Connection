package vacuumCleanerRobot;

public class DashboardUI implements Observer {
    @Override
    public void update(String message) {
        System.out.println("[Dashboard Update] " + message);
    }
}