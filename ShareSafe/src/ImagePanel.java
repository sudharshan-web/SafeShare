import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private Rectangle selection;
    private int startX;
    private int startY;
    private boolean dragging;

    public ImagePanel() {
        setBackground(new Color(242, 244, 247));
        setPreferredSize(new Dimension(760, 470));

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (image == null || !isInsideImage(e.getX(), e.getY())) return;
                startX = e.getX();
                startY = e.getY();
                selection = new Rectangle(startX, startY, 0, 0);
                dragging = true;
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragging) return;
                int x = clamp(e.getX(), imageX(), imageX() + displayWidth());
                int y = clamp(e.getY(), imageY(), imageY() + displayHeight());
                selection = createRectangle(startX, startY, x, y);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!dragging) return;
                dragging = false;
                int x = clamp(e.getX(), imageX(), imageX() + displayWidth());
                int y = clamp(e.getY(), imageY(), imageY() + displayHeight());
                selection = createRectangle(startX, startY, x, y);
                if (selection.width < 5 || selection.height < 5) selection = null;
                repaint();
            }
        };
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.selection = null;
        revalidate();
        repaint();
    }

    public BufferedImage getImage() { return image; }

    public Rectangle getImageSelection() {
        if (selection == null || image == null || selection.width <= 0 || selection.height <= 0) return null;
        double scaleX = (double) image.getWidth() / displayWidth();
        double scaleY = (double) image.getHeight() / displayHeight();
        int x = (int) Math.round((selection.x - imageX()) * scaleX);
        int y = (int) Math.round((selection.y - imageY()) * scaleY);
        int width = (int) Math.round(selection.width * scaleX);
        int height = (int) Math.round(selection.height * scaleY);
        return new Rectangle(x, y, width, height);
    }

    public void clearSelection() {
        selection = null;
        repaint();
    }

    public List<Rectangle> getSelectedRegions() {
        Rectangle region = getImageSelection();
        List<Rectangle> regions = new ArrayList<>();
        if (region != null) regions.add(region);
        return regions;
    }

    private boolean isInsideImage(int x, int y) {
        return x >= imageX() && x <= imageX() + displayWidth()
                && y >= imageY() && y <= imageY() + displayHeight();
    }

    private int imageX() { return (getWidth() - displayWidth()) / 2; }
    private int imageY() { return (getHeight() - displayHeight()) / 2; }

    private int displayWidth() {
        if (image == null) return 0;
        double scale = Math.min((double) Math.max(1, getWidth() - 30) / image.getWidth(),
                (double) Math.max(1, getHeight() - 30) / image.getHeight());
        scale = Math.min(scale, 1.0);
        return Math.max(1, (int) Math.round(image.getWidth() * scale));
    }

    private int displayHeight() {
        if (image == null) return 0;
        double scale = Math.min((double) Math.max(1, getWidth() - 30) / image.getWidth(),
                (double) Math.max(1, getHeight() - 30) / image.getHeight());
        scale = Math.min(scale, 1.0);
        return Math.max(1, (int) Math.round(image.getHeight() * scale));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            g.setColor(Color.GRAY);
            g.drawString("Select an image to begin", Math.max(10, getWidth() / 2 - 70), getHeight() / 2);
            return;
        }

        int width = displayWidth();
        int height = displayHeight();
        int x = imageX();
        int y = imageY();

        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(image, x, y, width, height, null);
        graphics.setColor(Color.DARK_GRAY);
        graphics.drawRect(x, y, width, height);

        if (selection != null) {
            graphics.setColor(new Color(255, 0, 0, 55));
            graphics.fill(selection);
            graphics.setColor(Color.RED);
            graphics.setStroke(new BasicStroke(2));
            graphics.draw(selection);
        }
        graphics.dispose();
    }

    private Rectangle createRectangle(int x1, int y1, int x2, int y2) {
        return new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
