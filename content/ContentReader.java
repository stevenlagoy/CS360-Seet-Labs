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
import java.util.Collections;
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

    public static final Path BASE_PATH = Path.of("seetLabs");
    public static final Path contentJSONSource = Path.of("content");
    public static final Path DATA_PATH = BASE_PATH.resolve("Data");
    public static final Path contentHTMLDestination = DATA_PATH;
    public static final Path javaBaseDestination = DATA_PATH.resolve("base_code");
    public static final Path unitTestDestination = DATA_PATH.resolve("test_cases");

    public static final String htmlFileExtension = ".html";
    public static final String jsonFileExtension = ".json";
    public static final String javaFileExtension = ".txt";
    public static final String txtFileExtension = ".txt";

    public static final Set<String> ignoredFiles = Set.of(
        "test_cases/",
        "base_code/"
    );

    // MAIN FUNCTIONS ---------------------------------------------------------

    public static void main(String[] args) {

        char input;
        Scanner stdin = ScannerUtil.createScanner(System.in);
        boolean active = true;
        do {
            System.out.print("> ");
            input = Character.toLowerCase(stdin.next().charAt(0));
            switch (input) {
                case 'c' :
                    active = active && clean();
                    break;
                case 'm' :
                    active = active && make();
                    break;
                case 'a' :
                    active = active && all();
                    break;
                case 'q' :
                    System.out.println("Quit");
                    break;
                default :
                    System.out.println("Commands:\n - [c]lean - Delete all non-ignored, non-directory files from the Data folder.\n - [m]ake - Create all files from JSON content.\n - [a]ll - Clean and Make. (Make will not run if Clean fails).\n - [q]uit - Exit ContentReader.");
            }
        } while (active && input != 'q');
        System.out.println("ContentReader Finished");
        stdin.close();
        System.exit(0);
    }
    
    public static boolean clean() {
        try {
            System.out.println("Clean");
            FileOperations.emptyFiles("seetLabs/Data/", htmlFileExtension);
            FileOperations.emptyFiles("seetLabs/Data/", jsonFileExtension);
            FileOperations.emptyFiles("seetLabs/Data/", javaFileExtension);
            FileOperations.emptyFiles("seetLabs/Data/", txtFileExtension);
            System.out.println("Successfully cleared Data folder");
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean make() {
        try {
            System.out.println("Make");
            Set<LinkedHashMap<Object, Object>> filesContents = JSONProcessor.readAllJSONFiles(contentJSONSource);
            for (LinkedHashMap<Object, Object> module : filesContents) {
                HTMLGenerator.createAllHTMLs(module);
            }
            System.out.println("Successfully created content HTML files");
            createDBFile(determineStructure(filesContents));
            System.out.println("Successfully created database file");
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
            
    }

    public static boolean all() {
        System.out.println("All");
        return clean() && make();
    }

    // END MAIN FUNCTIONS -----------------------------------------------------

    public static class ScannerUtil {
        public static Scanner createScanner(InputStream inputStream) {
            return new Scanner(inputStream, StandardCharsets.UTF_8.name());
        }
        public static Scanner createScanner(File file) throws FileNotFoundException {
            return new Scanner(file, StandardCharsets.UTF_8.name());
        }
    }

    public static class FileOperations {

        public static Set<String> listFiles(String dir) throws IOException {
            Set<String> fileSet = new HashSet<>();
            Path dirPath = Paths.get(dir).normalize();
            if(!Files.exists(dirPath)) Files.createDirectories(dirPath);
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
                for (Path path : stream) {
                    String fileName = path.getFileName().toString();
                    if (!Files.isDirectory(path) && !ContentReader.ignoredFiles.contains(fileName)) {
                        fileSet.add(fileName);
                    }
                }
                return fileSet;
            }
            catch (IOException e) {
                System.out.println("Error accessing directory: " + dirPath);
                throw e;
            }
        }

        public static void emptyFiles(String dir, String extension) throws IOException {
            Set<String> files = listFiles(dir);
            for (String fileName : files) {
                // Delete if extension matches or if wildcard
                if (extension.equals("*") || fileName.endsWith(extension)) {
                    try {
                        Path path = Paths.get(dir, fileName);
                        Files.delete(path);
                    } catch (IOException e) {
                        System.err.println("Failed to delete file: " + fileName);
                        throw e;
                    }
                }
            }
        }

        public static void writeHTML(String filename, List<String> content) {
            writeFile(filename, ContentReader.htmlFileExtension, ContentReader.contentHTMLDestination, content);
        }
        public static void writeHTML(String filename, String content) {
            writeFile(filename, ContentReader.htmlFileExtension, ContentReader.contentHTMLDestination, content);
        }

        public static void writeJava(String filename, List<String> content) {
            writeFile(filename, ContentReader.javaFileExtension, ContentReader.javaBaseDestination, content);
        }
        public static void writeJava(String filename, String content) {
            writeFile(filename, ContentReader.javaFileExtension, ContentReader.javaBaseDestination, content);
        }

        public static void writeJSON(String filename, List<String> content) {
            writeFile(filename, ContentReader.jsonFileExtension, ContentReader.DATA_PATH, content);
        }
        public static void writeJSON(String filename, String content) {
            writeFile(filename, ContentReader.jsonFileExtension, ContentReader.DATA_PATH, content);
        }

        public static void writeTxt(String filename, List<String> content) {
            writeFile(filename, ContentReader.txtFileExtension, ContentReader.DATA_PATH, content);
        }
        public static void writeTxt(String filename, String content) {
            writeFile(filename, ContentReader.txtFileExtension, ContentReader.DATA_PATH, content);
        }

        public static void writeFile(String filename, String extension, Path destination, String content) {
            writeFile(filename, extension, destination, Collections.singletonList(content));
        }
        public static void writeFile(String filename, String extension, Path destination, List<String> content) {
            Path filePath = destination.resolve(filename + extension);
            File file = filePath.toFile();
            writeFile(file, content);
        }

        public static void writeFile(File file, List<String> content) {
            try {
                Files.createDirectories(file.getParentFile().toPath());
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file, false)) {
                    for (String line : content) {
                        writer.write(line + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class JSONProcessor {

        public static Set<LinkedHashMap<Object, Object>> readAllJSONFiles(Path dir) throws IOException {
            Set<LinkedHashMap<Object, Object>> JSONs = new HashSet<>();
            Set<String> filenames = FileOperations.listFiles(dir.toString());
            for (String filename : filenames) {
                if (filename.contains(jsonFileExtension)) {
                    LinkedHashMap<Object, Object> fileJSON = readJSONFile(contentJSONSource.resolve(filename));
                    if (fileJSON != null) JSONs.add(fileJSON);
                }
            }
            return JSONs;
        }

        public static LinkedHashMap<Object, Object> readJSONFile(Path filepath) {
            ArrayList<String> contents = new ArrayList<String>();
            try {
                File file = filepath.toFile();
                Scanner scanner = ScannerUtil.createScanner(file);
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
            return JSONProcessor.readJSONObject(contents.subList(1, contents.size()-1));
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
    }

    public static class HTMLGenerator {
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

        public List<String> generateHTML(LinkedHashMap<Object, Object> JSON) {
            String type = JSON.get("type").toString();
            switch (type) {
                case "reading_activity" :
                    return generateReadingActivity(JSON);
                case "coding_activity" :
                    return generateCodingActivity(JSON);
                case "quiz_activity" :
                    return generateQuizActivity(JSON);
                default :
                    throw new IllegalArgumentException("Unknown activity type: " + type);
            }
        }

        public static void createAllHTMLs(LinkedHashMap<Object, Object> JSON){
            if (JSON.size() <= 1) return;
            for (int i = 1; i < Integer.parseInt(JSON.get("activity_count").toString()); i++){
                @SuppressWarnings("unchecked")
                LinkedHashMap<Object, Object> activityContent = (LinkedHashMap<Object, Object>) ((LinkedHashMap<Object, Object>) JSON.get("content")).get("activity" + i);
                if (activityContent != null)
                    createHTML(activityContent);
            }
        }
    
        public static void createHTML(LinkedHashMap<Object, Object> JSON) {
            HTMLGenerator generator = new HTMLGenerator();
            List<String> htmlContent = generator.generateHTML(JSON);
            File htmlFile = contentHTMLDestination.resolve(JSON.get("id") + htmlFileExtension).toFile();
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

        private static List<String> generateReadingActivity(LinkedHashMap<Object, Object> JSON) {

            List<String> html = new ArrayList<>();

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
        }
        
        private static List<String> generateCodingActivity(LinkedHashMap<Object, Object> JSON) {

            List<String> html = new ArrayList<>();

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
                @SuppressWarnings("unchecked")
                String baseCode = (((HashMap<Object, Object>) JSON.get("content")).get("base_code")).toString();
                String processed = processJavaMarkup(baseCode, 0);
                FileOperations.writeJava(JSON.get("id") + "_basecode", processed);
            }
            catch (NullPointerException e) {
            }


            return html;

            // create test cases file in a common useful format
            // two types: console out, unit tests - identified by a flag in the file
            // the user will write a function or class based on the instructions. the test code will use that function or class, by name as a black box, and check the output
        }
        
        private static List<String> generateQuizActivity(LinkedHashMap<Object, Object> JSON) {

            List<String> html = new ArrayList<>();

            html.add("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n\t<meta charset=\"UTF-8\">\n\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            html.add("\t<title>" + JSON.get("name") + "</title>");
            html.add("\t<link rel=\"stylesheet\" href=\"style.css\">");
            html.add("</head>");

            html.add("<!-- THIS IS A QUIZ ACTIVITY -->\n<!-- Let me know if you want there to be actual formatted HTML here. -S -->");
            html.add("</html>");

            return html;
        }

    }
    
    public static class Markup {

        // Markup Tags have a start and end tag and provide a modification to whatever lies between those tags. Tags must start with % followed by a unique set of characters.
        private static final List<Markup> htmlMarkupTags = new ArrayList<Markup>() {{
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

        private static final List<Markup> javaMarkupTags = new ArrayList<Markup>() {{
            add (new Markup("%%", "%"));
            add (new Markup("%n", "\n"));
            add (new Markup("%t", "\t"));
            add (new Markup("%'", "\""));
            add (new Markup("%w", " "));
        }};

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

        public static Markup findJavaMarkup(String tag) {
            for (Markup markup : javaMarkupTags) {
                if (markup.startTag.equals(tag)) return markup;
            }
            return null;
        }

        public static Markup findHTMLMarkup(String tag) {
            for (Markup markup : htmlMarkupTags) {
                if (markup.startTag.equals(tag)) return markup;
            }
            return null;
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
                Markup markup = Markup.findJavaMarkup(text.substring(i, i + 2));
                if (markup == null) {
                    System.out.println("WARNING: Unrecognized markup " + text.substring(i, i + 2) + " in ..." + text.substring(Integer.max(0, i - 10), Integer.min(text.length(), i + 10)) + "...");
                    result.append(text.charAt(i));
                    continue;
                }

                result.append(markup.startReplacement);

                i += 1;
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
                Markup markup = Markup.findHTMLMarkup(text.substring(i, i + 2));
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
            @SuppressWarnings("unchecked")
            LinkedHashMap<Object, Object> moduleContents = (LinkedHashMap<Object, Object>) module.get("content");
            HashMap<Integer, Integer> activityStructure = new HashMap<>();
            for (Object activity : moduleContents.keySet()) {
                @SuppressWarnings("unchecked")
                HashMap<Object, Object> activityContents = (HashMap<Object, Object>) moduleContents.get(activity);
                int activityNumber = Integer.parseInt(activityContents.get("id").toString().split("-")[1]);
                String type = activityContents.get("type").toString();
                int activityType = type.equals("reading_activity") ? 0 : type.equals("quiz_activity") ? 1 : type.equals("coding_activity") ? 2 : -1;
                activityStructure.put(activityNumber, activityType);
            }
            result.put(Integer.parseInt(module.get("number").toString()), activityStructure);
        }

        return result;
    }

    public enum ActivityType {
        READING("reading_activity", 0),
        QUIZ("quiz_activity", 1),
        CODING("coding_activity", 2);

        public final String type;
        public final int value;

        ActivityType(String type, int value) {
            this.type = type;
            this.value = value;
        }
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
                result.append("\t{\"id\" : \"")
                      .append(String.format("%d-%d", module, activity))
                      .append("\", \"type\" : ")
                      .append(ids.get(module).get(activity))
                      .append(", \"file\" : \"")
                      .append(String.format("%s-%s" + htmlFileExtension, module.toString(), activity.toString()))
                      .append("\"}");
            }
            result.append("\n\t]");
        }
        result.append("\n}");
        try {
            File dbFile = contentHTMLDestination.resolve("db" + jsonFileExtension).toFile();
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

    public static void createTestCaseFile(HashMap<Object, Object> testCases) {

    }
}