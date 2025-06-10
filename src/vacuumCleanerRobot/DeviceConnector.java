package vacuumCleanerRobot;

public class DeviceConnector {
    private FirmwareUpdater firmwareUpdater = new FirmwareUpdater();

    public void checkCompatibility() {
        if (!firmwareUpdater.checkVersion()) {
            firmwareUpdater.updateFirmware();
            System.out.print("Software was updated");
        } else {
        	System.out.print("No update needed");
        }
    }
}
