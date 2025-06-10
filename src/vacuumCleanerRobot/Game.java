package vacuumCleanerRobot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JFrame;

import vacuumCleanerRobot.Floor;
import vacuumCleanerRobot.LocatedRectangle;
import vacuumCleanerRobot.TestObject;
import vacuumCleanerRobot.VacuumRobotBridge;

public class Game extends JFrame implements Runnable {
  public static final int TARGET_FPS = 60;
  public static final boolean SHOULD_PRINT_FPS = false;
  private long timer;

  /**
   * If a second screen is being used (ex. a second monitor), return the bounds
   * of that screen, otherwise return the bounds of the primary screen (ex.
   * the laptop screen).
   * 
   * @return The {@link Rectangle bounds} of the window where the frame is
   *         rendered.
   */
  public static Rectangle getWindowBounds() {
    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] screens = graphicsEnvironment.getScreenDevices();
    int screenIndex = screens.length == 1 ? 0 : 1;

    return screens[screenIndex].getDefaultConfiguration().getBounds();
  }

  /**
   * If a single screen is being used (ex. the laptop screen), position the
   * frame on its top-left corner, otherwise position the frame on the top-left
   * corner of the second screen (ex. a second monitor).
   * 
   * @return The {@link Point position} where the frame should be rendered.
   */
  private static Point getWindowPosition() {
    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] screens = graphicsEnvironment.getScreenDevices();

    if (screens.length == 1) {
      return new Point();
    }

    Rectangle windowBounds = screens[0].getDefaultConfiguration().getBounds();
    return new Point(
        (int) windowBounds.getWidth(),
        0);
  }

  private final GameLoop gameLoop;
  private final List<GameObject> gameObjects;
  private final List<LocatedRectangle> interactionZones;
  private final List<LocatedRectangle> objectsWithHitbox;
  private final Map map;
  private final VacuumRobotBridge bridge;

  public Game() throws IOException {
    super("Vacuum Robot Simulation");
    this.gameLoop = new GameLoop(
        TARGET_FPS,
        SHOULD_PRINT_FPS,
        this::update,
        this::repaint,
        this::isRunning,
        this::onExit);

    this.gameObjects = new ArrayList<>();
    this.interactionZones = new ArrayList<>();
    this.objectsWithHitbox = new ArrayList<>();
    
    map = new Map(Game.getWindowBounds().getWidth(),Game.getWindowBounds().getWidth());

    TestObject testObject= new TestObject(new Dimension(100, 100), new Point(1100, 350), map);
    Floor floor = new Floor(map, testObject);
    
    this.gameObjects.addAll(List.of(floor, testObject));
    this.objectsWithHitbox.addAll(List.of(testObject));

    // Initialize bridge and connect simulation
    this.bridge = VacuumRobotBridge.getInstance();
    this.bridge.connectSimulation(testObject);

    this.setLocation(getWindowPosition());
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    this.add(new Root(this::draw));
    this.pack();
    this.setVisible(true);
    
    // Display bridge status
    System.out.println("Game initialized with bridge connection");
    System.out.println("Bridge status: " + bridge.getCleaningStats());
  }

  @Override
  public void run() {
    this.gameLoop.start();
  }

	private void update(long deltaTime) {

		this.gameObjects.forEach(gameObject -> gameObject.update(deltaTime));

		if (objectsWithHitbox.size() > 1) {
			for (LocatedRectangle part : objectsWithHitbox) {
				if (!objectsWithHitbox.get(1).vacantSpace(part) && !(objectsWithHitbox.get(1) == part)) {
					objectsWithHitbox.get(1).collisionDirection(part);
				}
			}

			for (LocatedRectangle part : objectsWithHitbox) {
				if (!objectsWithHitbox.get(0).vacantSpace(part) && !(objectsWithHitbox.get(0) == part)) {
					objectsWithHitbox.get(0).collisionDirection(part);
				}
			}
		}

	}

  private void draw(Graphics2D graphics2D) {
    this.gameObjects.forEach(gameObject -> gameObject.draw(graphics2D));
    
    // Draw bridge status on screen
    graphics2D.setColor(Color.WHITE);
    graphics2D.fillRect(10, 10, 400, 60);
    graphics2D.setColor(Color.BLACK);
    graphics2D.drawString("Bridge Status: " + bridge.getCleaningStats(), 15, 25);
    graphics2D.drawString("Mode: " + bridge.getCurrentMode() + " | Active: " + bridge.isActive(), 15, 45);
  }
  
  private void saveTime() {
	  this.timer=new Date().getTime();
  }
  
  private boolean isRunning() {
    return true;
  }

  private void onExit() {
    System.out.println("Exit");
    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

  private class Root extends JComponent {
    private final Consumer<Graphics2D> draw;

    public Root(Consumer<Graphics2D> draw) {
      this.draw = draw;
      this.setPreferredSize(Game.getWindowBounds().getSize());
    }

    @Override
    public void paintComponent(Graphics graphics) {
      super.paintComponent(graphics);
      Graphics2D graphics2D = (Graphics2D) graphics;
      this.draw.accept(graphics2D);
    }
  }
}
