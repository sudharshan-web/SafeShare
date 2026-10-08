# ShareSafe

ShareSafe is a simple Java Swing desktop prototype for demonstrating how potentially sensitive information in an image can be protected before sharing.

## Project idea

Sensitive information can accidentally appear in screenshots, forms, documents, or photographs. ShareSafe demonstrates a simple workflow:

**Select image → review → select sensitive region → redact → preview → save a new copy**

This is a **college-level prototype**, not a production security product. It uses local Java image processing and simple regular-expression pattern detection. It does not use AI, OCR, a database, a server, or the internet.

## Technologies

- Java 17 or newer
- Java Swing
- `BufferedImage`
- `Graphics2D`
- `ImageIO`
- `JFileChooser`
- Java regular expressions

No external libraries are required.

## Project structure

```text
ShareSafe/
├── src/
│   ├── Main.java
│   ├── MainFrame.java
│   ├── ImagePanel.java
│   ├── ImageProcessor.java
│   ├── TextDetector.java
│   └── RedactionManager.java
└── README.md
```

## Run from terminal

Open a terminal inside the `ShareSafe` project folder.

### Windows PowerShell / Command Prompt

```text
cd ShareSafe
javac -d out src\*.java
java -cp out Main
```

### Linux / macOS

```text
cd ShareSafe
javac -d out src/*.java
java -cp out Main
```

The application opens with the ShareSafe home screen.

## Run in IntelliJ IDEA

1. Open the `ShareSafe` folder.
2. Open `src/Main.java`.
3. Make sure the project SDK is Java 17 or newer.
4. Click the green Run button beside `main`.
5. The Swing window opens.

No Maven or Gradle setup is necessary.

## Run in Eclipse

1. Create/open a Java project.
2. Add the files from `src` to the source folder.
3. Set the JRE to Java 17 or newer.
4. Run `Main.java` as a Java Application.

## Run in VS Code

1. Install a Java 17+ JDK.
2. Open the `ShareSafe` folder.
3. Open `src/Main.java`.
4. Use the Run Java option above the `main` method.

## Demonstration

Use a fake/synthetic image containing information such as:

```text
SAMPLE DOCUMENT

Name: John Kumar
Phone: 9876543210
Email: john@example.com
ID: 1234 5678 9012
```

Suggested 3–5 minute demonstration:

1. Start ShareSafe.
2. Click **SELECT IMAGE**.
3. Select a JPG or PNG sample image.
4. Review the displayed image.
5. Drag the mouse over the fake phone/email/ID area.
6. Click **REDACT**.
7. Show that the selected area is covered with a black rectangle.
8. Click **RESET** to demonstrate restoration of the original in memory.
9. Redo the redaction if needed.
10. Click **SAVE REDACTED IMAGE**.
11. Save the result with a new filename such as `sample_redacted.png`.
12. Explain that the original file is never overwritten automatically.

## Important limitation

Standard Java image libraries do not provide OCR. Therefore this simple prototype does **not** read arbitrary text from pixels. The pattern detector is included as a demonstration of how phone numbers, emails, and ID-like numbers can be recognized from text when text is available. The actual image protection in this prototype is manual mouse-based region selection.

This limitation is intentional because the project is designed to stay simple, offline, dependency-free, and easy to explain in a college viva.

## Core classes

### `Main.java`
Starts the Swing application on the Event Dispatch Thread.

### `MainFrame.java`
Creates the GUI, handles image selection, redaction, reset, save, and error messages.

### `ImagePanel.java`
Displays the image and handles mouse drag selection. It converts the displayed selection back to original image coordinates.

### `ImageProcessor.java`
Loads images, creates copies, resizes images for display, and saves PNG/JPG files.

### `TextDetector.java`
Uses Java regular expressions to identify potential phone numbers, email addresses, and ID-like number groups.

### `RedactionManager.java`
Uses `Graphics2D` to draw a black rectangle over a selected region.

## Privacy concept

All image processing happens locally on the computer. The application does not upload images, use cloud services, or require an internet connection.

The application should be described as detecting **potential sensitive information**, not guaranteeing that every sensitive item is found.

## Testing checklist

- [ ] Application launches.
- [ ] `Main.java` contains `public static void main(String[] args)`.
- [ ] JPG image can be selected.
- [ ] JPEG image can be selected.
- [ ] PNG image can be selected.
- [ ] Image appears in the preview.
- [ ] Mouse drag creates a selection rectangle.
- [ ] REDACT covers the selected region.
- [ ] RESET restores the original image in memory.
- [ ] SAVE REDACTED IMAGE creates a new file.
- [ ] Original image is not automatically overwritten.
- [ ] Unsupported file types are rejected.
- [ ] Invalid image files show an error.
- [ ] Trying to redact without a selection shows an error.
- [ ] Trying to save without an image shows an error.
- [ ] The project runs without internet.
- [ ] No server, database, API key, Python, Node.js, or external dependency is required.

## Features demonstrated

- Local image selection
- Image preview
- Basic potential-sensitive-data pattern detection
- Manual region selection
- Black-box redaction
- Reset
- Save-as-new-file
- Basic error handling
- Offline processing

## Future enhancements

These are **not implemented** in this prototype:

- OCR-based text detection
- PDF support
- Face detection
- QR-code detection
- More advanced sensitive-data detection
- Encryption
- Mobile application
- AI-based detection

## Viva explanation

> ShareSafe is a Java desktop application that helps users identify potential sensitive information in images and hide selected information before sharing the image. The prototype uses simple pattern matching and manual redaction, and all processing happens locally.

The main image-processing idea is simple: the original image is copied into memory, the user selects a rectangle, and `Graphics2D.fillRect()` paints that region black. The modified copy is then saved as a separate image file.
