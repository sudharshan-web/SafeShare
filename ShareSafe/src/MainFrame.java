import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainFrame extends JFrame {
    private final ImagePanel imagePanel = new ImagePanel();
    private final JLabel fileLabel = new JLabel("No image selected");
    private final JLabel statusLabel = new JLabel("Your image is processed locally on this computer.");
    private final JTextArea detectionInput = new JTextArea(3, 30);
    private final JTextArea detectionArea = new JTextArea(4, 30);
    private final JPanel detectionPanel = new JPanel(new BorderLayout(5, 5));
    private final JButton redactButton = new JButton("REDACT");
    private final JButton resetButton = new JButton("RESET");
    private final JButton saveButton = new JButton("SAVE REDACTED IMAGE");
    private final JLabel stateLabel = new JLabel("Select a region with the mouse, then click REDACT.");

    private BufferedImage originalImage;
    private BufferedImage workingImage;
    private File originalFile;

    public MainFrame() {
        setTitle("ShareSafe - Image Privacy Prototype");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 700));
        setSize(1000, 760);
        setLocationRelativeTo(null);
        buildHome();
    }

    private void buildHome() {
        getContentPane().removeAll();
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        JPanel center = new JPanel(new GridLayout(1, 1));
        center.setBackground(Color.WHITE);
        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setLayout(new javax.swing.BoxLayout(content, javax.swing.BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("ShareSafe", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 38));
        title.setForeground(new Color(25, 55, 90));
        title.setAlignmentX(0.5f);
        content.add(title);

        JLabel subtitle = new JLabel("Protect sensitive information before sharing an image.", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitle.setAlignmentX(0.5f);
        content.add(subtitle);

        JLabel local = new JLabel("Offline desktop prototype • Local processing only", SwingConstants.CENTER);
        local.setForeground(new Color(80, 90, 100));
        local.setAlignmentX(0.5f);
        content.add(local);
        content.add(BoxPanel.space(35));

        JButton selectButton = createPrimaryButton("SELECT IMAGE");
        selectButton.addActionListener(e -> chooseImage());
        selectButton.setAlignmentX(0.5f);
        content.add(selectButton);
        content.add(BoxPanel.space(20));
        statusLabel.setAlignmentX(0.5f);
        content.add(statusLabel);

        center.add(content);
        root.add(center, BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);
        setContentPane(root);
        revalidate();
        repaint();
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(245, 247, 250));
        JLabel label = new JLabel("ShareSafe is a college project prototype. Detection is pattern-based and not guaranteed.");
        label.setForeground(new Color(90, 90, 90));
        footer.add(label);
        return footer;
    }

    private void buildReview() {
        getContentPane().removeAll();
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        root.setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        JLabel title = new JLabel("Review Image");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(25, 55, 90));
        header.add(title, BorderLayout.WEST);
        header.add(fileLabel, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        root.add(imagePanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setBackground(Color.WHITE);
        bottom.add(createDetectionPanel(), BorderLayout.CENTER);
        stateLabel.setForeground(new Color(70, 80, 90));
        bottom.add(stateLabel, BorderLayout.NORTH);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);
        redactButton.addActionListener(e -> redactSelection());
        resetButton.addActionListener(e -> resetImage());
        saveButton.addActionListener(e -> saveImage());
        actions.add(redactButton);
        actions.add(resetButton);
        actions.add(saveButton);
        bottom.add(actions, BorderLayout.SOUTH);

        root.add(bottom, BorderLayout.SOUTH);
        setContentPane(root);
        revalidate();
        repaint();
    }

    private JPanel createDetectionPanel() {
        detectionPanel.removeAll();
        detectionPanel.setBackground(new Color(248, 249, 251));
        detectionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        JPanel content = new JPanel(new BorderLayout(5, 5));
        content.setOpaque(false);

        JLabel heading = new JLabel("Potential Sensitive Information");
        heading.setFont(new Font("SansSerif", Font.BOLD, 15));
        content.add(heading, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(5, 5));
        center.setOpaque(false);
        JLabel inputLabel = new JLabel("Optional demo text to scan:");
        center.add(inputLabel, BorderLayout.NORTH);

        detectionInput.setLineWrap(true);
        detectionInput.setWrapStyleWord(true);
        detectionInput.setText("Phone: 9876543210\nEmail: john@example.com\nID: 1234 5678 9012");
        center.add(new JScrollPane(detectionInput), BorderLayout.CENTER);

        JButton detectButton = new JButton("DETECT PATTERNS");
        detectButton.addActionListener(e -> detectInformation(detectionInput.getText()));
        center.add(detectButton, BorderLayout.SOUTH);
        content.add(center, BorderLayout.CENTER);

        JPanel result = new JPanel(new BorderLayout(5, 5));
        result.setOpaque(false);
        result.add(new JLabel("Detected information:"), BorderLayout.NORTH);
        detectionArea.setEditable(false);
        detectionArea.setLineWrap(true);
        detectionArea.setWrapStyleWord(true);
        detectionArea.setBackground(Color.WHITE);
        detectionArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        result.add(new JScrollPane(detectionArea), BorderLayout.CENTER);
        content.add(result, BorderLayout.SOUTH);

        detectionPanel.add(content, BorderLayout.CENTER);
        return detectionPanel;
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select an image");
        chooser.setFileFilter(new FileNameExtensionFilter("Image files (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png"));

        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        if (!isSupported(file)) {
            showError("Unsupported image format.");
            return;
        }

        try {
            originalImage = ImageProcessor.loadImage(file);
            workingImage = ImageProcessor.copyImage(originalImage);
            originalFile = file;
            fileLabel.setText(file.getName());
            imagePanel.setImage(workingImage);
            buildReview();
            detectInformation(detectionInput.getText());
        } catch (IOException ex) {
            showError("Unable to load the selected image.");
        }
    }

    private void detectInformation(String text) {
        List<TextDetector.Detection> detections = TextDetector.detect(text == null ? "" : text);
        if (detections.isEmpty()) {
            detectionArea.setText("No potential sensitive information detected.\n\n" +
                    "Detection is pattern-based only. Always review the image manually.");
        } else {
            StringBuilder builder = new StringBuilder();
            for (TextDetector.Detection detection : detections) {
                builder.append("• ").append(detection).append('\n');
            }
            detectionArea.setText(builder.toString());
        }
    }

    private void redactSelection() {
        if (workingImage == null) {
            showError("Please select an image first.");
            return;
        }
        List<Rectangle> regions = imagePanel.getSelectedRegions();
        if (regions.isEmpty()) {
            showError("Please select a region on the image first.");
            return;
        }

        RedactionManager.redact(workingImage, regions);
        imagePanel.setImage(workingImage);
        stateLabel.setText("✓ Redaction applied. The original image is unchanged.");
        showInfo("The selected region has been redacted with a black rectangle.");
    }

    private void resetImage() {
        if (originalImage == null) {
            showError("Please select an image first.");
            return;
        }
        workingImage = ImageProcessor.copyImage(originalImage);
        imagePanel.setImage(workingImage);
        stateLabel.setText("Image reset to the original version.");
    }

    private void saveImage() {
        if (workingImage == null) {
            showError("Please select an image first.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save redacted image");
        chooser.setFileFilter(new FileNameExtensionFilter("PNG image (*.png)", "png"));
        String baseName = originalFile == null ? "redacted_image" : removeExtension(originalFile.getName()) + "_redacted";
        chooser.setSelectedFile(new File(baseName + ".png"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".png")) {
            file = new File(file.getAbsolutePath() + ".png");
        }

        try {
            if (file.exists()) {
                int answer = JOptionPane.showConfirmDialog(this, "That file already exists. Overwrite it?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (answer != JOptionPane.YES_OPTION) return;
            }
            if (!ImageProcessor.saveImage(workingImage, file)) {
                throw new IOException("Image format could not be written.");
            }
            showInfo("Redacted image saved successfully:\n" + file.getAbsolutePath());
        } catch (IOException ex) {
            showError("Unable to save the image.");
        }
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setMargin(new Insets(12, 28, 12, 28));
        return button;
    }

    private boolean isSupported(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
    }

    private String removeExtension(String name) {
        int dot = name.lastIndexOf('.');
        return dot > 0 ? name.substring(0, dot) : name;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "ShareSafe", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "ShareSafe", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class BoxPanel {
        static JPanel space(int height) {
            JPanel panel = new JPanel();
            panel.setOpaque(false);
            panel.setPreferredSize(new Dimension(10, height));
            return panel;
        }
    }

}