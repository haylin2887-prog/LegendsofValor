package valor.data;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// reads a data file into rows of words. the first line is a header, so it is skipped
public class DataFileReader {

    public static List<String[]> readRows(Path file) {
        List<String> lines = readLines(file);

        List<String[]> rows = new ArrayList<>();
        for (String line : lines.subList(Math.min(1, lines.size()), lines.size())) {
            if (!line.trim().isEmpty()) {
                // trim removes trailing spaces, \\s+ splits on any mix of tabs and spaces
                rows.add(line.trim().split("\\s+"));
            }
        }
        return rows;
    }

    // reads one file, turns each row into an object, and adds it to the list
    public static <T> void load(Path file, RecordParser<T> parser, List<T> destination) {
        for (String[] row : readRows(file)) {
            destination.add(parser.parse(row));
        }
    }

    private static List<String> readLines(Path file) {
        try {
            return Files.readAllLines(file);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read data file: " + file.toAbsolutePath(), e);
        }
    }
}
