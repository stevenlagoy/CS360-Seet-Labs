package content;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class ContentReader {

    public static void main(String[] args) {
        try {
            Set<LinkedHashMap<Object, Object>> filesContents = readAllJSONFiles(contentJSONSource);
            for (LinkedHashMap<Object, Object> module : filesContents) {
                createHTMLs(module);
            }

            createDBFile(determineStructure(filesContents));
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static final String contentJSONSource = "content/";
    public static final String contentHTMLDestination = "seetLabs/Data/";
    public static final String javaBaseDestination = "seetLabs/Data/";
    public static final String unitTestDestination = "seetLabs/Data/";

    public static class ScannerUtil {
        public Scanner createScanner(InputStream inputStream) {
            return new Scanner(inputStream, StandardCharsets.UTF_8.name());
        }
        public Scanner createScanner(File file) throws FileNotFoundException {
            return new Scanner(file, StandardCharsets.UTF_8.name());
        }
    }

    static ScannerUtil scannerUtil = new ScannerUtil();
    static Scanner stdin = scannerUtil.createScanner(System.in);

    public static Set<LinkedHashMap<Object, Object>> readAllJSONFiles(String dir) throws IOException {
        Set<LinkedHashMap<Object, Object>> JSONs = new HashSet<>();
        Set<String> filenames = listFiles(dir);
        for (String filename : filenames) {
            if (filename.contains(".json")) {
                LinkedHashMap<Object, Object> fileJSON = readJSONFile(contentJSONSource + filename);
                if (fileJSON != null) JSONs.add(fileJSON);
            }
        }
        return JSONs;
    }
    
    public static Set<String> listFiles(String dir) throws IOException {
        Set<String> fileSet = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileSet.add(path.getFileName().toString());
                }
            }
            return fileSet;
        }
    }

    public static LinkedHashMap<Object, Object> readJSONFile(String filename) {
        ArrayList<String> contents = new ArrayList<String>();
        try {
            File file = new File(filename);
            Scanner scanner = scannerUtil.createScanner(file);
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
            new Exception("The JSON object is malformed.").printStackTrace();
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
                    if (line.charAt(j) == ':' && !isInString(line, j)) {
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
                if (containsUnquotedChar(value, '[')) { // the value is a list
                    List<String> list = new ArrayList<String>(); 
                    for (String entry : value.replaceAll("\\[|\\]", "").split(",")) {
                        list.add(entry.replace("\"","").trim());
                    }
                    value = list.toString();
                }
                else {
                    for (int j = 0; j < value.length(); j++) {
                        if (value.charAt(j) == ',' && !isInString(value, j)) {
                            value = value.substring(0, j) + value.substring(j + 1, value.length());
                        }
                    }
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                // do nothing - this is expected at the end of a JSON object
            }
            if (containsUnquotedChar(line, '{') && !containsUnquotedChar(line, '}')) { // start of an object
                // jump over recursively-read lines to start of next object
                int braceCount = 1, startIndex = i + 1;
                while (braceCount > 0 && i < contents.size() - 1) {
                    i++;
                    String currentLine = contents.get(i);
                    if (containsUnquotedChar(currentLine, '{')) braceCount++;
                    if (containsUnquotedChar(currentLine, '}')) braceCount--;
                }
                // read the object
                object.put(key, readJSONObject(contents.subList(startIndex, contents.size())));
            }
            else if (!containsUnquotedChar(line, '{') && containsUnquotedChar(line, '}')) { // end of an object
                // end the object
                return object;
            }
            else if (containsUnquotedChar(line, '{') && containsUnquotedChar(line, '}')) { // object all on one line
                String objectContent = line.substring(line.indexOf("{") + 1, line.indexOf("}")).trim();
                LinkedHashMap<Object, Object> innerObject = new LinkedHashMap<>();
                if (!objectContent.isEmpty()) {
                    String[] pairs = objectContent.split(",");
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
                return object;
            }
            else{
                object.put(key, value.trim().replace("\"", ""));
            }
        }
        return object;
    }

    private static boolean isInString(String line, int position) {
        boolean inString = false;
        for (int i = 0; i < position; i++) {
            if (line.charAt(i) == '"' && (i == 0 || line.charAt(i-1) != '\\')) {
                inString = !inString;
            }
        }
        return inString;
    }
    
    private static boolean containsUnquotedChar(String line, char target) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == target && !isInString(line, i)) {
                return true;
            }
        }
        return false;
    }

    public static void createHTMLs(LinkedHashMap<Object, Object> JSON){
        if (JSON.size() <= 1) return;
        for (int i = 1; i < Integer.parseInt(JSON.get("activity_count").toString()); i++){
            @SuppressWarnings("unchecked")
            LinkedHashMap<Object, Object> activityContent = (LinkedHashMap<Object, Object>) ((LinkedHashMap<Object, Object>) JSON.get("content")).get("activity" + i);
            if (activityContent != null)
                createHTML(activityContent);
        }
    }

    public static void createHTML(LinkedHashMap<Object, Object> JSON) {
        List<String> htmlContent = JSONtoHTML(JSON);
        File htmlFile = new File(contentHTMLDestination + JSON.get("id") + ".html");
        try {
            htmlFile.createNewFile();
            try (FileWriter writer = new FileWriter(htmlFile, false)) {
                // Automatically clear the file

                for (String line : htmlContent) {
                    writer.write(line + "\n");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public static final Map<String, String> openingTags = Map.of(
        "t", "<title>",
        "h1", "<h1>",
        "p", "<p>"
    );
    public static final Map<String, String> closingTags = Map.of(
        "t", "</title>",
        "h1", "</h1>",
        "p", "</p>"
    );
    
    private static class Markup {
        public String startTag;
        public String startReplacement;
        public String endTag;
        public String endReplacement;
        public Markup(String tag, String replacement) {
            this(tag, replacement, null, null);
        }
        public Markup(String startTag, String startReplacement, String endTag, String endReplacement) {
            this.startTag = startTag;
            this.startReplacement = startReplacement;
            this.endTag = endTag;
            this.endReplacement = endReplacement;
        }
    }

    // Markup Tags have a start and end tag and provide a modification to whatever lies between those tags. Tags must start with % followed by a unique set of characters.
    // Praise be to Object Oriented Programming
    public static final List<Markup> markupTags = new ArrayList<Markup>() {{
        add (new Markup("%%", "%"));
        add (new Markup("%n", "<br>"));
        add (new Markup("%t", "&#9;"));
        add (new Markup("%'", "\""));
        add (new Markup("%i", "<i>", "%/i", "</i>"));
        add (new Markup("%k", "<span class=\"keyword\">", "%/k", "</span>"));
        add (new Markup("%c", "</p>\n\t<pre><code class=\"language-java\">", "%/c", "</code></pre>\n\t<p>"));
        add (new Markup("%b", "<b>", "%/b", "</b>"));
        add (new Markup("%l", "<a href=\"https://www.youtube.com/watch?v=dQw4w9WgXcQ\">", "%/l", "</a>"));
    }};
    public static Markup findMarkupByTag(String tag) {
        for (Markup markup : markupTags) {
            if (markup.startTag.equals(tag)) return markup;
        }
        return null;
    }

    public static List<String> JSONtoHTML(LinkedHashMap<Object, Object> JSON) {

        List<String> html = new ArrayList<>();

        if (JSON.get("type").toString().equals("reading_activity")) {

            // Create HTML setup and Head section
            html.add("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n\t<meta charset=\"UTF-8\">\n\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            html.add("\t<title>" + JSON.get("name") + "</title>");
            html.add("\t<link rel=\"stylesheet\" href=\"style.css\">");
            html.add("</head>");
            
            // Create Body section
            html.add("<body>");

            @SuppressWarnings("unchecked")
            HashMap<Object, Object> contents = (HashMap<Object, Object>) JSON.get("content");

            for (Object content : contents.keySet()) {
                try {
                    String contentLabel = content.toString().split("^\\d+")[1];
                    if (contentLabel.equals("t")) {
                        // Title
                        html.add("\t" + openingTags.get("t") + contents.get(content) + closingTags.get("t"));
                    }
                    else if (contentLabel.equals("h1")) {
                        // Header 1
                        html.add("\t" + openingTags.get("h1") + contents.get(content) + closingTags.get("h1"));
                    }
                    else if (contentLabel.equals("p")) {
                        // Paragraph
                        String text = contents.get(content).toString();
                        String processed = processHTMLMarkup(text, 0);
                        html.add("\t" + openingTags.get("p") + processed + closingTags.get("p"));
                    }
                }
                catch (IndexOutOfBoundsException e) {
                    continue; // expected when there is no content label
                }
            }

            html.add("</body>");
            html.add("</html>");

            return html;
        } // end of Reading Activity

        if (JSON.get("type").toString().equals("coding_activity")) {
            
            // CREATE HTML FOR INSTRUCTIONS / CONTEXT
            html.add("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n\t<meta charset=\"UTF-8\">\n\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            html.add("\t<title>" + JSON.get("name") + "</title>");
            html.add("\t<link rel=\"stylesheet\" href=\"style.css\">");
            html.add("</head>");
            
            // Create Body section
            html.add("<body>");

            @SuppressWarnings("unchecked")
            HashMap<Object, Object> contents = (HashMap<Object, Object>) ((HashMap<Object, Object>) JSON.get("content")).get("context");
            if (contents != null) {
                for (Object content : contents.keySet()) {
                    try {
                        String contentLabel = content.toString().split("^\\d+")[1];
                        if (contentLabel.equals("t")) {
                            // Title
                            html.add("\t" + openingTags.get("t") + contents.get(content) + closingTags.get("t"));
                        }
                        else if (contentLabel.equals("h1")) {
                            // Header 1
                            html.add("\t" + openingTags.get("h1") + contents.get(content) + closingTags.get("h1"));
                        }
                        else if (contentLabel.equals("p")) {
                            // Paragraph
                            String text = contents.get(content).toString();
                            String processed = processHTMLMarkup(text, 0);
                            html.add("\t" + openingTags.get("p") + processed + closingTags.get("p"));
                        }
                    }
                    catch (IndexOutOfBoundsException e) {
                        continue; // expected when there is not content label
                    }
                }
            }
            html.add("</body>");
            html.add("</html>");

            // CREATE JAVA FILE FOR BASE CODE

            try {
                String baseCode = (((HashMap<Object, Object>) JSON.get("content")).get("base_code")).toString();
                String processed = processJavaMarkup(baseCode, 0);
                createJavaFile(JSON.get("id") + "_basecode", processed);
            }
            catch (NullPointerException e) {
            }


            // create test cases file in a common useful format
            // two types: console out, unit tests - identified by a flag in the file
            // the user will write a function or class based on the instructions. the test code will use that function or class, by name as a black box, and check the output
        } // end of Coding Activity

        return html;
    }

    private static void createJavaFile(String filename, String content) {
        File javaFile = new File(javaBaseDestination + filename + ".java");
        try {
            javaFile.createNewFile();
            try (FileWriter writer = new FileWriter(javaFile, false)) {
                writer.write(content);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processJavaMarkup(String text, int startIndex) {
        StringBuilder result = new StringBuilder();

        for (int i = startIndex; i < text.length(); i++) {
            if (text.charAt(i) == '%') {
                if (i + 1 >= text.length()) {
                    result.append('%');
                    continue;
                }
                char markupChar = text.charAt(i+1);
                if (markupChar == 'n') {
                    result.append('\n');
                    i++;
                }
                else if (markupChar == 't') {
                    result.append('\t');
                    i++;
                }
            }
            else result.append(text.charAt(i));
        }

        return result.toString();
    }

    private static String processHTMLMarkup(String text, int startIndex) {
        StringBuilder result = new StringBuilder();
    
        for (int i = startIndex; i < text.length(); i++) {
            if (text.charAt(i) == '%') {
                if (i + 1 >= text.length()) {
                    result.append('%');
                    continue;
                }
                
                // Handle end tags
                if (text.charAt(i + 1) == '/') {
                    return result.toString() + "§" + i; // Use § as delimiter for returning position
                }
                
                // Get markup tag
                Markup markup = findMarkupByTag(text.substring(i, i + 2));
                if (markup == null) {
                    System.out.println("WARNING: Unrecognized markup " + text.substring(i, i + 2) + " in ..." + text.substring(Integer.max(0, i - 10), Integer.min(text.length(), i + 10)) + "...");
                    result.append(text.charAt(i));
                    continue;
                }
                
                // Handle single replacement tags
                if (markup.endReplacement == null) {
                    result.append(markup.startReplacement);
                    i++;
                    continue;
                }
                
                // Handle paired tags
                i += 2; // Skip the opening tag
                String processed = processHTMLMarkup(text, i);
                if (processed == null) {
                    System.out.println("WARNING: Unclosed tag at position " + i);
                    continue;
                }
                
                // Split position from content
                String[] parts = processed.split("§");
                String content = parts[0];
                i = Integer.parseInt(parts[1]); // Update position to end of nested content
                
                result.append(markup.startReplacement)
                    .append(content)
                    .append(markup.endReplacement);
                
                i += 2; // Skip the closing tag
            }
            else { // regular text
                result.append(text.charAt(i));
            }
        }
        
        return result.toString();
    }

    public static HashMap<Integer, HashMap<Integer, Integer>> determineStructure(Set<LinkedHashMap<Object, Object>> contents) {
        // 0 is reading, 1 is quiz, 2 is coding
        
        HashMap<Integer, HashMap<Integer, Integer>> result = new HashMap<Integer, HashMap<Integer, Integer>>();

        for (LinkedHashMap<Object, Object> module : contents) {
            LinkedHashMap<Object, Object> moduleContents = (LinkedHashMap<Object, Object>) module.get("content");
            HashMap<Integer, Integer> activityStructure = new HashMap<>();
            for (Object activity : moduleContents.keySet()) {
                HashMap<Object, Object> activityContents = (HashMap<Object, Object>) moduleContents.get(activity);
                int activityNumber = Integer.parseInt(activityContents.get("id").toString().split("-")[1]);
                String type = activityContents.get("type").toString();
                int activityType = type.equals("reading_activity") ? 0 : type.equals("quiz_activity") ? 1 : type.equals("coding_activity") ? 2 : -1;
                activityStructure.put(activityNumber, activityType);
                System.out.println(activityNumber + " " + activityType);
            }
            result.put(Integer.parseInt(module.get("number").toString()), activityStructure);
        }

        return result;
    }

    public static void createDBFile(HashMap<Integer, HashMap<Integer, Integer>> ids) {
        // 0 is reading, 1 is quiz, 2 is coding

        /* like this:
        "1": [
            {"id": "1", "type": 0, "file": "readingOne.html"},
            {"id": "2", "type": 0, "file": "readingTwo.html"}
        ],
        "2": [
            {"id": "1", "type": 1, "file": "Quiz Material One"},
            {"id": "2", "type": 1, "file": "Quiz Material Two"}
        ] 
        */

        StringBuilder result = new StringBuilder();

        result.append("{\n");
        for (Integer module : ids.keySet()) {
            if (result.charAt(result.length() - 1) == ']') result.append(",\n");
            result.append("\t\"").append(module.toString()).append("\" : [\n");
            for (Integer activity : ids.get(module).keySet()) {
                if (result.charAt(result.length() - 1) == '}') result.append(",\n\t");
                else result.append("\t");
                result.append("\t{\"id\" : \"");
                result.append(activity.toString());
                result.append("\", \"type\" : \"");
                result.append(ids.get(module).get(activity));
                result.append("\", \"file\" : \"");
                result.append(String.format("%s-%s.html", module.toString(), activity.toString()));
                result.append("\"}");
            }
            result.append("\n\t]");
        }
        result.append("\n}");
        try {
            File dbFile = new File(contentHTMLDestination + "db.json");
            dbFile.createNewFile();
            try (FileWriter writer = new FileWriter(dbFile, false)) {
                writer.write(result.toString());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
