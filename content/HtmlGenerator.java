package content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HtmlGenerator {
    
    public static final Map<String, String> openingTags = Map.of(
        "t", "<p>",
        "h1", "<h1>",
        "p", "<p>"
    );
    public static final Map<String, String> closingTags = Map.of(
        "t", "</p>",
        "h1", "</h1>",
        "p", "</p>"
    );

    public static List<List<String>> generateAllHtmlStrings(LinkedHashMap<Object, Object> JSON){
        List<List<String>> htmlStrings = new ArrayList<>();
        if (JSON.size() <= 1) return null;
        for (int i = 1; i < Integer.parseInt(JSON.get("activity_count").toString()); i++){
            @SuppressWarnings("unchecked")
            LinkedHashMap<Object, Object> activityContent = (LinkedHashMap<Object, Object>) ((LinkedHashMap<Object, Object>) JSON.get("content")).get("activity" + i);
            if (activityContent != null)
                htmlStrings.add(generateHtmlString(activityContent));
        }
        return htmlStrings;
    }

    public static List<String> generateHtmlString(LinkedHashMap<Object, Object> JSON) {
        String type = JSON.get("type").toString();
        switch (type) {
            case "reading_activity" :
                return generateReadingActivityHtmlString(JSON);
            case "coding_activity" :
                return generateCodingActivityHtmlString(JSON);
            case "quiz_activity" :
                return generateQuizActivityHtmlString(JSON);
            default :
                throw new IllegalArgumentException("Unknown activity type: " + type);
        }
    }

    public static void createTestCaseFile(String activityID, LinkedHashMap<Object, Object> testCases, int numTestCases, boolean isInputOutput) {
        StringBuilder result = new StringBuilder();
        int counted = 0;
        result.append(numTestCases)
                .append(isInputOutput ? " io" : " co")
                .append("\n");
        for (Object key : testCases.keySet()) {
            counted++;
            @SuppressWarnings("unchecked")
            String input = ((HashMap<Object, Object>) testCases.get(key)).get("input").toString();
            @SuppressWarnings("unchecked")
            String output = ((HashMap<Object, Object>) testCases.get(key)).get("output").toString();
            result.append(key.toString()).append(" : \"")
                    .append(input).append("\" : \"").append(output)
                    .append("\"\n");
        }
        if (counted != numTestCases) {
            System.out.printf("WARNING: Incorrect number of test cases. Expected %d test cases but only found %d. Activity: %s%n", numTestCases, counted, activityID);
        }
        FileOperations.writeFile(activityID + "_test_cases", StringOperations.TEXT_EXT, FilePaths.TEST_CASE_DEST, result.toString().trim());
    }

    private static List<String> generateReadingActivityHtmlString(LinkedHashMap<Object, Object> JSON) {

        List<String> html = new ArrayList<>();

        // Create HTML setup and Head section
        html.add("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n\t<meta charset=\"UTF-8\">\n\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        html.add("\t<p>" + JSON.get("name") + "</p>");
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
                    String processed = StringOperations.processHtmlMarkup(text, 0);
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
    
    private static List<String> generateCodingActivityHtmlString(LinkedHashMap<Object, Object> JSON) {

        List<String> html = new ArrayList<>();

        // CREATE HTML FOR INSTRUCTIONS / CONTEXT
        html.add("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n\t<meta charset=\"UTF-8\">\n\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        html.add("\t<p>" + JSON.get("name") + "</p>");
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
                        String processed = StringOperations.processHtmlMarkup(text, 0);
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
            String processed = StringOperations.processJavaMarkup(baseCode, 0);
            FileOperations.writeJava(JSON.get("id") + "_basecode", processed);
        }
        catch (NullPointerException e) {
        }

        // CREATE TEXT FILE FOR TEST CASES
        try {
            @SuppressWarnings("unchecked")
            HashMap<Object, Object> content = (HashMap<Object, Object>) JSON.get("content");
            String activityID = JSON.get("id").toString();
            @SuppressWarnings("unchecked")
            LinkedHashMap<Object, Object> testCases = (LinkedHashMap<Object, Object>) content.get("test_cases");
            int numTestCases = Integer.parseInt(content.get("number_test_cases").toString());
            boolean isInputOutput = content.get("output_type").toString().equals("input output");
            createTestCaseFile(activityID, testCases, numTestCases, isInputOutput);
        }
        catch (NumberFormatException e){
            System.out.println("Expected number, got " + JSON.get("number_test_cases").toString() + " in activity " + JSON.get("id").toString());
        }
        catch (NullPointerException e) {
            System.out.print("WARNING: ");
            e.printStackTrace();
        }


        return html;

        // create test cases file in a common useful format
        // two types: console out, unit tests - identified by a flag in the file
        // the user will write a function or class based on the instructions. the test code will use that function or class, by name as a black box, and check the output
    }
    
    private static List<String> generateQuizActivityHtmlString(LinkedHashMap<Object, Object> JSON) {

        List<String> html = new ArrayList<>();

        html.add("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n\t<meta charset=\"UTF-8\">\n\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        html.add("\t<p>" + JSON.get("name") + "</p>");
        html.add("\t<link rel=\"stylesheet\" href=\"style.css\">");
        html.add("</head>");

        html.add("<!-- THIS IS A QUIZ ACTIVITY -->\n<!-- Let me know if you want there to be actual formatted HTML here. -S -->");
        html.add("</html>");

        return html;
    }

}
