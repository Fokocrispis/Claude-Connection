package vacuumCleanerRobot;

import java.awt.Graphics2D;

public interface GameObject {
  public void update(long deltaTime);

  public void draw(Graphics2D graphics2D);
}
