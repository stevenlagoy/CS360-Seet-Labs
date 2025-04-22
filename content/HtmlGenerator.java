package content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HtmlGenerator {

    public static enum ActivityType {
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

    public static class HtmlResult {
        public String id;
        public List<String> htmlString;
        public ActivityType type;
        public HtmlResult(String id, List<String> htmlString, ActivityType type) {
            this.id = id;
            this.htmlString = htmlString;
            this.type = type;
        }
        public HtmlResult(String id, List<String> htmlString, int activityType) {
            this.id = id;
            this.htmlString = htmlString;
            for (ActivityType type : ActivityType.values())
                if (type.value == activityType)
                    this.type = type;
            if (this.type == null) throw new IllegalArgumentException("Invalid activity type: " + activityType);
        }
        public HtmlResult(String id, List<String> htmlString, String activityType) {
            this.id = id;
            this.htmlString = htmlString;
            for (ActivityType type : ActivityType.values())
                if (type.type.equals(activityType))
                    this.type = type;
            if (this.type == null) throw new IllegalArgumentException("Invalid activity type: " + activityType);
        }
    } 
    
    public static final Map<String, String> openingTags = Map.of(
        "t", "<h1>",
        "h1", "<h2>",
        "p", "<p>"
    );
    public static final Map<String, String> closingTags = Map.of(
        "t", "</h1>",
        "h1", "</h2>",
        "p", "</p>"
    );

    public static List<HtmlResult> generateAllHtmlStrings(LinkedHashMap<Object, Object> json){
        List<HtmlResult> htmlStrings = new ArrayList<>();
        if (json.size() <= 1) return null;
        for (int i = 1; i < Integer.parseInt(json.get("activity_count").toString()); i++){
            @SuppressWarnings("unchecked")
            LinkedHashMap<Object, Object> activityContent = (LinkedHashMap<Object, Object>) ((LinkedHashMap<Object, Object>) json.get("content")).get("activity_" + i);
            if (activityContent != null)
                htmlStrings.add(generateHtmlString(activityContent));
        }
        return htmlStrings;
    }

    public static HtmlResult generateHtmlString(LinkedHashMap<Object, Object> json) {
        String type = json.get("type").toString();
        switch (type) {
            case "reading_activity" :
            return new HtmlResult(json.get("id").toString(), generateReadingActivityHtmlString(json), json.get("type").toString());
            case "coding_activity" :
                return new HtmlResult(json.get("id").toString(), generateCodingActivityHtmlString(json), json.get("type").toString());
            case "quiz_activity" :
                return new HtmlResult(json.get("id").toString(), generateQuizActivityHtmlString(json), json.get("type").toString());
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
        FileOperations.writeFile(activityID + "_test_cases", FileOperations.TEXT_EXT, FilePaths.TEST_CASE_DEST, result.toString().trim());
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
        html.add("\t<h1>" + JSON.get("name") + "</h1>");
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

        return html;
    }
    
    private static List<String> generateQuizActivityHtmlString(LinkedHashMap<Object, Object> JSON) {

        List<String> html = new ArrayList<>();

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

}
