import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class RedactionManager {
    private RedactionManager() { }

    public static void redact(BufferedImage image, List<Rectangle> rectangles) {
        if (image == null || rectangles == null || rectangles.isEmpty()) return;

        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.BLACK);
        for (Rectangle rectangle : rectangles) {
            Rectangle clipped = rectangle.intersection(new Rectangle(0, 0, image.getWidth(), image.getHeight()));
            if (!clipped.isEmpty()) {
                graphics.fillRect(clipped.x, clipped.y, clipped.width, clipped.height);
            }
        }
        graphics.dispose();
    }
}
