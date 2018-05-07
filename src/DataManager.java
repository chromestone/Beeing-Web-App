import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * The DataManager class
 * Created by Derek Zhang on 5/5/18.
 */
class DataManager {

    private final File dataFile;

    DataManager(File dataFile) {

        this.dataFile = dataFile;
    }

    private boolean incrementAux(int recordNum, Map<String, String> records, String[] output) {

        for (int i = 0; i < Main.INPUT_NAMES.length; i++) {

            String key = String.format(Main.INPUT_NAMES[i], recordNum);
            String val = records.get(key);
            if (val == null) {

                return false;
            }
            val = val.trim();
            if (val.isEmpty()) {

                return false;
            }
            output[i] = val;
        }

        return true;
    }

    boolean append(int id, Map<String, String> records) throws IOException {

        String[] output = new String[Main.INPUT_NAMES.length];
        try (FileWriter writer = new FileWriter(dataFile, true)) {

            int recordNum = 0;
            while (incrementAux(recordNum, records, output)) {

                writer.write(String.valueOf(id));
                writer.write(',');

                int i = 0;
                while (true) {
                    String val = output[i];
                    writer.write(val);
                    i += 1;
                    if (i >= Main.INPUT_NAMES.length) {

                        writer.write('\n');
                        break;
                    }
                    writer.write(',');
                }

                recordNum += 1;
            }

            return recordNum != 0;
        }
    }
}
