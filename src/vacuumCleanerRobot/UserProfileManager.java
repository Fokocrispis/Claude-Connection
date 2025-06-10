package vacuumCleanerRobot;

public class UserProfileManager {
    private SettingsManager settings;

    public UserProfileManager() {
        this.settings = new SettingsManager();
    }

    public void updatePreferences(String prefKey, String value) {
        settings.setPreference(prefKey, value);
    }
}
