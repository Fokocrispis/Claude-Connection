package vacuumCleanerRobot;

public class MainApp {
    private static MainApp instance;
    private UserProfileManager profileManager;
    private RemoteControlHandler remoteControl;
    private CleaningScheduler scheduler;
    private DeviceConnector deviceConnector;

    private MainApp() {
        profileManager = new UserProfileManager();
        remoteControl = new RemoteControlHandler();
        scheduler = new CleaningScheduler();
        deviceConnector = new DeviceConnector();
    }

    public static MainApp getInstance() {
        if (instance == null) {
            instance = new MainApp();
        }
        return instance;
    }

    public UserProfileManager getProfileManager() { return profileManager; }
    public RemoteControlHandler getRemoteControl() { return remoteControl; }
    public CleaningScheduler getScheduler() { return scheduler; }
    public DeviceConnector getDeviceConnector() { return deviceConnector; }
}
