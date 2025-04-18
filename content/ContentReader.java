package content;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
            FileOperations.emptyFiles(FilePaths.DATA_PATH, FileOperations.HTML_EXT);
            FileOperations.emptyFiles(FilePaths.DATA_PATH, FileOperations.JSON_EXT);
            FileOperations.emptyFiles(FilePaths.JAVA_BASE_DEST, FileOperations.JAVA_EXT);
            FileOperations.emptyFiles(FilePaths.TEST_CASE_DEST, FileOperations.TEXT_EXT);
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

            // Read JSON files and create HTML strings
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
            for (LinkedHashMap<Object, Object> module : contentsJson) {
                List<String> result = generateAllJavaBaseStrings(module);
                if (result.size() == 0) continue;
                String id = result.removeFirst();
                FileOperations.writeFile(id + FileOperations.BASE_CODE_DESC, FileOperations.JAVA_EXT, FilePaths.JAVA_BASE_DEST, result);
            }
            System.out.println("Successfully created base code Java files");
            
            // Create test case Text files
            for (LinkedHashMap<Object, Object> module : contentsJson) {
                List<String> result = generateAllTestCaseStrings(module);
                if (result.size() == 0) continue;
                String id = result.removeFirst();
                FileOperations.writeFile(id + FileOperations.TEST_CASE_DESC, FileOperations.TEXT_EXT, FilePaths.TEST_CASE_DEST, result);
            }
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

    public static List<String> generateAllTestCaseStrings(LinkedHashMap<Object, Object> module) {
        List<String> testCaseStrings = new ArrayList<>();

        @SuppressWarnings("unchecked")
        Map<Object, Object> moduleContents = (Map<Object, Object>) module.get("content");
        for (Object activity : moduleContents.keySet()) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> activityContents = (Map<Object, Object>) moduleContents.get(activity);
            if (activityContents.get("type").toString().equals("coding_activity")) {

                testCaseStrings.add(String.format("%s", activityContents.get("id").toString()));

                @SuppressWarnings("unchecked")
                Map<Object, Object> activityContent = (Map<Object, Object>) activityContents.get("content");
                String numberCases = activityContent.get("number_test_cases").toString();
                String outputType = activityContent.get("output_type").toString().equals("console out") ? "co" : "io";
                testCaseStrings.add(String.format("%s %s", numberCases, outputType));

                @SuppressWarnings("unchecked")
                Map<Object, Object> testCases = (Map<Object, Object>) activityContent.get("test_cases");
                for (Object testCase : testCases.keySet()) {
                    String testCaseName = testCase.toString();
                    @SuppressWarnings("unchecked")
                    String input = ((Map<Object, Object>) testCases.get(testCase)).get("input").toString();
                    @SuppressWarnings("unchecked")
                    String output = ((Map<Object, Object>) testCases.get(testCase)).get("output").toString();

                    testCaseStrings.add(String.format("%s : \"%s\" : \"%s\"", testCaseName, input, output));
                }
            }
        }
        return testCaseStrings;
    }

    public static List<String> generateAllJavaBaseStrings(LinkedHashMap<Object, Object> module) {
        List<String> baseCodeStrings = new ArrayList<>();

        @SuppressWarnings("unchecked")
        Map<Object, Object> moduleContents = (Map<Object, Object>) module.get("content");
        for (Object activity : moduleContents.keySet()) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> activityContents = (Map<Object, Object>) moduleContents.get(activity);
            if (activityContents.get("type").toString().equals("coding_activity")) {

                baseCodeStrings.add(String.format("%s", activityContents.get("id").toString()));

                @SuppressWarnings("unchecked")
                Map<Object, Object> activityContent = (Map<Object, Object>) activityContents.get("content");
                String baseCode = activityContent.get("base_code").toString();
                baseCode = StringOperations.processJavaMarkup(baseCode, 0);
                baseCodeStrings.add(baseCode);
            }
        }
        return baseCodeStrings;
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
                @SuppressWarnings("unchecked") Map<Object, Object> content = (Map<Object, Object>) activity.get("content");
                for (Object questionKey : content.keySet()) {
                    List<String> questionLines = new ArrayList<>();
                    @SuppressWarnings("unchecked")
                    Map<Object, Object> questionContents = (Map<Object, Object>) content.get(questionKey);
                    questionLines.add(String.format("\"type\" : \"%s\",", questionContents.get("type").toString()));
                    questionLines.add(String.format("\"question\" : \"%s\",", questionContents.get("question").toString()));
                    questionLines.add(String.format("\"options\" : {"));
                    @SuppressWarnings("unchecked")
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
                        questionLines.add(String.format("\t\"%s\"", response.replace("[", "").replace("]", "").replace(" ", "").replace(",", "\", \"")));
                        questionLines.set(questionLines.size() - 1, questionLines.get(questionLines.size() - 1) + ",");
                    }
                    questionLines.set(questionLines.size() - 1, StringOperations.replaceLast(questionLines.get(questionLines.size() - 1), ",", ""));
                    questionLines.add(String.format("],"));
                    questionLines.add(String.format("\"points\" : %s,", questionContents.get("points")));
                    questionLines.add(String.format("\"feedback\" : {"));
                    @SuppressWarnings("unchecked")
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
                String baseFile = id + FileOperations.BASE_CODE_DESC + FileOperations.JAVA_EXT;
                String launcherFile = "launcher";
                String testCaseFile = id + FileOperations.TEST_CASE_DESC + FileOperations.TEXT_EXT;
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
            "{\"id\" : \"%s\", \"type\" : 2, \"context\" : \"%s\", \"base\" : \"%s\", \"launcher\" : \"%s\", \"testCases\" : \"%s\"}",
            id.split("-")[1], contextFile, baseFile, launcherFile, testCaseFile
        ));
        return result;
    }

}