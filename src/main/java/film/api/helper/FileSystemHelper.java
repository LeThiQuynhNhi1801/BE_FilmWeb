package film.api.helper;

import java.nio.file.Paths;

public class FileSystemHelper {

    public static String WORKING_DIR = System.getProperty("user.dir");

    public static String STATIC_FILES_DIR = Paths.get(WORKING_DIR, "Media").toAbsolutePath().toString();
}
