package vacuumCleanerRobot;

import java.awt.Point;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class RemoteControlServer {
    private static final int PORT = 8080;
    private ServerSocket serverSocket;
    private ExecutorService executor;
    private final MainApp app = MainApp.getInstance();
    private final VacuumRobotBridge bridge = VacuumRobotBridge.getInstance();
    private boolean running = false;

    public RemoteControlServer() {
        executor = Executors.newCachedThreadPool();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            running = true;
            System.out.println("Remote Control Server started on port " + PORT);
            
            // Accept connections in a separate thread
            executor.submit(() -> {
                while (running) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Client connected: " + clientSocket.getInetAddress());
                        executor.submit(() -> handleClient(clientSocket));
                    } catch (IOException e) {
                        if (running) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String command;
            while ((command = in.readLine()) != null) {
                System.out.println("Received command: " + command);
                String response = processCommand(command);
                out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }
    }

    private String processCommand(String command) {
        String[] parts = command.split(":");
        String action = parts[0];
        
        switch (action) {
            case "AUTO_CLEAN":
                app.getRemoteControl().triggerCleaning("AUTO");
                return "OK:Auto cleaning started";
                
            case "PET_MODE":
                app.getRemoteControl().triggerCleaning("PET");
                return "OK:Pet mode activated";
                
            case "STOP":
                bridge.stopCleaning();
                return "OK:Cleaning stopped";
                
            case "STATUS":
                Point pos = bridge.getRobotPosition();
                String status = bridge.getCleaningStats();
                return "OK:Status=" + status + ";Position=" + pos.x + "," + pos.y;
                
            case "FIRMWARE":
                app.getDeviceConnector().checkCompatibility();
                return "OK:Firmware checked/updated";
                
            case "MANUAL":
                if (parts.length >= 3) {
                    try {
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);
                        bridge.setRobotSpeed(x, y);
                        return "OK:Speed set to " + x + "," + y;
                    } catch (NumberFormatException e) {
                        return "ERROR:Invalid speed values";
                    }
                }
                return "ERROR:Missing speed values";
                
            case "SCHEDULE":
                if (parts.length >= 2) {
                    app.getScheduler().scheduleTask(parts[1]);
                    return "OK:Task scheduled";
                }
                return "ERROR:Missing task configuration";
                
            default:
                return "ERROR:Unknown command";
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}