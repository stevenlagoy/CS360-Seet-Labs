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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class FileOperations {

    public static final String HTML_EXT = ".html";
    public static final String JSON_EXT = ".json";
    public static final String JAVA_EXT = ".java";
    public static final String TEXT_EXT = ".txt";

    public static final String DB_FILE_NAME = "db";
    public static final String TEST_CASE_DESC = "_test_case";
    public static final String BASE_CODE_DESC = "_base_code";

    public static class ScannerUtil {
        public static Scanner createScanner(InputStream inputStream) {
            return new Scanner(inputStream, StandardCharsets.UTF_8.name());
        }
        public static Scanner createScanner(File file) throws FileNotFoundException {
            return new Scanner(file, StandardCharsets.UTF_8.name());
        }
    }

    public static Set<String> listFiles(Path dir) throws IOException {
        Set<String> fileSet = new HashSet<>();
        Path dirPath = dir.normalize();
        if(!Files.exists(dirPath)) Files.createDirectories(dirPath);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for (Path path : stream) {
                String fileName = path.getFileName().toString();
                if (!Files.isDirectory(path) && !FilePaths.IGNORED_FILES.contains(fileName)) {
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

    public static void emptyFiles(Path dir, String extension) throws IOException {
        Set<String> files = listFiles(dir); // does not include ignored files
        for (String fileName : files) {
            // Delete if extension matches or if wildcard
            if (!FilePaths.CONTENT_FILES.contains(fileName) && extension.equals("*") || fileName.endsWith(extension)) {
                try {
                    Path path = dir.resolve(fileName);
                    Files.delete(path);
                } catch (IOException e) {
                    System.err.println("Failed to delete file: " + fileName);
                    throw e;
                }
            }
        }
    }

    public static void writeHtml(String filename, List<String> content) {
        writeFile(filename, HTML_EXT, FilePaths.HTML_DEST, content);
    }
    public static void writeHtml(String filename, String content) {
        writeFile(filename, HTML_EXT, FilePaths.HTML_DEST, content);
    }

    public static void writeJava(String filename, List<String> content) {
        writeFile(filename, JAVA_EXT, FilePaths.JAVA_BASE_DEST, content);
    }
    public static void writeJava(String filename, String content) {
        writeFile(filename, JAVA_EXT, FilePaths.JAVA_BASE_DEST, content);
    }

    public static void writeJSON(String filename, List<String> content) {
        writeFile(filename, JSON_EXT, FilePaths.DATA_PATH, content);
    }
    public static void writeJSON(String filename, String content) {
        writeFile(filename, JSON_EXT, FilePaths.DATA_PATH, content);
    }

    public static void writeText(String filename, List<String> content) {
        writeFile(filename, TEXT_EXT, FilePaths.DATA_PATH, content);
    }
    public static void writeText(String filename, String content) {
        writeFile(filename, TEXT_EXT, FilePaths.DATA_PATH, content);
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
