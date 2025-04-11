package content;

import java.util.ArrayList;
import java.util.List;

public class StringOperations {
    
    public static boolean isInString(String line, int position) {
        boolean inString = false;
        for (int i = 0; i < position; i++) {
            if (line.charAt(i) == '"' && (i == 0 || line.charAt(i-1) != '\\')) {
                inString = !inString;
            }
        }
        return inString;
    }
    
    public static boolean containsUnquotedChar(String line, char target) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == target && !isInString(line, i)) {
                return true;
            }
        }
        return false;
    }

    public static String[] splitByUnquotedString(String string, String separator) {
        List<String> parts = new ArrayList<>();
        int lastSplitIndex = 0;
        
        for (int i = 0; i <= string.length() - separator.length(); i++) {
            // Check if this position contains the separator
            if (string.startsWith(separator, i)) {
                // Verify the separator is not in quotes
                if (!isInString(string, i)) {
                    // Add the part before this separator
                    parts.add(string.substring(lastSplitIndex, i).trim());
                    lastSplitIndex = i + separator.length();
                    i += separator.length() - 1; // Skip the rest of the separator
                }
            }
        }
        
        // Add the remaining part after the last separator
        if (lastSplitIndex <= string.length()) {
            parts.add(string.substring(lastSplitIndex).trim());
        }
        
        return parts.toArray(new String[0]);
    }
}