package vacuumCleanerRobot;

import java.awt.Point;
import vacuumCleanerRobot.Game;

/**
 * Demonstration class showing the integration between the vacuum control app
 * and the 2D simulation through the VacuumRobotBridge.
 */
public class IntegrationDemo {
    public static void main(String[] args) {
        try {
            System.out.println("=== Vacuum Robot Integration Demo ===");
            
            // 1. Start the simulation
            System.out.println("1. Starting 2D simulation...");
            Game game = new Game();
            Thread gameThread = new Thread(game);
            gameThread.start();
            
            // Wait for simulation to initialize
            Thread.sleep(2000);
            
            // 2. Get references to control systems
            MainApp app = MainApp.getInstance();
            VacuumRobotBridge bridge = VacuumRobotBridge.getInstance();
            
            // 3. Attach observers
            app.getScheduler().attach(new DashboardUI());
            app.getScheduler().attach(new NotificationManager());
            
            System.out.println("2. Control systems initialized and connected!");
            
            // 4. Demonstrate AUTO mode
            System.out.println("\n3. Testing AUTO cleaning mode...");
            app.getRemoteControl().triggerCleaning("AUTO");
            Thread.sleep(3000);
            
            System.out.println("Robot Status: " + bridge.getCleaningStats());
            
            // 5. Demonstrate PET mode
            System.out.println("\n4. Switching to PET mode...");
            app.getRemoteControl().triggerCleaning("PET");
            Thread.sleep(3000);
            
            System.out.println("Robot Status: " + bridge.getCleaningStats());
            
            // 6. Demonstrate scheduling
            System.out.println("\n5. Testing task scheduling...");
            app.getScheduler().scheduleTask("Clean living room at 2 PM");
            
            // 7. Demonstrate manual control
            System.out.println("\n6. Testing manual control...");
            bridge.setRobotSpeed(1.0, 2.0);
            Thread.sleep(2000);
            
            Point currentPos = bridge.getRobotPosition();
            System.out.println("Current position: (" + currentPos.x + ", " + currentPos.y + ")");
            
            // 8. Stop cleaning
            System.out.println("\n7. Stopping cleaning...");
            bridge.stopCleaning();
            
            System.out.println("\n=== Demo completed successfully! ===");
            System.out.println("The vacuum control app and simulation are now integrated.");
            System.out.println("You can use the console interface to control the robot in real-time.");
            
        } catch (Exception e) {
            System.err.println("Demo failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}