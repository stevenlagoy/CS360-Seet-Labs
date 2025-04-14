package content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import content.FileOperations.ScannerUtil;
import content.HtmlGenerator.HtmlResult;

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
            FileOperations.emptyFiles("seetLabs/Data/", FileOperations.HTML_EXT);
            FileOperations.emptyFiles("seetLabs/Data/", FileOperations.JSON_EXT);
            FileOperations.emptyFiles("seetLabs/Data/", FileOperations.JAVA_EXT);
            FileOperations.emptyFiles("seetLabs/Data/", FileOperations.TEXT_EXT);
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
            List<LinkedHashMap<Object, Object>> contentsJson = JsonReader.readAllJSONFiles(FilePaths.JSON_SOURCE);
            List<List<HtmlResult>> htmls = new ArrayList<>();
            for (LinkedHashMap<Object, Object> module : contentsJson) {
                List<HtmlResult> result = HtmlGenerator.generateAllHtmlStrings(module);
                htmls.add(result);
            }
            // Create all the Html files
            for (List<HtmlResult> module : htmls) {
                for (HtmlResult activity : module) {
                    FileOperations.writeHtml(activity.id, activity.htmlString);
                }
            }
            System.out.println("Successfully created content HTML files");
            // Create all the Java files
            System.out.println("Successfully created base code Java files");
            // Create test case Text files
            // for (List<HtmlResult> module : htmls) {
            //     for (HtmlResult activity : module) {
            //         FileOperations.writeText(activity.id + FileOperations.TEST_CASE_DESC, createTestCaseStrings());
            //     }
            // }
            System.out.println("Successfully created test case Text files");
            // Create DB File
            FileOperations.writeJSON(FileOperations.DB_FILE_NAME, createDBStrings(contentsJson));
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

    private static HashMap<Integer, HashMap<Integer, Integer>> determineStructure(Set<LinkedHashMap<Object, Object>> contents) {
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

    public static String createTestCaseStrings() {
        return null;
    }

    public static List<String> createDBStrings(List<LinkedHashMap<Object, Object>> contentsJson) {
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

        List<String> result = new ArrayList<>();
        result.add("{");
        for (LinkedHashMap<Object, Object> module : contentsJson) {
            List<String> moduleEntry = createDBModuleEntry(module);
            for (String line : moduleEntry) {
                result.add("\t" + line);
            }
            result.set(result.size() - 1, result.get(result.size() - 1) + ",");
        }
        result.set(result.size() - 1, StringOperations.replaceLast(result.get(result.size() - 1), ",", ""));
        result.add("}");
        return result;
    }

    public static List<String> createDBModuleEntry(LinkedHashMap<Object, Object> module) {
        List<String> result = new ArrayList<>();
        @SuppressWarnings("unchecked")
        Map<Object, Object> activities = (Map<Object, Object>) module.get("content");
        result.add(String.format("\"%s\" : [", module.get("name").toString().split("_")[module.get("name").toString().split("_").length - 1]));
        result.add(String.format("\t{\"name\" : \"%s\"},", module.get("title").toString()));
        for (Object activityKey : activities.keySet()) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> activityContent = (Map<Object, Object>) activities.get(activityKey);
            List<String> activityEntry = createDBActivityEntry(activityContent);
            for (String line : activityEntry) {
                result.add("\t" + line);
            }
            result.set(result.size() - 1, result.get(result.size() - 1) + ",");
        }
        result.set(result.size() - 1, StringOperations.replaceLast(result.get(result.size() - 1), ",", ""));
        result.add("]");
        return result;
    }

    public static List<String> createDBActivityEntry(Map<Object, Object> activity) {
        String id = activity.get("id").toString();
        switch (activity.get("type").toString()) {

            case "reading_activity" :
                return createReadingDBEntry(id, id + FileOperations.HTML_EXT);
            
            case "quiz_activity" :
                List<List<String>> questions = new ArrayList<>();
                Map<Object, Object> content = (Map<Object, Object>) activity.get("content");
                for (Object questionKey : content.keySet()) {
                    List<String> questionLines = new ArrayList<>();
                    Map<Object, Object> questionContents = (Map<Object, Object>) content.get(questionKey);
                    questionLines.add(String.format("\"type\" : \"%s\",", questionContents.get("type").toString()));
                    questionLines.add(String.format("\"question\" : \"%s\",", questionContents.get("question").toString()));
                    questionLines.add(String.format("\"options\" : {"));
                    Map<String, String> questionOptions = (Map<String, String>) questionContents.get("options");
                    for (String option : questionOptions.keySet()) {
                        questionLines.add(String.format("\t\"%s\" : \"%s\"", option, questionOptions.get(option)));
                        questionLines.set(questionLines.size() - 1, questionLines.get(questionLines.size() - 1) + ",");
                    }
                    questionLines.set(questionLines.size() - 1, StringOperations.replaceLast(questionLines.get(questionLines.size() - 1), ",", ""));
                    questionLines.add(String.format("},"));
                    questionLines.add(String.format("\"correct_responses\" : ["));
                    List<String> correctResponses = List.of(questionContents.get("correct_responses").toString());
                    for (String response : correctResponses) {
                        questionLines.add(String.format("\t\"%s\"", response.replace("[", "").replace("]", "").trim().replace(",", "\", \"")));
                        questionLines.set(questionLines.size() - 1, questionLines.get(questionLines.size() - 1) + ",");
                    }
                    questionLines.set(questionLines.size() - 1, StringOperations.replaceLast(questionLines.get(questionLines.size() - 1), ",", ""));
                    questionLines.add(String.format("],"));
                    questionLines.add(String.format("\"points\" : %s,", questionContents.get("points")));
                    questionLines.add(String.format("\"feedback\" : {"));
                    Map<String, String> feedbacks = (Map<String, String>) questionContents.get("feedback");
                    for (String feedback : feedbacks.keySet()) {
                        questionLines.add(String.format("\t\"%s\" : \"%s\"", feedback, feedbacks.get(feedback)));
                        questionLines.set(questionLines.size() - 1, questionLines.get(questionLines.size() - 1) + ",");
                    }
                    questionLines.set(questionLines.size() - 1, StringOperations.replaceLast(questionLines.get(questionLines.size() - 1), ",", ""));
                    questionLines.add(String.format("}"));
                    List<String> fullQuestion = new ArrayList<>();
                    fullQuestion.add(String.format("\"%s\" : {", questionKey));
                    for (String line : questionLines) {
                        fullQuestion.add(String.format("\t%s", line));
                    }
                    fullQuestion.add(String.format("}"));
                    fullQuestion.set(fullQuestion.size() - 1, fullQuestion.get(fullQuestion.size() - 1) + ",");
                    questions.add(fullQuestion);
                }
                List<String> lastEntry = questions.get(questions.size() - 1);
                lastEntry.set(lastEntry.size() - 1, StringOperations.replaceLast(lastEntry.get(lastEntry.size() - 1), ",", ""));
                String name = activity.get("name").toString();
                return createQuizDBEntry(id, questions, name);
            
            case "coding_activity" :
                String contextFile = id + FileOperations.HTML_EXT;
                String baseFile = id + FileOperations.BASE_CODE_DESC;
                String launcherFile = "launcher";
                String testCaseFile = id + FileOperations.TEST_CASE_DESC;
                return createCodingDBActivity(id, contextFile, baseFile, launcherFile, testCaseFile);
            
            default : return null;
        }
    }

    public static List<String> createReadingDBEntry(String id, String contentFile) {
        List<String> result = new ArrayList<>();
        result.add(String.format(
            "{\"id\" : \"%s\", \"type\" : 0, \"file\" : \"%s\"}",
            id.split("-")[1], contentFile
        ));
        return result;
    }

    public static List<String> createQuizDBEntry(String id, List<List<String>> questions, String name) {
        List<String> result = new ArrayList<>();
        result.add(String.format(
            "{\"id\" : \"%s\", \"type\" : 1, \"questions\" : {",
            id.split("-")[1]
        ));
        result.add(String.format(
            "\t\"name\" : \"%s\",",
            name
        ));
        result.add(String.format("\t\"content\" : {"));
        for (List<String> question : questions) {
            for (String line : question) {
                result.add(String.format("\t\t%s",
                    line
                ));
            }
        }
        result.add(String.format("\t\t}"));
        result.add(String.format("\t}"));
        result.add(String.format("}"));
        return result;
    }

    public static List<String> createCodingDBActivity(String id, String contextFile, String baseFile, String launcherFile, String testCaseFile) {
        List<String> result = new ArrayList<>();
        result.add(String.format(
            "{\"id\" : \"%s\", \"type\" : 3, \"context\" : \"%s\", \"base\" : \"%s\", \"launcher\" : \"%s\", \"testCases\" : \"%s\"}",
            id.split("-")[1], contextFile, baseFile, launcherFile, testCaseFile
        ));
        return result;
    }

}