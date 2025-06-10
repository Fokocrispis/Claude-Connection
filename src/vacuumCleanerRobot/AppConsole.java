package vacuumCleanerRobot;
/*
import java.awt.Point;
import java.util.Scanner;
import vacuumCleanerRobot.Game;

public class AppConsole {
    public static void main(String[] args) {
        try {
            // Start the simulation in a separate thread
            Game game = new Game();
            Thread gameThread = new Thread(game);
            gameThread.start();
            
            // Give the simulation time to initialize
            Thread.sleep(1000);
            
            MainApp app = MainApp.getInstance();
            VacuumRobotBridge bridge = VacuumRobotBridge.getInstance();
            Scanner scanner = new Scanner(System.in);

            // Attach observers
            app.getScheduler().attach(new DashboardUI());
            app.getScheduler().attach(new NotificationManager());

            System.out.println("=== Integrated Vacuum Robot Control System ===");
            System.out.println("Simulation and control app are now connected!");

            while (true) {
                System.out.println("\n=== Robot Control ===");
                System.out.println("1. Start Auto Clean");
                System.out.println("2. Activate Pet Mode");
                System.out.println("3. Schedule Task");
                System.out.println("4. Check Firmware");
                System.out.println("5. Stop Cleaning");
                System.out.println("6. Robot Status");
                System.out.println("7. Manual Control");
                System.out.println("8. Exit");
                System.out.print("Select option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        app.getRemoteControl().triggerCleaning("AUTO");
                        System.out.println("Auto cleaning started in simulation!");
                        break;
                    case 2:
                        app.getRemoteControl().triggerCleaning("PET");
                        System.out.println("Pet mode activated in simulation!");
                        break;
                    case 3:
                        System.out.print("Enter task configuration: ");
                        String config = scanner.nextLine();
                        app.getScheduler().scheduleTask(config);
                        break;
                    case 4:
                        app.getDeviceConnector().checkCompatibility();
                        break;
                    case 5:
                        bridge.stopCleaning();
                        System.out.println("Cleaning stopped!");
                        break;
                    case 6:
                        System.out.println("Current Status: " + bridge.getCleaningStats());
                        Point pos = bridge.getRobotPosition();
                        System.out.println("Robot Position: (" + pos.x + ", " + pos.y + ")");
                        break;
                    case 7:
                        System.out.println("Manual Control Mode");
                        System.out.print("Enter X speed (-5 to 5): ");
                        double speedX = scanner.nextDouble();
                        System.out.print("Enter Y speed (-5 to 5): ");
                        double speedY = scanner.nextDouble();
                        bridge.setRobotSpeed(speedX, speedY);
                        System.out.println("Manual speed set to: (" + speedX + ", " + speedY + ")");
                        break;
                    case 8:
                        scanner.close();
                        System.out.println("Shutting down integrated system...");
                        System.exit(0);
                        return;
                    default:
                        System.out.println("Invalid selection");
                }
            }
        } catch (Exception e) {
            System.err.println("Error starting integrated system: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class AppConsole extends JFrame {

    private final MainApp app = MainApp.getInstance();
    private final VacuumRobotBridge bridge = VacuumRobotBridge.getInstance();
    private RemoteControlServer remoteServer; 


    public AppConsole() {
    setTitle("Vacuum Cleaner Robot Control Panel");
    setSize(300, 400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    try {
        // Start the simulation in a separate thread
        Game game = new Game();
        Thread gameThread = new Thread(game);
        gameThread.start();

        // Give the simulation time to initialize
        Thread.sleep(1000);

        // Attach observers
        app.getScheduler().attach(new DashboardUI());
        app.getScheduler().attach(new NotificationManager());

        // Start the remote control server
        remoteServer = new RemoteControlServer();
        remoteServer.start();

        System.out.println("=== Integrated Vacuum Robot Control System ===");
        System.out.println("Simulation and control app are now connected!");
        System.out.println("Remote control server started on port 8080");

    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Error starting simulation: " + e.getMessage(),
                "Initialization Error",
                JOptionPane.ERROR_MESSAGE);
    }

    initComponents();
    }      
    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton autoCleanButton = new JButton("Start Auto Clean");
        JButton petModeButton = new JButton("Activate Pet Mode");
        JButton scheduleTaskButton = new JButton("Schedule Task");
        JButton checkFirmwareButton = new JButton("Check Firmware");
        JButton stopCleaningButton = new JButton("Stop Cleaning");
        JButton statusButton = new JButton("Robot Status");
        JButton manualControlButton = new JButton("Manual Control");
        JButton exitButton = new JButton("Exit");

        panel.add(autoCleanButton);
        panel.add(petModeButton);
        panel.add(scheduleTaskButton);
        panel.add(checkFirmwareButton);
        panel.add(stopCleaningButton);
        panel.add(statusButton);
        panel.add(manualControlButton);
        panel.add(exitButton);

        autoCleanButton.addActionListener(e -> {
            app.getRemoteControl().triggerCleaning("AUTO");
            JOptionPane.showMessageDialog(this, "Auto cleaning started!");
        });

        petModeButton.addActionListener(e -> {
            app.getRemoteControl().triggerCleaning("PET");
            JOptionPane.showMessageDialog(this, "Pet mode activated!");
        });

        scheduleTaskButton.addActionListener(e -> {
            String config = JOptionPane.showInputDialog(this, "Enter task configuration:");
            if (config != null && !config.trim().isEmpty()) {
                app.getScheduler().scheduleTask(config);
                JOptionPane.showMessageDialog(this, "Task scheduled.");
            }
        });

        checkFirmwareButton.addActionListener(e -> {
        	app.getDeviceConnector().checkCompatibility();
            JOptionPane.showMessageDialog(this, "Updating firmware to latest version\r\n"
            		+ "Software was updated.");
        });

        stopCleaningButton.addActionListener(e -> {
            bridge.stopCleaning();
            JOptionPane.showMessageDialog(this, "Cleaning stopped.");
        });

        statusButton.addActionListener(e -> {
            Point pos = bridge.getRobotPosition();
            String status = bridge.getCleaningStats();
            JOptionPane.showMessageDialog(this,
                    "Status: " + status + "\nPosition: (" + pos.x + ", " + pos.y + ")");
        });

        manualControlButton.addActionListener(e -> {
            JPanel inputPanel = new JPanel(new GridLayout(2, 2));
            JTextField xField = new JTextField();
            JTextField yField = new JTextField();

            inputPanel.add(new JLabel("X Speed (-5 to 5):"));
            inputPanel.add(xField);
            inputPanel.add(new JLabel("Y Speed (-5 to 5):"));
            inputPanel.add(yField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel,
                    "Manual Control", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    double x = Double.parseDouble(xField.getText());
                    double y = Double.parseDouble(yField.getText());
                    bridge.setRobotSpeed(x, y);
                    JOptionPane.showMessageDialog(this, "Speed set to: (" + x + ", " + y + ")");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter numbers.");
                }
            }
        });

        exitButton.addActionListener(e -> {
            if (remoteServer != null) {
                remoteServer.stop();
            }
            System.exit(0);
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppConsole().setVisible(true));
    }
}