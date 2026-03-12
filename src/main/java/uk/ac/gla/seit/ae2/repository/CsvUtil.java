package uk.ac.gla.seit.ae2.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class CsvUtil {

    private CsvUtil() {
        // utility class
    }

    public static List<String> readAllLines(Path path) {
        try {
            if (!Files.exists(path)) {
                return new ArrayList<>();
            }
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + path, e);
        }
    }

    public static void writeAllLinesAtomically(Path path, List<String> lines) {
        try {
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            Path tempFile = path.resolveSibling(path.getFileName() + ".tmp");
            Files.write(tempFile, lines, StandardCharsets.UTF_8);

            try {
                Files.move(
                        tempFile,
                        path,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(
                        tempFile,
                        path,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write CSV file: " + path, e);
        }
    }

    public static List<String> splitSemicolon(String value) {
        if (value == null || value.isBlank()) {
            return new ArrayList<>();
        }

        String[] parts = value.split(";");
        List<String> result = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }

    public static String joinSemicolon(Collection<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        List<String> cleaned = new ArrayList<>();
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                cleaned.add(value.trim());
            }
        }
        return String.join(";", cleaned);
    }

    public static List<String> withHeader(String header, List<String> bodyLines) {
        List<String> lines = new ArrayList<>();
        lines.add(header);
        if (bodyLines != null) {
            lines.addAll(bodyLines);
        }
        return lines;
    }

    public static List<String> removeHeader(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(lines.subList(1, lines.size()));
    }
}