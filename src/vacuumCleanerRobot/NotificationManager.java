package vacuumCleanerRobot;

public class NotificationManager implements Observer {
    @Override
    public void update(String message) {
        System.out.println("[Notification] " + message);
    }
}