package vacuumCleanerRobot;

import java.util.HashMap;

public class SettingsManager {
    private HashMap<String, String> preferences = new HashMap<>();

    public void setPreference(String key, String value) {
        preferences.put(key, value);
    }

    public String getPreference(String key) {
        return preferences.getOrDefault(key, "default");
    }
}