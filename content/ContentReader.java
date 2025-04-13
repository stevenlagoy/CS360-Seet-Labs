package content;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import content.FileOperations.ScannerUtil;

public class ContentReader {

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
            FileOperations.emptyFiles("seetLabs/Data/", StringOperations.HTML_EXT);
            FileOperations.emptyFiles("seetLabs/Data/", StringOperations.JSON_EXT);
            FileOperations.emptyFiles("seetLabs/Data/", StringOperations.JAVA_EXT);
            FileOperations.emptyFiles("seetLabs/Data/", StringOperations.TEXT_EXT);
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
            List<LinkedHashMap<Object, Object>> filesContents = JsonReader.readAllJSONFiles(FilePaths.JSON_SOURCE);
            List<List<List<String>>> htmlStrings = new ArrayList<>();
            for (LinkedHashMap<Object, Object> module : filesContents) {
                htmlStrings.add(HtmlGenerator.generateAllHtmlStrings(module));
            }
            // create all the htmls
            System.out.println("Successfully created content HTML files");
            System.out.println("Successfully created base code Java files");
            System.out.println("Successfully created test case Text files");
            createDBString(htmlStrings);
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

    public static String createDBString(List<List<List<String>>> htmlStrings) {
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

        for (int moduleNum = 0; moduleNum < htmlStrings.size(); moduleNum++){
            List<List<String>> moduleContent = htmlStrings.get(moduleNum);
            for (int activityNum = 0; activityNum < moduleContent.size(); activityNum++) {
                List<String> activityContent = moduleContent.get(activityNum);
                for(String line : activityContent) {
                    System.out.println(line);
                }
            }

        }
        return result.toString();
    }

    public static String createReadingDBEntry(int id, String contentFile) {
        String result = String.format(
            "{\"id\" : \"%s\", \"tupe\" : 0, \"file\" : \"%s\"}",
            id, contentFile
        );
        return result;
    }

    public static String createQuizDBEntry(int id, String questions, String name) {
        String result = String.format(
            "{\"id\" : \"%d\", \"type\" : 1, \"questions\" : {\n\t\"name\" : \"name\"\n\t\"content\" : {\n\t\t%s}}}",
            id, name, questions
        ); // the content of each question will not be properly indented
        return result;
    }

    public static String createCodingDBActivity(int id, String contextFile, String baseFile, String launcherFile, String testCaseFile) {
        String result = String.format(
            "{\"id\" : \"%d\", \"type\" : 3, \"context\" : \"%s\", \"base\" : \"%s\", \"launcher\" : \"%s\", \"testCases\" : \"%s\"}",
            id, contextFile, baseFile, launcherFile, testCaseFile
        );
        return result;
    }

}