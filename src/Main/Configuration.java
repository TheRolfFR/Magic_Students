package Main;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Configuration {
    private static JSONObject configurationFile = null;
    private static final String FILE_PATH = "configuration.json";

    static JSONObject getConfigurationFile() {
        if (configurationFile == null) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(FILE_PATH)));

                configurationFile = new JSONObject(content);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        return configurationFile;
    }
}
