package vacuumCleanerRobot;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GameLoop {
  private final int targetFps;
  private final boolean shouldPrintFps;
  private final double nanosecondsPerFrame;

  private final Consumer<Long> update;
  private final Runnable draw;
  private final Supplier<Boolean> isRunning;
  private final Runnable onExit;

  private double loopTimeProgress;
  private int frameCount;
  private long previousLoopTime;
  private long previousFrameTime;
  private long timer;

  public GameLoop(
      int targetFps,
      boolean shouldPrintFps,
      Consumer<Long> update,
      Runnable draw,
      Supplier<Boolean> isRunning,
      Runnable onExit) {
    this.targetFps = targetFps;
    this.shouldPrintFps = shouldPrintFps;
    this.nanosecondsPerFrame = Duration.ofSeconds(1).toNanos() / this.targetFps;

    this.update = update;
    this.draw = draw;
    this.isRunning = isRunning;
    this.onExit = onExit;

    this.loopTimeProgress = 0;
    this.frameCount = 0;
    this.previousLoopTime = 0;
    this.previousFrameTime = 0;
    this.timer = 0;
  }

  public void start() {
    previousLoopTime = System.nanoTime();
    previousFrameTime = System.currentTimeMillis();
    timer = System.currentTimeMillis();

    while (this.isRunning.get()) {
      // This game loop is made so that the FPS is capped at targetFps. This is done
      // by updating loopTimeProgress at each loop iteration and if it's greater than
      // 1, that means it's time to update and render the game.
      long currentLoopTime = System.nanoTime();
      loopTimeProgress += (currentLoopTime - previousLoopTime) / nanosecondsPerFrame;
      previousLoopTime = currentLoopTime;

      if (loopTimeProgress >= 1) {
        // Calculate the time since the last frame (deltaTime) and pass it to update so
        // that everything scales accordingly with the FPS.
        long currentFrameTime = System.currentTimeMillis();
        long deltaTime = currentFrameTime - previousFrameTime;
        previousFrameTime = currentFrameTime;

        this.update.accept(deltaTime);
        this.draw.run();

        frameCount++;
        loopTimeProgress--;
      }

      boolean hasOneSecondPassed = System.currentTimeMillis() - timer > Duration.ofSeconds(1).toMillis();
      if (hasOneSecondPassed) {
        if (this.shouldPrintFps) {
          System.out.println(String.format("FPS: %d", frameCount));
        }
        frameCount = 0;
        timer += Duration.ofSeconds(1).toMillis();
      }
    }
    this.onExit.run();
  }
}