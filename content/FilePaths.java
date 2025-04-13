package content;

import java.nio.file.Path;
import java.util.List;

public class FilePaths {
    
    public static final Path BASE_PATH = Path.of("seetLabs");
    public static final Path JSON_SOURCE = Path.of("content");
    public static final Path DATA_PATH = BASE_PATH.resolve("Data");
    public static final Path PUBLIC_PATH = BASE_PATH.resolve("public");
    public static final Path HTML_DEST = DATA_PATH;
    public static final Path JAVA_BASE_DEST = PUBLIC_PATH.resolve("base-code");
    public static final Path TEST_CASE_DEST = PUBLIC_PATH.resolve("test-cases");

    public static final List<String> IGNORED_FILES = List.of(
    );

}
