package vacuumCleanerRobot;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Map {
	private static int DIRT = 1;
	private static int SOLID = 2;
	private int[][] pixels;
	private BufferedImage dirtMask;
	private double width;
	private double height;
	
	public Map(double d, double e) {
		this.width=d;
		this.height=e;
		generateDirtMask();
	}
	
	
	private void generateDirtMask() {
        int width = (int) Game.getWindowBounds().getWidth();
        int height = (int) Game.getWindowBounds().getHeight();

        pixels = new int[height][width];
        dirtMask = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Color semiBlack = new Color(0, 0, 0, 100);
        Color black = Color.BLACK;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y][x] = DIRT;
                dirtMask.setRGB(x, y, semiBlack.getRGB());
            }
        }
        
        for(int y = 100; y < 400; y++) {
        	for(int x = 100; x< 400; x++) {
        		pixels[y][x] = SOLID;
                dirtMask.setRGB(x, y, black.getRGB());
        	}
        }
    }
	
	public BufferedImage getDirtMask() {
		return dirtMask;
	}
	
	public void set(Point position, int value) {
		this.pixels[position.y][position.x] = value;
	}
	
	public int[][] get(){
		return pixels;
	}
	

}
