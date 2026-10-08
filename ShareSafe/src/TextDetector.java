import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextDetector {
    public static class Detection {
        private final String type;
        private final String value;

        public Detection(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String getType() { return type; }
        public String getValue() { return value; }

        @Override
        public String toString() {
            return type + ": " + value;
        }
    }

    private static final Pattern PHONE = Pattern.compile("(?<!\\d)(?:[6-9]\\d{9})(?!\\d)");
    private static final Pattern EMAIL = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    private static final Pattern ID_NUMBER = Pattern.compile("(?<!\\d)\\d{4}(?:[ -]\\d{4}){2}(?!\\d)");

    private TextDetector() { }

    public static List<Detection> detect(String text) {
        List<Detection> results = new ArrayList<>();
        findMatches(PHONE, text, "Potential phone number", results);
        findMatches(EMAIL, text, "Potential email address", results);
        findMatches(ID_NUMBER, text, "Potential ID-like number", results);
        return results;
    }

    private static void findMatches(Pattern pattern, String text, String type, List<Detection> results) {
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            results.add(new Detection(type, matcher.group()));
        }
    }
}
