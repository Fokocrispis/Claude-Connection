package vacuumCleanerRobot;

public class CleaningScheduler extends Subject {
    public void scheduleTask(String taskConfig) {
        System.out.println("Scheduling new task: " + taskConfig);
        notifyObservers("New task scheduled: " + taskConfig);
    }
}
