package content;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class JsonReader {

    public static final String jsonFileExtension = ".json";

    public static ArrayList<LinkedHashMap<Object, Object>> readAllJSONFiles(Path dir) throws IOException {
        ArrayList<LinkedHashMap<Object, Object>> JSONs = new ArrayList<>();
        Set<String> filenames = FileOperations.listFiles(dir);
        for (String filename : filenames) {
            if (filename.contains(jsonFileExtension)) {
                LinkedHashMap<Object, Object> fileJSON = readJSONFile(FilePaths.JSON_SOURCE.resolve(filename));
                if (fileJSON != null) JSONs.add(fileJSON);
            }
        }
        return JSONs;
    }
    
    public static LinkedHashMap<Object, Object> readJSONFile(Path filepath) {
        ArrayList<String> contents = new ArrayList<String>();
        try {
            File file = filepath.toFile();
            Scanner scanner = new Scanner(file, StandardCharsets.UTF_8.name());
            while (scanner.hasNextLine()) {
                contents.add(scanner.nextLine());
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        // split the contents by JSON object, use nested Lists to represent the JSON structure
        Stack<Integer> stack = new Stack<Integer>();
        Integer[] indices = new Integer[contents.size()];
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i) == null) break;
            if (contents.get(i).contains("{")) stack.push(i);
            if (contents.get(i).contains("}")) {
                try {
                    indices[stack.pop()] = i;
                }
                catch (EmptyStackException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        if (!stack.isEmpty()) {
            new Exception("WARNING: The JSON object is malformed. File: " + filepath.toString()).printStackTrace();
            return null;
        }
        if (contents.size() == 0) return null;
        return readJSONObject(contents.subList(1, contents.size()-1));
    }

    public static LinkedHashMap<Object, Object> readJSONObject(List<String> contents) {
        LinkedHashMap<Object, Object> object = new LinkedHashMap<Object, Object>();
        for (int i = 0; i < contents.size(); i++) {
            String line = contents.get(i);
            if (line.trim().isEmpty()) continue; // Skip empty lines
            String key = line.split(":")[0].trim().replace("\"", "");
            if (key.equals("")) continue; // Skip empty keys: invalid entry format
            String value = "";
            try {
                int colonIndex = -1;
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == ':' && !StringOperations.isInString(line, j)) {
                        colonIndex = j;
                        break;
                    }
                }
                if (colonIndex != -1) {
                    value = line.substring(colonIndex + 1).trim();
                }
                else {
                    value = "";
                }
                if (StringOperations.containsUnquotedChar(value, '[')) { // the value is a list
                    List<String> list = new ArrayList<String>(); 
                    for (String entry : value.replaceAll("\\[|\\]", "").split(",")) {
                        list.add(entry.replace("\"","").trim());
                    }
                    value = list.toString();
                }
                else {
                    for (int j = 0; j < value.length(); j++) {
                        if (value.charAt(j) == ',' && !StringOperations.isInString(value, j)) {
                            value = value.substring(0, j) + value.substring(j + 1, value.length());
                        }
                    }
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                // do nothing - this is expected at the end of a JSON object
            }
            if (StringOperations.containsUnquotedChar(line, '{') && !StringOperations.containsUnquotedChar(line, '}')) { // start of an object
                // jump over recursively-read lines to start of next object
                int braceCount = 1, startIndex = i + 1;
                while (braceCount > 0 && i < contents.size() - 1) {
                    i++;
                    String currentLine = contents.get(i);
                    if (StringOperations.containsUnquotedChar(currentLine, '{')) braceCount++;
                    if (StringOperations.containsUnquotedChar(currentLine, '}')) braceCount--;
                }
                // read the object
                object.put(key, readJSONObject(contents.subList(startIndex, contents.size())));
            }
            else if (!StringOperations.containsUnquotedChar(line, '{') && StringOperations.containsUnquotedChar(line, '}')) { // end of an object
                // end the object
                return object;
            }
            else if (StringOperations.containsUnquotedChar(line, '{') && StringOperations.containsUnquotedChar(line, '}')) { // object all on one line
                String objectContent = line.substring(line.indexOf("{") + 1, line.indexOf("}")).trim();
                LinkedHashMap<Object, Object> innerObject = new LinkedHashMap<>();
                if (!objectContent.isEmpty()) {
                    String[] pairs = StringOperations.splitByUnquotedString(objectContent, ",");
                    for (String pair : pairs) {
                        String[] keyValue = pair.split(":");
                        if (keyValue.length == 2) {
                            String innerKey = keyValue[0].trim().replace("\"", "");
                            String innerValue = keyValue[1].trim().replace("\"", "");
                            innerObject.put(innerKey, innerValue);
                        }
                    }
                }
                object.put(key, innerObject);
            }
            else{
                object.put(key, value.trim().replace("\"", ""));
            }
        }
        return object;
    }
}
