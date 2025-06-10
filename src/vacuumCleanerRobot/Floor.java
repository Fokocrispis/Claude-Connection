package vacuumCleanerRobot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import vacuumCleanerRobot.Game;
import vacuumCleanerRobot.GameObject;
import vacuumCleanerRobot.Map;

public class Floor implements GameObject {

    private BufferedImage texture;
    private BufferedImage dirtMask;
    private int[][] pixels;
    private TestObject to;
    private Map map;

    public Floor(Map map, TestObject to) throws IOException {
        //this.texture = ImageIO.read(new File(".//src//resources//Floor.jpg"));
    	this.texture = ImageIO.read(new File("resources/Floor.jpg"));
        this.map=map;
        this.to=to;
        this.pixels = map.get();
    }

    public void clean(int x, int y) {
        if (x >= 0 && y >= 0 && y < pixels.length && x < pixels[0].length) {
            if (pixels[y][x] == 1) {
                map.set(new Point(x,y), 0);
                map.getDirtMask().setRGB(x, y, new Color(0, 0, 0, 0).getRGB()); // Transparent pixel
            }
            
        }
    }

    public void cleanCircle(int centerX, int centerY, int radius) {
        int minX = Math.max(0, centerX - radius);
        int maxX = Math.min(pixels[0].length - 1, centerX + radius);
        int minY = Math.max(0, centerY - radius);
        int maxY = Math.min(pixels.length - 1, centerY + radius);

        int rSq = radius * radius;
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                int dx = x - centerX;
                int dy = y - centerY;
                if (dx * dx + dy * dy <= rSq) {
                    clean(x, y);
                }
            }
        }
    }

    @Override
    public void update(long deltaTime) {
        int centerX = to.getPosition().x + to.getDimension().width / 2;
        int centerY = to.getPosition().y + to.getDimension().height / 2;
        int radius = Math.min(to.getDimension().width, to.getDimension().height) / 2;

        cleanCircle(centerX, centerY, radius);
    }

    @Override
    public void draw(Graphics2D graphics2d) {
        graphics2d.drawImage(texture, 0, 0,
            (int) Game.getWindowBounds().getWidth(),
            (int) Game.getWindowBounds().getHeight(), null);

        graphics2d.drawImage(map.getDirtMask(), 0, 0, null);
    }
}
